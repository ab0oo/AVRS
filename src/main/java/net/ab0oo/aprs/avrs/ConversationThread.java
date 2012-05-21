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
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author johng
 *
 */
public class ConversationThread implements Runnable {
    protected int count;
    protected DataOutputStream stream;
    protected Date createTime;
    protected ScheduledExecutorService timer;

    public ConversationThread() {
        createTime = new Date();
    }
    
    /**
     * @return the stream
     */
    public final DataOutputStream getStream() {
        return stream;
    }

    /**
     * @param stream the stream to set
     */
    public final void setStream(DataOutputStream stream) {
        this.stream = stream;
    }

    /**
     * @return the count
     */
    public final int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public final void setCount(int count) {
        this.count = count;
    }

    public final boolean isComplete() {
        return count <= 0;
    }
    
    @Override
    public void run() {
    }

    /**
     * @return the createTime
     */
    public final Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public final void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	/**
	 * @return the timer
	 */
	public ScheduledExecutorService getTimer() {
		return timer;
	}

	/**
	 * @param timer the timer to set
	 */
	public void setTimer(ScheduledExecutorService timer) {
		this.timer = timer;
	}; // must be overridden in subclasses
}
