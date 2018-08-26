package br.com.inatel.coaptohttp.coap;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;

import static org.eclipse.californium.core.coap.CoAP.ResponseCode.CREATED;

import org.apache.http.client.methods.HttpPost;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.server.resources.CoapExchange;

import br.com.inatel.configuration.Message;
import br.com.inatel.http.DataHTTP;
import br.com.inatel.http.Headers;
import br.com.inatel.http.HttpConnection;

class MyResource extends CoapResource {
		
	private List<Headers> listV;
	private boolean validation;
	Message message = new Message();
	DataHTTP dataHttp = new DataHTTP();
	HttpConnection httpConnection = new HttpConnection();
	HttpPost httppost;
	
	public MyResource(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	public void connection(DataHTTP dataHttp, Message message, List<Headers> listV, HttpPost httppost) {
		this.dataHttp = dataHttp;
		this.listV = listV;
		this.message = message;
		this.httppost = httppost;
		this.validation = message.isValidation();
	}
	
	@Override
	public void handleGET(CoapExchange exchange) {		
		exchange.respond("hello world"); // reply with 2.05 payload (text/plain)
	}

	@Override
	public void handlePOST(CoapExchange exchange) {						
		exchange.accept(); // make it a separate response´
		String messageArrived = new String(exchange.getRequestPayload());				
		exchange.respond(CREATED); // reply with response code only (shortcut)							
		http(messageArrived);
	}
	
	public String http(String messageArrived) {
		// if the user has enabled validation
		if(this.validation==true) {			
			String[] msgArrived = messageArrived.split(",");
			int tamMsg = msgArrived.length;
			if(!(tamMsg == (message.getMessageSize()))) {				
				JOptionPane.showMessageDialog(null, "The number of variables received does not fit the model");
				return null;				
			}											
		}
		// start http connection and message conversion
		try {				
			httpConnection.dataHttp(messageArrived, message, dataHttp, this.listV, httppost);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
