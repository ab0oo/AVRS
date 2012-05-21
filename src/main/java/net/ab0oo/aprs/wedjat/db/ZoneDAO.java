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
package net.ab0oo.aprs.wedjat.db;

import java.util.List;

import net.ab0oo.aprs.parser.Position;
import net.ab0oo.aprs.wedjat.models.ReferencePoint;
import net.ab0oo.aprs.wedjat.models.Zone;

/**
 * @author johng
 *
 */
public interface ZoneDAO {
	public Zone getZone(Long zoneId);
	public List<Zone> getZonesByUserId(Long userId);
	public int getDistanceInMeters( Long zoneId, Position position);
	public boolean isContained( Long zoneId, Position position);
	public boolean isExcursion( Position initialPosition, Position secondPosition, Long zoneId );
	public boolean isIncursion( Position initialPosition, Position secondPosition, Long zoneId );
	public List<ReferencePoint> listClosestCities(Position position);
}
