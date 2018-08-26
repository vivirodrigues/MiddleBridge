package br.com.inatel.ddstohttp.dds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

import org.apache.http.client.methods.HttpPost;

import com.rti.dds.cdr.CdrBuffer;
import com.rti.dds.cdr.CdrInputStream;
import com.rti.dds.cdr.CdrOutputStream;
import com.rti.dds.domain.*;
import com.rti.dds.infrastructure.*;
import com.rti.dds.publication.builtin.*;
import com.rti.dds.subscription.*;
import com.rti.dds.subscription.builtin.*;
import com.rti.dds.topic.Topic;
import com.rti.dds.type.builtin.StringDataReader;
import com.rti.dds.type.builtin.StringTypeSupport;
import com.rti.dds.typecode.TypeCode;
import com.rti.dds.typecode.TypeCodeFactory;
import com.rti.dds.util.NativeInterface;
import com.rti.ndds.config.Version;

import br.com.inatel.coaptohttp.coap.DataCoAP;
import br.com.inatel.configuration.Message;
import br.com.inatel.http.DataHTTP;
import br.com.inatel.http.Headers;

public class Discovery {
	private static boolean shutdown_flag = false;
	private static int verbosity = 1;
	private int domainId;
	private DomainParticipant participant;
	private PublicationBuiltinTopicDataDataReader publicationsDR;
	private SubscriptionBuiltinTopicDataDataReader subscriptionsDR;
	private final static int MAX_ACTIVE_CONDITIONS = 3; // We will only install e conditions on the
	private ConditionSeq activeConditionSeq;
	private WaitSet discoveryWaitSet;
	private ConcurrentSkipListMap<String, TypeCode> discoveredTypes;
	private static String ipDDS;

	public static final void main(String[] args) {
		new Discovery().connection(null);

	}
	
	public void connection(DataDDS dataDDS) {				
		ipDDS = dataDDS.getIP();
		
		Discovery typesSpy = new Discovery();
		if (!typesSpy.start(dataDDS)) {
			System.out.println("Error");
			return;
		}

		//while (true) {
			typesSpy.waitForDiscoveryData();
			typesSpy.processDiscoveredTypes();
			//myExample.connection(dataDDS, dataHttp, message, listV, httppost);
		//}
		
	}

	public boolean start(DataDDS dataDDS) {
		domainId = dataDDS.getDomain();

		Version version = Version.get_instance();
		System.out.println("Running RTI Connext version: " + version);

		discoveredTypes = new ConcurrentSkipListMap<String, TypeCode>();

		// domainId = theDomainId;
		DomainParticipantFactory factory = DomainParticipantFactory.get_instance();
		DomainParticipantFactoryQos factoryQos = new DomainParticipantFactoryQos();

		factoryQos.entity_factory.autoenable_created_entities = false;
		factory.get_qos(factoryQos);

		DomainParticipantQos pQos = new DomainParticipantQos();
		factory.get_default_participant_qos(pQos);

		pQos.discovery.initial_peers.add(ipDDS);
		pQos.discovery.initial_peers.add("4@builtin.udpv4://192.168.0.139");//+ipDDS);
		pQos.discovery.initial_peers.add("builtin.shmem://");
		pQos.discovery.multicast_receive_addresses.clear();

		/*
		 * final String[] NDDS_DISCOVERY_INITIAL_PEERS = { "1@udpv4://192.168.0.110",
		 * "1@udpv4://192.168.0.139" };
		 * pQos.discovery.initial_peers.ensureCapacity(NDDS_DISCOVERY_INITIAL_PEERS.
		 * length); for (int i = 0; i < NDDS_DISCOVERY_INITIAL_PEERS.length; ++i) {
		 * pQos.discovery.initial_peers.add( NDDS_DISCOVERY_INITIAL_PEERS[i]); }
		 */

		// Initialize listener if desired
		DomainParticipantListener participant_listener = null;
		// Create the participant
		DomainParticipant participant = null;

		pQos.participant_name.name = "RTI Connext Monitor Types Snippet";
		try {

			// participant = factory.create_participant(
			// domainId, pQos, participant_listener, StatusKind.STATUS_MASK_NONE);
			// participant.enable();
			participant = factory.create_participant(domainId, pQos, // DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT,
					null, // listener
					StatusKind.STATUS_MASK_NONE);
		} catch (Exception e) {
			String lastStartError = "Error creating the DDS domain. Common causes are:"
					+ "\n   - Lack of a network. E.g disconected wireless."
					+ "\n   - A network interface that does not bind multicast addresses. In some platforms enabling using the TUN interface "
					+ "\n      for (Open)VPN causes this. If this is your situation try configure (Open)VPN to use TAP instead.";

			System.out.println(lastStartError);
			return false;
		}

		// We count ourselves as a participant that is present

		publicationsDR = (PublicationBuiltinTopicDataDataReader) participant.get_builtin_subscriber()
				.lookup_datareader("DCPSPublication");

		subscriptionsDR = (SubscriptionBuiltinTopicDataDataReader) participant.get_builtin_subscriber()
				.lookup_datareader("DCPSSubscription");

		try {
			participant.enable();
		} catch (Exception e) {
			String lastStartError = "Error enabling the DDS domain. Common causes are:"
					+ "\n   - Lack of a network. E.g disconected wireless."
					+ "\n   - A network interface that does not bind multicast addresses. In some platforms enabling using the TUN interface "
					+ "\n      for (Open)VPN causes this. If this is your situation try configure (Open)VPN to use TAP instead.";

			System.out.println(lastStartError);
			return false;
		}

		discoveryWaitSet = new WaitSet();

		discoveryWaitSet.attach_condition(publicationsDR.get_statuscondition());
		publicationsDR.get_statuscondition().set_enabled_statuses(StatusKind.DATA_AVAILABLE_STATUS);

		discoveryWaitSet.attach_condition(subscriptionsDR.get_statuscondition());
		subscriptionsDR.get_statuscondition().set_enabled_statuses(StatusKind.DATA_AVAILABLE_STATUS);

		activeConditionSeq = new ConditionSeq(MAX_ACTIVE_CONDITIONS);
		return true;
	}

