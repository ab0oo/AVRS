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

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * @author johng
 * 
 */
public class SimpleMessageProducer {

	@Autowired
	protected JmsTemplate jmsTemplate;
	private static int i=0;

	public void sendMessage(final Serializable payload) throws JMSException {
		jmsTemplate.send(new MessageCreator() {
			@Override
            public Message createMessage(Session session) throws JMSException {
				ObjectMessage message = session.createObjectMessage(payload);
				message.setIntProperty("messageCount", i);
				return message;
			}
		});
		i++;
	}

	/**
	 * @param jmsTemplate the jmsTemplate to set
	 */
	public final void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}
}
