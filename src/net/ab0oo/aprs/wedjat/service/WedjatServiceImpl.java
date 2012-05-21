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

import java.io.Serializable;
import java.util.List;

import net.ab0oo.aprs.parser.Position;
import net.ab0oo.aprs.wedjat.db.AlertHistoryDAO;
import net.ab0oo.aprs.wedjat.db.MonitoredStationDAO;
import net.ab0oo.aprs.wedjat.db.NotificationAddressDAO;
import net.ab0oo.aprs.wedjat.db.NotificationDAO;
import net.ab0oo.aprs.wedjat.db.RuleDAO;
import net.ab0oo.aprs.wedjat.db.UserDAO;
import net.ab0oo.aprs.wedjat.db.ZoneDAO;
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
public class WedjatServiceImpl implements WedjatService, Serializable {
	private static final long serialVersionUID = 1L;
	private MonitoredStationDAO monitoredStationDAO;
	private NotificationAddressDAO notificationAddressDAO;
	private NotificationDAO notificationDAO;
	private RuleDAO ruleDAO;
	private UserDAO userDAO;
	private ZoneDAO zoneDAO;
	private AlertHistoryDAO alertHistoryDAO;
	
	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjat.service.WedjatService#getActiveRulesByStationId(java.lang.Long)
	 */
	@Override
	public List<Rule> getActiveRulesByStationId(Long stationId) {
		return ruleDAO.getActiveRulesByStationId(stationId);
	}

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjat.service.WedjatService#getRulesByUserId(java.lang.Long)
	 */
	@Override
	public List<Rule> getRulesByUserId(Long userId) {
		return ruleDAO.getRulesByUserId(userId);
	}

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjat.service.WedjatService#getMonitoredStationByStationId(java.lang.Long)
	 */
	@Override
	public MonitoredStation getMonitoredStationByStationId(Long stationId) {
		return monitoredStationDAO.getMonitoredStationByStationId(stationId);
	}

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjat.service.WedjatService#getMonitoredStationsList(java.lang.String)
	 */
	@Override
	public List<MonitoredStation> getMonitoredStationsList(String callsign) {
		return monitoredStationDAO.getMonitoredStationsList(callsign);
	}

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjat.service.WedjatService#getMonitoredStationsList(java.lang.String)
	 */
	@Override
	public List<MonitoredStation> getMonitoredsStationsListByUserId(Long userId) {
		return monitoredStationDAO.getMonitoredStationsByUserId(userId);
	}

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjat.service.WedjatService#getMonitoredStations()
	 */
	@Override
	public List<MonitoredStation> getMonitoredStations() {
		return monitoredStationDAO.getMonitoredStations();
	}

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjat.service.WedjatService#getNotificationAddress(java.lang.Long)
	 */
	@Override
	public NotificationAddress getNotificationAddress(Long notificationAddressId) {
		return notificationAddressDAO.getNotificationAddress(notificationAddressId);
	}

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjat.service.WedjatService#getNotificationsByRuleId(java.lang.Long)
	 */
	@Override
	public List<Notification> getNotificationsByRuleId(Long ruleId) {
		return notificationDAO.getNotificationsByRuleId(ruleId);
	}

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjat.service.WedjatService#getUser(java.lang.Long)
	 */
	@Override
	public User getUser(Long userId) {
		return userDAO.getUser(userId);
	}

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjat.service.WedjatService#getZone(java.lang.Long)
	 */
	@Override
	public Zone getZone(Long zoneId) {
		return zoneDAO.getZone(zoneId);
	}

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjat.service.WedjatService#isExcursion(net.ab0oo.aprs.parser.Position, net.ab0oo.aprs.parser.Position, java.lang.Long)
	 */
	@Override
	public boolean isExcursion(Position lastPosition, Position currentPosition,
			Long zoneId) {
		return zoneDAO.isExcursion(lastPosition, currentPosition, zoneId);
	}

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjat.service.WedjatService#isIncursion(net.ab0oo.aprs.parser.Position, net.ab0oo.aprs.parser.Position, java.lang.Long)
	 */
	@Override
	public boolean isIncursion(Position lastPosition, Position currentPosition,
			Long zoneId) {
		return zoneDAO.isIncursion(lastPosition, currentPosition, zoneId);
	}

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjat.service.WedjatService#resetRuleTimer(java.lang.Long)
	 */
	@Override
	public int resetRuleTimer(Long ruleId) {
		return ruleDAO.resetRuleTimer(ruleId);
	}

	/**
	 * @param monitoredStationDAO the monitoredStationDAO to set
	 */
	public final void setMonitoredStationDAO(MonitoredStationDAO monitoredStationDAO) {
		this.monitoredStationDAO = monitoredStationDAO;
	}

	/**
	 * @param notificationAddressDAO the notificationAddressDAO to set
	 */
	public final void setNotificationAddressDAO(
			NotificationAddressDAO notificationAddressDAO) {
		this.notificationAddressDAO = notificationAddressDAO;
	}

	/**
	 * @param notificationDAO the notificationDAO to set
	 */
	public final void setNotificationDAO(NotificationDAO notificationDAO) {
		this.notificationDAO = notificationDAO;
	}

	/**
	 * @param ruleDAO the ruleDAO to set
	 */
	public final void setRuleDAO(RuleDAO ruleDAO) {
		this.ruleDAO = ruleDAO;
	}

	/**
	 * @param userDAO the userDAO to set
	 */
	public final void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	/**
	 * @param zoneDao the zoneDao to set
	 */
	public final void setZoneDAO(ZoneDAO zoneDao) {
		this.zoneDAO = zoneDao;
	}

	/**
	 * @param alertHistoryDao the alertHistoryDao to set
	 */
	public final void setAlertHistoryDAO(AlertHistoryDAO alertHistoryDao) {
		this.alertHistoryDAO = alertHistoryDao;
	}

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjat.service.WedjatService#getNotificationAddressesByUserId(java.lang.Long)
	 */
	@Override
	public List<NotificationAddress> getNotificationAddressesByUserId(
			Long userId) {
		return notificationAddressDAO.getNotificationAddresses(userId);
	}

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjat.service.WedjatService#getNotificationsByUserId(java.lang.Long)
	 */
	@Override
	public List<Notification> getNotificationsByUserId(Long userId) {
		return notificationDAO.getNotificationsByUserId(userId);
	}

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjat.service.WedjatService#getZonesByUserId(java.lang.Long)
	 */
	@Override
	public List<Zone> getZonesByUserId(Long userId) {
		return zoneDAO.getZonesByUserId(userId);
	}

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjat.service.WedjatService#listClosestCities(net.ab0oo.aprs.parser.Position)
	 */
	@Override
	public List<ReferencePoint> listClosestCities(Position position) {
		return zoneDAO.listClosestCities(position);
	}

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjat.service.WedjatService#getAlertHistoryById(java.lang.Integer)
	 */
	@Override
	public AlertHistory getAlertHistoryById(Long alertId) {
		return alertHistoryDAO.getAlertHistoryById(alertId);
	}

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjat.service.WedjatService#getAlertHistoryByUserId(java.lang.Integer)
	 */
	@Override
	public List<AlertHistory> getAlertHistoryByUserId(Long userId) {
		return alertHistoryDAO.getAlertHistoryByUserId(userId);
	}

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjat.service.WedjatService#saveAlertHistory(net.ab0oo.aprs.wedjat.models.AlertHistory)
	 */
	@Override
	public void saveAlertHistory(AlertHistory alertHistory) {
		alertHistoryDAO.saveAlertHistory(alertHistory);
	}

}
