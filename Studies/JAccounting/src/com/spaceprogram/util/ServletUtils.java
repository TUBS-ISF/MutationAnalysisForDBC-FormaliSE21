package com.spaceprogram.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** Some simple time savers.
 *  Part of tutorial on servlets and JSP that appears at
 *  http://www.apl.jhu.edu/~hall/java/Servlet-Tutorial/
 *  1999 Marty Hall; may be freely used or adapted.
 */

public class ServletUtils {
	public static final String DOCTYPE =
		"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">";
	
	public static String headWithTitle(String title) {
		return(DOCTYPE + "\n" +
				   "<HTML>\n" +
				   "<HEAD><TITLE>" + title + "</TITLE></HEAD>\n");
	}
	
	/** Read a parameter with the specified name, convert it to an int,
	 and return it. Return the designated default value if the parameter
	 doesn't exist or if it is an illegal integer format.
	 */
	
	public static int getIntParameter(HttpServletRequest request,
									  String paramName,
									  int defaultValue) {
		String paramString = request.getParameter(paramName);
        if(paramString == null){
            return defaultValue;
        }
		int paramValue;
		try {
			paramValue = Integer.parseInt(paramString);
		} catch(NumberFormatException nfe) {
			paramValue = defaultValue;
		}
		return(paramValue);
	}
	/**
	 Returns true if param exists
	 */
	public static boolean getBooleanParameter(HttpServletRequest request, String paramName) {
		boolean ret;
		if(request.getParameter(paramName) != null){
			ret = true;
		}
		else ret = false;
		return ret;
	}

    /**
     * Will return a string of length 0 if null.   Same as if passing "" as defaultValue in second version of this
     * method.
     */
	public static String getStringParameter(HttpServletRequest request, String paramName) {
		String ret = request.getParameter(paramName);
		if(ret == null){
			ret = "";
		}
		return ret;
	}
	public static String getStringParameter(HttpServletRequest request, String paramName, String defaultValue) {
		String ret = request.getParameter(paramName);
		if(ret == null){
			ret = defaultValue;
		}
		return ret;
	}

	public static double getDoubleParameter(HttpServletRequest request, String paramName, double defaultValue) {

		String paramString = request.getParameter(paramName);
		if(paramString == null){
            return defaultValue;
        }
        double paramValue;
		try {
			paramValue = Double.parseDouble(paramString);
		} catch(NumberFormatException nfe) {
			paramValue = defaultValue;
		}
		return paramValue;


	}
	// Approximate values are fine.
	/*
	 public static final int SECONDS_PER_MONTH = 60*60*24*30;
	 public static final int SECONDS_PER_YEAR = 60*60*24*365;*/
	
