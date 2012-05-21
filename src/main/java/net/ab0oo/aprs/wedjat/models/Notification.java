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

/**
 * @author johng
 *
 */
public class Notification implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long notificationId  = -1L; 
	private Long notificationAddressId, ruleId, userId;
	private Integer startTime, endTime, validDays;

	/**
	 * @return the notificationId
	 */
	public Long getNotificationId() {
		return notificationId;
	}
	/**
	 * @param notificationId the notificationId to set
	 */
	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}
	/**
	 * @return the notificationAddressId
	 */
	public Long getNotificationAddressId() {
		return notificationAddressId;
	}
	/**
	 * @param notificationAddressId the notificationAddressId to set
	 */
	public void setNotificationAddressId(Long notificationAddressId) {
		this.notificationAddressId = notificationAddressId;
	}
	/**
	 * @return the ruleId
	 */
	public Long getRuleId() {
		return ruleId;
	}
	/**
	 * @param ruleId the ruleId to set
	 */
	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
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
	 * @return the startTime
	 */
	public Integer getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Integer startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the endTime
	 */
	public Integer getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the validDays
	 */
	public Integer getValidDays() {
		return validDays;
	}
	/**
	 * @param validDays the validDays to set
	 */
	public void setValidDays(Integer validDays) {
		this.validDays = validDays;
	}
	
}
