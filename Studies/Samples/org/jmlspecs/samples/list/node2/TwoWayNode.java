// @(#) $Id: TwoWayNode.java 1199 2009-02-17 19:42:32Z smshaner $

// Copyright (C) 1998, 1999 Iowa State University

// This file is part of JML

// JML is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2, or (at your option)
// any later version.

// JML is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with JML; see the file COPYING.  If not, write to
// the Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139, USA.

// Author:  Clyde Ruby

package org.jmlspecs.samples.list.node2;

//@ refine "TwoWayNode.jml";

public class TwoWayNode extends OneWayNode { // Doubly Linked Node

    // data members

    protected /*@ nullable @*/ TwoWayNode prevNode_;

    public TwoWayNode(/*@ nullable @*/ Object ent) {
	entry_ = ent;
	prevNode_ = null;
	nextNode_ = null;
	nextLink_ = new DualLink(null);
    }
    public void insertAfter(/*@ nullable @*/ Object newEntry) {
	nextNode_ = new TwoWayNode(newEntry, this, (TwoWayNode)nextNode_);
    }
    public void removeNextNode() {
	if (nextNode_ != null) {
	    TwoWayNode nextNextNode = (TwoWayNode) nextNode_.getNextNode();

	    // remove the current links
	    // the next line is needed in case nextNode_ is aliased
	    ((TwoWayNode) nextNode_).linkTo(null);

	    this.linkTo(nextNextNode);
	}
    }
    public /*@ pure nullable @*/ TwoWayNode getPrevNode() {
	return prevNode_;
    }
    public /*@ pure nullable @*/ DualLink getPrevLink() {
	if (prevNode_ == null) {
	    return null;
	} else {
	    return (DualLink) prevNode_.getNextLink();
	}
    }
    public void insertBefore(/*@ nullable @*/ Object newEntry) {
	prevNode_ = new TwoWayNode(newEntry, prevNode_, this);
    }
    public void removePrevNode() {
	if (prevNode_ != null) {
	    TwoWayNode prevPrev = prevNode_.getPrevNode();
	    prevPrev.linkTo(this);
	}
    }
    public String toString() {
	String str = "";
	str += stringOfPrevEntries(((TwoWayNode)this).getPrevNode());
	str += " || ";
	str += stringOfEntries(this);
	return str;
    }

    /** The first invocation of this method should be with curr ==
     * prevNode_. If this is done, then the string returned will be a
     * concatentation of all nodes prior to this node up to and excluding the
     * end of the chain or this, which ever is reached first (i.e. this method
     * will terminate even for circular lists).
     */
    protected /*@ pure @*/ String stringOfPrevEntries(
                                     /*@ nullable @*/ TwoWayNode curr)
    {
	if (curr == null
	    // the following disjunct prevents infinite recursion
	    || curr == this) {
	    return "";
	}
	return stringOfPrevEntries(curr.getPrevNode())
	    + curr.getEntry() + ", ";
    }
    private /*@ helper @*/ void linkTo(/*@ nullable @*/ TwoWayNode nxtNode) {
	if (nextNode_ != null) {
	    // needed in case nextNode_ is aliased

	    ((TwoWayNode)nextNode_).prevNode_ = null;
	}
	nextNode_ = nxtNode;
	nextLink_ = new DualLink((TwoWayNode) nextNode_);
	if (nxtNode != null) {
	    nxtNode.prevNode_ = this;  // how to solve this problem
	}
    }
    protected TwoWayNode(/*@ nullable @*/ Object ent,
                         /*@ nullable @*/ TwoWayNode prvNode,
                         /*@ nullable @*/ TwoWayNode nxtNode)
    {
	this(ent);
	if (prvNode != null) {
	    prvNode.linkTo(this);
	}
	if (nxtNode != null) {
	    this.linkTo(nxtNode);
	}
    }
}

