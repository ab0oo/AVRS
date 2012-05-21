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

import net.ab0oo.aprs.parser.Position;
import net.ab0oo.aprs.wedjat.db.ZoneDAO;
import net.ab0oo.aprs.wedjat.models.ReferencePoint;
import net.ab0oo.aprs.wedjat.models.Zone;

import org.postgresql.PGConnection;

/**
 * @author johng
 * 
 */
public class JdbcZoneDAO extends BaseJdbcDAO implements ZoneDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.ab0oo.aprs.wedjet.db.ZoneDAO#getZone(java.lang.Long)
	 */
	@Override
	public Zone getZone(Long zoneId) {
		Connection dbconn = null;
		PreparedStatement selectNotification = null;
		ResultSet rs = null;
		Zone zone = null;
		try {
			dbconn = ds.getConnection();
			try {
				((PGConnection) dbconn).addDataType("geometry", Class.forName("org.postgis.PGgeometry"));
			} catch (ClassNotFoundException nfe) {
				System.err.println("Aaiiiiiii:  " + nfe);
			}
			selectNotification = dbconn.prepareStatement("SELECT * from zones where zone_id=?");
			selectNotification.setLong(1, zoneId);
			rs = selectNotification.executeQuery();
			while (rs.next()) {
				zone = new Zone();
				zone.setUserId(rs.getLong("user_id"));
				zone.setZoneId(rs.getLong("zone_id"));
				zone.setDescription(rs.getString("description"));
				zone.setPointRadius(rs.getLong("point_radius"));
			}
		} catch (SQLException sqlex) {
			System.err.println("SQL Exception:  " + sqlex);
        } finally {
            try {
                dbconn.close();
            } catch ( Exception ex ) {}
		}
		return zone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.ab0oo.aprs.wedjet.db.ZoneDAO#getZonesByUserId(java.lang.Long)
	 */
	@Override
	public List<Zone> getZonesByUserId(Long userId) {
		Connection dbconn = null;
		PreparedStatement selectNotification = null;
		ResultSet rs = null;
		List<Zone> zones = new ArrayList<Zone>();
		try {
			dbconn = ds.getConnection();
			try {
				((PGConnection) dbconn).addDataType("geometry", Class.forName("org.postgis.PGgeometry"));
			} catch (ClassNotFoundException nfe) {
				System.err.println("Aaiiiiiii:  " + nfe);
			}
			selectNotification = dbconn.prepareStatement("SELECT * from zones where user_id=?");
			selectNotification.setLong(1, userId);
			rs = selectNotification.executeQuery();
			while (rs.next()) {
				Zone zone = new Zone();
				zone.setUserId(rs.getLong("user_id"));
				zone.setZoneId(rs.getLong("zone_id"));
				zone.setDescription(rs.getString("desription"));
				zone.setPointRadius(rs.getLong("point_radius")); 
				zones.add(zone);
			}
		} catch (SQLException sqlex) {
			System.err.println("SQL Exception:  " + sqlex);
        } finally {
            try {
                dbconn.close();
            } catch ( Exception ex ) {}
		}
		return zones;
	}
	
	@Override
    public int getDistanceInMeters( Long zoneId, Position position) {
		Connection dbconn = null;
		PreparedStatement selectNotification = null;
		ResultSet rs = null;
		int distance = -1;
		try {
			dbconn = ds.getConnection();
			String geomText = "GeometryFromText('POINT("+position.getLongitude()+" "+position.getLatitude()+")',4326)";
			selectNotification = dbconn.prepareStatement(
					"SELECT st_distance(point_geom,"+geomText+") from zones where zone_id=?");
			selectNotification.setLong(1, zoneId);
			rs = selectNotification.executeQuery();
			while (rs.next()) {
				distance = Math.round(rs.getFloat(1));
			}
		} catch (SQLException sqlex) {
			System.err.println("SQL Exception:  " + sqlex);
        } finally {
            try {
                dbconn.close();
            } catch ( Exception ex ) {}
		}
		return distance;
	}

	@Override
    public boolean isContained( Long zoneId, Position position) {
		Connection dbconn = null;
		PreparedStatement testGeometry = null;
		ResultSet rs = null;
		boolean contains = false;
		try {
			dbconn = ds.getConnection();
			String sql = "SELECT st_within(GeometryFromText('POINT("+position.getLongitude()+
				" "+position.getLatitude()+")',4326), poly_geom) from zones where zone_id=?";
			//System.out.println(sql);
			testGeometry = dbconn.prepareStatement(sql);
			testGeometry.setLong(1, zoneId);
			rs = testGeometry.executeQuery();
			while (rs.next()) {
				contains = rs.getBoolean(1);
			}
			if ( contains ) return contains;
			String geomText = "ST_Transform(GeometryFromText('POINT("+position.getLongitude()+
				" "+position.getLatitude()+")',4326),2163)";
			sql = "SELECT ST_DWithin("+geomText+",ST_Transform(point_geom,2163), point_radius) " +
				"from zones where zone_id=?";
			//System.out.println(sql);
			testGeometry = dbconn.prepareStatement(sql);
			testGeometry.setLong(1, zoneId);
			rs = testGeometry.executeQuery();
			while (rs.next()) {
				contains = rs.getBoolean(1);
			}
		} catch (SQLException sqlex) {
			System.err.println("SQL Exception:  " + sqlex);
        } finally {
            try {
                dbconn.close();
            } catch ( Exception ex ) {}
		}
		return contains;
	}

    @Override
    public boolean isExcursion( Position initialPosition, Position secondPosition, Long zoneId ) {
    	boolean wasIn = isContained(zoneId, initialPosition);
    	boolean isIn = isContained(zoneId, secondPosition);
    	if ( wasIn && !isIn ) return true;
    	return false;
    }
    
    @Override
    public boolean isIncursion( Position initialPosition, Position secondPosition, Long zoneId ) {
    	boolean wasIn = isContained(zoneId, initialPosition);
    	boolean isIn = isContained(zoneId, secondPosition);
    	if ( !wasIn && isIn ) return true;
    	return false;
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
            try {
                dbconn.close();
            } catch ( Exception ex ) {}
		}
		return retlist;
	}
}