	public void waitForDiscoveryData() {
		Duration_t waitDuration = new Duration_t(Duration_t.DURATION_INFINITE_SEC, Duration_t.DURATION_INFINITE_NSEC);

		//System.out.println("waitForDiscoveryData");
		discoveryWaitSet.wait(activeConditionSeq, waitDuration);
	}

	public ArrayList<TypeCode> processDiscoveredTypes() {
		ArrayList<TypeCode> newTypeCodes = new ArrayList<TypeCode>();

		//System.out.println("processDiscoveredTypes");
		if (publicationsDR.get_statuscondition().get_trigger_value()) {
			processTypesInDiscoveredDataWriters(newTypeCodes);
		}
		if (subscriptionsDR.get_statuscondition().get_trigger_value()) {
			processTypesInDiscoveredDataReaders(newTypeCodes);
		}

		return newTypeCodes;
	}

	private void processTypesInDiscoveredDataReaders(ArrayList<TypeCode> newTypeCodes) {
		SubscriptionBuiltinTopicData subscriptionData = new SubscriptionBuiltinTopicData();
		;
		SampleInfo info = new SampleInfo();
		;

		try {
			while (true) {
				subscriptionsDR.take_next_sample(subscriptionData, info);

				if (info.view_state == ViewStateKind.NEW_VIEW_STATE) {
					System.out.println("DataReader (New)" + " name: \"" + subscriptionData.subscription_name.name + "\""
							+ " topic: \"" + subscriptionData.topic_name + "\"" + " type: \""
							+ subscriptionData.type_name + "\"");
					if (processType(subscriptionData.type_name, subscriptionData.type_code)) {
						newTypeCodes.add(subscriptionData.type_code);
					}
				}
			}
		} catch (RETCODE_NO_DATA noData) {
		}
		// catch (RETCODE_BAD_PARAMETER badParam) { }
		finally {
		}
	}

	private void processTypesInDiscoveredDataWriters(ArrayList<TypeCode> newTypeCodes) {
		PublicationBuiltinTopicData publicationData = new PublicationBuiltinTopicData();
		SampleInfo info = new SampleInfo();

		try {
			while (true) {
				publicationsDR.take_next_sample(publicationData, info);
				if (info.view_state == ViewStateKind.NEW_VIEW_STATE) {
					System.out.println("DataWriter (New)" + " name: \"" + publicationData.publication_name.name + "\""
							+ " topic: \"" + publicationData.topic_name + "\"" + " type: \"" + publicationData.type_name
							+ "\"");

					if (processType(publicationData.type_name, publicationData.type_code)) {
						newTypeCodes.add(publicationData.type_code);
					}

				}
			}
		} catch (RETCODE_NO_DATA noData) {
		}
		// catch (RETCODE_BAD_PARAMETER badParam) { }
		finally {
		}
	}

	public byte[] serializeTypeCode(TypeCode typeCode) {
		int bufferSize = typeCode.get_serialized_size(0);
		CdrOutputStream stream = new CdrOutputStream(bufferSize);
		typeCode.serialize(stream);
		return stream.getBuffer().getBuffer();
	}

	// a file or sent out-of band.
	public TypeCode deserializeTypeCode(byte[] serializedTypeCode) {
		boolean needByteSwap = NativeInterface.getInstance().isNativeByteOrderLittleEndian();

		CdrInputStream stream = new CdrInputStream(serializedTypeCode, needByteSwap);
		TypeCode typeCode = TypeCodeFactory.get_instance().create_tc_from_stream(stream);

		return typeCode;
	}

	private boolean processType(String type_name, TypeCode type_code) {
		TypeCode existingType = null;

		System.out.println("Discovered type: " + type_name);
		if (type_code == null) {
			System.out.println("No type information was supplied for type: \"" + type_name + "\"");
			return false;
		}

		// See if we already had a type with the same name:
		existingType = discoveredTypes.get(type_name);

		if (existingType == null) {
			System.out.println("This type had not seen before. Its structure is:");
			type_code.print_IDL(0);
			discoveredTypes.put(type_name, type_code);
			return true;
		} else {
			System.out.println("This type had been seen already. Comparing the type definitions...");
			if (existingType.equals(type_code)) {
				System.out.println("The type matches the existing definition");
				return false;
			} else {
				System.out.println("The type DOES NOT match the existing definition");

				System.out.println("This is the existing definition:");
				existingType.print_IDL(0);

				System.out.println("This is the definition of the type just discovered:");
				type_code.print_IDL(0);
				return true;
			}
		}
	}

	/*
	 * public static void main(String args[]) throws InterruptedException { String
	 * NDDSHOME = System.getenv("NDDSHOME"); String DYLD_LIBRARY_PATH =
	 * System.getenv("DYLD_LIBRARY_PATH"); System.out.println("NDDSHOME="+
	 * NDDSHOME); System.out.println("DYLD_LIBRARY_PATH="+ DYLD_LIBRARY_PATH); int
	 * domainId = 0;
	 * 
	 * 
	 * Discovery typesSpy = new Discovery(); if ( !typesSpy.start(domainId) ) {
	 * System.out.println("Error"); return; }
	 * 
	 * 
	 * while (true) { typesSpy.waitForDiscoveryData();
	 * typesSpy.processDiscoveredTypes();
	 * 
	 * 
	 * }}
	 */

}
