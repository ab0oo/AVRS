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
package net.ab0oo.aprs.wedjat.db.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.ab0oo.aprs.wedjat.db.MonitoredStationDAO;
import net.ab0oo.aprs.wedjat.models.MonitoredStation;

/**
 * @author johng
 *
 */
public class JdbcMonitoredStationDAO extends BaseJdbcDAO implements MonitoredStationDAO {

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjet.MonitoredStationDAO#getMonitoredStationsList(java.lang.String)
	 */
	@Override
	public MonitoredStation getMonitoredStationByStationId(Long stationId) {
		MonitoredStation retval = null;
        Connection dbconn = null;
        PreparedStatement selectMonitoredStations = null;
        ResultSet rs = null;
        try { 
            dbconn = ds.getConnection();
            selectMonitoredStations = dbconn .prepareStatement(
                    "SELECT * from monitored_stations where station_id=?" );
            selectMonitoredStations.setLong(1, stationId);
            rs = selectMonitoredStations.executeQuery();
            while ( rs.next() ) {
            	retval = new MonitoredStation();
                retval.setCallsign(rs.getString("callsign"));
                retval.setUserId(rs.getLong("user_id"));
                retval.setStationId(rs.getLong("station_id"));
            }
        } catch ( SQLException sqlex ) {
        	System.err.println("SQL Exception:  "+sqlex);
        	sqlex.printStackTrace();
        } finally {
            try {
                dbconn.close();
            } catch ( Exception ex ) {}
        }
		return retval;
	}
	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjet.MonitoredStationDAO#getMonitoredStation(java.lang.Long)
	 */
	@Override
	public MonitoredStation getMonitoredStation(Long ruleId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjet.MonitoredStationDAO#getMonitoredStationsList(java.lang.String)
	 */
	@Override
	public List<MonitoredStation> getMonitoredStationsList(String callsign) {
        ArrayList<MonitoredStation> infos = new ArrayList<MonitoredStation>();
        Connection dbconn = null;
        PreparedStatement selectMonitoredStations = null;
        ResultSet rs = null;
        try { 
            dbconn = ds.getConnection();
            String cleanCall = callsign.toUpperCase().trim();
            selectMonitoredStations = dbconn .prepareStatement(
                    "SELECT * from monitored_stations where upper(callsign)=?" );
            selectMonitoredStations.setString(1, cleanCall);
            rs = selectMonitoredStations.executeQuery();
            while ( rs.next() ) {
            	MonitoredStation retval = new MonitoredStation();
                retval.setCallsign(rs.getString("callsign"));
                retval.setUserId(rs.getLong("user_id"));
                retval.setStationId(rs.getLong("station_id"));
                infos.add(retval);
            }
        } catch ( SQLException sqlex ) {
        	System.err.println("SQL Exception:  "+sqlex);
        	sqlex.printStackTrace();
        } finally {
            try {
                dbconn.close();
            } catch ( Exception ex ) {}
        }
		return infos;
	}

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjat.db.MonitoredStationDAO#getMonitoredStationsByUserId(java.lang.Long)
	 */
	@Override
	public List<MonitoredStation> getMonitoredStationsByUserId(Long userId) {
        ArrayList<MonitoredStation> infos = new ArrayList<MonitoredStation>();
        Connection dbconn = null;
        PreparedStatement selectMonitoredStations = null;
        ResultSet rs = null;
        try { 
            dbconn = ds.getConnection();
            selectMonitoredStations = dbconn .prepareStatement(
                    "SELECT * from monitored_stations where user_id=?" );
            selectMonitoredStations.setLong(1, userId);
            rs = selectMonitoredStations.executeQuery();
            while ( rs.next() ) {
            	MonitoredStation retval = new MonitoredStation();
                retval.setCallsign(rs.getString("callsign"));
                retval.setUserId(rs.getLong("user_id"));
                retval.setStationId(rs.getLong("station_id"));
                infos.add(retval);
            }
        } catch ( SQLException sqlex ) {
        	System.err.println("SQL Exception:  "+sqlex);
        	sqlex.printStackTrace();
        } finally {
            try {
                dbconn.close();
            } catch ( Exception ex ) {}
        }
		return infos;
	}

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjat.db.MonitoredStationDAO#getMonitoredStationsByUserId(java.lang.Long)
	 */
	@Override
	public List<MonitoredStation> getMonitoredStations() {
        ArrayList<MonitoredStation> infos = new ArrayList<MonitoredStation>();
        Connection dbconn = null;
        PreparedStatement selectMonitoredStations = null;
        ResultSet rs = null;
        try { 
            dbconn = ds.getConnection();
            selectMonitoredStations = dbconn .prepareStatement(
                    "SELECT * from monitored_stations" );
            rs = selectMonitoredStations.executeQuery();
            while ( rs.next() ) {
            	MonitoredStation retval = new MonitoredStation();
                retval.setCallsign(rs.getString("callsign"));
                retval.setUserId(rs.getLong("user_id"));
                retval.setStationId(rs.getLong("station_id"));
                infos.add(retval);
            }
        } catch ( SQLException sqlex ) {
        	System.err.println("SQL Exception:  "+sqlex);
        	sqlex.printStackTrace();
        } finally {
            try {
                dbconn.close();
            } catch ( Exception ex ) {}
        }
		return infos;
	}
}
