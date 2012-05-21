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
 * This class will populate a Postgres table with the following schema in the
 * 
 * CREATE TABLE all_positions (
 callsign character varying(10) NOT NULL,
 basecall character(10) NOT NULL,
 ssid character(3) NOT NULL,
 destination character varying(10) NOT NULL,
 toi timestamp with time zone DEFAULT now() NOT NULL,
 symbol_table character(1),
 symbol character(1),
 igate character varying(11),
 digi0 character varying(11),
 digi1 character varying(11),
 digi2 character varying(11),
 digi3 character varying(11),
 digi4 character varying(11) );
 SELECT AddGeometryColumn('','all_positions','position',4326,'POINT',2);
 ALTER TABLE all_positions ADD CONSTRAINT all_positions_uk UNIQUE (callsign);
 CREATE INDEX all_positions_basecall_idx ON all_positions(basecall);
 CREATE INDEX all_positions_geom_idx ON all_positions USING GIST (position);
 CREATE INDEX all_positions_digi0_idx ON all_positions(digi0);
 CREATE INDEX all_positions_igate_idx ON all_positions(igate);
 */
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;

import net.ab0oo.aprs.avrs.db.AVRSDao;
import net.ab0oo.aprs.avrs.db.jdbc.JdbcAVRSDao;
import net.ab0oo.aprs.avrs.models.AllPositionEntry;
import net.ab0oo.aprs.parser.APRSPacket;
import net.ab0oo.aprs.parser.Parser;
import net.ab0oo.aprs.parser.Position;
import net.ab0oo.aprs.parser.PositionPacket;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.postgresql.ds.PGSimpleDataSource;

public class PGClient implements PacketListener {

	static String PROPERTIES_FILE_NAME = "pg_client.xml";
	static DecimalFormat df = new DecimalFormat("###.000000");
	static String username = null, database = null, password = null, aprsFilter = null,
			host = null, server = null, callsign = null, aprsPass = null;
//	private PreparedStatement update = null;
	private Socket clientSocket = null;
    private DataOutputStream outToServer;
    private BufferedReader inFromServer;
	static int port = 10152, staleMs;
	static boolean update = true;
	private AVRSDao dao;

	public static void main(String argv[]) throws Exception {
		String filename;
		if (argv.length > 0) {
			filename = argv[0];
		} else {
			filename = PROPERTIES_FILE_NAME;
		}
		PGClient pgClient = new PGClient();
		try {
			XMLConfiguration config = new XMLConfiguration(filename);
			username = config.getString("postgres.username");
			database = config.getString("postgres.dbname");
			password = config.getString("postgres.password");
			host = config.getString("postgres.host");
			server = config.getString("aprsis.host");
			port = config.getInt("aprsis.port");
			callsign = config.getString("aprsis.callsign");
			aprsPass = config.getString("aprsis.password");
			aprsFilter = config.getString("aprsis.filter");
			staleMs = config.getInt("avrs.staleMs");
			update = config.getBoolean("avrs.update");
		} catch (ConfigurationException cfex) {
			System.err.println("Unable to load avrs.xml configuration.");
			System.exit(1);
		}
    	PGSimpleDataSource source = new PGSimpleDataSource();
    	source.setServerName(host);
    	source.setDatabaseName(database);
    	source.setUser(username);
    	source.setPassword(password);
    	AVRSDao adao = new JdbcAVRSDao();
    	adao.setDataSource(source);
    	pgClient.setDao(adao);
		pgClient.connectAndParse();
	}

	public PGClient() {
		System.out.println("Initializing PGClient...");
	}

	public void connectAndParse() {
		String sentence;
		String modifiedSentence = "";
		try {
			clientSocket = new Socket(server, port);
        	outToServer = new DataOutputStream(clientSocket.getOutputStream());
        	inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (UnknownHostException uhex) {
			System.err.println("Unknown hostname " + server + ":  " + uhex);
			System.exit(1);
		} catch (IOException ioex) {
			System.err.println("Unable to use TCP/IP socket to APRS-IS server:  "+ ioex);
			System.exit(1);
		}
		try {
        	sentence = "user "+callsign+" pass "+aprsPass+" vers test 1.0 "+ aprsFilter;
        	outToServer.writeBytes(sentence + '\n');
		} catch (IOException ioex) {
			System.err.println("Unable to use TCP/IP socket to APRS-IS server:  "+ ioex);
		}
		APRSPacket packet;
		while (true) {
			try {
				modifiedSentence = inFromServer.readLine();
				if (!modifiedSentence.startsWith("#") ) {
					packet = Parser.parse(modifiedSentence);
					processPacket(packet);
				}
			} catch (Exception ex) {
				System.err.println("Exception during Network read:  " + ex);
				ex.printStackTrace();
				System.err.println("Culprit was "+modifiedSentence);
			}
		}
	}
	
	@Override
    public void processPacket(APRSPacket packet) {
		try {
			storePosition(packet);
		} catch ( Exception ex ) {
			System.err.println("Error processing packet in PGClient:  "+ex);
		}
	}

	public void storePosition(APRSPacket packet) {
		try {
			if (packet.hasFault()) {
				return;
			}
			if (packet.isAprs()&& packet.getAprsInformation() instanceof PositionPacket) {
				PositionPacket pp = (PositionPacket) packet.getAprsInformation();
				Position p = pp.getPosition();
				if ( p == null ) { return; }
				//if ( p.getLatitude() > 35.00 || p.getLatitude() < 30.00) { return; }
				//if ( p.getLongitude() > -80.95 || p.getLongitude() < -85.6) { return; }
				if (p != null) {
					AllPositionEntry entry = new AllPositionEntry();
					entry.setCallsign(packet.getSourceCall());
					entry.setDestination(packet.getDestinationCall());
					entry.setDigis(packet.getDigipeaters());
					entry.setPosition(p);
					entry.setIgate(packet.getIgate());
					if ( update ) { 
					    dao.saveLatestPosition(entry);
					} else {
                        dao.savePosition(entry);
					}
				}
			}
		} catch (Exception ex) {
			System.err.println("Exception during database transaction:  " + ex);
			System.err.println("Source call was "+packet.getSourceCall());
			ex.printStackTrace();
		}
	}

	/**
	 * @param dao the dao to set
	 */
	public final void setDao(AVRSDao dao) {
		this.dao = dao;
	}

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.clients.PacketListener#setOutputStream(java.io.DataOutputStream)
	 */
	@Override
	public void setOutputStream(DataOutputStream outToServer) {
		// TODO Auto-generated method stub
		
	}
}