	public static void displayAllParameters(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws IOException {
	    PrintWriter out = response.getWriter();
	    String title = "Reading All Request Parameters";
	    out.println(
			"<H1 ALIGN=CENTER>" + title + "</H1>\n" +
				"<TABLE BORDER=1 ALIGN=CENTER>\n" +
				"<TR BGCOLOR=\"#FFAD00\">\n" +
				"<TH>Parameter Name<TH>Parameter Value(s)");
	    Enumeration paramNames = request.getParameterNames();
	    while(paramNames.hasMoreElements()) {
			String paramName = (String)paramNames.nextElement();
			out.println("<TR><TD>" + paramName + "\n<TD>");
			String[] paramValues = request.getParameterValues(paramName);
			if (paramValues.length == 1) {
				String paramValue = paramValues[0];
				if (paramValue.length() == 0)
					out.print("<I>No Value</I>");
				else
					out.print(paramValue);
			} else {
				out.println("<UL>");
				for(int i=0; i<paramValues.length; i++) {
					out.println("<LI>" + paramValues[i]);
				}
				out.println("</UL>");
			}
	    }
	    out.println("</TABLE>\n");
		
	}
	public static String getAllParametersAsHTML(javax.servlet.http.HttpServletRequest request){
		String ret;
		String title = "Reading All Request Parameters";
	    ret = "<H1 ALIGN=CENTER>" + title + "</H1>\n" +
			"<TABLE BORDER=1 ALIGN=CENTER>\n" +
			"<TR BGCOLOR=\"#FFAD00\">\n" +
			"<TH>Parameter Name<TH>Parameter Value(s)";
	    Enumeration paramNames = request.getParameterNames();
	    while(paramNames.hasMoreElements()) {
			String paramName = (String)paramNames.nextElement();
			ret += "<TR><TD>" + paramName + "\n<TD>";
			String[] paramValues = request.getParameterValues(paramName);
			if (paramValues.length == 1) {
				String paramValue = paramValues[0];
				if (paramValue.length() == 0)
					ret += "<I>No Value</I>";
				else
					ret += paramValue;
			} else {
				ret += "<UL>";
				for(int i=0; i<paramValues.length; i++) {
					ret += "<LI>" + paramValues[i];
				}
				ret += "</UL>";
			}
	    }
	    ret += "</TABLE>\n";
		return ret;
	}

    public static boolean returnFile(HttpServletResponse response, HttpServletRequest request, String directory, String file_name, String content_type, boolean use_persistance, boolean use_encoding) throws FileNotFoundException, IOException{
		boolean ok = true; // return var
		String file_path = directory + file_name;
		File f = new File(file_path);
		if(!f.exists()){
			throw new FileNotFoundException("Return File Not Found.");
		}
		response.reset();
		OutputStream out = (OutputStream)(response.getOutputStream());

		response.setContentType(content_type);
		//response.setHeader("Content-Disposition: attachment; filename="fname.ext", file_name);
		// the following one makes it come up with a save as or open from dialog
		// response.setHeader("Content-Disposition", "attachment; filename=" + file_name);
		// the following does it inline as thought you were to click a link normally
		response.setHeader("Content-Disposition", "inline; filename=" + file_name);

		// THIS PERSISTANCE CRAP WON'T WORK, WHAT THE HELL???
		/*if(use_persistance){
			long file_length = f.length();
			response.setContentLength((int)file_length);
		 }*/
		// THIS GZIP CRAP WON'T WORK, WHAT THE HELL???
		/*if(use_encoding){ // gzip encoding
			String encodings = request.getHeader("Accept-Encoding");
			if(encodings != null && encodings.indexOf("gzip") != -1){
				out = new BufferedOutputStream(new java.util.zip.GZIPOutputStream(out));
				response.setHeader("Content-Encoding", "gzip");
			}
			else{
				out = new BufferedOutputStream(out);
			}
		}
		 else{ */
			BufferedOutputStream bout = new BufferedOutputStream(out);
		//}

		BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
		int file_byte;

			//ut.print("got output stream...<br>"); out.flush();
		try{
			while((file_byte = in.read()) != -1){
				bout.write(file_byte);
			}
		}
		catch(java.net.SocketException ex){
			ok = false;
		}
		bout.flush();
		in.close();

		return ok;


	}

    public static boolean returnFile(HttpServletResponse response, HttpServletRequest request, String directory, String file_name, String content_type, boolean use_persistance, boolean use_encoding, String fileDisplayName) throws FileNotFoundException, IOException{
		boolean ok = true; // return var
		String file_path = directory + file_name;
		File f = new File(file_path);
		if(!f.exists()){
			throw new FileNotFoundException("Return File Not Found.");
		}
		response.reset();
		OutputStream out = (OutputStream)(response.getOutputStream());

		response.setContentType(content_type);
		//response.setHeader("Content-Disposition: attachment; filename="fname.ext", file_name);
		// the following one makes it come up with a save as or open from dialog
		// response.setHeader("Content-Disposition", "attachment; filename=" + file_name);
		// the following does it inline as thought you were to click a link normally
		response.setHeader("Content-Disposition", "inline; filename=" + fileDisplayName);

		// THIS PERSISTANCE CRAP WON'T WORK, WHAT THE HELL???
		/*if(use_persistance){
			long file_length = f.length();
			response.setContentLength((int)file_length);
		 }*/
		// THIS GZIP CRAP WON'T WORK, WHAT THE HELL???
		/*if(use_encoding){ // gzip encoding
			String encodings = request.getHeader("Accept-Encoding");
			if(encodings != null && encodings.indexOf("gzip") != -1){
				out = new BufferedOutputStream(new java.util.zip.GZIPOutputStream(out));
				response.setHeader("Content-Encoding", "gzip");
			}
			else{
				out = new BufferedOutputStream(out);
			}
		}
		 else{ */
			BufferedOutputStream bout = new BufferedOutputStream(out);
		//}

		BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
		int file_byte;

			//ut.print("got output stream...<br>"); out.flush();
		try{
			while((file_byte = in.read()) != -1){
				bout.write(file_byte);
			}
		}
		catch(java.net.SocketException ex){
			ok = false;
		}
		bout.flush();
		in.close();

		return ok;


	}

    public static BigDecimal getBigDecimalParameter(HttpServletRequest request, String param, String defaultValue) {
        String paramString = request.getParameter(param);
		if(paramString == null){
            return new BigDecimal(defaultValue);
        }
       BigDecimal paramValue;
		try {
			paramValue = new BigDecimal(paramString);
		} catch(NumberFormatException nfe) {
			paramValue = new BigDecimal(defaultValue);
		}
        return paramValue;

    }


}
