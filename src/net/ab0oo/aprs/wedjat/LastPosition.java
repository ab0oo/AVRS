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
package net.ab0oo.aprs.wedjat;

import java.io.Serializable;
import java.util.Date;

import net.ab0oo.aprs.parser.Position;

/**
 * @author johng
 *
 */
public class LastPosition implements Serializable {
	private static final long serialVersionUID = 1L;
	private Date positionTimestamp;
	private Position lastPosition;
	private Date lastNotification;
	
	public LastPosition( Date postionTimestamp, Position lastPosition) {
		this.positionTimestamp = postionTimestamp;
		this.lastPosition = lastPosition;
	}
	
	public LastPosition( Position lastPosition) {
		this.positionTimestamp = new Date();
		this.lastPosition = lastPosition;
	}

	/**
	 * @return the positionTimestamp
	 */
	public final Date getPositionTimestamp() {
		return positionTimestamp;
	}

	/**
	 * @param positionTimestamp the positionTimestamp to set
	 */
	public final void setPositionTimestamp(Date positionTimestamp) {
		this.positionTimestamp = positionTimestamp;
	}

	/**
	 * @return the lastPosition
	 */
	public final Position getLastPosition() {
		return lastPosition;
	}

	/**
	 * @param lastPosition the lastPosition to set
	 */
	public final void setLastPosition(Position lastPosition) {
		this.lastPosition = lastPosition;
	}

	/**
	 * @return the lastNotification
	 */
	public final Date getLastNotification() {
		return lastNotification;
	}

	/**
	 * @param lastNotification the lastNotification to set
	 */
	public final void setLastNotification(Date lastNotification) {
		this.lastNotification = lastNotification;
	}
	
}
