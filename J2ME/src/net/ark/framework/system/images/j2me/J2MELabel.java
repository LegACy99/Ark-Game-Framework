/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ark.framework.system.images.j2me;

import net.ark.framework.system.images.Label;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 *
 * @author LegACy
 */
public class J2MELabel extends Label {
	public J2MELabel(String text, int font) {
		this(text, font, 0, 0);
	}

	public J2MELabel(String text, int font, float x, float y) {
		//Initialize
		super(text, x, y);

		//Create image
		m_Image = BitmapFont.getFont(font).renderTransparentText(m_Text, 0x000000);

		//Set data
		m_Width				= m_Image.getWidth();
		m_Height			= m_Image.getHeight();
		m_OriginalHeight	= m_Height;
		m_OriginalWidth		= m_Width;
	}

	public void draw(Graphics g) {
		//Draw
		g.drawImage(m_Image, (int)m_X, (int)m_Y, Graphics.TOP | Graphics.LEFT);
	}

	//Component
	protected Image m_Image;
}
