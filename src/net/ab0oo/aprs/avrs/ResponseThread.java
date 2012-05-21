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

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.ab0oo.aprs.parser.APRSPacket;
import net.ab0oo.aprs.parser.InformationField;
import net.ab0oo.aprs.parser.MessagePacket;
import net.ab0oo.aprs.parser.ObjectPacket;

/**
 * @author johng This class encases the logic required to send a new Message to
 *         a recipient
 * 
 */

public class ResponseThread extends ConversationThread implements Runnable {

	APRSPacket outgoingPacket;
	HashMap<String, TimerTask> scheduledMessages = new HashMap<String, TimerTask>();

	public ResponseThread(APRSPacket outgoingPacket, int count, int txInterval,
			DataOutputStream stream, ScheduledExecutorService timer) {
		super();
		this.outgoingPacket = outgoingPacket;
		this.count = count;
		this.stream = stream;
		this.timer = timer;
	}

	@Override
    public void run() {
		InformationField info = outgoingPacket.getAprsInformation();
		if (info instanceof MessagePacket) {
			MessagePacket mp = (MessagePacket) info;
			String msgNumber = mp.getMessageNumber();
			String recipient = mp.getTargetCallsign();
			if (count <= 0) {
				System.out.println("Marking transmission " + msgNumber + " to " + recipient + " as expired");
				return;
			} else {
				count--;
				try {
					stream.writeBytes(outgoingPacket.toString() + "\n");
				} catch (IOException ioex) {
					System.err.println("Unable to send MSG to " + recipient + ": " + ioex);
				}
				timer.schedule(this, 5L, TimeUnit.SECONDS);
			}
		} else if (info instanceof ObjectPacket) {
			ObjectPacket op = (ObjectPacket) info;
			if (count <= 0) {
				System.out.println(new Date()
						+ ":  Completed transmitting packet for object " + op.getObjectName());
				return;
			} else {
				count--;
				try {
					stream.writeBytes(outgoingPacket.toString() + "\n");
				} catch (IOException ioex) {
					System.err.println("Unable to object packet for " + op.getObjectName() + ": " + ioex);
				}
				timer.schedule(this, 5L, TimeUnit.SECONDS);
			}
		}
	}
}
