package br.com.inatel.mqtttohttp.mqtt;

public class DataMQTT {
	private String ipMqtt;
	private String portMqtt;
	private String topicMQTT;
	private String URL;
	
	public void setURL(String ip,String port) {
		this.URL = ip + ":" + port;
	}
	
	public String getURL() {
		return this.URL;
	}
	
	public void setIpMqtt(String ip) {
		this.ipMqtt = ip;		
	}
	
	public void setPortMqtt(String port) {
		this.portMqtt = port;
	}
	
	public void setTopicMqtt(String topic){
		this.topicMQTT = topic;
	}
	
	public String getIpMqtt() {
		return this.ipMqtt;
	}
	
	public String getPortMqtt(){
		return this.portMqtt;
	}
	
	public String getTopicMqtt() {
		return this.topicMQTT;
	}

}
