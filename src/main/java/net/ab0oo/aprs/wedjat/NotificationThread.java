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

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import net.ab0oo.aprs.parser.Position;
import net.ab0oo.aprs.parser.PositionPacket;
import net.ab0oo.aprs.parser.Utilities;
import net.ab0oo.aprs.wedjat.models.AlertHistory;
import net.ab0oo.aprs.wedjat.models.MonitoredStation;
import net.ab0oo.aprs.wedjat.models.Notification;
import net.ab0oo.aprs.wedjat.models.NotificationAddress;
import net.ab0oo.aprs.wedjat.models.ReferencePoint;
import net.ab0oo.aprs.wedjat.models.Rule;
import net.ab0oo.aprs.wedjat.models.User;
import net.ab0oo.aprs.wedjat.models.Zone;
import net.ab0oo.aprs.wedjat.service.WedjatService;

/**
 * @author johng
 * 
 */
public class NotificationThread implements Runnable {

    static DecimalFormat degFmt = new DecimalFormat("###.0");
    static DecimalFormat distFmt = new DecimalFormat("###.00");
	static final String FROM_ADDRESS = "alert@aprs-alert.net";
	private List<NotificationInfo> notificationList;
	private Object lockingObject;
	private WedjatService wedjatService;

	public NotificationThread() {
		System.out.println("Notification thread starting");
	}

	/*
	 * The NotificationThread uses a simple subscriber model to wait() on an object.
	 * The thread merely parks on the lockingObject waiting for a notify().  When it
	 * gets one, it pulls all pending notifications off the list and begins sending
	 * the notifications via email.
	 * TODO:  This should be more modular, to allow for different types of notifications
	 * For example, I should be able to send an APRS message via APRS-IS as a notification 
	 */
	@Override
	public void run() {
		NotificationInfo oneInfo = null;
		while (true) {
			try {
				@SuppressWarnings("unused")
				int size = 0;
				oneInfo = null;
				synchronized (lockingObject) {
					while ((size = notificationList.size()) <= 0) {
						try {
							lockingObject.wait(60000);
						} catch (InterruptedException ie) {
						}
					}
					if ( notificationList.size() > 0 ) {
					    oneInfo = notificationList.remove(0);
					    size--;
					}
					if (oneInfo != null) {
						notify(oneInfo);
					} else {
					    System.err.println(new Date()+":  Managed to fall out of the wait() without an object");
					}
				}
			} catch (Throwable t) {
				System.err.println("HURL!  " + t);
				t.printStackTrace();
			}
		}
	}

	private void notify(NotificationInfo info) {
		Rule rule = info.getRule();
		PositionPacket positionPacket = info.getPosition();
		Position position = positionPacket.getPosition();
		MonitoredStation ms = wedjatService.getMonitoredStationByStationId(rule.getStationId());
		String sourceCall = ms.getCallsign();
		String alertString = sourceCall + " is moving";
		User user = wedjatService.getUser(rule.getUserId());
		TimeZone notificationTimeZone = user.getTimezone();
		String mSystem = user.getMeasurementSystem();
		String distanceUnit = " km ";
		if ( mSystem.equalsIgnoreCase("SAE")) {
			distanceUnit = " miles ";
		}
		if (rule.getRuleType().equals("INCURSION")) {
			Zone alertZone = wedjatService.getZone(rule.getZoneId());
			alertString = sourceCall + " has entered zone "+ alertZone.getDescription();
		} else if (rule.getRuleType().equals("EXCURSION")) {
			Zone alertZone = wedjatService.getZone(rule.getZoneId());
			alertString = sourceCall + " has exited zone "+ alertZone.getDescription();
		} else if (rule.getRuleType().equals("MOVEMENT")) {
			List<ReferencePoint> referencePoints = wedjatService.listClosestCities(position);
			ReferencePoint rp1 = referencePoints.get(0);
			double distance =  rp1.getMetersDistance() / 1000f;
			if ( mSystem.equalsIgnoreCase("SAE")) {
				distance = Utilities.metersToMiles(distance * 1000);
			}
			alertString += ". "+distFmt.format(distance)+ distanceUnit +
			Utilities.degressToCardinal(rp1.getBearingTo()) + " of "+
				rp1.getCity()+", "+rp1.getRegion();
		}

		List<Notification> notifications = wedjatService.getNotificationsByRuleId(rule.getRuleId());
		int count = 0;
		Calendar calendar = new GregorianCalendar();
		calendar.setTimeZone(notificationTimeZone);
		int minuteOfDay = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);
		int today = calendar.get(Calendar.DAY_OF_WEEK) -1; //make days 0-based, Sunday=0, Saturday=6
		for (Notification notification : notifications) {
			// check time and day values here. for now, we're just going to page
			boolean validDay = ((int)Math.pow(2,today) & notification.getValidDays()) == Math.pow(2, today);
			if ( notification.getStartTime() <= minuteOfDay && 
					notification.getEndTime() > minuteOfDay && validDay) {
				NotificationAddress nAddress = wedjatService.getNotificationAddress(notification.getNotificationAddressId());
				String toAddress = nAddress.getEmailAddress();
				System.out.println(new Date()+": Sending " + alertString + " to " + toAddress);
				SendMail sender = new SendMail(FROM_ADDRESS, toAddress,"APRS Alert", alertString);
				sender.send();
				AlertHistory ah = new AlertHistory(rule.getUserId(), alertString);
				wedjatService.saveAlertHistory(ah);
				count++;
			} else {
				System.out.println(new Date()+": Notification for rule "+rule.getRuleId()+" outside notification window");
			}
		}
		System.out.println(new Date()+": Sent "+count+" notifications for rule "+rule.getRuleId());
	}

	/**
	 * @param notificationList
	 *            the notificationList to set
	 */
	public final void setNotificationList(List<NotificationInfo> notificationList) {
		this.notificationList = notificationList;
	}

	/**
	 * @param lockingObject
	 *            the lockingObject to set
	 */
	public final void setLockingObject(Object lockingObject) {
		this.lockingObject = lockingObject;
	}

	/**
	 * @param wedjatService the wedjatService to set
	 */
	public final void setWedjatService(WedjatService wedjatService) {
		this.wedjatService = wedjatService;
	}

}
