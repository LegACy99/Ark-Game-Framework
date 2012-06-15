package net.ark.framework.components.buttons;

import org.json.JSONObject;

import net.ark.framework.system.Utilities;
import net.ark.framework.system.android.input.TouchInfo;

public class ButtonList extends ButtonContainer {
	protected ButtonList() {
		//Super
		super();
		
		//Initialize
		m_X				= 0;
		m_Y				= 0;
		m_Speed			= 0;
		m_Scroll		= 0;
		m_TopItem 		= 0;
		m_Slowing		= 0;
		m_MaxItems		= -1;
		m_WindowHeight	= -1;
		m_TotalHeight	= 0;
		m_Scrolling		= false;
	}
	
	protected ButtonList(float x, float y) {
		//Init
		this();
		
		//Save position
		m_X	= x;
		m_Y	= y;
	}
	
	public ButtonList(int items, float x, float y) {
		//Default
		this(x , y);
		
		//Set window size
		m_MaxItems = items;
	}
	
	public ButtonList(float window, float x, float y) {
		//Default
		this(x, y);
		
		//Set window size
		m_WindowHeight = window;
	}

	@Override
	public Button addButton(int id, JSONObject[] json, String text) {
		//Add
		Button NewButton = super.addButton(id, json, text);
		updateButtons();
		
		//Return
		return NewButton;
	}

	@Override
	public Button addButton(Button button) {
		//Add a button
		Button Added = super.addButton(button);
		updateButtons();
		
		//Return
		return Added;
	}

	@Override
	public void addButtons(Button[] buttons) {
		//Add buttons
		super.addButtons(buttons);
		updateButtons();
	}
	
	@Override
	public int update(int[] keys, TouchInfo[] touches) {
		return update(keys, touches, 0);
	}	
	
	public int update(int[] keys, TouchInfo[] touches, long time) {		
		//Super
		int Result = super.update(keys, touches);
		
		//If there's touch
		if (touches != null) {
			//If there's a pressed button
			if (m_Pressed >= 0) {			
				//If scrolling
				if (m_Scrolling) {
					//Get offset
					float Offset 	= touches[0].getOffsetY() / Utilities.instance().getScale();
					m_Speed			= time <= 0 ? 0 : Offset * 1000f / (float)time;
					m_Slowing		= -m_Speed;
					
					//Scroll
					m_Scroll -= Offset;
					
					//Set frame
					m_Buttons.get(m_Pressed).setState(Button.STATE_NORMAL);
				}
				else {
					//If out of button
					if (!m_Buttons.get(m_Pressed).isInside(touches[0].getCurrentX(), touches[0].getCurrentY())) {
						//Start scroll mode
						m_Scrolling = true;
						touches[0].getOffsetY();
					}
				}
			} else {
				//If scrolling
				if (m_Scrolling) {
					//No button
					Result = NO_BUTTON;
					
					//If there's speed
					if (m_Speed != 0) {
						
						//Save sign
						float Sign = Math.signum(m_Speed);
						m_Speed += m_Slowing * (float)time / 1000f;
						
						//If sign is different, done
						if (Math.signum(m_Speed) != Sign) m_Speed = 0;
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
		}
		
		//If scrolling
		if (m_Scrolling) {
			//Update buttons
			updateScroll();
			updateButtons();
		}
		
		//Return
		return Result;
	}
	
	protected void updateScroll() {
		//Get total height
		m_TotalHeight = 0;
		for (int i = 0; i < m_Buttons.size(); i++) m_TotalHeight += m_Buttons.get(i).getOriginalHeight();
		
		//If using window
		if (m_WindowHeight > 0) {
			//Correct scroll
			if (m_Scroll + m_WindowHeight > m_TotalHeight) 	m_Scroll = m_TotalHeight - m_WindowHeight;
			if (m_Scroll < 0) 								m_Scroll = 0;
		} else if (m_MaxItems > 0) {
			//Correct item
			if (m_TopItem + m_MaxItems > m_Buttons.size()) 	m_TopItem = m_Buttons.size() - m_MaxItems;
			if (m_TopItem < 0) 								m_TopItem = 0;
			
			//Set scroll
			m_Scroll = 0;
			for (int i = 0; i < m_TopItem; i++) m_Scroll += m_Buttons.get(i).getOriginalHeight();
		}
	}
	
	protected void updateButtons() {
		//Initialize position
		float Top = m_Y - m_Scroll;
		
		//For each button
		for (int i = 0; i < m_Buttons.size(); i++) {
			//Set position
			m_Buttons.get(i).setPosition(m_X, Top);
			Top += m_Buttons.get(i).getOriginalHeight();
			
			//Hide
			m_Buttons.get(i).Visible = false;
		}
		
		//Window?
		if (m_WindowHeight > 0) {	
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
		}
	}
	
	//data
	protected float 	m_X;
	protected float 	m_Y;
	protected float		m_Speed;
	protected float		m_Slowing;
	protected boolean	m_Scrolling;
	
	//Items
	protected int 	m_TopItem;
	protected int 	m_MaxItems;
	protected float	m_WindowHeight;
	protected float	m_TotalHeight;
	protected float	m_Scroll;
	protected float	m_Gap;
}
