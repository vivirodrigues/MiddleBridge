package br.com.inatel.websockettohttp.websocket;
/*
 * Copyright (c) 2010-2018 Nathan Rajlich
 *
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without
 *  restriction, including without limitation the rights to use,
 *  copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following
 *  conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *  OTHER DEALINGS IN THE SOFTWARE.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import br.com.inatel.configuration.Message;
import br.com.inatel.http.DataHTTP;
import br.com.inatel.http.Headers;
import br.com.inatel.http.HttpConnection;


/**
 * A simple WebSocketServer implementation. Keeps track of a "chatroom".
 */
public class ChatServer extends WebSocketServer {
	public static Message Message = new Message();
	static HttpConnection httpConnection = new HttpConnection();
	public static DataHTTP dataHttp = new DataHTTP();	
	static HttpPost httppost;	
	private static List<Headers> listV;
	private static boolean validacao;
	
	/*public static void main(String[] args) throws InterruptedException , IOException {
		new CoAPConnection().connection(null, null, null, null, null);
	}*/
	
	public ChatServer( int port ) throws UnknownHostException {
		super( new InetSocketAddress( port ) );
	}

	public ChatServer( InetSocketAddress address ) {
		super( address );
	}

	@Override
	public void onOpen( WebSocket conn, ClientHandshake handshake ) {
		conn.send("Welcome to the server!"); //This method sends a message to the new client
		broadcast( "new connection: " + handshake.getResourceDescriptor() ); //This method sends a message to all clients connected
		System.out.println( conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!" );
	}

	@Override
	public void onClose( WebSocket conn, int code, String reason, boolean remote ) {
		broadcast( conn + " has left the room!" );
		System.out.println( conn + " has left the room!" );
	}

	@Override
	public void onMessage( WebSocket conn, String message ) {
		broadcast( message );
		System.out.println( conn + ": " + message );
		try {
			valida(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	
	@Override
	public void onMessage( WebSocket conn, ByteBuffer message ) {
		broadcast( message.array() );
		System.out.println( conn + ": " + message );
	}
	
	public String valida(String messageArrived) throws ClientProtocolException, IOException {		
		if(this.validacao==true) {			
			String[] msgArrived = messageArrived.split(",");
			int tamMsg = msgArrived.length;			
			if(!(tamMsg == (Message.getMessageSize()))) {				
				JOptionPane.showMessageDialog(null, "The number of variables received does not fit the model");
				return null;				
			}											
		}		
		httpConnection.dataHttp(messageArrived, Message, dataHttp, this.listV, httppost);		
		return null;
	}

	//public static void main( String[] args ) throws InterruptedException , IOException {
	public void connection(DataWebsocket dataWebsocket, DataHTTP dataHttp, Message message, List<Headers> listV, HttpPost httppost) throws IOException, InterruptedException {		
	//public void connection()throws IOException, InterruptedException {
		
		this.dataHttp = dataHttp;				
		this.listV = listV;
		this.httppost = httppost;
		this.Message = message;
		this.validacao = message.isValidation();
		
		
		WebSocketImpl.DEBUG = true;
		//int port = 8887; // 843 flash policy port
		int port = dataWebsocket.getPort();
		/*try {
			port = Integer.parseInt( args[ 0 ] );
		} catch ( Exception ex ) {
		}*/
		ChatServer s = new ChatServer( port );
		s.start();
		System.out.println( "ChatServer started on port: " + s.getPort() );

		BufferedReader sysin = new BufferedReader( new InputStreamReader( System.in ) );
		while ( true ) {
			String in = sysin.readLine();
			s.broadcast( in );
			if( in.equals( "exit" ) ) {
				s.stop(1000);
				break;
			}
		}
	}
	@Override
	public void onError( WebSocket conn, Exception ex ) {
		ex.printStackTrace();
		if( conn != null ) {
			// some errors like port binding failed may not be assignable to a specific websocket
		}
	}

	@Override
	public void onStart() {
		System.out.println("Server started!");
	}

}
