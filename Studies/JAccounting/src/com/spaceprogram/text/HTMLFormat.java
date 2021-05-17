/*
 * HTMLFormat.java
 *
 * Created on June 19, 2000, 7:49 PM
 */
 
package com.spaceprogram.text;

/**
 *
 * @author  prophecy
 * @version
 */
public class HTMLFormat extends Object {

	public final static String whitespace_chars = "\r\n \"\t<>()[]:,";
	
	boolean convert_line_breaks = true;
	boolean convert_links = false;
	
	
	char http_string[] = { 'h','t','t','p',':','/','/' };
	char https_string[] = { 'h','t','t','p','s',':','/','/' };
	char http_string2[] = { 'w', 'w', 'w', '.' };
	String link_start = "<a href=\""; // default for hyperlinks starting
	String link_end = "\">";
	
	
	String email_link_start = "<a href=\"mailto:"; // default for mailto links
	String email_link_end = "\">";
	
	
	  /** Creates new HTMLFormat */
	  public HTMLFormat() {

	  }

	public String format(String s){
		StringBuffer sb = new StringBuffer(s);
		//int len = s.length();
		char lastword[] = new char[30];
		int lw_size = 0;
		char c;
		String tab_string = "&nbsp;&nbsp;&nbsp;&nbsp;";
		int word_start = -1;
		//int word_end = -1;
		int http_index = 0;
		int http_index2 = 0;
		int https_index = 0;
		int is_email = -1;
		//int link_start = -1;
		//boolean is_link = false;
		//boolean first_space = true;
		//System.out.println("formatting");
		for(int i = 0; i < sb.length(); i++){
			//System.out.println("loop: i = " + i + " sb.length() = " + sb.length());
			c = sb.charAt(i);
			
			boolean foundchar = false;
			if(convert_links){
				
				if(c == http_string[http_index]){
					http_index++;
					foundchar = true;
				}
				else http_index = 0;
				
				if(c == http_string2[http_index2]){ // this one is for www. strings
					http_index2++;
					foundchar = true;
				}
				else http_index2 = 0;
				
				if(c == https_string[https_index]){ // for https strings
					https_index++;
					foundchar = true;
				}
				else https_index = 0;
				
				
				// now see if full string has been found
				if(http_index == http_string.length){
					http_index = 0;
					i = insertLinkIntoBuffer(sb,word_start,i);
					continue;
					
				}
				else if(http_index2 == http_string2.length){
					// www. found
					http_index2 = 0;
					i = insertLinkIntoBuffer(sb,word_start,i);
					continue;
				}
				else if(https_index == https_string.length){
					// https found
					https_index = 0;
					i = insertLinkIntoBuffer(sb,word_start,i);
					continue;
				}
				
				
				
				if(is_email == -1){
					if(c == '@'){
					// then maybe an email
						is_email = i; // holds position of @ symbol
						continue;
					}
				}
				else {// if(is_email != -1){
					if(c == '.'){
						// then getting closer
						if(i > is_email + 1){
							if(whitespace_chars.indexOf(sb.charAt(i+1)) == -1){ // so no whitespace
								// therefore assume this is an email
								int word_end = getWordEnd(sb,i);
								String word = sb.substring(word_start,word_end);
								String link = makeEmailLink(word);
								int link_length = link.length();
								sb.replace(word_start,word_end,link);
								i = word_start + link_length;
								is_email = -1;
							}
							else{
								is_email = -1;
							}
						}
						else{
							is_email = -1;
						}
						continue;
					}
				}
	
			}
			
			
			if(c == ' '){
				/*if(first_space){
					first_space = false;
				}
				else{
					sb.replace(i, (i + 1), "&nbsp;");
					i += 5;
					first_space = true;
				}*/
				if(i > 0 && sb.charAt(i-1) == ' '){
					sb.replace(i, (i + 1), "&nbsp;");
					i += 5;
				}
				word_start = -1;
			}
			/*else if(c == '.'){
				//if(is_email == -1){
					word_start = -1;
				//}
			}*/
			
					
			else if(c == ':'){
				if(!foundchar){
					word_start = -1;
				}
			}
			else if(c == '\n'){
				//System.out.println("i = " +i);
				if(convert_line_breaks){
					sb.insert(i, "<br>");
					i += 4;
				}
				word_start = -1;
				//System.out.println("4th char is " + sb.charAt(i-1) + "  5th char is: " + sb.charAt(i));
			}
			else if(c == '\t'){
				sb.replace(i, (i+1), tab_string);
				i += tab_string.length()-1;
				word_start = -1;
			}
			else if(c == '<'){
				sb.replace(i, (i + 1), "&lt;");
				i += 3;
				word_start = -1;
			}
			else if(c == '>'){
				sb.replace(i, (i + 1), "&gt;");
				i += 3;
				word_start = -1;
			}
			else if(c == '"'){
				sb.replace(i, (i + 1), "&quot;");
				i += 5;
				word_start = -1;
			}
			else if(c == '&'){
				sb.replace(i, (i + 1), "&amp;");
				i += 4;
				word_start = -1;
			}
		
			else{
				if(word_start == -1 && whitespace_chars.indexOf(c) == -1){ // so not a whitespace char
					word_start = i;
				}
			}
			
			if(word_start == -1){ // then no word yet, so skip next part
				is_email = -1;
				continue;
			}
			
			
			
			
			/*else if(c == ':'){
				if(lw_size == 4){
					if(lastword[0] == 'h' && lastword[1] == 't' && lastword[2] == 't' && lastword[3] == 'p'){
						// then we have an http link but we need to get the whole link
						//sb.insert(i - 4, "<a href=\"
						
			else if(c == ' '){ // space so reset last word
				lw_size = 0;
			}
			else{
				lastword[lw_size] = Character.toLowerCase(c);
			}*/
				
		}
		return sb.toString();
		
          
	}
	int insertLinkIntoBuffer(StringBuffer sb, int word_start, int i){
		int word_end = getWordEnd(sb,i);
		String word = sb.substring(word_start,word_end);
		String link = makeLink(word);
		int link_length = link.length();
		sb.replace(word_start,word_end,link);
		return word_start + link_length;
	}
		
