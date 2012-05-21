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

import java.util.Date;

/**
 * @author johng
 *
 */
public class AlertHistory {
	private Long userId;
	private Long alertId;
	private Date alertTime;
	private String message;
	
	public AlertHistory( Long userId, String message ) {
		this.alertId=0l;
		this.userId=userId;
		this.message = message;
		this.alertTime = new Date();
	}
	
	/**
	 * @return the userId
	 */
	public final Long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public final void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * @return the alertId
	 */
	public final Long getAlertId() {
		return alertId;
	}
	/**
	 * @param alertId the alertId to set
	 */
	public final void setAlertId(Long alertId) {
		this.alertId = alertId;
	}
	/**
	 * @return the message
	 */
	public final String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public final void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the alertTime
	 */
	public final Date getAlertTime() {
		return alertTime;
	}

	/**
	 * @param alertTime the alertTime to set
	 */
	public final void setAlertTime(Date alertTime) {
		this.alertTime = alertTime;
	}
	
}
