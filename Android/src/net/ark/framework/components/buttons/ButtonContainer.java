package net.ark.framework.components.buttons;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.json.JSONObject;

import net.ark.framework.system.SoundManager;
import net.ark.framework.system.Utilities;
import net.ark.framework.system.android.input.TouchInfo;
import net.ark.framework.system.resource.ResourceManager;

public class ButtonContainer {
	public ButtonContainer() {
		//Initialize
		m_Pressed = -1;
		m_Buttons = new ArrayList<Button>();
	}
	
	public Button addButton(int id, String images, String text) {
		return addButton(id, (JSONObject[]) ResourceManager.instance().getTextures(images), text);
	}
	
	public Button addButton(int id, JSONObject[] json, String text) {
		//Create button
		Button NewButton = new Button(id, json, text);
		m_Buttons.add(NewButton);
		
		//Return
		return NewButton;
	}
	
	public void addButton(Button button) {
		//Add a button
		m_Buttons.add(button);
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
		if (index >= 0 && index < m_Buttons.size()) Result = m_Buttons.get(index);
		else 										for (int i = 0; i < m_Buttons.size(); i++) if (m_Buttons.get(i).getID() == id) Result = m_Buttons.get(i);
		
		//Return
		return Result;
	}
	
	public void removeButtons() {
		//Destroy buttons
		m_Buttons.clear();
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
					if (m_Buttons.get(i).Visible && m_Buttons.get(i).isActive() && m_Buttons.get(i).isInside(touches[0].getStartX(), touches[0].getStartY())) {
						//Set pressed button
						m_Pressed = i;
						m_Buttons.get(i).setState(Button.STATE_PRESSED);
						
						//SFX
						SoundManager.instance().playSFX(Utilities.SFX_FOLDER + "cursor.wav");
					}
				}
			} else {
				//Set state
				Button Pressed = m_Buttons.get(m_Pressed);
				if (Pressed.isInside(touches[0].getCurrentX(), touches[0].getCurrentY())) 	Pressed.setState(Button.STATE_PRESSED);
				else																		Pressed.setState(Button.STATE_NORMAL);
			}
		} else {
			//If there's a pressed button
			if (m_Pressed >= 0) {
				//If released inside set as result
				if (m_Buttons.get(m_Pressed).isInside(touches[0].getCurrentX(), touches[0].getCurrentY()))	Result = m_Buttons.get(m_Pressed).getID();
				
				//Reset button state
				m_Buttons.get(m_Pressed).setState(Button.STATE_NORMAL);
			}
			
			//Nothing pressed
			m_Pressed = -1;
		}
		
		
		//Return
		return Result;
	}
	
	public void draw(GL10 gl) {
		//Draw all buttons
		for (int i = 0; i < m_Buttons.size(); i++) m_Buttons.get(i).draw(gl);
	}
	
	//Constants
	public static final int NO_BUTTON = -1;
	
	//Data
	protected int				m_Pressed;
	protected ArrayList<Button> m_Buttons;
}
