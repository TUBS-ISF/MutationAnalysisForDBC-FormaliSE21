/*
 * MessageData.java
 *
 * Created on June 23, 2000, 4:07 PM
 */
 
package com.spaceprogram.mail;

//import java.util.*;
//import javax.mail.*;
//import javax.mail.internet.*;

class AttachmentData {
	String filename;
	String content_type;
	long filesize;
	
	//@ post filename.equals(f);
	AttachmentData(String f, String c, long fs){
		filename = f;
		content_type = c;
		filesize = fs;
	}
}
