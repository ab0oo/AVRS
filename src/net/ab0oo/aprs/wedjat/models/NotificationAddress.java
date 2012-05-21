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
public class NotificationAddress implements Serializable {

	private static final long serialVersionUID = 1L;
	private String emailAddress;
	private boolean shortForm, primary;
	private Long naId = -1L, userId;

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}
	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	/**
	 * @return the shortForm
	 */
	public boolean isShortForm() {
		return shortForm;
	}
	/**
	 * @param shortForm the shortForm to set
	 */
	public void setShortForm(boolean shortForm) {
		this.shortForm = shortForm;
	}
	/**
	 * @return the primary
	 */
	public boolean isPrimary() {
		return primary;
	}
	/**
	 * @param primary the primary to set
	 */
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}
	/**
	 * @return the naId
	 */
	public Long getNaId() {
		return naId;
	}
	/**
	 * @param naId the naId to set
	 */
	public void setNaId(Long naId) {
		this.naId = naId;
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
}
