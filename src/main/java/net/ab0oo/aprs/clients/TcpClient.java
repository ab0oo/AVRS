/*
 * AVRS - http://avrs.sourceforge.net/
 *
 * Copyright (C) 2011 John Gorkos, AB0OO
 *
 * AVRS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * AVRS is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AVRS; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */
package net.ab0oo.aprs.clients;

/**
 * @author johng
 *   This is a simple test class that connects to an APRS-IS server, logs in, and then receives and processes
 *   packets sent over the APRS-IS system.  It takes a list of PacketListener implementors and calls processPacket()
 *   on each listener in the list.
 */
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import net.ab0oo.aprs.parser.APRSPacket;
import net.ab0oo.aprs.parser.Parser;

class TcpClient {

	static DecimalFormat df = new DecimalFormat("###.000000");
	private  String server = "rotate.aprs.net";
	private  int port = 10152;
	private  String user = "ab0oo-w";
	private  int pass = 19951;
	private  String version = "Wedjat 0.2";
	private  String filter = "";
	private List<PacketListener> processors = new ArrayList<PacketListener>();
	private Boolean dumpErrors = false;
	
	public static void main(String argv[]) throws Exception {
		TcpClient client = new TcpClient();
		client.init();
	}
	
	public TcpClient() {
	}

	public void init() throws Exception {
	    while ( true ) {
	        Socket clientSocket = this.connect();
	        this.monitorStream(clientSocket);
	    }
	}
	
	public Socket connect() throws Exception {
		Socket clientSocket = new Socket(server, port);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		// sentence =
		// "user ab0oo-15 pass 19951 vers test 1.0 filter a/35.000/-85.6/30.3555/-80.84";
		String sentence = "user " + user + " pass " + pass + " vers " + version+ " " + filter;
		outToServer.writeBytes(sentence + '\n');
		return clientSocket;
	}
	
	public void monitorStream(Socket clientSocket) {
			APRSPacket packet = null;
			String modifiedSentence;
			HashMap<Object,Vector<Object>> workerQueues = new HashMap<Object,Vector<Object>>();
			try {
				BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
				// here we set up and start running the worker queues.
				for ( PacketListener processor : processors ) {
                    Object syncObject = new Object();
                    Vector<Object> queue = new Vector<Object>();
                    processor.setOutputStream( outToServer);
                    ThreadedPacketListener tpl = new ThreadedPacketListener( processor, queue, syncObject );
                    new Thread(tpl).start();
                    workerQueues.put(syncObject, queue);
                }
				
				while (true) {
					modifiedSentence = inFromServer.readLine();
					if (modifiedSentence.charAt(0) == '#') {
						System.out.println(modifiedSentence);
						continue;
					}
					try {
						packet = Parser.parse(modifiedSentence);
					} catch (Exception ex) {
						if ( dumpErrors ) {
							System.err.println(new Date()+": "+ex+"|"+ modifiedSentence);
						}
						continue;
					}
					if (packet.hasFault()) {continue;}
					if (!packet.isAprs()) {continue;}
					for ( Object syncObject : workerQueues.keySet() ) {
			            synchronized (syncObject) {
	                        workerQueues.get(syncObject).add(packet);
			                syncObject.notify();
			            }
					}
				}
			} catch (Exception ex) {
				System.err.println("Exception during Network read:  " + ex);
				ex.printStackTrace();
			}
			System.err.println("Connection dropped, restarting");
	}

	/**
	 * @return the server
	 */
	public final String getServer() {
		return server;
	}

	/**
	 * @param server the server to set
	 */
	public final void setServer(String server) {
		this.server = server;
	}

	/**
	 * @return the port
	 */
	public final int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public final void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the user
	 */
	public final String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public final void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the pass
	 */
	public final int getPass() {
		return pass;
	}

	/**
	 * @param pass the pass to set
	 */
	public final void setPass(int pass) {
		this.pass = pass;
	}

	/**
	 * @return the version
	 */
	public final String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public final void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the filter
	 */
	public final String getFilter() {
		return filter;
	}

	/**
	 * @param filter the filter to set
	 */
	public final void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * @param processors the processors to set
	 */
	public  final void setProcessors(List<PacketListener> processors) {
		this.processors = processors;
	}
	
	public final void setProcessor(PacketListener processor) {
		processors.add(processor);
	}

	/**
	 * @param dumpErrors the dumpErrors to set
	 */
	public final void setDumpErrors(Boolean dumpErrors) {
		this.dumpErrors = dumpErrors;
	}

}