	/////////////////////
	// Takes a string and turns it into a link
	public String makeLink(String text){
		String link_text = text;
		if(!text.startsWith("http")){
			link_text = "http://" + text;
		}
		text = link_start + link_text + link_end + text + "</a>";
		return text;
	}
	public String makeEmailLink(String text){
		text = email_link_start + text + email_link_end + text + "</a>";
		return text;
	}
	public void setEmailStartLink(String s){
		email_link_start = s;
	}
	public void setEmailEndLink(String s){
		email_link_end = s;
	}
	public void setStartLink(String s){
		link_start = s;
	}
	public void setEndLink(String s){
		link_end = s;
	}
	int getWordEnd(StringBuffer sb, int word_start){
		int i = word_start;
		char c;
		while(true){
			if(++i >= sb.length()){
				//i--;
				break;
			}
			c = sb.charAt(i);
			if(whitespace_chars.indexOf(c) != -1){ // then whitespace found // TOOK OUT i == sb.length() ||
				//System.out.println("white space found");
				if(c == ','){ // anchordesk links have commas
					// so check if character after comma is whitespace
					if(whitespace_chars.indexOf(sb.charAt(i+1)) > -1){ // has whitespace
						// so end link or whatever it is.
						break;
					}
				}
				else break;
				
				
			}
		}
		int word_end = i;
		//System.out.println("word_start = " + word_start + "    word_end = " + word_end);
		//return = sb.substring(word_start,word_end);
		return word_end;
	}
	public void convertLineBreaks(boolean b){
		convert_line_breaks = b;
	}
	public void convertLinks(boolean b){
		convert_links = b;
	}
		


}
