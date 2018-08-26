package br.com.inatel.http;

public class Headers {
	private String name;
	private String value;
	
		
	public void setHeaders(String name, String value){
		this.name = name;
		this.value = value;	
	}
	public void setHeaderValue(String value) {
		this.value = value;
	}	
	public String getHeadersName() {
		return this.name;		
	}
	public String getHeadersValue() {
		return this.value;
	}
	
}
