package br.com.inatel.coaptohttp.coap;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;
import org.apache.http.client.methods.HttpPost;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.EndpointManager;

import br.com.inatel.configuration.Message;
import br.com.inatel.http.DataHTTP;
import br.com.inatel.http.Headers;

public class CoAPConnection extends CoapServer {

	private String pathCoAP;
	private int portCoAP;
	
	public static void main(String[] args) throws SocketException {
		new CoAPConnection().connection(null, null, null, null, null);
	}
	
	public void connection(DataCoAP dataCoAP, DataHTTP dataHttp, Message message, List<Headers> listV, HttpPost httppost) {						
		
		try {
			
			this.pathCoAP = dataCoAP.getPathCoAP();
			this.portCoAP = Integer.parseInt(dataCoAP.getPortCoAP());
			// create server
			CoAPConnection server = new CoAPConnection();						
			MyResource rs = new MyResource(this.pathCoAP);
			server.add(rs);
			// start server
			server.start();			
			rs.connection(dataHttp, message, listV, httppost);
			
		} catch (SocketException e) {
			System.err.println("Failed to initialize server: " + e.getMessage());
		}
	}

	/* Here, the resources of the server are initialized */
	public CoAPConnection() throws SocketException {
		add(new MyResource("\""+this.pathCoAP+"\""));
	}	
}