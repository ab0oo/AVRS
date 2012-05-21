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

import net.ab0oo.aprs.wedjat.db.NotificationDAO;
import net.ab0oo.aprs.wedjat.models.Notification;

/**
 * @author johng
 *
 */
public class JdbcNotificationDAO extends BaseJdbcDAO implements NotificationDAO {

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjet.db.NotificationDAO#getNotification(java.lang.Long)
	 */
	@Override
	public Notification getNotification(Long notificationId) {
		Connection dbconn = null;
		PreparedStatement selectNotification = null;
		ResultSet rs = null;
		Notification notification = null;
		try {
			dbconn = ds.getConnection();
			selectNotification = dbconn.prepareStatement("SELECT * from notifications where n_id=?");
			selectNotification.setLong(1, notificationId);
			rs = selectNotification.executeQuery();
			while (rs.next()) {
					notification = new Notification();
					notification.setUserId(rs.getLong("user_id"));
					notification.setRuleId(rs.getLong("rule_id"));
					notification.setNotificationAddressId(rs.getLong("na_id"));
					notification.setNotificationId(rs.getLong("n_id"));
					notification.setStartTime(rs.getInt("start_time"));
					notification.setEndTime(rs.getInt("end_time"));
					notification.setValidDays(rs.getInt("valid_days"));
			}
		} catch (SQLException sqlex) {
			System.err.println("SQL Exception:  " + sqlex);
        } finally {
            try {
                dbconn.close();
            } catch ( Exception ex ) {}
		}
		return notification;
	}

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjet.db.NotificationDAO#getNotificationsByRuleId(java.lang.Long)
	 */
	@Override
	public List<Notification> getNotificationsByRuleId(Long ruleId) {
		Connection dbconn = null;
		PreparedStatement selectNotification = null;
		ResultSet rs = null;
		List<Notification> notifications = new ArrayList<Notification>();
		try {
			dbconn = ds.getConnection();
			selectNotification = dbconn.prepareStatement("SELECT * from notifications where rule_id=?");
			selectNotification.setLong(1, ruleId);
			rs = selectNotification.executeQuery();
			while (rs.next()) { 
				Notification notification = new Notification();
				notification.setUserId(rs.getLong("user_id"));
				notification.setRuleId(rs.getLong("rule_id"));
				notification.setNotificationAddressId(rs.getLong("na_id"));
				notification.setNotificationId(rs.getLong("n_id"));
				notification.setStartTime(rs.getInt("start_time"));
				notification.setEndTime(rs.getInt("end_time"));
				notification.setValidDays(rs.getInt("valid_days"));
				notifications.add(notification);
			}
		} catch (SQLException sqlex) {
			System.err.println("SQL Exception:  " + sqlex);
        } finally {
            try {
                dbconn.close();
            } catch ( Exception ex ) {}
		}
		return notifications;
	}

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjet.db.NotificationDAO#getNotificationsByUserId(java.lang.Long)
	 */
	@Override
	public List<Notification> getNotificationsByUserId(Long userId) {
		Connection dbconn = null;
		PreparedStatement selectNotification = null;
		ResultSet rs = null;
		List<Notification> notifications = new ArrayList<Notification>();
		try {
			dbconn = ds.getConnection();
			selectNotification = dbconn.prepareStatement("SELECT * from notifications where user_id=?");
			selectNotification.setLong(1, userId);
			rs = selectNotification.executeQuery();
			while (rs.next()) { 
				Notification notification = new Notification();
				notification.setUserId(rs.getLong("user_id"));
				notification.setRuleId(rs.getLong("rule_id"));
				notification.setNotificationAddressId(rs.getLong("na_id"));
				notification.setNotificationId(rs.getLong("n_id"));
				notification.setStartTime(rs.getInt("start_time"));
				notification.setEndTime(rs.getInt("end_time"));
				notification.setValidDays(rs.getInt("valid_days"));
				notifications.add(notification);
			}
		} catch (SQLException sqlex) {
			System.err.println("SQL Exception:  " + sqlex);
        } finally {
            try {
                dbconn.close();
            } catch ( Exception ex ) {}
		}
		return notifications;
	}
}