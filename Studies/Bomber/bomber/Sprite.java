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
 *  File:       NokiaSprite.java
 *  Content:    Sprite (raster image) class with support for rotation and
 *              flipping
 *  Created:    November 2002
 *  Created by: gorazd breskvar
 *
 *  To-do:      Replace with MIDP 2.0 standard Sprite
 *
 ****************************************************************************/


package bomber;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import java.io.IOException;
import java.io.DataInputStream;


public class Sprite implements Drawable
{

    protected Image[]     m_images;
    protected byte[]      m_center;
    protected boolean     m_enable_flipping;
    protected byte[]      m_user_data;




    public Sprite(DataInputStream dis, boolean flipping) throws IOException
    {
        int image_num = dis.readByte();
        if (image_num == 0) // special mode
        {
            int user_data_size = dis.readByte();
            m_user_data = new byte[user_data_size];
            dis.read(m_user_data);
            image_num = dis.readByte();
        }


        boolean condensed = false;
        if (image_num < 0)
        {
            condensed = true;
            image_num = -image_num;
        }

        m_images = new Image[image_num];
        m_center = new byte[2 * image_num];
        m_enable_flipping = flipping;

        if (condensed)  // combined image
        {
            byte [] tmp_x = new byte[image_num];
            byte [] tmp_y = new byte[image_num];
            for (int i = 0; i < image_num; i++)
            {
                tmp_x[i] = dis.readByte();
                tmp_y[i] = dis.readByte();
                m_center[2 * i]   =  dis.readByte();  // center x
                m_center[2 * i+1] =  dis.readByte();  // center y
            }

            int len = dis.readInt();        // image length
            byte[] data = new byte[len];    // load to buffer
            dis.read(data, 0, len);         // read buffer
            Image tmp_image = Image.createImage(data, 0, data.length);  // creates temporary image
            int c_width = 0;       // current width of the image
            for (int i = 0; i < image_num; i++)
            {
                m_images[i] = Image.createImage(tmp_image, c_width, 0, tmp_x[i], tmp_y[i], javax.microedition.lcdui.game.Sprite.TRANS_NONE);
                c_width += tmp_x[i];
            }
        }
        else
        {
            for (int i = 0; i < image_num; i++)
            {
                m_center[2 * i]     =  dis.readByte();    // center x
                m_center[2 * i + 1] =  dis.readByte();    // center y
                int len = dis.readInt();
                byte[] data = new byte[len];
                dis.read(data, 0, len);
                m_images[i] = Image.createImage(data, 0, data.length);
            }
        }
    }


    protected int getDimensionWidth(int index, boolean flip)
    {
        if (m_enable_flipping && index >= m_images.length)
        {
            if (index < 2 * m_images.length)
            {
                flip = !flip;
                index -= m_images.length;
            }
            else if (index < 3 * m_images.length)
            {
                index -= 2 * m_images.length;
            }
            else
            {
                flip = !flip;
                index -= 3 * m_images.length;
            }
        }
        if (flip) return m_images[index].getHeight();
        else return m_images[index].getWidth();
    }




     public int getWidth(int index)
    {
        return getDimensionWidth(index, false);
    }

    public int getHeight(int index)
    {
        return getDimensionWidth(index, true);
    }



    public int getCenterX(int index)
    {
        int center_x = m_center[2 * index];
        int center_y = m_center[2 * index + 1];
        if (m_enable_flipping)
        {
            if (index <  m_images.length) return center_x;
            if (index < 2 * m_images.length) return getWidth(index) - center_y;
            else if (index < 3 * m_images.length) return getWidth(index) - center_x;
            else return center_y;
        }
        else return center_x;
    }

    public int getCenterY(int index)
    {
        int center_x = m_center[2 * index];
        int center_y = m_center[2 * index + 1];

        if (m_enable_flipping)
        {
            if (index <  m_images.length) return center_y;
            if (index < 2 * m_images.length) return center_x;
            else if (index < 3 * m_images.length) return getWidth(index) - center_y;
            else return getWidth(index) - center_x;
        }
        else return center_y;
    }

    public byte getUserData(int index)
    {
        return m_user_data[index];
    }

    public int getNumber()
    {
        if (m_enable_flipping) return m_images.length * 4;
        else return m_images.length;
    }

    public int getIndexFromAngle(int angle)
    {
        return (int)((getNumber() * angle + (Common.FIXED * 360) / (getNumber() * 2)) / (360 * Common.FIXED)) % getNumber();
    }


    public void drawImageWithAngle(Graphics g, int angle, int x, int y)
    {

        drawImage(g, getIndexFromAngle(angle) , x, y);
    }

    public void drawImage(Graphics g, int index, int x, int y)
    {

        if (m_enable_flipping && index >= m_images.length)
        {
            int manipulation;
            if (index < 2 * m_images.length)
            {
                manipulation = javax.microedition.lcdui.game.Sprite.TRANS_ROT90;
                index -= m_images.length;

                //cy = m_images[index].getHeight() - cy;
                g.drawRegion(m_images[index], 0, 0, m_images[index].getWidth(), m_images[index].getHeight(), manipulation, x - (m_images[index].getHeight() - m_center[index * 2 + 1]), y - m_center[index * 2], 0);
            }
            else if (index < 3 * m_images.length)
            {
                //cx = m_images[index].getWidth() - cx;
                manipulation = javax.microedition.lcdui.game.Sprite.TRANS_ROT180;
                index -= 2 * m_images.length;
                g.drawRegion(m_images[index], 0, 0, m_images[index].getWidth(), m_images[index].getHeight(), manipulation, x - (m_images[index].getWidth() - m_center[index * 2]), y - (m_images[index].getHeight() - m_center[index * 2 + 1]), 0);
            }
            else
            {
                manipulation = javax.microedition.lcdui.game.Sprite.TRANS_ROT270;
                index -= 3 * m_images.length;
                g.drawRegion(m_images[index], 0, 0, m_images[index].getWidth(), m_images[index].getHeight(), manipulation, x - m_center[index * 2 + 1], y - (m_images[index].getWidth() - m_center[index * 2]), 0);
            }
        }
        else
        {
            g.drawImage(m_images[index], x - m_center[index * 2] , y - m_center[index * 2 + 1], 0);
        }
    }
}
