package net.ark.framework.components;

import javax.microedition.khronos.opengles.GL10;

import net.ark.framework.system.Utilities;
import net.ark.framework.system.android.input.TouchInfo;

public class ItemList extends Drawable {
	protected ItemList() {
		//Super
		super();
		
		//Initialize
		m_Gap		= 0;
		m_Speed		= 0;
		m_Total		= 0;
		m_Window	= 0;
		m_Scroll	= 0;
		m_Slowing	= 0;
		m_Pressed	= false;
		m_Scrolling	= false;
		m_Items		= new Croppable[0];
	}

	public ItemList(float window) 					{ this(window, 0, 0);		}
	public ItemList(float window, float x, float y) { this(window, x, y, 0); 	}
	
	/**
	 * Instantiates a new item list.
	 *
	 * @param window the window
	 * @param x the x
	 * @param y the y
	 * @param gap the gap
	 */
	public ItemList(float window, float x, float y, float gap) {
		//Default
		this();
		
		//Save data
		m_Gap				= gap;
		m_Window 			= window;
		m_Height			= m_Window * Utilities.instance().getScale();
		m_OriginalHeight	= m_Window; 
		
		//Set position
		setPosition(x, y);
		
	}

	public Croppable addItem(Croppable item) {
		//Skip if null
		if (item == null) return null;
		
		//Add
		Croppable[] Items = new Croppable[m_Items.length + 1];
		System.arraycopy(m_Items, 0, Items, 0, m_Items.length);
		Items[m_Items.length]	= item;
		m_Items					= Items;
		
		//Set position
		item.setPosition(m_X / Utilities.instance().getScale(), (m_Y / Utilities.instance().getScale()) + m_Total + (m_Items.length > 1 ? m_Gap : 0));
		
		//Calculate vertical size
		float Old	 = m_Total;
		m_Total 	+= item.getOriginalHeight() + (m_Items.length > 1 ? m_Gap : 0);
		if (m_Total > m_Window) item.setRegion(0, 0, item.getOriginalWidth(), m_Window - Old - (m_Items.length > 1 ? m_Gap : 0));
		
		//if width is more than current
		if (item.getOriginalWidth() > m_OriginalWidth) {
			//Save width
			m_Width			= item.getWidth();
			m_OriginalWidth = item.getOriginalWidth();
		}

		//Return the item
		return item;
	}

	//Accessor
	public boolean isPressed()	{ return m_Pressed;	}
	
	public void update(int[] keys, TouchInfo[] touches, long time) {		
		//If touched
		if (touches[0].isPressed()) {
			//If was pressed
			if (m_Pressed) {
				//If scrolling
				if (m_Scrolling) {
					//Get offset
					float Offset 	= touches[0].getOffsetY() / Utilities.instance().getScale();
					m_Speed			= time <= 0 ? 0 : Offset / (float)time * 1000f;
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
			//Not pressed anymore
			m_Pressed = false;
			
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
		}
		
		//If scrolling
		if (m_Scrolling) {
			//Correct scroll
			if (m_Scroll + m_Window > m_Total) 	m_Scroll = m_Total - m_Window;
			if (m_Scroll < 0) 					m_Scroll = 0;
			
			//Update items
			updateItems();
		}
	}
	
	protected boolean isInside(float x, float y) {
		//Check for false
		if (x < m_X)			return false;
		if (y < m_Y)			return false;
		if (x > m_X + m_Width)	return false;
		if (y > m_Y + m_Height)	return false;

		//If passed, inside
		return true;
	}
	
	protected void updateItems() {
		//Calculate
		float X			= m_X / Utilities.instance().getScale();
		float Y 		= (m_Y / Utilities.instance().getScale()) - m_Scroll;
		float Window	= m_Window * Utilities.instance().getScale();
		
		//For each item
		for (int i = 0; i < m_Items.length; i++) {			
			//Set position
			float OldY = Y;
			m_Items[i].setPosition(X, Y);
			Y += m_Items[i].getOriginalHeight() + m_Gap;
			
			//Calculate
			float RegionY 		= m_Y - m_Items[i].getY();
			float RegionHeight 	= m_Y + Window - (OldY * Utilities.instance().getScale());
			m_Items[i].setRegion(0, RegionY / Utilities.instance().getScale(), m_Items[i].getOriginalWidth(), RegionHeight / Utilities.instance().getScale());
		}
	}

	@Override
	public void draw(GL10 gl) {
		//Draw
		for (int i = 0; i < m_Items.length; i++) m_Items[i].draw(gl);
	}
	
	//Constants
	protected final static float SCROLL_OFFSET = 20;
	
	//data
	protected float		m_Speed;
	protected float		m_Slowing;
	protected boolean	m_Pressed;
	protected boolean	m_Scrolling;
	
	//Items
	protected float			m_Gap;
	protected float			m_Total;
	protected float			m_Window;
	protected float			m_Scroll;
	protected Croppable[]	m_Items;

}
