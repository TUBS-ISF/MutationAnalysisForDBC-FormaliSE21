/*
 * StringUtils.java
 *
 * Created on June 6, 2000, 10:53 AM
 */

package com.spaceprogram.util;

/**
 *
 * @author  prophecy
 * @version
 */
public class StringUtils extends Object {

    // WHITE SPACE UTILS
    public static final String whitespace = " \t\n\r";

    public static final String DIGIT_BAG = "1234567890";

    /**
     This function is probabably invalid.

     See stripCR for working code
     */
    public static String toJavascriptString(String s) {
        // replaces \n with \\n so it gets spit out properly
        // also " to \"
        /*StringBuffer sb = new StringBuffer(s);
        int index = s.indexOf('\n');
        while(index != -1){
            sb.replace(index-1,index+1,"\\n");
            s = sb.toString();
            index = s.indexOf('\n',index);
        }
         return s;*/
        StringBuffer sb = new StringBuffer(s);

        char c;
        int i;
        //int counter = 0;
        for (i = 0; i < sb.length(); i++) {
            c = sb.charAt(i);
            if (c == '\n') { // found back slash, so determine what is after
                sb.replace(i - 1, i + 1, "\\n");
                i++;
            }
            else if (c == '"') {
                sb.replace(i, i + 1, "'");
                //i++;
            }

        }
        return sb.toString();
    }

    public static String nullToString(String s) {
        if (s == null) {
            return "";
        }
        return s;
    }

    public static String stripCR(String s) {
        s = s.replace('\n', ' ');
        s = s.replace('\r', ' ');
        return s;

    }

    public static String stripCR(String s, String replace_with) {
        StringBuffer sb = new StringBuffer(s);

        char c;
        int i;
        //int counter = 0;
        int r_with_len = replace_with.length();
        //System.out.println("strip 1");
        boolean cr = false;
        for (i = 0; i < sb.length(); i++) {
            c = sb.charAt(i);

            if (c == '\r') {
                // check if \n follows
                cr = true;
            }
            else if (c == '\n') { // found back slash, so determine what is after
                if (cr) {
                    sb.replace(i - 1, i + 1, replace_with);
                }
                /* else
                 if(i > 0){
                sb.replace(i-1, i+1, replace_with);
                 }*/


                else
                    sb.replace(i, i + 1, replace_with);

                cr = false;
                //i+= r_with_len-1;
            }
        }

        return sb.toString();
    }

    public static String stripDoubleQuotes(String s) {
        s = s.replace('"', ' ');
        return s;
    }

    public static String SQLescape(String s) {
        StringBuffer sb = new StringBuffer(s);

        char c;
        for (int i = 0; i < sb.length(); i++) {
            c = sb.charAt(i);
            if (c == '\'') {
                sb.insert(i, '\'');
                i += 1;
            }
            else if (c == '\\') {
                sb.insert(i, '\\');
                i += 1;
            }
        }
        return sb.toString();
        /*int index1 = s.indexOf("'");
        String s1;
        String s2;
        boolean is_string = true;
        //String newstr;
        while(index1 >= 0){

            s1 = s.substring(0,index1); // first half
            s2 = s.substring(index1); // second half
            //System.out.println("\n\ns2 = \n" + s2);
            s = s1 + "'" + s2;
            //index = index1;
            index1 = s.indexOf("'", index1 + 1);
            //System.out.println(s);
        }
        return s;*/
    }

    /*String xmlEscape(String string)
    {
        int MAXSTRLEN = 2048; // from qstat, probably unecessary in this case.
        static String _buf[4][MAXSTRLEN];
        static int _buf_index= 0;
        String result, b;

        if ( string == null)
        return "";

        if ( strpbrk( string, "&<>") == null)
        return string;

        result= &_buf[_buf_index][0];
        _buf_index= (_buf_index+1) % 4;

        b= result;
        for ( ; *string; string++)  {
        switch ( *string)  {
        case '&':
            *b++= '&';
            *b++= 'a';
            *b++= 'm';
            *b++= 'p';
            *b++= ';';
            break;
        case '<':
            *b++= '&';
            *b++= 'l';
            *b++= 't';
            *b++= ';';
            break;
        case '>':
            *b++= '&';
            *b++= 'g';
            *b++= 't';
            *b++= ';';
            break;
        default:
            *b++= *string;
            break;
        }
        }
        *b= '\0';
        return result;
    }
*/

    public static String padString(String currstr, int length, char padchar) {
        return StringUtils.padString(currstr, length, padchar, 'r'); // default pad right

    }

    // extra param side to say which side of the string should be padded
    // 'l' for left, 'r' for right
    public static String padString(String currstr, int length, char padchar, char side) {
        if (side == 'r') {
            if (currstr.length() < length) {
                for (int i = currstr.length(); i < length; i++) {
                    currstr += padchar;
                }
            }
        }
        else { // left
            if (currstr.length() < length) {
                for (int i = currstr.length(); i < length; i++) {
                    currstr = padchar + currstr;
                }
            }
        }
        return currstr;

    }

