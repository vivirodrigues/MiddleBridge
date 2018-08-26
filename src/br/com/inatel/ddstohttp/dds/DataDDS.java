package br.com.inatel.ddstohttp.dds;

public class DataDDS {
	private String topic;
	private int domainID;
	private String IP;
	
	public String getTopic() {
		return this.topic;
	}
	public int getDomain() {
		return this.domainID;
	}
	public String getIP() {
		return this.IP;
	}
	public void setTopic(String topic) {
		this.topic = topic;		
	}
	public void setDomain(int domain) {
		this.domainID = domain;		
	}
	public void setIP(String ip) {
		this.IP = ip;
	}
	
}
