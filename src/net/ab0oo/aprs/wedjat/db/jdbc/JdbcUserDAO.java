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
import java.util.TimeZone;

import net.ab0oo.aprs.wedjat.db.UserDAO;
import net.ab0oo.aprs.wedjat.models.User;

/**
 * @author johng
 *
 */
public class JdbcUserDAO extends BaseJdbcDAO implements UserDAO {

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjet.db.UserDAO#getUser(java.lang.Long)
	 */
	@Override
	public User getUser(Long userId) {
		Connection dbconn = null;
		PreparedStatement selectUser = null;
		ResultSet rs = null;
		User user = null;
		try {
			dbconn = ds.getConnection();
			selectUser = dbconn.prepareStatement("SELECT * from users where user_id=?");
			selectUser.setLong(1, userId);
			rs = selectUser.executeQuery();
			while (rs.next()) {
					user = new User();
					user.setUsername(rs.getString("username"));
					user.setPassword(rs.getString("password"));
					user.setCreateTimestamp(rs.getTimestamp("create_time"));
					user.setUserId(userId);
					user.setTimezone(TimeZone.getTimeZone(rs.getString("timezone")));
					user.setMeasurementSystem(rs.getString("measurement_system"));
			}
		} catch (SQLException sqlex) {
			System.err.println("SQL Exception:  " + sqlex);
        } finally {
            try {
                dbconn.close();
            } catch ( Exception ex ) {}
		}
		return user;
	}
	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjet.db.UserDAO#getUser(java.lang.String)
	 */
	@Override
	public User getUser(String userName) {
		Connection dbconn = null;
		PreparedStatement selectUser = null;
		ResultSet rs = null;
		User user = null;
		try {
			dbconn = ds.getConnection();
			selectUser = dbconn.prepareStatement("SELECT * from users where username=?");
			selectUser.setString(1, userName);
			rs = selectUser.executeQuery();
			while (rs.next()) {
					user = new User();
					user.setUserId(rs.getLong("user_id"));
					user.setPassword(rs.getString("password"));
					user.setCreateTimestamp(rs.getTimestamp("createTime"));
					user.setTimezone(TimeZone.getTimeZone(rs.getString("timezone")));
					user.setMeasurementSystem(rs.getString("measurement_system"));
			}
		} catch (SQLException sqlex) {
			System.err.println("SQL Exception:  " + sqlex);
        } finally {
            try {
                dbconn.close();
            } catch ( Exception ex ) {}
		}
		return user;
	}
	
	@Override
    public boolean createUser(User newUser) {
		Connection dbconn = null;
		PreparedStatement createUserQuery = null;
		boolean success = false;
		if ( newUser.getUserId() < 1 )
			newUser.setUserId(getNextSequenceNumber("users_user_id_seq"));
		try {
			dbconn = ds.getConnection();
			createUserQuery = dbconn.prepareStatement("INSERT INTO users " +
					"(user_id,user_name,password,timezone,measurement_system) " +
					"values (?,?,?,?,?)");
			createUserQuery.setLong(1, newUser.getUserId());
			createUserQuery.setString(2,newUser.getUsername());
			createUserQuery.setString(3,newUser.getPassword());
			createUserQuery.setString(4, newUser.getTimezone().getDisplayName());
			createUserQuery.setString(5, newUser.getMeasurementSystem());
			success = createUserQuery.execute();
		} catch (SQLException sqlex) {
			System.err.println("SQL Exception:  " + sqlex);
        } finally {
            try {
                dbconn.close();
            } catch ( Exception ex ) {}
		}
		return success;
	}

	@Override
    public int updateUser(User newUser) {
		Connection dbconn = null;
		PreparedStatement updateUserQuery = null;
		int updated = 0;
		if ( newUser.getUserId() < 1 )
			newUser.setUserId(getNextSequenceNumber("users_user_id_seq"));
		try {
			dbconn = ds.getConnection();
			updateUserQuery = dbconn.prepareStatement("UPDATE users set " +
					"user_name=?,password=?,timezone=?, measurement_system=? " +
					" where user_ud=?");
			updateUserQuery.setString(1,newUser.getUsername());
			updateUserQuery.setString(2,newUser.getPassword());
			updateUserQuery.setString(3, newUser.getTimezone().getDisplayName());
			updateUserQuery.setString(4,newUser.getMeasurementSystem());
			updateUserQuery.setLong(5, newUser.getUserId());
			updated = updateUserQuery.executeUpdate();
		} catch (SQLException sqlex) {
			System.err.println("SQL Exception:  " + sqlex);
        } finally {
            try {
                dbconn.close();
            } catch ( Exception ex ) {}
		}
		return updated;
	}
}