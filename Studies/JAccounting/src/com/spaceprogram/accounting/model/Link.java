
 
package com.spaceprogram.accounting.model;

/** 
 *
 * @author  prophecy
 * @version 
 */
/*@nullable_by_default@*/
public class Link extends Object {

	String url;
	String title;
	String target;
	boolean selected;
    private String onClick;


    public Link(String title, String url){

		this.title = title;
        	this.url = url;
	}
	public Link(String title, String url, String target){
		this.url = url;
		this.title = title;
		this.target = target;
	}
	public String getTitle(){
		return title;
	}
	public String getTarget(){
		return target;
	}
	public String getURL(){
		return url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

    public String createLink(){
        String ret = "<a href=\"" + url + "\"";
        if(target != null){
            ret += " target=\"" + target + "\"";

        }
        ret += ">" + title + "</a>";
        return ret;
    }

    public void setOnClick(String s) {
        onClick = s;
    }

    public String getOnClick() {
        return onClick;
    }
}
