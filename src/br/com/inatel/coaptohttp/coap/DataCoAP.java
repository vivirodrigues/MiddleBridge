package br.com.inatel.coaptohttp.coap;

public class DataCoAP {
	private String ipCoAP;
	private String portCoAP;
	private String pathCoAP;
	private String URL;
	
	public void setURL(String ip,String port,String path) {
		this.URL = ip + ":" + port + "/" + path;
	}
	
	public String getURL() {
		return this.URL;
	}
	
	public void setIpCoAP(String ip) {
		this.ipCoAP = ip;		
	}
	
	public void setPortCoAP(String port) {
		this.portCoAP = port;
	}
	
	public void setPathCoAP(String path){
		this.pathCoAP = path;
	}
	
	public String getIpCoAP() {
		return this.ipCoAP;
	}
	
	public String getPortCoAP(){
		return this.portCoAP;
	}
	
	public String getPathCoAP() {
		return this.pathCoAP;
	}
	
}
