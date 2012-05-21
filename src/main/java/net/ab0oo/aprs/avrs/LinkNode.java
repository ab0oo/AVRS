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
package net.ab0oo.aprs.avrs;
/**
 * @author johng
 *
 * Members of this class represent a single type of Internet-enabled link node,
 * usually either EchoLink, IRLP, or WIRES.
 */

public class LinkNode implements Comparable<LinkNode> {
	private String callsign;
	private String nodeType; 
	private double frequency;
	private String tone;
	private double latitude, longitude;
	private int node;
	private double distance;

	/**
	 * 
	 * @param node
	 * Create a default node, with only a node number and type to identify it.
	 */
	public LinkNode(int node, String type) {
		this.node=node;
		this.nodeType=type;
		this.callsign="N0CALL";
		this.frequency=146.00;
		this.tone="0.0";
		this.latitude = 0.0;
		this.longitude=0.0;
	}
	
	/**
	 * @return the callsign
	 */
	public String getCallsign() {
		return callsign;
	}
	/**
	 * @param callsign the callsign to set
	 */
	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}
	/**
	 * @return the frequency
	 */
	public double getFrequency() {
		return frequency;
	}
	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}
	/**
	 * @return the tone
	 */
	public String getTone() {
		return tone;
	}
	/**
	 * @param tone the tone to set
	 */
	public void setTone(String tone) {
		this.tone = tone;
	}
	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	/**
	 * @return the node
	 */
	public int getNode() {
		return node;
	}
	/**
	 * @param node the node to set
	 */
	public void setNode(int node) {
		this.node = node;
	}

	/**
	 * @return the distance
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

	@Override
	public int compareTo(LinkNode o) {
		if ( this.distance > o.getDistance()) return 1;
		if ( this.distance < o.getDistance()) return -1;
		return 0;
	}

	/**
	 * @return the nodeType
	 */
	public String getNodeType() {
		return nodeType;
	}

	/**
	 * @param nodeType the nodeType to set
	 */
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	
	
}
