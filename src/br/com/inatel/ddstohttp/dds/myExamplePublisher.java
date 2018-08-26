package br.com.inatel.ddstohttp.dds;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import com.rti.dds.domain.*;
import com.rti.dds.infrastructure.*;
import com.rti.dds.publication.*;
import com.rti.dds.topic.*;
import com.rti.dds.type.builtin.StringDataWriter;
import com.rti.dds.type.builtin.StringTypeSupport;


// ===========================================================================

public class myExamplePublisher {
    // -----------------------------------------------------------------------
    // Public Methods
    // -----------------------------------------------------------------------

    public static void main(String[] args) {
	    	// Create the DDS Domain participant on domain ID 0
	    	DomainParticipant participant = DomainParticipantFactory.get_instance().create_participant(
	    	0,                                                // Domain ID = 0
	    	DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT, // QoS Settings = Default
	    	null,                                             // listener
	    	StatusKind.STATUS_MASK_NONE);                     // mask
	    	if (participant == null) {
	    	    System.err.println("Unable to create domain participant");
	    	    return;
	    	}
	    	Topic helloWorldTopic = participant.create_topic(
	    		    "Hello, World",                      //topic_name
	    		    StringTypeSupport.get_type_name(),   //type_name
	    		    DomainParticipant.TOPIC_QOS_DEFAULT, //QoS
	    		    null,                                //listener
	    		    StatusKind.STATUS_MASK_NONE);        //mask
	    		if (helloWorldTopic == null) {
	    		    System.err.println("Unable to create topic.");
	    		    return;
	    		}
	    		
	    		// Create the data writer using the default publisher
	    		StringDataWriter dataWriter = (StringDataWriter)participant.create_datawriter(
	    		    helloWorldTopic,                  // Topic
	    		    Publisher.DATAWRITER_QOS_DEFAULT, // QoS
	    		    null,                             // listener
	    		    StatusKind.STATUS_MASK_NONE);     // mask
	    		if (dataWriter == null) {
	    		    System.err.println("Unable to create data writer\n");
	    		    return;
	    		}
	    		
	    		System.out.println("Ready to write data.");
	    		System.out.println("When the subscriber is ready, you can start writing.");
	    		System.out.print("Press CTRL+C to terminate or enter an empty line to do a clean shutdown.\n\n"); 

	    		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	    		try
	    		{
	    		while (true) {
	    		System.out.print("Please type a message> ");
	    		    String toWrite = reader.readLine();
	    		        dataWriter.write(toWrite, InstanceHandle_t.HANDLE_NIL); 

	    		        if (toWrite.equals("")) break;
	    		    }
	    		}
	    		catch (IOException e)
	    		{
	    		    e.printStackTrace();
	    		} catch (RETCODE_ERROR e) {
	    		    // This exception can be thrown from DDS write operation
	    		    e.printStackTrace();
	    		} 

	    		System.out.println("Exiting..."); 

	    		// Deleting entities of DomainParticipant and DomainParticipant
	    		participant.delete_contained_entities();
	    		DomainParticipantFactory.get_instance().delete_participant(participant);
	    		}
}

    	/*

    // -----------------------------------------------------------------------
    // Private Methods
    // -----------------------------------------------------------------------

    // --- Constructors: -----------------------------------------------------

    private myExamplePublisher() {
        super();
    }

    // -----------------------------------------------------------------------

    private static void publisherMain(int domainId, int sampleCount) {

        DomainParticipant participant = null;
        Publisher publisher = null;
        Topic topic = null;
        myExampleDataWriter writer = null;

        try {
            // --- Create participant --- //

            // To customize participant QoS, use
            //the configuration file
            //USER_QOS_PROFILES.xml 

            participant = DomainParticipantFactory.TheParticipantFactory.
            create_participant(
                domainId, DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT,
                null, StatusKind.STATUS_MASK_NONE);
            if (participant == null) {
                System.err.println("create_participant error\n");
                return;
            }        

            // --- Create publisher --- //

            // To customize publisher QoS, use
            //the configuration file USER_QOS_PROFILES.xml

            publisher = participant.create_publisher(
                DomainParticipant.PUBLISHER_QOS_DEFAULT, null,
                StatusKind.STATUS_MASK_NONE);
            if (publisher == null) {
                System.err.println("create_publisher error\n");
                return;
            }                   

            // --- Create topic --- //

            // Register type before creating topic 
            String typeName = myExampleTypeSupport.get_type_name();
            myExampleTypeSupport.register_type(participant, typeName);

            // To customize topic QoS, use
            //the configuration file USER_QOS_PROFILES.xml 

            topic = participant.create_topic(
                "Example myExample",
                typeName, DomainParticipant.TOPIC_QOS_DEFAULT,
                null, StatusKind.STATUS_MASK_NONE);
            if (topic == null) {
                System.err.println("create_topic error\n");
                return;
            }           

            // --- Create writer --- //

            //To customize data writer QoS, use
            //the configuration file USER_QOS_PROFILES.xml 

            writer = (myExampleDataWriter)
            publisher.create_datawriter(
                topic, Publisher.DATAWRITER_QOS_DEFAULT,
                null, StatusKind.STATUS_MASK_NONE);
            if (writer == null) {
                System.err.println("create_datawriter error\n");
                return;
            }           

            // --- Write --- //

            // Create data sample for writing 
            myExample instance = new myExample();
            //instance.ID = 10;
            //instance.value = 3.14;
            
            InstanceHandle_t instance_handle = InstanceHandle_t.HANDLE_NIL;
            // For a data type that has a key, if the same instance is going to be
            //written multiple times, initialize the key here
            //and register the keyed instance prior to writing 
            //instance_handle = writer.register_instance(instance);

            final long sendPeriodMillis = 4 * 1000; // 4 seconds

            for (int count = 0;
            (sampleCount == 0) || (count < sampleCount);
            ++count) {
                System.out.println("Writing myExample, count " + count);

                
                //Modify the instance to be written here 
              
                instance.value = 7.98;
                instance.ID = 4;
                
                
                //Write data 
                writer.write(instance, instance_handle);
                try {
                    Thread.sleep(sendPeriodMillis);
                } catch (InterruptedException ix) {
                    System.err.println("INTERRUPTED");
                    break;
                }
            }

            //writer.unregister_instance(instance, instance_handle);

        } finally {

            // --- Shutdown --- //

            if(participant != null) {
                participant.delete_contained_entities();

                DomainParticipantFactory.TheParticipantFactory.
                delete_participant(participant);
            }
            // RTI Data Distribution Service provides finalize_instance()
            //method for people who want to release memory used by the
            //participant factory singleton. Uncomment the following block of
            //code for clean destruction of the participant factory
            //singleton. 
            //DomainParticipantFactory.finalize_instance();
        }
    }
}*/

