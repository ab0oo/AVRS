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
/*
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
*/
/**
 * @author johng
 *
 */

public class SpringHibernateMonitoredStationDAO { // extends HibernateDaoSupport implements MonitoredStationDAO {

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjet.MonitoredStationDAO#getMonitoredStation(java.lang.Long)
	 */
	/*
	@Override
	public MonitoredStations getMonitoredStation(Long ruleId) {
		// TODO Auto-generated method stub
		return null;
	}
*/
	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjet.MonitoredStationDAO#getMonitoredStationsList(java.lang.String)
	 */
/*
	@SuppressWarnings("unchecked")
	@Override
	public List<MonitoredStations> getMonitoredStationsList(String callsign) {
		List<?> ms = getHibernateTemplate().find("from MonitoredStations as ms where monitored_callsign=?", callsign);
		return (List<MonitoredStations>)ms;
	}
*/
}
