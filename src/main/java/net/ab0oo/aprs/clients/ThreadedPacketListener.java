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

import java.io.DataOutputStream;
import java.util.Vector;

import net.ab0oo.aprs.parser.APRSPacket;

/**
 * @author johng
 *
 */
public class ThreadedPacketListener implements Runnable {
    private Object syncObject;
    private Vector<Object> queue;
    private PacketListener packetListener;

    public ThreadedPacketListener( PacketListener packetListener, Vector<Object> queue, Object syncObject ) {
        this.packetListener = packetListener;
        this.queue = queue;
        this.syncObject = syncObject;
    }
    /* (non-Javadoc)
     * @see net.ab0oo.aprs.clients.PacketListener#processPacket(net.ab0oo.aprs.parser.APRSPacket)
     */
    @Override
    public void run() {
        int size = 0;
        Object obj;
        long lastErrMsgTime = 0L;
        while (true) {
            synchronized (syncObject) {
                while ( (size = queue.size()) <= 0 ) {
                    try {
                        syncObject.wait();
                    } catch (InterruptedException ie) {}
                } 
                // At this point, we are guaranteed: that size is the
                // current queue size and size > 0, so we can remove
                // one object.
                obj = queue.remove(0);
                // Decrement size to take into account the remove. We
                // still own the lock so we don't need to requery the
                // queue size.
                size--;
                if ( obj instanceof APRSPacket ) {
                    packetListener.processPacket( (APRSPacket)obj );
                }
                if ( size > 10 && System.currentTimeMillis() > lastErrMsgTime+30000 ) {
                    System.err.println(size+" pending APRS packets in "+packetListener.getClass().getSimpleName());
                    lastErrMsgTime = System.currentTimeMillis();
                }
            }
        }

    }

    public void setOutputStream(DataOutputStream outToServer) {
        if ( packetListener != null ) {
            packetListener.setOutputStream(outToServer);
        }
    }

    /**
     * @param syncObject the syncObject to set
     */
    public final void setSyncObject(Object syncObject) {
        this.syncObject = syncObject;
    }

    /**
     * @param queue the queue to set
     */
    public final void setQueue(Vector<Object> queue) {
        this.queue = queue;
    }

    /**
     * @param packetListener the packetListener to set
     */
    public final void setPacketListener(PacketListener packetListener) {
        this.packetListener = packetListener;
    }

}
