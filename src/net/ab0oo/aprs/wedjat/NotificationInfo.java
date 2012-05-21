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

import net.ab0oo.aprs.parser.PositionPacket;
import net.ab0oo.aprs.wedjat.models.Rule;

/**
 * @author johng
 *
 */
public class NotificationInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	Rule rule;
	PositionPacket position;
	String sourceCall;
	
	public NotificationInfo(String sourceCall, Rule rule, PositionPacket position) {
		this.sourceCall=sourceCall;
		this.rule=rule;
		this.position=position;
	}
	/**
	 * @return the rule
	 */
	public final Rule getRule() {
		return rule;
	}
	/**
	 * @param rule the rule to set
	 */
	public final void setRule(Rule rule) {
		this.rule = rule;
	}
	/**
	 * @return the position
	 */
	public final PositionPacket getPosition() {
		return position;
	}
	/**
	 * @param position the position to set
	 */
	public final void setPosition(PositionPacket position) {
		this.position = position;
	}
	/**
	 * @return the sourceCall
	 */
	public final String getSourceCall() {
		return sourceCall;
	}
	/**
	 * @param sourceCall the sourceCall to set
	 */
	public final void setSourceCall(String sourceCall) {
		this.sourceCall = sourceCall;
	}
}
