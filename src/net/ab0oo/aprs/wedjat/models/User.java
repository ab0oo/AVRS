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
import java.util.Date;
import java.util.TimeZone;

/**
 * @author johng
 *
 */
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long userId = -1L;
	private String username, password;
	private Date createTimestamp;
	TimeZone timezone;
	private String measurementSystem;
	
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
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the createTimestamp
	 */
	public Date getCreateTimestamp() {
		return createTimestamp;
	}
	/**
	 * @param createTimestamp the createTimestamp to set
	 */
	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}
	/**
	 * @return the timezone
	 */
	public final TimeZone getTimezone() {
		return timezone;
	}
	/**
	 * @param timezone the timezone to set
	 */
	public final void setTimezone(TimeZone timezone) {
		this.timezone = timezone;
	}
	/**
	 * @return the measurementSystem
	 */
	public final String getMeasurementSystem() {
		return measurementSystem;
	}
	/**
	 * @param measurementSystem the measurementSystem to set
	 */
	public final void setMeasurementSystem(String measurementSystem) {
		this.measurementSystem = measurementSystem;
	}
}
