/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pedongi.framework.system.images.j2me;

import com.pedongi.framework.components.Drawable;
import com.pedongi.framework.system.Utilities;
import com.pedongi.framework.system.images.Label;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 *
 * @author LegACy
 */
public class Number extends Label {	
	public Number(int number, int font) {
		this(number, font, 0, 0, 0, Drawable.ANCHOR_LEFT);
	}
	
	public Number(int number, int font, int alignment) {
		this(number, font, 0, 0, 0, alignment);
	}
	
	public Number(int number, int font, float x, float y) {
		this(number, font, x, y, 0, Drawable.ANCHOR_LEFT);
	}
	
	public Number(int number, int font, float x, float y, int alignment) {
		this(number, font, x, y, 0, alignment);
	}
	
	public Number(int number, int font, float x, float y, int offset, int alignment) {
		//Super
		super(String.valueOf(number), 0, 0);
		
		//Initialize
		m_Offset	= offset;
		m_Align		= alignment;
		
		//Create number
		int[] Digits 	= Utilities.instance().getDigits(number);
		m_Number		= new Image[Digits.length];
		for (int i = 0; i < m_Number.length; i++) m_Number[i] = BitmapFont.getFont(font).getNumber(Digits[Digits.length - 1 - i]);
		
		//Calculate size
		m_Width = m_Offset * (m_Number.length - 1);
		for (int i = 0; i < m_Number.length; i++) {
			m_Width += m_Number[i].getWidth();
			if (m_Number[i].getHeight() > m_Height) m_Height = m_Number[i].getHeight();
		}
		m_OriginalWidth		= m_Width;
		m_OriginalHeight	= m_Height;
		
		//Set position
		setPosition(x, y);
	}
	
	public void draw(Graphics g) {
		//Get position
		float X = m_X;
		if (m_Align == Drawable.ANCHOR_HCENTER)		X -= (m_Width / 2);
		else if (m_Align == Drawable.ANCHOR_RIGHT)	X -= m_Width;
		
		//For each digit
		for (int i = 0; i < m_Number.length; i++) {
			//Draw
			g.drawImage(m_Number[i], (int)X, (int)m_Y, Graphics.TOP | Graphics.LEFT);
			
			//Get next position
			X += m_Number[i].getWidth() + m_Offset;
		}
	}
	
	//Data
	protected int		m_Align;
	protected int		m_Offset;
	protected Image[]	m_Number;
}
