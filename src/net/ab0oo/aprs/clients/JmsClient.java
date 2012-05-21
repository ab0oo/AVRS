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
package net.ab0oo.aprs.clients;

/**
 * @author johng
 *   This is a simple test class that connects to an APRS-IS server and parses the packets found
 *   there-in.
 */
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.ab0oo.aprs.parser.APRSPacket;

class JmsClient implements MessageListener {

	static DecimalFormat df = new DecimalFormat("###.000000");
	private List<PacketListener> processors = new ArrayList<PacketListener>();;
	
	public static void main(String argv[]) throws Exception {
		new JmsClient();
		while (true) {
			try {
				Thread.sleep(10000);
			} catch ( InterruptedException iex ) {
			}
		}
	}
	
	public JmsClient() throws Exception {
	}
	
	
	@Override
    public void onMessage(Message message) {
		APRSPacket packet = null;
		if ( !(message instanceof ObjectMessage) )
			return;
		ObjectMessage om = (ObjectMessage)message;
		try {
			Object o = om.getObject();
			if ( o instanceof APRSPacket ) {
				packet = (APRSPacket)o;
			}
		} catch (Exception ex) {
			System.err.println("Unable to extract APRS packet from JMS message:  "+ex);
			return;
		}
		if (packet.hasFault()) {return;}
		if (!packet.isAprs()) {return;}
		for ( PacketListener processor : processors ) {
			processor.processPacket(packet);
		}
	}


	/**
	 * @param processors the processors to set
	 */
	public final void setProcessors(List<PacketListener> processors) {
		this.processors = processors;
	}
	
	public final void setProcessor(PacketListener processor) {
		this.processors.add(processor);
	}

}