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
package net.ab0oo.aprs.avrs.db.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import net.ab0oo.aprs.avrs.LinkNode;
import net.ab0oo.aprs.avrs.db.AVRSDao;
import net.ab0oo.aprs.avrs.models.AllPositionEntry;
import net.ab0oo.aprs.parser.Position;
import net.ab0oo.aprs.avrs.models.ReferencePoint;

import org.postgis.Geometry;
import org.postgis.PGgeometry;
import org.postgis.Point;

/**
 * @author johng
 *
 */
public class JdbcAVRSDao extends BaseJdbcDAO implements AVRSDao {
    
	@Override
	public TreeSet<LinkNode> getNodes(String callsign) {
    	TreeSet<LinkNode> nodes = new TreeSet<LinkNode>();
		Connection dbconn = null;
		PreparedStatement findClosest = null;
		ResultSet rs = null;
		int nodeId=0;
		try {
			dbconn = ds.getConnection();
        	findClosest = dbconn.prepareStatement(
        			"select ST_distance_sphere(a.position, e.position)/1609 as distance, e.callsign, e.node, " +
        			"astext(e.position), e.nodetype, e.tone, e.frequency" +
        			" from all_positions a, node_positions e " +
        			"where upper(a.callsign)=? and ST_distance_sphere(a.position, e.position)/1609 < 50 " +
        			"order by distance asc"
        			 );

			findClosest.setString(1, callsign.toUpperCase());
			rs = findClosest.executeQuery();
			while ( rs.next() ) {
				String astext = rs.getString("astext");
				astext = astext.substring(6,astext.length()-1);
				String[] lonlat = astext.split(" ");
				double lon = Double.parseDouble(lonlat[0]);
				double lat = Double.parseDouble(lonlat[1]);
				nodeId = rs.getInt("node");
				String type = rs.getString("nodetype");
				LinkNode node = new LinkNode(nodeId, type);
				node.setLatitude(lat);
				node.setLongitude(lon);
				node.setFrequency(rs.getDouble("frequency"));
				node.setTone(rs.getString("tone"));
				node.setDistance(rs.getDouble("distance"));
				nodes.add(node);
			}
		} catch ( SQLException sqlex ) {
			System.err.println("Unable to fetch closest gateway to "+callsign+":  "+sqlex);
			return null;
		} catch ( NumberFormatException nfe ) {
			System.err.println("Unable to read lat/lon for node #"+nodeId );
		} finally {
			try { dbconn.close(); } catch ( Exception ex ) {}
		}
		return nodes;
    }

	@Override
	public Date getLatestPosition(String callsign) {
		Connection dbconn = null;
		PreparedStatement findLatestPosition = null;
		Date lastPositionTimestamp = null;
		ResultSet rs = null;
		try {
			dbconn = ds.getConnection();
        	findLatestPosition = dbconn.prepareStatement("select toi from all_positions where upper(callsign)=?");
        	findLatestPosition.setString(1, callsign.toUpperCase());
			rs = findLatestPosition.executeQuery();
			while (rs.next()) {
				Timestamp ts = rs.getTimestamp("toi");
				lastPositionTimestamp = new Date(ts.getTime());
			}
		} catch (SQLException sqlex) {
			System.err.println("Unable to fetch last position timestamp for "
					+ callsign + ":  " + sqlex);
	    } finally {
	        try { dbconn.close(); } catch ( Exception ex ) {}
	    }
		System.out.println("Value of last TOI for "+callsign+" is "+lastPositionTimestamp);
		return lastPositionTimestamp;

	}
	
	@Override
    public AllPositionEntry getPosition( String callsign ) {
		AllPositionEntry entry = new AllPositionEntry();
		Connection dbconn = null;
		PreparedStatement findPosition = null;
		ResultSet rs = null;
		try {
			dbconn = ds.getConnection();
        	findPosition = dbconn.prepareStatement("select * from all_positions where upper(callsign)=?");
        	findPosition.setString(1, callsign.toUpperCase());
			rs = findPosition.executeQuery();
			while (rs.next()) {
				entry.setDestination(rs.getString("destination"));
				entry.setIgate(rs.getString("igate"));
				entry.setCallsign(callsign);
				Position p = new Position();
				p.setSymbolTable(rs.getString("symbol_table").charAt(0));
				p.setSymbolCode((char)rs.getString("symbol").charAt(0));
				p.setAltitude(rs.getInt("altitude"));
				p.setPositionAmbiguity(rs.getInt("pos_ambiguity"));
                Timestamp ts = rs.getTimestamp("toi");
                p.setTimestamp(new Date(ts.getTime()));
				PGgeometry geom = (PGgeometry)rs.getObject("position");
				Geometry g = geom.getGeometry();
				Point pnt = g.getPoint(0);
				p.setLongitude(pnt.getX());
				p.setLatitude(pnt.getY());
				entry.setPosition(p);
			}
		} catch (SQLException sqlex) {
			System.err.println("Unable to fetch last position timestamp for "
					+ callsign + ":  " + sqlex);
		} finally {
			try { dbconn.close(); } catch ( Exception ex ) {}
		}
		return entry;
	}

