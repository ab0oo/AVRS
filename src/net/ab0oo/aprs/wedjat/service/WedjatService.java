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
package net.ab0oo.aprs.wedjat.service;

import java.util.List;

import net.ab0oo.aprs.parser.Position;
import net.ab0oo.aprs.wedjat.models.AlertHistory;
import net.ab0oo.aprs.wedjat.models.MonitoredStation;
import net.ab0oo.aprs.wedjat.models.Notification;
import net.ab0oo.aprs.wedjat.models.NotificationAddress;
import net.ab0oo.aprs.wedjat.models.ReferencePoint;
import net.ab0oo.aprs.wedjat.models.Rule;
import net.ab0oo.aprs.wedjat.models.User;
import net.ab0oo.aprs.wedjat.models.Zone;

/**
 * @author johng
 *
 */
public interface WedjatService {
	// monitored station methods
	List<MonitoredStation> getMonitoredStationsList(String callsign);
	List<MonitoredStation> getMonitoredsStationsListByUserId(Long userId);
	MonitoredStation getMonitoredStationByStationId(Long stationId);
	public List<MonitoredStation> getMonitoredStations();
	
	// notification address methods 
	NotificationAddress getNotificationAddress(Long notificationAddressId);
	List<NotificationAddress> getNotificationAddressesByUserId(Long userId);
	
	// notification methods
	List<Notification> getNotificationsByRuleId(Long ruleId);
	List<Notification> getNotificationsByUserId(Long userId);
	
	// rule methods
	List<Rule> getActiveRulesByStationId(Long stationId);
	List<Rule> getRulesByUserId(Long userId);
	int resetRuleTimer(Long ruleId);
	
	// user methods
	User getUser(Long userId);

	// zone methods
	Zone getZone(Long zoneId);
	List<Zone> getZonesByUserId(Long userId);
	boolean isIncursion(Position lastPosition, Position currentPosition, Long zoneId);
	boolean isExcursion(Position lastPosition, Position currentPosition, Long zoneId);
	public List<ReferencePoint> listClosestCities(Position position);
	public AlertHistory getAlertHistoryById(Long alertId);
	public List<AlertHistory> getAlertHistoryByUserId(Long userId);
	public void saveAlertHistory(AlertHistory alertHistory);
}
