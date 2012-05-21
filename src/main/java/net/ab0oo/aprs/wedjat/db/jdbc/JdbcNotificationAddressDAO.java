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

import net.ab0oo.aprs.wedjat.db.NotificationAddressDAO;
import net.ab0oo.aprs.wedjat.models.NotificationAddress;

/**
 * @author johng
 *
 */
public class JdbcNotificationAddressDAO extends BaseJdbcDAO implements
		NotificationAddressDAO {

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjet.db.NotificationAddressDAO#getNotificationAddress(java.lang.Long)
	 */
	@Override
	public NotificationAddress getNotificationAddress(Long notificationAddressId) {
        NotificationAddress retval = null;
        Connection dbconn = null;
        PreparedStatement selectNotificationAddress = null;
        ResultSet rs = null;
        try { 
            dbconn = ds.getConnection();
            selectNotificationAddress = dbconn .prepareStatement(
                    "SELECT * from notification_addresses where na_id=?" );
            selectNotificationAddress.setLong(1, notificationAddressId);
//            System.out.print("Going to the dbfor "+cleanCall+"...");
//            long now = System.currentTimeMillis();
            rs = selectNotificationAddress.executeQuery();
//            System.out.println("back in "+(System.currentTimeMillis()-now)+"ms");
            while ( rs.next() ) {
            	retval = new NotificationAddress();
                retval.setEmailAddress(rs.getString("address"));
                retval.setUserId(rs.getLong("user_id"));
                retval.setNaId(rs.getLong("na_id"));
                retval.setPrimary(rs.getBoolean("primary_address"));
                retval.setShortForm(rs.getBoolean("short_form"));
            }
        } catch ( SQLException sqlex ) {
        	System.err.println("SQL Exception:  "+sqlex);
        } finally {
            try {
                dbconn.close();
            } catch ( Exception ex ) {}
        }
		return retval;
	}

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjet.db.NotificationAddressDAO#getNotificationAddresses(java.lang.Long)
	 */
	@Override
	public List<NotificationAddress> getNotificationAddresses(Long userId) {
        List<NotificationAddress> infos = new ArrayList<NotificationAddress>();
        Connection dbconn = null;
        PreparedStatement selectNotificationAddress = null;
        ResultSet rs = null;
        try { 
            dbconn = ds.getConnection();
            selectNotificationAddress = dbconn .prepareStatement(
                    "SELECT * from notification_addresses where user_id=?" );
            selectNotificationAddress.setLong(1, userId);
//            System.out.print("Going to the dbfor "+cleanCall+"...");
//            long now = System.currentTimeMillis();
            rs = selectNotificationAddress.executeQuery();
//            System.out.println("back in "+(System.currentTimeMillis()-now)+"ms");
            while ( rs.next() ) {
            	NotificationAddress retval = new NotificationAddress();
                retval.setEmailAddress(rs.getString("email_address"));
                retval.setUserId(rs.getLong("user_id"));
                retval.setNaId(rs.getLong("na_id"));
                retval.setPrimary(rs.getBoolean("primary_address"));
                retval.setShortForm(rs.getBoolean("short_form"));
                infos.add(retval);
            }
        } catch ( SQLException sqlex ) {
        	System.err.println("SQL Exception:  "+sqlex);
        } finally {
            try {
                dbconn.close();
            } catch ( Exception ex ) {}
        }
		return infos;
	}

}
