package br.com.inatel.mqtttohttp.mqtt;

import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import br.com.inatel.configuration.Message;
import br.com.inatel.http.DataHTTP;
import br.com.inatel.http.Headers;
import br.com.inatel.http.HttpConnection;



public class MqttConnection implements MqttCallback{
	public Message message = new Message();
	HttpConnection httpConnection = new HttpConnection();
	public DataHTTP dataHttp = new DataHTTP();
	public DataMQTT dataMqtt = new DataMQTT();
	HttpPost httppost;	
	private List<Headers> listV;
	private boolean validacao;
	
	public static void main(String[] args) {
		new MqttConnection().connection(null, null, null, null, null);				
	}
	public void connection(DataMQTT dataMqtt, DataHTTP dataHttp, Message message, List<Headers> listV, HttpPost httppost) {
		this.dataHttp = dataHttp;				
		this.listV = listV;
		this.httppost = httppost;
		this.message = message;
		this.dataMqtt = dataMqtt;
		String topicMqtt = dataMqtt.getTopicMqtt();
		this.validacao = message.isValidation();
		
		try {
			MqttClient client = new MqttClient( 
				    "tcp://"+ dataMqtt.getURL(),//ipMqtt +":"+ portMqtt, //URI 
				    MqttClient.generateClientId(), //ClientId 
				    new MemoryPersistence()); //Persistence
			client.connect();
			client.setCallback(this);
	        client.subscribe(topicMqtt);
			System.out.println("Conectado:"+ client.isConnected());						
			
		}catch(MqttException e) {
			System.out.println("reason " + e.getReasonCode());
	        System.out.println("msg " + e.getMessage());
	        System.out.println("loc " + e.getLocalizedMessage());
	        System.out.println("cause " + e.getCause());
	        System.out.println("excep " + e);
	        e.printStackTrace();
		}
	}
	public void connectionLost(Throwable cause) {
	    // TODO Auto-generated method stub
	}
	
	public void messageArrived(String topic, MqttMessage messageArrived) throws Exception {										
		valida(messageArrived.toString());
	}
	
	public void deliveryComplete(IMqttDeliveryToken token) {
	    // TODO Auto-generated method stub
	}
	
	public String valida(String messageArrived) throws ClientProtocolException, IOException {		
		if(this.validacao==true) {			
			String[] msgArrived = messageArrived.split(",");
			int tamMsg = msgArrived.length;			
			if(!(tamMsg == (message.getMessageSize()))) {				
				JOptionPane.showMessageDialog(null, "The number of variables received does not fit the model");
				return null;				
			}											
		}							
		httpConnection.dataHttp(messageArrived, message, dataHttp, this.listV, httppost);		
		return null;
	}

}
