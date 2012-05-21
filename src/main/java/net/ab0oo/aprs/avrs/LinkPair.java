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

/**
 * @author johng
 *
 */
public class LinkPair {
	private LinkNode sourceNode, targetNode;
	
	public LinkPair( LinkNode sourceNode, LinkNode targetNode) {
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
	}

	/**
	 * @return the sourceNode
	 */
	public LinkNode getSourceNode() {
		return sourceNode;
	}

	/**
	 * @param sourceNode the sourceNode to set
	 */
	public void setSourceNode(LinkNode sourceNode) {
		this.sourceNode = sourceNode;
	}

	/**
	 * @return the targetNode
	 */
	public LinkNode getTargetNode() {
		return targetNode;
	}

	/**
	 * @param targetNode the targetNode to set
	 */
	public void setTargetNode(LinkNode targetNode) {
		this.targetNode = targetNode;
	}
}
