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

/**
 * @author johng
 *
 */
public class ReferencePoint {
	String city;
	String region;
	Double bearingTo;
	Double bearingFrom;
	Double metersDistance;
	
	public ReferencePoint(String city, String region, Double bearingTo, Double metersDistance ) {
		this.city=city;
		this.region=region;
		this.bearingTo = bearingTo;
		if ( bearingTo >= 180 ) {
			this.bearingFrom = bearingTo - 180.0; 
		} else {
			this.bearingFrom = bearingTo + 180.0;
		}
		this.metersDistance = metersDistance;
	}
	
	/**
	 * @return the name
	 */
	public final String getCity() {
		return city;
	}
	/**
	 * @param name the name to set
	 */
	public final void setCity(String name) {
		this.city = name;
	}

	/**
	 * @return the region
	 */
	public final String getRegion() {
		return region;
	}

	/**
	 * @param region the region to set
	 */
	public final void setRegion(String region) {
		this.region = region;
	}

	/**
	 * @return the bearingTo
	 */
	public final Double getBearingTo() {
		return bearingTo;
	}
	/**
	 * @param bearingTo the bearingTo to set
	 */
	public final void setBearingTo(Double bearingTo) {
		this.bearingTo = bearingTo;
	}
	/**
	 * @return the bearingFrom
	 */
	public final Double getBearingFrom() {
		return bearingFrom;
	}
	/**
	 * @param bearingFrom the bearingFrom to set
	 */
	public final void setBearingFrom(Double bearingFrom) {
		this.bearingFrom = bearingFrom;
	}
	/**
	 * @return the metersDistance
	 */
	public final Double getMetersDistance() {
		return metersDistance;
	}
	/**
	 * @param metersDistance the metersDistance to set
	 */
	public final void setMetersDistance(Double metersDistance) {
		this.metersDistance = metersDistance;
	}
}
