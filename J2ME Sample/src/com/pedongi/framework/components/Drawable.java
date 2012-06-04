/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pedongi.framework.components;

import javax.microedition.lcdui.Graphics;

/**
 *
 * @author LegACy
 */
public abstract class Drawable {
	protected Drawable() {
		//Initialize
		m_X					= 0;
		m_Y					= 0;
		m_Width				= 0;
		m_Height			= 0;
		m_OriginalWidth		= 0;
		m_OriginalHeight	= 0;
	}

	//Draw
	public abstract void draw(Graphics g);

	public void setPosition(float x, float y) {
		setPosition(x, y, ANCHOR_LEFT, ANCHOR_TOP);
	}

	public void setPosition(float x, float y, int horizontal) {
		setPosition(x, y, horizontal, ANCHOR_TOP);
	}

	public void setPosition(float x, float y, int horizontal, int vertical) {
		//initialize
		m_X = x;
		m_Y = y;

		//Set X
		if (horizontal == ANCHOR_RIGHT)			m_X -= m_Width;
		else if (horizontal == ANCHOR_HCENTER)	m_X -= (m_Width / 2f);

		//Set Y
		if (vertical == ANCHOR_BOTTOM)			m_Y -= m_Height;
		else if (vertical == ANCHOR_VCENTER)	m_Y -= (m_Height / 2f);
	}

	//Accessors
	public float getX()					{	return m_X;					}
	public float getY()					{	return m_Y;					}
	public float getWidth()				{	return m_Width;				}
	public float getHeight()			{	return m_Height;			}
	public float getOriginalWidth()		{	return m_OriginalWidth;		}
	public float getOriginalHeight()	{	return m_OriginalHeight;	}

	//Mirror constants
	protected final static int MIRROR_NONE			= 0;
	protected final static int MIRROR_VERTICAL		= 1;
	protected final static int MIRROR_HORIZONTAL	= 2;
	protected final static int MIRROR_BOTH			= 3;

	//Anchor
	public final static int ANCHOR_LEFT 	= 0;
	public final static int ANCHOR_HCENTER 	= 1;
	public final static int ANCHOR_RIGHT 	= 2;
	public final static int ANCHOR_TOP 		= 0;
	public final static int ANCHOR_VCENTER 	= 1;
	public final static int ANCHOR_BOTTOM 	= 2;

	//Data
	protected float m_X;
	protected float m_Y;
	protected float m_Width;
	protected float m_Height;
	protected float m_OriginalWidth;
	protected float m_OriginalHeight;
}
