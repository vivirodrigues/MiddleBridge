package br.com.inatel.http;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import br.com.inatel.coaptohttp.coap.CoAPConnection;
import br.com.inatel.configuration.Message;

public class HttpConnection {
	public List<Headers> list_headers = new ArrayList<Headers>();
	public List<Headers> list_headers_v = new ArrayList<Headers>();
	

	public String dataHttp(String messageArrived, Message message, DataHTTP dataHttp,
		List<Headers> listV, HttpPost httppost) throws ClientProtocolException, IOException {
		HttpClient httpclient = HttpClients.createDefault();								
		
		// message
		String variable = message.getVariable();
		String[] msgArrived = messageArrived.split(",");
		String messageHttp = message.getModel();
		int modelSize = message.getVariablesSize();		

		/** If there is an incomplete header,
		 *  the value is expected to be sent in the message **/
		int incompleteHeader = message.getHeaderSize(); // number of incomplete headers

		// expected message size
		int totalSize = modelSize + incompleteHeader;

		int n = 0;
		
		// if there is an incomplete header
		if(incompleteHeader>0) {
			for (int i = 0; i < listV.size(); i++) {
				String h = msgArrived[i].trim();
				httppost.setHeader(listV.get(i).getHeadersName(), h);
				//System.out.println("Header " + listV.get(i).getHeadersName() + " " + h + " adicionado");
				n += 1;
			}
		}
		
		//System.out.println("total"+totalSize);
		for (int g = n; g < totalSize; g++) {
			msgArrived[g] = msgArrived[g].trim();
			messageHttp = messageHttp.replaceFirst(variable, msgArrived[g]);
		}

		//System.out.println(messageHttp);

		/*************************************************************/

		httppost.setEntity(new StringEntity(messageHttp));

		HttpResponse response = httpclient.execute(httppost);
		System.out.println(response);
		HttpEntity entity = response.getEntity();

		if (entity != null) {
			InputStream instream = entity.getContent();
			try {
				// do something useful
			} finally {
				instream.close();
			}
		}
		return null;
	}

	public void doThings() throws ClientProtocolException, IOException {

	}
}
