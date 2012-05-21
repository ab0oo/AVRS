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

/**
 * @author johng
 *
 */
public class Rule implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long ruleId = -1L, userId, stationId, zoneId;
	private String ruleType;
	private Long cycleTime;
	private Date nextEnabled;
	
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
	 * @return the ruleType
	 */
	public String getRuleType() {
		return ruleType;
	}
	/**
	 * @param ruleType the ruleType to set
	 */
	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}
	/**
	 * @return the cycleTime
	 */
	public Long getCycleTime() {
		return cycleTime;
	}
	/**
	 * @param cycleTime the cycleTime to set
	 */
	public void setCycleTime(Long cycleTime) {
		this.cycleTime = cycleTime;
	}
	/**
	 * @return the nextEnabled
	 */
	public Date getNextEnabled() {
		return nextEnabled;
	}
	/**
	 * @param nextEnabled the nextEnabled to set
	 */
	public void setNextEnabled(Date nextEnabled) {
		this.nextEnabled = nextEnabled;
	}
	/**
	 * @param stationId the stationId to set
	 */
	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}
	/**
	 * @return the stationId
	 */
	public Long getStationId() {
		return stationId;
	}
	/**
	 * @return the zoneId
	 */
	public final Long getZoneId() {
		return zoneId;
	}
	/**
	 * @param zoneId the zoneId to set
	 */
	public final void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}
}