    /**
     Convenience class for ints...  pads by default with zeros to the left side
     Same as above, but pads an integer and returns the string
     */
    public static String padInt(int currint, int length) {
        return padString(Integer.toString(currint), length, '0', 'l');
    }

    /**
     trims a string to a specific length
     */
    public static String trim(String s, int maxlength) {
        if (s != null) {
            if (maxlength < s.length()) {
                s = s.substring(0, maxlength);
            }
        }
        return s;
    }




    /**
     * gets rid of all whitespace
     *
     */
    // Removes all whitespace characters from s.
    // Global variable whitespace (see above)
    // defines which characters are considered whitespace.

    public static String stripWhitespace(String s) {
        return stripCharsInBag(s, whitespace);
    }

    /**
     *
     // Removes all characters which appear in string bag from string s.
     */
    public static String stripCharsInBag(String s, String bag) {
        StringBuffer sb = new StringBuffer(s);

        char c;
        int i;
        // Search through string's characters one by one.
        // If character is in bag, remove from buffer
        // or could append to a returnString, depends which is faster, but i don't know right now

        for (i = 0; i < sb.length(); i++) {
            c = sb.charAt(i);

            if (bag.indexOf(c) != -1) { // then it's there
                sb.deleteCharAt(i);
                i--; // get it back to proper place?
            }
        }

        return sb.toString();

    }

    /**
     // Removes all characters which do NOT appear in string bag
     // from string s.
     */
    public static String stripCharsNotInBag(String s, String bag) {
        StringBuffer sb = new StringBuffer(s);

        char c;
        int i;
        // Search through string's characters one by one.
        // If character is in bag, remove from buffer
        // or could append to a returnString, depends which is faster, but i don't know right now

        for (i = 0; i < sb.length(); i++) {
            c = sb.charAt(i);

            if (bag.indexOf(c) == -1) { // then it's not there, so remove
                sb.deleteCharAt(i);
                i--; // get it back to proper place?
            }
        }

        return sb.toString();
    }

    public static String stringReplace(String s, String replace, String replace_with) {
        StringBuffer sb = new StringBuffer(s);
        char[] rchars = replace.toCharArray();
        char c;
        int index = 0;
        for (int i = 0; i < sb.length(); i++) {
            //System.out.println("loop: i = " + i + " sb.length() = " + sb.length());
            c = sb.charAt(i);
            if (c == rchars[index]) {
                index++;

            }

            if (index == rchars.length) { // then complete
                // so replace chunk with new chunk
                index = 0;
                sb.replace(i - rchars.length + 1, i + 1, replace_with);
                i = i - rchars.length + replace_with.length();
                continue;
            }

        }
        return sb.toString();


    }

    /**
     * This will take the three pre-defined entities in XML 1.0
     * (used specifically in XML elements) and convert their character
     * representation to the appropriate entity reference, suitable for
     * XML element content.
     * from jdom XMLOutputter.escapeElementEntities
     *
     * @param str <code>String</code> input to escape.
     * @return <code>String</code> with escaped content.
     */
    public static String xmlEscape(String str) {
        StringBuffer buffer;
        char ch;
        String entity;

        buffer = null;
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            switch (ch) {
                case '<':
                    entity = "&lt;";
                    break;
                case '>':
                    entity = "&gt;";
                    break;
                case '&':
                    entity = "&amp;";
                    break;
                default :
                    entity = null;
                    break;
            }
            if (buffer == null) {
                if (entity != null) {
                    // An entity occurred, so we'll have to use StringBuffer
                    // (allocate room for it plus a few more entities).
                    buffer = new StringBuffer(str.length() + 20);
                    // Copy previous skipped characters and fall through
                    // to pickup current character
                    buffer.append(str.substring(0, i));
                    buffer.append(entity);
                }
            }
            else {
                if (entity == null) {
                    buffer.append(ch);
                }
                else {
                    buffer.append(entity);
                }
            }
        }

