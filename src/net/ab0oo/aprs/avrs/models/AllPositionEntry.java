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
package net.ab0oo.aprs.avrs.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import net.ab0oo.aprs.parser.Digipeater;
import net.ab0oo.aprs.parser.Position;

/**
 * @author johng
 * 
 */
public class AllPositionEntry implements Serializable, Comparable<AllPositionEntry> {
	private static final long serialVersionUID = 1L;
	private String callsign;
	private String destination;
	private String igate;
	private String[] digis = { null, null, null, null, null };
	private Position position = new Position();

	/**
	 * @return the callsign
	 */
	public final String getCallsign() {
		return callsign.trim().toUpperCase();
	}

	public final String getBaseCall() {
		if (callsign.contains("-")) {
			return callsign.substring(0, callsign.indexOf("-")).trim().toUpperCase();
		} else {
			return callsign;
		}
	}

	public final String getSsid() {
		if (callsign.contains("-")) {
			return callsign.substring(callsign.indexOf("-") + 1).trim().toUpperCase();
		} else {
			return "";
		}
	}

	/**
	 * @param callsign
	 *            the callsign to set
	 */
	public final void setCallsign(String callsign) {
		this.callsign = callsign;
	}

	/**
	 * @return the destination
	 */
	public final String getDestination() {
		return destination;
	}

	/**
	 * @param destination
	 *            the destination to set
	 */
	public final void setDestination(String destination) {
		this.destination = destination;
	}

	/**
	 * @return the toi
	 */
	public final Date getToi() {
		return this.position.getTimestamp();
	}

	/**
	 * @param toi
	 *            the toi to set
	 */
	public final void setToi(Date toi) {
		this.position.setTimestamp(toi);
	}

	/**
	 * @return the symbolTable
	 */
	public final Character getSymbolTable() {
		return this.position.getSymbolTable();
	}

	/**
	 * @param symbolTable
	 *            the symbolTable to set
	 */
	public final void setSymbolTable(char symbolTable) {
		this.position.setSymbolTable(symbolTable);
	}

	/**
	 * @return the symbol
	 */
	public final Character getSymbol() {
		return this.position.getSymbolCode();
	}

	/**
	 * @param symbol
	 *            the symbol to set
	 */
	public final void setSymbol(char symbol) {
		this.position.setSymbolCode(symbol);
	}

	/**
	 * @return the igate
	 */
	public final String getIgate() {
		return igate;
	}

	/**
	 * @param igate
	 *            the igate to set
	 */
	public final void setIgate(String igate) {
		this.igate = igate;
	}

	/**
	 * @return the digi
	 */
	public final String[] getDigis() {
		return digis;
	}

	public final String getDigi(int digiNum) {
		if (digiNum >= digis.length || digiNum < 0) {
			return null;
		}
		return digis[digiNum];
	}

	/**
	 * @param digi
	 *            the digi to set
	 */
	public final void setDigis(String[] digi) {
		this.digis = digi;
	}
	
	public final void setDigis(ArrayList<Digipeater> digiAL) {
		digis = new String[digiAL.size()];
		int i=0;
		for (Digipeater oneDigi : digiAL ) {
			digis[i] = oneDigi.toString();
			i++;
		}
		
	}

	/**
	 * @return the position
	 */
	public final Position getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public final void setPosition(Position position) {
		this.position = position;
	}
	
	public final Integer getPositionAmbiguity() {
		return this.position.getPositionAmbiguity();
	}
	
	public final void setPositionAmbiguity(int posAmb) {
		this.position.setPositionAmbiguity(posAmb);
	}
	
	public final Integer getAltitude() {
		return this.position.getAltitude();
	}
	
	public final void setAltitude(Integer altitude) {
		this.position.setAltitude(altitude);
	}
	
	public final Double getLongitude() {
		return this.position.getLongitude();
	}
	
	public final Double getLatitude() {
		return this.position.getLatitude();
	}
	
	public final void setLongitude(Double longitude) {
		this.position.setLongitude(longitude);
	}
	
	public final void setLatitude(Double latitude) {
		this.position.setLatitude(latitude);
	}

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(AllPositionEntry o) {
        if ( this.getPosition().getTimestamp().getTime() < o.getPosition().getTimestamp().getTime() ) return -1;
        if ( this.getPosition().getTimestamp().getTime() > o.getPosition().getTimestamp().getTime() ) return 1;
        return 0;
    }
}
