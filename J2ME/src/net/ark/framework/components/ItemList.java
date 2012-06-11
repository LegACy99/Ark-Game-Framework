/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ark.framework.components;

import net.ark.framework.system.Utilities;
import net.ark.framework.system.input.TouchInfo;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author LegACy
 */
public class ItemList extends Drawable {
	protected ItemList() {
		//Super
		super();
		
		//Initialize
		m_Speed			= 0;
		m_Scroll		= 0;
		m_Slowing		= 0;
		m_Pressed		= false;
		m_Scrolling		= false;
		m_Bottom		= false;
		m_Items			= new Drawable[0];
	}
	
	protected ItemList(float x, float y) {
		//Init
		this();
		
		//Save position
		m_X	= x;
		m_Y	= y;
	}
	
	public ItemList(float width, float height, float x, float y) {
		//Default
		this(x, y);
		
		//Set size
		m_OriginalHeight	= height;
		m_OriginalWidth		= width;
		m_Width				= m_OriginalWidth * Utilities.instance().getScale();
		m_Height			= m_OriginalHeight * Utilities.instance().getScale();
		//System.out.println("x " + m_X + " width " + m_Width + " y " + m_Y + " height " + m_Height);
	}

	public Drawable addItem(Drawable item) {
		//Add
		Drawable[] Items = new Drawable[m_Items.length + 1];
		System.arraycopy(m_Items, 0, Items, 0, m_Items.length);
		Items[m_Items.length]	= item;
		m_Items					= Items;
		
		//Update
		updateItems();

		//Return the button
		return item;
	}
	
	public boolean isTop()		{	return m_Scroll == 0;	}
	public boolean isBottom()		{	return m_Bottom;	}
	public boolean isPressed()	{	return m_Pressed;		}
	
	public void update(int[] keys, TouchInfo[] touches, long time) {		
		//Super
		//int Result = super.update(keys, touches);
		
		//If touched
		if (touches[0].isPressed()) {
			//If was pressed
			if (m_Pressed) {
				//If scrolling
				if (m_Scrolling) {
					//Get offset
					float Offset 	= touches[0].getOffsetY() / Utilities.instance().getScale();
					m_Speed			= time <= 0 ? 0 : Offset * 1000f / (float)time;
					m_Slowing		= -m_Speed;

					//Scroll
					m_Scroll -= Offset;
				} else {
					//If difference is more than offset
					if (Math.abs(touches[0].getCurrentY() - touches[0].getStartY()) > SCROLL_OFFSET) {
						//Start scrolling
						m_Scrolling = true;
						touches[0].getOffsetY();
					}					
				}
			} else {
				//If inside
				if (isInside(touches[0].getStartX(), touches[0].getStartY())) {
					//Pressed
					m_Pressed = true;
				}
			}
		} else {
			//if pressed
			//if (m_Pressed) {
				//If scrolling
				if (m_Scrolling) {
					//If there's speed
					if (m_Speed != 0) {

						//Save sign
						boolean Minus = m_Speed < 0;
						m_Speed += m_Slowing * (float)time / 1000f;

						//If sign is different, done
						if (m_Speed < 0 != Minus) m_Speed = 0;
					}

					//Scroll
					m_Scroll -= m_Speed * (float)time / 1000f;

					//If died
					if (m_Speed == 0) {
						//No more scroll
						m_Speed		= 0;
						m_Slowing	= 0;
						m_Scrolling = false;
					}
				}
			//}
				
			//Not pressed
			m_Pressed = false;
		}
		
		//System.out.println("scrolling: " + m_Scrolling + " pressed: " + m_Pressed + " touched: " + touches[0].isPressed());
		
		//If scrolling
		if (m_Scrolling) {
			//Update items
			updateScroll();
			updateItems();
		}
	}
	
	protected boolean isInside(float x, float y) {
		//Check for false
		if (y < m_X)			return false;
		if (x < m_Y)			return false;
		if (y > m_Y + m_Height)	return false;
		if (x > m_X + m_Width)	return false;

		//If passed, inside
		return true;
	}
	
	protected void updateScroll() {
		//Get total height
		float Total = 0;
		for (int i = 0; i < m_Items.length; i++) Total += m_Items[i].getOriginalHeight();
		
		//If using window
		if (m_Height > 0) {
			//Correct scroll
			if (m_Scroll + m_Height > Total) 	m_Scroll = Total - m_Height;
			if (m_Scroll < 0) 					m_Scroll = 0;
			m_Bottom = m_Scroll == Total - m_Height;
		}
	}
	
	protected void updateItems() {
		//Initialize position
		float Top = m_Y - m_Scroll;
		
		//For each button
		for (int i = 0; i < m_Items.length; i++) {
			//Set position
			m_Items[i].setPosition(m_X, Top);
			Top += m_Items[i].getOriginalHeight();
			
			//Hide
			//m_Items[i].Visible = false;
		}
		
		//Window?
		/*if (m_WindowHeight > 0) {	
			//For each button
			for (int i = 0; i < m_Buttons.size(); i++) {
				//Get position
				float Y = m_Buttons.get(i).getY() / Utilities.instance().getScale();
				
				//Visible if inside window
				if (Y + m_Buttons.get(i).getOriginalHeight() > m_Y && Y < m_Y + m_WindowHeight) m_Buttons.get(i).Visible = true; 
			}
		} else if (m_MaxItems > 0) {
			//Visible buttons
			for (int i = m_TopItem; i < m_TopItem + m_MaxItems; i++) m_Buttons.get(i).Visible = true;
		}*/
	}

	public void draw(Graphics g) {
		//Draw
		for (int i = 0; i < m_Items.length; i++) m_Items[i].draw(g);
	}
	
	//Constants
	protected final static float SCROLL_OFFSET = 20;
	
	//data
	protected float		m_Speed;
	protected float		m_Slowing;
	protected boolean	m_Scrolling;
	protected boolean	m_Pressed;
	protected int		m_Cursor;
	
	//Items
	protected float			m_Gap;
	protected float			m_Scroll;
	protected boolean		m_Bottom;
	protected Drawable[]	m_Items;
}
