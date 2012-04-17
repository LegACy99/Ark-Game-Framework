package net.ark.framework.components;

import javax.microedition.khronos.opengles.GL10;

import net.ark.framework.system.Utilities;
import net.ark.framework.system.android.input.TouchInfo;

public class ItemList extends Drawable {
	protected ItemList() {
		//Super
		super();
		
		//Initialize
		m_X				= 0;
		m_Y				= 0;
		m_Speed			= 0;
		m_Scroll		= 0;
		m_Slowing		= 0;
		m_Bottom		= false;
		m_Pressed		= false;
		m_Scrolling		= false;
		m_Horizontal	= Drawable.ANCHOR_LEFT;
		m_Vertical		= Drawable.ANCHOR_TOP;
		m_Items			= new Drawable[0];
	}
	
	protected ItemList(float x, float y) {
		//Init
		this();
		
		//Save position
		setPosition(x, y);
	}

	public ItemList(float width, float height, float x, float y) {
		this(width, height, x, y, 0);
	}

	public ItemList(float width, float height, float x, float y, float gap) {
		this(width, height, x, y, gap, Drawable.ANCHOR_LEFT);
	}

	public ItemList(float width, float height, float x, float y, float gap, int horizontal) {
		this(width, height, x, y, gap, horizontal, Drawable.ANCHOR_TOP);
	}
	
	public ItemList(float width, float height, float x, float y, float gap, int horizontal, int vertical) {
		//Default
		this(x, y);
		
		//Set size
		m_Gap				= gap;
		m_OriginalWidth		= width;
		m_OriginalHeight	= height;
		m_Vertical			= vertical;
		m_Horizontal		= horizontal;
		m_Height			= m_OriginalHeight * Utilities.instance().getScale();
		m_Width				= m_OriginalWidth * Utilities.instance().getScale();
		
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
	
	protected float getItemsHeight() {
		//Get total height
		float Total = 0;
		if (m_Items.length > 1) Total += m_Gap * (m_Items.length - 1);
		for (int i = 0; i < m_Items.length; i++) Total += m_Items[i].getOriginalHeight();
		
		//Return
		return Total;
	}
	
	protected void updateScroll() {
		//Get total height
		float Total = getItemsHeight();
		
		//If using window
		if (m_OriginalHeight > 0) {
			//Correct scroll
			if (m_Scroll + m_OriginalHeight > Total) 	m_Scroll = Total - m_OriginalHeight;
			if (m_Scroll < 0) 							m_Scroll = 0;
			
			//Is on bottom?
			m_Bottom = m_Scroll == Total - m_OriginalHeight;
		}
	}
	
	protected void updateItems() {
		//Get X
		float X	= m_X / Utilities.instance().getScale();
		if (m_Horizontal == ANCHOR_RIGHT)	X += m_OriginalWidth;
		if (m_Horizontal == ANCHOR_HCENTER)	X += (m_OriginalWidth / 2f);
		
		//Get Y
		float Total	= getItemsHeight();
		float Top 	= (m_Y / Utilities.instance().getScale()) - m_Scroll;
		if (m_OriginalHeight > Total) {
			//Calculate alignment
			if (m_Vertical == ANCHOR_BOTTOM)	Top += m_OriginalHeight - Total;	
			if (m_Vertical == ANCHOR_VCENTER)	Top += (m_OriginalHeight - Total) / 2f;
		}
		
		//For each button
		for (int i = 0; i < m_Items.length; i++) {			
			//Set position
			m_Items[i].setPosition(X, Top, m_Horizontal);
			Top += m_Items[i].getOriginalHeight() + m_Gap;
			
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
	protected int		m_Horizontal;
	protected int		m_Vertical;
	protected int		m_Cursor;
	
	//Items
	protected float			m_Gap;
	protected float			m_Scroll;
	protected boolean		m_Bottom;
	protected Drawable[]	m_Items;

}