        // If there were any entities, return the escaped characters
        // that we put in the StringBuffer. Otherwise, just return
        // the unmodified input string.
        return (buffer == null) ? str : buffer.toString();
    }

    /**
     * will strip out any html tags contained in the string.
     *
     * @param s
     * @return
     */
    public static String stripHtmlTags(String s) {
        StringBuffer buffer = new StringBuffer(s.length());

        char c;
        int i;
        // Search through string's characters one by one.
        // If character is < then find closing > and remove all between
        // or could append to a returnString, depends which is faster, but i don't know right now

        int startTag = -1;
        int endTag = -1;
        for (i = 0; i < s.length(); i++) {
            c = s.charAt(i);


            if (startTag == -1) { // no start tag found yet
                if (c == '<') { // then it's there
                    startTag = i;
                }
            }
            else {
                // want to find end tag
                if (c == '>') {
                    endTag = i;
                    // now cut that chunk out, we've already skipped over it.


                }
            }

            if (startTag == -1) { // not in tag and no tag found yet
                buffer.append(c);
            }
            else if(endTag != -1){
                // end tag found, so skip this char and reset
                startTag = -1; // reset
                endTag = -1;
            }


        }


        return buffer.toString();

    }


    public static void main(/*@nullable@*/String args[]) {
        /// testing string replace
        //System.out.println(stringReplace("Dirty deeds, done dirt cheap, come on, dirty deeds, done dirt yo.", "deeds", "coolio"));

        // stripcharsNotInBag test
        //System.out.println(stripCharsNotInBag(args[0], DIGIT_BAG));

        // striphtml test
        //System.out.println(stripHtmlTags("Strip this you cocksucker <p> YOu shall pay! <img src=\"wordup.gif\">"));

        // stripWhitespace toSinglspace test
        System.out.println(stripWhitespaceToSingleSpace("Fuck     you mofo   fugger! \t\n you shit for brains, suck it!"));
    }
    /**
     * Will stript string down to only one space of white space
     * same as stripWhitespace, but leaves a space when stripping
     * @param s
     * @return
     */

    public static String stripWhitespaceToSingleSpace(String s) {
        String bag = whitespace;
        StringBuffer sb = new StringBuffer(s);

        char c;
        int i;
        // Search through string's characters one by one.
        // If character is in bag, remove from buffer
        // or could append to a returnString, depends which is faster, but i don't know right now
        int count = 0;
        for (i = 0; i < sb.length(); i++) {
            c = sb.charAt(i);

            if (bag.indexOf(c) != -1) { // then it's there
                if(count > 0){
                    sb.deleteCharAt(i);
                    i--; // get it back to proper place?
                }
                else{
                    // first one, so make sure it's a space
                    if(c != ' '){
                        sb.deleteCharAt(i);
                        sb.insert(i, ' ');
                    }
                }
                count++;
            }
            else{
                // not whitespace so reset
                count = 0;
            }
        }

        return sb.toString();
    }
    /**
     * borrowed from: http://www.rgagnon.com/javadetails/java-0306.html
     * @param s
     * @return
     */
    public static final String escapeHTML(String s, boolean doNbsp){
       StringBuffer sb = new StringBuffer();
       int n = s.length();
       for (int i = 0; i < n; i++) {
          char c = s.charAt(i);
          switch (c) {
             case '<': sb.append("&lt;"); break;
             case '>': sb.append("&gt;"); break;
             case '&': sb.append("&amp;"); break;
             case '"': sb.append("&quot;"); break;
             case 'à': sb.append("&agrave;");break;
             case 'À': sb.append("&Agrave;");break;
             case 'â': sb.append("&acirc;");break;
             case 'Â': sb.append("&Acirc;");break;
             case 'ä': sb.append("&auml;");break;
             case 'Ä': sb.append("&Auml;");break;
             case 'å': sb.append("&aring;");break;
             case 'Å': sb.append("&Aring;");break;
             case 'æ': sb.append("&aelig;");break;
             case 'Æ': sb.append("&AElig;");break;
             case 'ç': sb.append("&ccedil;");break;
             case 'Ç': sb.append("&Ccedil;");break;
             case 'é': sb.append("&eacute;");break;
             case 'É': sb.append("&Eacute;");break;
             case 'è': sb.append("&egrave;");break;
             case 'È': sb.append("&Egrave;");break;
             case 'ê': sb.append("&ecirc;");break;
             case 'Ê': sb.append("&Ecirc;");break;
             case 'ë': sb.append("&euml;");break;
             case 'Ë': sb.append("&Euml;");break;
             case 'ï': sb.append("&iuml;");break;
             case 'Ï': sb.append("&Iuml;");break;
             case 'ô': sb.append("&ocirc;");break;
             case 'Ô': sb.append("&Ocirc;");break;
             case 'ö': sb.append("&ouml;");break;
             case 'Ö': sb.append("&Ouml;");break;
             case 'ø': sb.append("&oslash;");break;
             case 'Ø': sb.append("&Oslash;");break;
             case 'ß': sb.append("&szlig;");break;
             case 'ù': sb.append("&ugrave;");break;
             case 'Ù': sb.append("&Ugrave;");break;
             case 'û': sb.append("&ucirc;");break;
             case 'Û': sb.append("&Ucirc;");break;
             case 'ü': sb.append("&uuml;");break;
             case 'Ü': sb.append("&Uuml;");break;
             case '®': sb.append("&reg;");break;
             case '©': sb.append("&copy;");break;
             // be carefull with this one (non-breaking whitee space)

                 case ' ':
                  if(doNbsp){
                      sb.append("&nbsp;");break;
                  }
             

             default:  sb.append(c); break;
          }
       }
       return sb.toString();
    }




}

