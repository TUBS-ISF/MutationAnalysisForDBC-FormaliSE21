/*  Bomber for Nokia Series 60 Phones
    Copyright (C) 2003, 2004  While True, d.o.o.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

    For any info contact gorazd@whiletrue.com.
*/

/*==========================================================================;
 *
 *  While True, d.o.o.
 *
 *  File:       BitmapFont.java
 *  Content:    Bitmap font support
 *  Created:    December 2002
 *  Created by: gorazd breskvar
 *
 ****************************************************************************/


package bomber;

import javax.microedition.lcdui.*;
import java.io.IOException;
import java.io.DataInputStream;


// =========================================================================;
//	Name:	BitmapFont
//	Desc:	Support for bitmapped fonts
// ==========================================================================;

public class BitmapFont
{

    protected Image       m_font_image;     // image that contains fonts
    protected /*@ spec_public @*/short       m_start_char;     // first font in bitmap (inclusive)
    protected /*@ spec_public @*/short       m_end_char;       // last font in bitmap (inclusive)
    protected /*@ spec_public @*/short[]     m_start_pos;      // start pos of the font
    //protected int         m_clip_width;
    //protected int         m_clip_height;
    protected /*@ spec_public @*/byte        m_sub_font_width;
    protected int         m_selected_sub_font;





    public BitmapFont(Image image, short start_c, short end_c, short[] start_pos, byte sub_font_width)
    {
            m_font_image = image;
            m_start_char = start_c;
            m_end_char = end_c;
            m_start_pos = start_pos;
            //m_clip_width = clip_width;
            //m_clip_height= clip_height;
            m_sub_font_width = sub_font_width;
    }

    //@ post \result == (char)m_start_char;
    public char getFirstChar()
    {
        return (char)m_start_char;
    }

    //@ post \result == (m_end_char - m_start_char + 1);
    public int getCharNum()
    {
        return m_end_char - m_start_char + 1;
    }

    //@ post \result == m_sub_font_width; 
    public int getCharHeight()
    {
        return m_sub_font_width;
    }

    //@ ensures \result >= 0;
    public int getStringWidth(String s)
    {
        int ret_val = 0;
        for (int i = 0; i < s.length(); i++)
        {
            ret_val += getCharWidth(s.charAt(i)) + 1;
        }
        return ret_val;
    }

    //@ requires true;
    //@ ensures (\result == 8) ==> (c < m_start_char || c > m_end_char);
    //@ ensures (\result != 8) ==> (\result == (m_start_pos[c - m_start_char + 1] - m_start_pos[c - m_start_char]));
    public int getCharWidth(char c)
    {
        if (c < m_start_char || c > m_end_char) return 8;
        return m_start_pos[c - m_start_char + 1] - m_start_pos[c - m_start_char];
    }

    //@ requires true;
    //@ ensures true;
    public void drawString(Graphics g, int x, int y, String s)
    {
        int x_pos = x;
        for (int i = 0; i < s.length(); i++)
        {
            drawChar(g, x_pos, y, s.charAt(i));
            x_pos += getCharWidth(s.charAt(i));// + 1;
        }
    }

    //@ requires true;
    //@ ensures true;
    public void drawChar(Graphics g, int x, int y, char c)
    {
        if (c < m_start_char || c > m_end_char) return;

        // Store current clip (should be full canvas)
        int cx, cy, cw, ch;
        cx = g.getClipX();
        cy = g.getClipY();
        cw = g.getClipWidth();
        ch = g.getClipHeight();

        // Clip to single character and draw it
        g.setClip(x, y, getCharWidth(c), getCharHeight());
        g.drawImage(m_font_image, x - m_start_pos[c - m_start_char], y - m_selected_sub_font, 0);

        // Restore current clip (should be full canvas)
        g.setClip(cx, cy, cw, ch);
    }

    //@ requires true;
    //@ ensures true;
    public void setSubFont(byte sf)
    {
        m_selected_sub_font = sf * m_sub_font_width;
    }
}
