/**
 * @author Travis Reeder - travis@spaceprogram.com
 * Date: May 14, 2002
 * Time: 3:20:29 PM
 *
 */
package com.spaceprogram.accounting.model;



import java.util.ArrayList;
import java.util.List;

public class PageElements {

	String pageTitle;
	java.util.List extraHead = new ArrayList();
	String sectionColor;
	String sectionTitle;
	String mainPage;
	java.util.List extraNav = new ArrayList();
	LinkGroup navLinkGroup = new LinkGroup();
	LinkGroup subNavLinkGroup = new LinkGroup();
    List errors = new ArrayList();


	boolean showSearch = false;
	String searchSection;

	//com.thinkvirtual.structures.FolderTree folderTree = null;

	// string for extra params to be appended to links.
	String urlParams = "";
/*
	public com.thinkvirtual.structures.FolderTree getFolderTree() {
		return folderTree;
	}

	public void setFolderTree(com.thinkvirtual.structures.FolderTree folderTree) {
		this.folderTree = folderTree;
	}*/

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public String getSectionTitle() {
		return sectionTitle;
	}

	public String getUrlParams() {
		return urlParams;
	}

	public void setUrlParams(String urlParams) {
		this.urlParams = urlParams;
	}
	public void addUrlParam(String x){
		this.urlParams += "&" + x;
	}

	public void setSectionTitle(String sectionTitle) {
		this.sectionTitle = sectionTitle;
	}

	public String getSectionColor() {
		return sectionColor;
	}

	public void setSectionColor(String sectionColor) {
		this.sectionColor = sectionColor;
	}

	public String getMainPage() {
		return mainPage;
	}

	public void setMainPage(String mainPage) {
		this.mainPage = mainPage;
	}

	public java.util.List getNavLinks() {
		return navLinkGroup.getLinks();
	}

	public void setExtraHead(java.util.List extraHead) {
		this.extraHead = extraHead;
	}

	public LinkGroup getNavLinkGroup() {
		return navLinkGroup;
	}

	public void setNavLinkGroup(LinkGroup navLinkGroup) {
		this.navLinkGroup = navLinkGroup;
	}

	public LinkGroup getSubNavLinkGroup() {
		return subNavLinkGroup;
	}

	public void setSubNavLinkGroup(LinkGroup subNavLinkGroup) {
		this.subNavLinkGroup = subNavLinkGroup;
	}

	public void setNavLinks(java.util.List navLinks) {
		this.navLinkGroup.setLinks(navLinks);
	}
	public void addNavLink(Link l){
		this.navLinkGroup.addLink(l);
	}

	public java.util.List getSubNavLinks() {
		return subNavLinkGroup.getLinks();
	}

	public void setSubNavLinks(java.util.List subNavLinks) {
		subNavLinkGroup.setLinks(subNavLinks);
	}
	public void addSubNavLink(Link l){
		subNavLinkGroup.addLink(l);
	}

	public String getExtraHead() {
		String ret = "";
		for (int i = 0; i < extraHead.size(); i++) {
			ret += extraHead.get(i);
		}
		return ret;
	}

	public void addExtraHead(String s) {
		extraHead.add(s);
	}

	public String getExtraNav() {
		String ret = "";
		for (int i = 0; i < extraNav.size(); i++) {
			ret += extraNav.get(i);
		}
		return ret;
	}

	public void setExtraNav(List extraNav) {
		this.extraNav = extraNav;
	}
	public void addExtraNav(String s){
		extraNav.add(s);
	}

	public boolean showSearch() {
		return showSearch;
	}

	public void setShowSearch(boolean showSearch) {
		this.showSearch = showSearch;
	}

	public String getSearchSection() {
		return searchSection;
	}

	public void setSearchSection(String searchSection) {
		this.searchSection = searchSection;
		showSearch = true;
	}

    public void addError(String s) {
        errors.add(s);
    }

    public String getErrorsAsString() {
        String ret = "";
        for (int i = 0; i < errors.size(); i++) {
            String s = (String) errors.get(i);
            ret += s + "<br>";
        }
        return ret;
    }

    public boolean hasErrors() {
        return errors.size() > 0 ? true : false;
    }

    public boolean hasExtraNav(){
        if(extraNav== null || extraNav.size() == 0){
            return false;
        }
        return true;
    }

}