	@Override
    public List<AllPositionEntry> getPositions( String callsign ) {
		List<AllPositionEntry> positionList = new ArrayList<AllPositionEntry>();
		Connection dbconn = null;
		PreparedStatement findPosition = null;
		ResultSet rs = null;
		try {
			dbconn = ds.getConnection();
        	findPosition = dbconn.prepareStatement("select * from all_positions where upper(callsign)=?");
        	findPosition.setString(1, callsign.toUpperCase());
			rs = findPosition.executeQuery();
			while (rs.next()) {
				AllPositionEntry entry = new AllPositionEntry();
				entry.setDestination(rs.getString("destination"));
				entry.setIgate(rs.getString("igate"));
				entry.setCallsign(callsign);
				Position p = new Position();
				p.setSymbolTable(rs.getString("symbol_table").charAt(0));
				p.setSymbolCode((char)rs.getString("symbol").charAt(0));
				p.setAltitude(rs.getInt("altitude"));
				p.setPositionAmbiguity(rs.getInt("pos_ambiguity"));
				PGgeometry geom = (PGgeometry)rs.getObject("position");
				Geometry g = geom.getGeometry();
				Point pnt = g.getPoint(0);
				p.setLongitude(pnt.getX());
				p.setLatitude(pnt.getY());
				entry.setPosition(p);
				positionList.add(entry);
			}
		} catch (SQLException sqlex) {
			System.err.println("Unable to fetch last position timestamp for "
					+ callsign + ":  " + sqlex);
		} finally {
			try { dbconn.close(); } catch ( Exception ex ) {}
		}
		return positionList;
	}

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.avrs.db.AVRSDao#saveLatestPosition(net.ab0oo.aprs.avrs.models.AllPositionEntry)
	 */
	@Override
	public void saveLatestPosition(AllPositionEntry entry) {
		Connection dbconn = null;
		PreparedStatement updatePosition = null;
		PreparedStatement insertPosition = null;
		try {
        	Point pnt = new Point(entry.getLongitude(), entry.getLatitude());
        	PGgeometry geom = new PGgeometry(pnt);
        	pnt.setSrid(4326);
        	dbconn = ds.getConnection();
        	updatePosition = dbconn.prepareStatement("update all_positions set destination=?, toi=?, symbol_table=?,"+
        			"symbol=?, igate=?, altitude=?, pos_ambiguity=?, digis=?, position=? where upper(callsign) = ?");
        	updatePosition.setString(1, entry.getDestination().toUpperCase());
        	updatePosition.setTimestamp(2, new java.sql.Timestamp(entry.getToi().getTime()));
        	updatePosition.setString(3, entry.getSymbolTable().toString());
        	updatePosition.setString(4, entry.getSymbol().toString());
        	updatePosition.setString(5, entry.getIgate());
        	updatePosition.setInt(6, entry.getAltitude());
        	updatePosition.setInt(7, entry.getPositionAmbiguity());
        	updatePosition.setArray(8, new PostgreSQLTextArray(entry.getDigis()));
        	updatePosition.setObject(9, geom);
        	updatePosition.setString(10, entry.getCallsign().toUpperCase());
        	int rows = updatePosition.executeUpdate();
        	/*
 callsign      | character varying(10)    | not null
 basecall      | character(10)            | not null
 ssid          | character(3)             | not null
 destination   | character varying(10)    | not null
 toi           | timestamp with time zone | not null default now()
 symbol_table  | character(1)             | 
 symbol        | character(1)             | 
 igate         | character varying(11)    | 
 position      | geometry                 | 
 altitude      | integer                  | 
 pos_ambiguity | integer                  | 
 digis         | character(10)[]          | 

        	 */
        	if ( rows == 0 ) {
        		insertPosition = dbconn.prepareStatement("insert into all_positions "+
        				" values (?,?,?,?,?,?,?,?,?,?,?,? )");
            	insertPosition.setString(1, entry.getCallsign());
            	insertPosition.setString(2, entry.getBaseCall());
            	insertPosition.setString(3,	entry.getSsid());
            	insertPosition.setString(4, entry.getDestination());
            	insertPosition.setTimestamp(5, new java.sql.Timestamp(entry.getToi().getTime()));
            	insertPosition.setString(6, entry.getSymbolTable().toString());
            	insertPosition.setString(7, entry.getSymbol().toString());
            	insertPosition.setString(8, entry.getIgate());
            	insertPosition.setObject(9, geom);
            	insertPosition.setInt(10, entry.getAltitude());
            	insertPosition.setInt(11, entry.getPositionAmbiguity());
            	insertPosition.setArray(12, new PostgreSQLTextArray(entry.getDigis()));
            	int result = insertPosition.executeUpdate();
            	if ( result != 1 ) {
            		System.err.println("Error storing new position for "+entry.getCallsign());
            	}
        	}
		} catch (SQLException sqlex) {
			System.err.println("Unable to persist last position entry for "+entry.getCallsign()+" :"+ sqlex );
			System.err.println(" Station was "+entry.getCallsign()+", or "+entry.getBaseCall()+" - "+entry.getSsid());
		} finally {
			try { dbconn.close(); } catch ( Exception ex ) {}
		}
	}
	
