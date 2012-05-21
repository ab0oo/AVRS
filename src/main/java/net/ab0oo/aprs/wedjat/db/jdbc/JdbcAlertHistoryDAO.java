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
import java.util.Date;
import java.util.List;

import net.ab0oo.aprs.wedjat.db.AlertHistoryDAO;
import net.ab0oo.aprs.wedjat.models.AlertHistory;

/**
 * @author johng
 *
 */
public class JdbcAlertHistoryDAO extends BaseJdbcDAO implements AlertHistoryDAO {

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjat.db.AlertHistoryDAO#getAlertHistoryById(java.lang.Integer)
	 */
	@Override
	public AlertHistory getAlertHistoryById(Long alertId) {
		AlertHistory retval = null;
        Connection dbconn = null;
        PreparedStatement selectAlertHistory = null;
        ResultSet rs = null;
        try { 
            dbconn = ds.getConnection();
            selectAlertHistory = dbconn .prepareStatement(
                    "SELECT * from alert_history where alert_id=?" );
            selectAlertHistory.setLong(1, alertId);
            rs = selectAlertHistory.executeQuery();
            while ( rs.next() ) {
            	retval = new AlertHistory(rs.getLong("user_id"), rs.getString("message"));
            	retval.setAlertId(alertId);
            	retval.setAlertTime(new Date( rs.getTimestamp("sent_time").getTime()));
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
	 * @see net.ab0oo.aprs.wedjat.db.AlertHistoryDAO#getAlertHistoryByUserId(java.lang.Integer)
	 */
	@Override
	public List<AlertHistory> getAlertHistoryByUserId(Long userId) {
		List<AlertHistory> retval = new ArrayList<AlertHistory>();
        Connection dbconn = null;
        PreparedStatement selectAlertHistory = null;
        ResultSet rs = null;
        try { 
            dbconn = ds.getConnection();
            selectAlertHistory = dbconn .prepareStatement(
                    "SELECT * from alert_history where user_id=?" );
            selectAlertHistory.setLong(1, userId);
            rs = selectAlertHistory.executeQuery();
            while ( rs.next() ) {
           		AlertHistory ah = new AlertHistory(rs.getLong("user_id"), rs.getString("message"));
               	ah.setAlertId(rs.getLong("alert_id"));
               	ah.setAlertTime(new Date( rs.getTimestamp("sent_time").getTime()));
               	retval.add(ah);
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
	 * @see net.ab0oo.aprs.wedjat.db.AlertHistoryDAO#saveAlertHistory(net.ab0oo.aprs.wedjat.models.AlertHistory)
	 */
	@Override
	public void saveAlertHistory(AlertHistory alertHistory) {
        Connection dbconn = null;
        PreparedStatement insertAlertHistory = null;
        try { 
            dbconn = ds.getConnection();
            insertAlertHistory = dbconn .prepareStatement(
                    "insert into alert_history (user_id, message) values (?,?)" );
            insertAlertHistory.setLong(1, alertHistory.getUserId());
            insertAlertHistory.setString(2, alertHistory.getMessage());
            insertAlertHistory.executeUpdate();
        } catch ( SQLException sqlex ) {
        	System.err.println("SQL Exception:  "+sqlex);
        	sqlex.printStackTrace();
        } finally {
            try {
                dbconn.close();
            } catch ( Exception ex ) {}
        }
	}

}
