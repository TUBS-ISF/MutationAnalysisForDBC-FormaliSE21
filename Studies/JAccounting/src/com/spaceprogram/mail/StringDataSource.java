/*

hmmmm, this may not work as well as planned, use org.apache.soap.util.mime.ByteArrayDataSource instead

 * Created by IntelliJ IDEA.
 * User: User
 * Date: Oct 22, 2002
 * Time: 1:21:19 PM
 * To change this template use Options | File Templates.
 */
package com.spaceprogram.mail;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.activation.DataSource;

public class StringDataSource implements DataSource {
    String s;
    StringReader in;
    StringWriter out;
    public InputStream getInputStream() throws IOException {
        return null;
    }

    public OutputStream getOutputStream() throws IOException {
        return null;
    }

    public String getContentType() {
        return null;
    }

    public String getName() {
        return null;
    }

}
