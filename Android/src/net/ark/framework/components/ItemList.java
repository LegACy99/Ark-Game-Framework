package net.ark.framework.components;

import javax.microedition.khronos.opengles.GL10;

import net.ark.framework.system.Utilities;
import net.ark.framework.system.android.input.TouchInfo;

public class ItemList extends Croppable {
	protected ItemList() {
		//Super
		super();
		
		//Initialize
		m_Gap			= 0;
		m_Speed			= 0;
		m_Total			= 0;
		m_Window		= 0;
		m_Scroll		= 0;
		m_Slowing		= 0;
		m_DrawnSize		= 0;
		m_DrawnFirst	= 0;
		m_Scrolling		= false;
		m_Pressed		= false;
		m_Grow			= false;
		m_Items			= new Croppable[0];
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
		
		//Check window
		if (m_Window <= 0) {
			//Growing
			m_Grow				= true;
			m_OriginalHeight	= 0;
			m_Window 			= 0;
			m_Height			= 0;
		}
		
		//Set position
		setPosition(x, y);
		setRegion(0, 0, m_OriginalWidth, m_OriginalHeight);
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
		
		//If taller than window
		if (m_Total > m_Window) {
			//If don't grow, crop
			if (!m_Grow) item.setRegion(0, 0, item.getOriginalWidth(), m_Window - Old - (m_Items.length > 1 ? m_Gap : 0));
			else {
				//Calculate new size
				m_Window 			= m_Total;
				m_OriginalHeight	= m_Window;
				m_Height			= m_Window * Utilities.instance().getScale();
				
				//Set region
				setRegion(m_OriginalRegionX, m_OriginalRegionY, m_OriginalRegionWidth, m_OriginalHeight);
			}
		}
		
		//if width is more than current
		if (item.getOriginalWidth() > m_OriginalWidth) {
			//Save width
			m_Width			= item.getWidth();
			m_OriginalWidth = item.getOriginalWidth();
			
			//Set region
			setRegion(m_OriginalRegionX, m_OriginalRegionY, m_OriginalWidth, m_OriginalRegionHeight);
		}
		
		//Update items
		adjustItems();
		updateItems();

		//Return the item
		return item;
	}

	//Accessor
	public int getItemSize()	{ return m_Items.length;	}
	public boolean isPressed()	{ return m_Pressed;			}

	public Croppable getItem(int position) {
		//Get item
		Croppable Result = null;
		if (position >= 0 && position < m_Items.length) Result = m_Items[position];
		
		//Return
		return Result;
	}
	
	@Override
	public void setPosition(float x, float y, int horizontal, int vertical) {
		//Super
		super.setPosition(x, y, horizontal, vertical);
		
		//Refresh
		updateItems();
	}
	
	@Override
	public void setRegion(float x, float y, float width, float height) {
		//SUper
		super.setRegion(x, y, width, height);
		
		//Refresh
		adjustItems();
		updateItems();
	}
	
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
			adjustItems();
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
	
	public void adjustItems() {
		//Initialize
		float Y			= 0;
		m_DrawnFirst	= 0;
		m_DrawnSize 	= 0;
		
		//For all item
		for (int i = 0; i < m_Items.length; i++) {
			//Get height
			float Height = m_Items[i].getOriginalHeight();
			
			//If not started yet
			if (m_DrawnSize <= 0) {
				//Check if inside
				if (Y + Height > m_Scroll + m_OriginalRegionY) {
					//Start
					m_DrawnFirst 	= i;
					m_DrawnSize		= 1;
				}
			} else {
				//Add if inside
				float Bottom = m_Scroll + Math.min(m_Window, m_OriginalRegionY + m_OriginalRegionHeight);
				if (Y < Bottom) m_DrawnSize++;
			}
			
			//Next
			Y += Height + m_Gap;
		}
	}
	
	public void updateItems() {
		//Calculate
		float X	= m_X / Utilities.instance().getScale();
		float Y = (m_Y / Utilities.instance().getScale()) - m_Scroll;
		
		//For all item
		for (int i = 0; i < m_Items.length; i++) {
			//Get item
			Croppable Item = m_Items[i];
			
			//If drawn
			if (i >= m_DrawnFirst && i < m_DrawnFirst + m_DrawnSize) {				
				//If not first or last
				if (i > m_DrawnFirst && i < m_DrawnFirst + m_DrawnSize - 1) {
					//Ensure that it's drawn fully
					if (Item.getOriginalRegionHeight() < Item.getOriginalHeight() || Item.getOriginalRegionY() > 0) {
						Item.setRegion(m_OriginalRegionX, 0, m_OriginalRegionWidth, Item.getOriginalHeight());
					}
				} else {
					//Calculate
					float RegionY 		= m_Y + m_RegionY - (Y * Utilities.instance().getScale());
					float RegionHeight 	= m_Y + m_RegionY + ((Math.min(m_Window, m_OriginalRegionHeight) - Y) * Utilities.instance().getScale());
					if (RegionY > 0) RegionHeight -= RegionY;
					
					//Crop if needed
					//if (RegionY >= 0 || RegionHeight <= Item.getHeight() || RegionHeight != Item.getOriginalRegionHeight())
					
					//Crop
					Item.setRegion(m_OriginalRegionX, RegionY / Utilities.instance().getScale(), m_OriginalRegionWidth, RegionHeight / Utilities.instance().getScale());
				}
				
				//Set position
				Item.setPosition(X, Y);
			}
			
			//Next Y
			Y += Item.getOriginalHeight() + m_Gap;
		}
	}

	public void calculateSize() { calculateSize(-1); }
	public void calculateSize(int changed) {
		//Save old
		float NewScroll = -1;
		
		//For all items
		m_Total = 0;
		for (int i = 0; i < m_Items.length; i++) {
			//Check if changed after or before
			if (i == changed && m_Total < m_Scroll) NewScroll = m_Total;
				
			//Add height
			m_Total += m_Items[i].getOriginalHeight();
			if (i < m_Items.length - 1) m_Total += m_Gap;
		}
		
		//If growing
		if (m_Grow) {
			//Save as window
			m_Window 			= m_Total;
			m_OriginalHeight	= m_Window;
			m_Height			= m_Window * Utilities.instance().getScale();
		}
		
		//Correct scrolling
		if (NewScroll >= 0) 				m_Scroll = NewScroll;
		if (m_Scroll + m_Window > m_Total) 	m_Scroll = m_Total - m_Window;
		if (m_Scroll < 0) 					m_Scroll = 0;
		
		//Not scrolling
		m_Scrolling = false;
	}

	@Override
	public void draw(GL10 gl) {
		//Draw
		for (int i = m_DrawnFirst; i < m_DrawnFirst + m_DrawnSize; i++) m_Items[i].draw(gl);
	}
	
	//Constants
	protected final static float SCROLL_OFFSET = 20;
	
	//Data
	protected float		m_Speed;
	protected float		m_Slowing;
	protected boolean	m_Pressed;
	protected boolean	m_Scrolling;
	protected boolean	m_Grow;
	
	//Items
	protected float			m_Gap;
	protected float			m_Total;
	protected float			m_Window;
	protected float			m_Scroll;
	protected int			m_DrawnFirst;
	protected int			m_DrawnSize;
	protected Croppable[]	m_Items;

}
