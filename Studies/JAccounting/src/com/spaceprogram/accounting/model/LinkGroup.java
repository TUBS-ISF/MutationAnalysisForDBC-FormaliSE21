/**
 *
 * Will just hold a list of links, but you can set stuff on the group as a whole
 *
 * @author Travis Reeder - travis@spaceprogram.com
 * Date: May 15, 2002
 * Time: 11:28:40 AM
 * 
 */
package com.spaceprogram.accounting.model;

import com.spaceprogram.accounting.model.Link;

import java.util.ArrayList;

public class LinkGroup {
	java.util.List links;

	public LinkGroup() {
		links =  new ArrayList();
	}

	public LinkGroup(java.util.List links) {
		this.links = links;
	}

	public void setSelectedByTitle(String title){
		for (int i = 0; i < links.size(); i++) {
			Link link = (Link) links.get(i);
			if(link.getTitle().equals(title)){
				link.setSelected(true);
			}
		}
	}

	public java.util.List getLinks() {
		return links;
	}

	public void setLinks(java.util.List links) {
		this.links = links;
	}
	public void addLink(Link link){
		links.add(link);
	}
}
