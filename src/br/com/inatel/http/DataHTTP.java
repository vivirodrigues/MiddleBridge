package br.com.inatel.http;

public class DataHTTP {	
		private String ipHTPP;
		private String portHTTP;
		private String pathHTTP;	
		private String URL;
		
		public void setURL(String ip, String port, String path) {
			this.URL = ip + ":" + port + "/" + path;
		}
		public String getURL() {
			return this.URL;
		}
		public void setIpHttp(String ip) {
			this.ipHTPP = ip;			
		}
		public void setPortHttp(String port) {
			this.portHTTP = port;
		}
		public void setPathHttp(String path) {
			this.pathHTTP = path;
		}
		public String getIpHttp() {
			return this.ipHTPP;
		}
		public String getportHttp() {
			return this.portHTTP;
		}
		public String getPath() {
			return this.pathHTTP;
		}
}
