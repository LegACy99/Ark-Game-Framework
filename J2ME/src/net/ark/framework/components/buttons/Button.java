/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ark.framework.components.buttons;

import net.ark.framework.system.Utilities;
import net.ark.framework.system.images.Label;
import net.ark.framework.system.images.Sprite;
import javax.microedition.lcdui.Graphics;
import net.ark.framework.components.Drawable;

/**
 *
 * @author LegACy
 */
public class Button extends Drawable {
	protected Button() {
		//Super
		super();

		//Initialize
		m_ID			= 0;
		m_InputX		= 0;
		m_InputY		= 0;
		m_InputWidth	= 0;
		m_InputHeight	= 0;
		m_Images		= null;
		m_Labels		= null;
		m_Active		= true;
		Visible			= true;
		m_State			= STATE_NORMAL;
	}

	public Button(int id, String images) {
		this(id, images, null, 0, 0);
	}

	public Button(int id, String images, String text) {
		this(id, images, text, 0, 0);
	}

	public Button(int id, String images, int x, int y) {
		this(id, images, null, x, y);
	}

	public Button(int id, String images, String text, int x, int y) {
		//Default
		this();

		//Save
		m_ID = id;

		//If image exist
		if (images != null) {
			//Get image
			m_Images = Sprite.create(images, 0);

			//Get data
			m_Width 			= m_Images.getWidth();
			m_Height 			= m_Images.getHeight();
			m_OriginalHeight	= m_Height;
			m_OriginalWidth 	= m_Width;
		}

		//If text exist
		if (text != null) {
			//Create label
			m_Labels 				= new Label[m_Images.getMaxFrame()];
			m_Labels[STATE_NORMAL] 	= Label.create(text, Utilities.instance().getSystemFont());
			m_Labels[STATE_PRESSED] = Label.create(text, Utilities.instance().getSystemFont());
			if (m_Labels.length > STATE_INACTIVE) m_Labels[STATE_INACTIVE] = Label.create(text, Utilities.instance().getSystemFont());
		}

		//Set position and size
		setPosition(x, y);
		setSize(m_X, m_Y, m_Width, m_Height, false, false);
	}

	//Accessors
	public int getID()			{	return m_ID;		}
	public int getState()		{	return m_State;		}
	public boolean isActive()	{	return m_Active;	}

	public void setPosition(float x, float y, int horizontal, int vertical) {
		//Super
		super.setPosition(x, y, horizontal, vertical);

		//Set components position
		if (m_Images != null) m_Images.setPosition(x, y, horizontal, vertical);
		if (m_Labels != null)
			for (int i = 0; i < m_Labels.length; i++)
				m_Labels[i].setPosition((m_X + (m_Width / 2f)) / Utilities.instance().getScale(), (m_Y + (m_Height / 2f)) / Utilities.instance().getScale(), Drawable.ANCHOR_HCENTER, Drawable.ANCHOR_VCENTER);
	}

	public void setSize(float x, float y, float width, float height) {
		//Scale
		setSize(x, y, width, height, true, true);
	}

	public void setSize(float x, float y, float width, float height, boolean scaleOffset, boolean scaleSize) {
		//Set offset
		m_InputX = x;
		m_InputY = y;

		//Set size
		m_InputWidth 	= width;
		m_InputHeight	= height;
	}

	public void setActive(boolean active) {
		//Set
		m_Active = active;
	}

	public void setState(int state) {
		//Set state
		m_State  = state;
		if (m_Images != null) m_State %= m_Images.getMaxFrame();
	}

	public boolean isInside(float x, float y) {
		//Init variables
		float Top 	= m_Y + m_InputY;
		float Left	= m_X + m_InputX;

		//Check for false
		if (y < Top) 					return false;
		if (x < Left)					return false;
		if (y > Top + m_InputHeight)	return false;
		if (x > Left + m_InputWidth)	return false;

		//If passed, inside
		return true;
	}

	public void draw(Graphics g) {
		//Skip if not visible
		if (!Visible) return;

		//Get state
		int State = m_State;
		if (!isActive()) State = STATE_INACTIVE;
		if (m_Images != null) m_Images.setFrame(State);

		//Draw buttons
		if (m_Images != null) m_Images.draw(g);
		if (m_Labels != null) m_Labels[State].draw(g);
	}

	//Constants
	public static final int STATE_NORMAL		= 0;
	public static final int STATE_PRESSED		= 1;
	protected static final int STATE_INACTIVE	= 2;

	//Size
	protected float m_InputX;
	protected float m_InputY;
	protected float m_InputWidth;
	protected float m_InputHeight;

	//Data
	protected int 		m_ID;
	protected int 		m_State;
	protected Label[]	m_Labels;
	protected Sprite 	m_Images;
	protected boolean	m_Active;
	public boolean		Visible;
}
