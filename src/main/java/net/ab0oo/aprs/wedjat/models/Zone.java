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
package net.ab0oo.aprs.wedjat.models;

import java.io.Serializable;

import net.ab0oo.aprs.parser.Position;

import org.postgis.Geometry;
import org.postgis.Point;

/**
 * @author johng
 *
 */
public class Zone implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long zoneId = -1L, userId;
	private Long pointRadius;
	private Geometry point, polygon;
	private String description;
	
	/**
	 * @return the zoneId
	 */
	public Long getZoneId() {
		return zoneId;
	}
	/**
	 * @param zoneId the zoneId to set
	 */
	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}
	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * @return the pointRadius
	 */
	public Long getPointRadius() {
		return pointRadius;
	}
	/**
	 * @param pointRadius the pointRadius to set
	 */
	public void setPointRadius(Long pointRadius) {
		this.pointRadius = pointRadius;
	}
	/**
	 * @return the point
	 */
	public Geometry getPoint() {
		return point;
	}
	/**
	 * @param point the point to set
	 */
	public void setPoint(Geometry point) {
		this.point = point;
	}
	/**
	 * @return the polygon
	 */
	public Geometry getPolygon() {
		return polygon;
	}
	/**
	 * @param polygon the polygon to set
	 */
	public void setPolygon(Geometry polygon) {
		this.polygon = polygon;
	}
	
	public Position getPointPosition() {
		Position pos = null;
		if ( point != null ) {
			Point p = point.getFirstPoint();
			pos = new Position(p.getX(), p.getY());
		}
		return pos;
	}
	/**
	 * @return the description
	 */
	public final String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public final void setDescription(String description) {
		this.description = description;
	}
}