    /* (non-Javadoc)
     * @see net.ab0oo.aprs.avrs.db.AVRSDao#saveLatestPosition(net.ab0oo.aprs.avrs.models.AllPositionEntry)
     */
    @Override
    public void savePosition(AllPositionEntry entry) {
        Connection dbconn = null;
        PreparedStatement insertPosition = null;
        try {
            Point pnt = new Point(entry.getLongitude(), entry.getLatitude());
            PGgeometry geom = new PGgeometry(pnt);
            pnt.setSrid(4326);
            dbconn = ds.getConnection();
            insertPosition = dbconn.prepareStatement("insert into position_history "
                    + " values (?,?,?,?,?,?,?,?,?,?,?,? )");
            insertPosition.setString(1, entry.getCallsign());
            insertPosition.setString(2, entry.getBaseCall());
            insertPosition.setString(3, entry.getSsid());
            insertPosition.setString(4, entry.getDestination());
            insertPosition.setTimestamp(5, new java.sql.Timestamp(entry.getToi().getTime()));
            insertPosition.setString(6, entry.getSymbolTable().toString());
            insertPosition.setString(7, entry.getSymbol().toString());
            insertPosition.setString(8, entry.getIgate());
            insertPosition.setObject(9, geom);
            insertPosition.setInt(10, entry.getAltitude());
            insertPosition.setInt(11, entry.getPositionAmbiguity());
            insertPosition.setArray(12, new PostgreSQLTextArray(entry.getDigis()));
            int result = insertPosition.executeUpdate();
            if (result != 1) {
                System.err.println("Error storing new position for " + entry.getCallsign());
            }
        } catch (SQLException sqlex) {
            System.err.println("Unable to persist last position entry for "+entry.getCallsign()+" :"+ sqlex );
            System.err.println(" Station was "+entry.getCallsign()+", or "+entry.getBaseCall()+" - "+entry.getSsid());
        } finally {
            try { dbconn.close(); } catch ( Exception ex ) {}
        }
    }

    @Override
    public List<ReferencePoint> listClosestCities(Position position) {
		Connection dbconn = null;
		PreparedStatement testGeometry = null;
		ResultSet rs = null;
		ArrayList<ReferencePoint> retlist = new ArrayList<ReferencePoint>();
		try {
			dbconn = ds.getConnection();
			String lat = Double.toString(position.getLatitude());
			String lon = Double.toString(position.getLongitude());
			String sql = "select accent_city, region, population, " +
					"ST_distance_sphere(the_geom, geometryFromText('POINT("+lon+" "+lat+")',4326) ) as dist, " +
					"degrees(azimuth(the_geom,geometryFromText('POINT("+lon+" "+lat+")',4326) )) as bearing " +
					"from world_cities where population > 15000 order by dist asc limit 2";
			//System.out.println(sql);
			testGeometry = dbconn.prepareStatement(sql);
			rs = testGeometry.executeQuery();
			while (rs.next()) {
				ReferencePoint rp = new ReferencePoint(rs.getString("accent_city"),rs.getString("region"),
						rs.getDouble("bearing"), rs.getDouble("dist") );
				retlist.add(rp);
			}
		} catch (SQLException sqlex) {
			System.err.println("SQL Exception:  " + sqlex);
        } finally {
            try { dbconn.close(); } catch ( Exception ex ) {}
        }
		return retlist;
	}

}
