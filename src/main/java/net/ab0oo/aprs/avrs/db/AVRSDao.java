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
package net.ab0oo.aprs.avrs.db;

import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import javax.sql.DataSource;

import net.ab0oo.aprs.avrs.LinkNode;
import net.ab0oo.aprs.avrs.models.AllPositionEntry;
import net.ab0oo.aprs.parser.Position;
import net.ab0oo.aprs.avrs.models.ReferencePoint;

/**
 * @author johng
 *
 */
public interface AVRSDao {
    public TreeSet<LinkNode> getNodes(String callsign);
    public Date getLatestPosition(String callsign);
	public List<AllPositionEntry> getPositions( String callsign );
	public AllPositionEntry getPosition( String callsign );
    public void setDataSource(DataSource ds );
    public void saveLatestPosition(AllPositionEntry entry);
    public void saveLatestPositions(List<AllPositionEntry> entries);
    public void savePosition(AllPositionEntry entry);
    public void savePositions(List<AllPositionEntry> entries);
    public List<ReferencePoint> listClosestCities(Position position);

}
