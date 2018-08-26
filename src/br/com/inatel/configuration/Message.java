package br.com.inatel.configuration;

public class Message {
	private String variable;
	private String model;
	private int headerSize; //num Headers a se esperar na msg recebida
	private int variablesSize;
	private boolean validation;
	private int messageSize;
	
	public void setMessageSize(int messageSize) {
		this.messageSize = messageSize;
	}
	
	public int getMessageSize() {
		return messageSize;
	}
	
	public boolean isValidation() {
		return validation;
	}
	public void setValidation(boolean validation) {
		this.validation = validation;
	}
	public void setVariable(String variable) {
		this.variable = variable;		
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getModel() {
		return this.model;
	}
	public String getVariable() {
		return this.variable;
	}
	public void setHeaderSize(int headerSize) {
		this.headerSize = headerSize;
	}
	public int getHeaderSize() {
		return this.headerSize;
	}
	public void setVariablesSize(int variablesSize) {
		this.variablesSize = variablesSize;
	}
	public int getVariablesSize() {
		return this.variablesSize;
	}
}
