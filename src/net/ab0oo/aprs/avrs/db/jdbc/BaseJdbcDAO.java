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

import javax.sql.DataSource;

/**
 * @author johng
 *
 */
public class BaseJdbcDAO {
    protected DataSource ds;

    public void setDataSource(DataSource ds ) {
        this.ds = ds;
    }

    public DataSource getDataSource() {
        return this.ds;
    }
    
	public Long getNextSequenceNumber(String sequenceName) {
		Connection dbconn = null;
		PreparedStatement selectSeqNum = null;
		ResultSet rs = null;
		int seqNum = 0;
		try {
			dbconn = ds.getConnection();
			selectSeqNum = dbconn.prepareStatement("SELECT nextval('"+sequenceName+"')");
			rs = selectSeqNum.executeQuery();
			while (rs.next()) {
				seqNum = rs.getInt(1);
			}
			dbconn.close();
		} catch (SQLException sqlex) {
			System.err.println("SQL Exception:  " + sqlex);
		}
		return new Long(seqNum);
	}

}
