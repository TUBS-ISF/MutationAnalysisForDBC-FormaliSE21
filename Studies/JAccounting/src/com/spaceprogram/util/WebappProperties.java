package com.spaceprogram.util;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * For setting things like host url, mail server, etc
 *
 * Just for convenience, it's a singleton
 *
 * @author Travis Reeder - travis@spaceprogram.com
 * Date: May 16, 2003
 * Time: 10:24:16 PM
 * @version 0.1
 */
/*@nullable_by_default@*/
public class WebappProperties extends Properties{
    static WebappProperties props;
    public synchronized static WebappProperties getSingletonInstance() throws Exception {

        if(props == null){

            props = new WebappProperties();
            URL url = WebappProperties.class.getResource("/webapp.properties");
            if(url == null){
                props = null;
                throw new Exception("Could not find file: webapp.properties!");
            }
            InputStream is = url.openStream();
            props.load(is);
            is.close();
        }
        return props;
    }

}
