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
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.ab0oo.aprs.parser.APRSPacket;
import net.ab0oo.aprs.parser.MessagePacket;

/**
 * @author johng This class encases the logic required to send an ACK to a message originator
 * 
 */
public class AckThread extends ConversationThread implements Runnable {

    private String   recipient;
    private String   msgNumber;
    private String   source;
    private boolean  ack;

    public AckThread(boolean ack, String source, String recipient, String msgNumber, int count, DataOutputStream stream, ScheduledExecutorService timer) {
        super();
        this.source = source;
        this.recipient = recipient;
        this.msgNumber = msgNumber;
        this.count = count;
        this.stream = stream;
        this.ack = ack;
        this.timer = timer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Callable#call()
     */
    @Override
    public void run() {
        MessagePacket mp = new MessagePacket(recipient, (ack ? "ack" : "rej"), msgNumber);
        APRSPacket outgoingPacket = new APRSPacket(source, "APZ013", null, mp);
        if (count <= 0) {
            System.out.println("Completed sending acks for msg "+msgNumber+" to "+recipient);
            return;
        } else {
        	count--;
        	try {
        		stream.writeBytes(outgoingPacket.toString()+"\n");
        		System.out.println("Sending "+outgoingPacket.toString());
        	} catch (IOException ioex) {
        		System.err.println("Unable to send ACK to " + recipient + ": " + ioex);
        	}
        	timer.schedule(this, 10L, TimeUnit.SECONDS);
        }
    }

}
