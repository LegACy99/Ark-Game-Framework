/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ark.framework.components.buttons;

import java.util.Vector;
import net.ark.framework.system.SoundManager;
import net.ark.framework.system.Utilities;
import net.ark.framework.system.input.TouchInfo;
import javax.microedition.lcdui.Graphics;
import net.ark.framework.components.Drawable;

/**
 *
 * @author LegACy
 */
public class ButtonContainer extends Drawable {
	public ButtonContainer() {
		//Initialize
		m_Pressed	= -1;
		m_Buttons	= new Vector();
	}	
	
	public Button addButton(int id, String images, String text) 	{ return addButton(new Button(id, images, text)/*id, (JSONObject[]) ResourceManager.instance().getTextures(images), text*/); 	}	
	//public Button addButton(int id, JSONObject[] json, String text) { return addButton(new Button(id, json, text)); 												}	
	public Button addButton(Button button) {
		//Add a button
		m_Buttons.addElement(button);
		calculateSize();
		
		//Return
		return button;
	}

	public void addButtons(Button[] buttons) {
		//Add buttons
		for (int i = 0; i < buttons.length; i++) addButton(buttons[i]);
	}
	

	public Button getButton(int index) {
		return getButton(index, NO_BUTTON);
	}

	public Button getButton(int index, int id) {
		//Initialize
		Button Result = null;

		//If index valid
		if (index >= 0 && index < m_Buttons.size()) Result = (Button) m_Buttons.elementAt(index);
		else { 
			//For each button
			for (int i = 0; i < m_Buttons.size(); i++) {
				//Get result
				Button Current = (Button) m_Buttons.elementAt(i);
				if (Current.getID() == id) Result = Current;
			}
		}

		//Return
		return Result;
	}

	public void removeButtons() {
		//Destroy buttons
		m_Buttons.removeAllElements();
	}
	
	public void setPosition(float x, float y, int horizontal, int vertical) {
		//Super
		super.setPosition(x, y, horizontal, vertical);
		reposition();
	}
	
	public void reposition() {		
		//For each buttons
		for (int i = 0; i < m_Buttons.size(); i++) {
			//Calculate
			Button Current	= (Button) m_Buttons.elementAt(i);
			float NewX		= (m_X + Current.getX()) / Utilities.instance().getScale();
			float NewY		= (m_Y + Current.getY()) / Utilities.instance().getScale();
			
			//Set new position
			Current.setPosition(NewX, NewY);
		}
	}
	
	public void calculateSize() {
		//Initialize
		m_Width		= 0;
		m_Height	= 0;
		
		//For each buttons
		for (int i = 0; i < m_Buttons.size(); i++) {
			//Calculate right and bottom
			Button Current	= (Button) m_Buttons.elementAt(i);
			float Bottom	= Current.getY() + Current.getHeight() - m_Y;
			float Right		= Current.getX() + Current.getWidth() - m_X;
			
			//Compare with width and height
			if (Right > m_Width)	m_Width = Right;
			if (Bottom > m_Height)	m_Height = Bottom;
		}
		
		//Scale
		m_OriginalWidth		= m_Width / Utilities.instance().getScale();
		m_OriginalHeight	= m_Width / Utilities.instance().getScale();
	}

	public int update(int[] keys, TouchInfo[] touches) {
		//Initialize
		int Result = NO_BUTTON;

		//Check
		if (m_Buttons.size() <= m_Pressed) m_Pressed = -1;
		
		//If pressed
		if (touches[0].isPressed()) {
			//If no button is pressed
			if (m_Pressed < 0) {
				//For each button
				for (int i = 0; i < m_Buttons.size(); i++) {
					//If pressed
					Button Current = (Button)m_Buttons.elementAt(i);
					if (Current.Visible && Current.isActive() && Current.isInside(touches[0].getStartX(), touches[0].getStartY())) {
						//Set pressed button
						m_Pressed = i;
						Current.setState(Button.STATE_PRESSED);

						//SFX
						if (Utilities.instance().getSystemPressSFX() != null) SoundManager.instance().playSFX(Utilities.instance().getSystemPressSFX());
					}
				}
			} else {
				//Set state
				Button Pressed = getButton(m_Pressed);
				if (Pressed.isInside(touches[0].getCurrentX(), touches[0].getCurrentY())) 	Pressed.setState(Button.STATE_PRESSED);
				else																		Pressed.setState(Button.STATE_NORMAL);
			}
		} else {
			//If there's a pressed button
			if (m_Pressed >= 0) {
				//If released inside
				Button PressedButton = (Button) m_Buttons.elementAt(m_Pressed);
				if (PressedButton.isInside(touches[0].getCurrentX(), touches[0].getCurrentY())){
					//Set as result
					Result = PressedButton.getID();

					//SFX
					if (Utilities.instance().getSystemReleaseSFX() != null) SoundManager.instance().playSFX(Utilities.instance().getSystemReleaseSFX());
				}

				//Reset button state
				PressedButton.setState(Button.STATE_NORMAL);
			}

			//Nothing pressed
			m_Pressed = -1;
		}

		//Return
		return Result;
	}

	public void draw(Graphics g) {
		//Draw all buttons
		for (int i = 0; i < m_Buttons.size(); i++) ((Button)m_Buttons.elementAt(i)).draw(g);
	}

	//Constants
	public static final int NO_BUTTON = -1;

	//Data
	protected int		m_Pressed;
	protected Vector	m_Buttons;
}
