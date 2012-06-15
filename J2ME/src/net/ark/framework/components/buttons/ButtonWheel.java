/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ark.framework.components.buttons;

import net.ark.framework.system.Device;
import net.ark.framework.system.SoundManager;
import net.ark.framework.system.Utilities;
import net.ark.framework.system.input.TouchInfo;

/**
 *
 * @author LegACy
 */
public class ButtonWheel extends ButtonContainer {
	public ButtonWheel() { this(0, 0); }	
	public ButtonWheel(float x, float y) {
		//Super
		super();
		
		//Initialize
		m_Selected	= 0;
		setPosition(x, y);
	}
	
	public Button addButton(Button button) {		
		//If button exist
		if (!m_Buttons.isEmpty()) {
			//Set position under last
			Button Last = (Button) m_Buttons.lastElement();
			button.setPosition(Last.getX() / Utilities.instance().getScale(), (Last.getY() + Last.getHeight()) / Utilities.instance().getScale());
		}
		
		//Super
		Button Added = super.addButton(button);
		
		//If selected
		if (m_Buttons.size() == m_Selected + 1) ((Button)m_Buttons.lastElement()).setState(Button.STATE_PRESSED);
		
		//Return
		return Added;
	}
	
	public int update(int[] keys, TouchInfo[] touches) {
		//Initialize
		int Result = NO_BUTTON;

		//Check
		if (m_Buttons.size() <= m_Pressed) m_Pressed = -1;
		
		//Initialize
		boolean Up		= false;
		boolean Down	= false;
		boolean Fire	= false;
		for (int i = 0; i < keys.length; i++) {
			//Check
			if (keys[i] == Device.instance().getUpButton())			Up = true;
			else if (keys[i] == Device.instance().getDownButton())	Down = true;
			else if (keys[i] == Device.instance().getFireButton())	Fire = true;
		}
		
		//If up or down
		if (Up || Down) {
			//Save index
			int Old = m_Selected;
			
			//Change index
			if (Up)		m_Selected--;
			if (Down)	m_Selected++;

			//Correct selection
			if (m_Selected < 0) m_Selected = m_Buttons.size() - 1;
			m_Selected = m_Selected % m_Buttons.size();

			//If changed
			if (m_Selected != Old) {
				//Change state
				((Button)m_Buttons.elementAt(Old)).setState(Button.STATE_NORMAL);
				((Button)m_Buttons.elementAt(m_Selected)).setState(Button.STATE_PRESSED);
				
				//SFX
				if (Utilities.instance().getSystemCursorSFX() != null) SoundManager.instance().playSFX(Utilities.instance().getSystemCursorSFX());
			}
		} else if (Fire) {
			//Set result
			Result = getButton(m_Selected).getID();
			if (Utilities.instance().getSystemPressSFX() != null) SoundManager.instance().playSFX(Utilities.instance().getSystemPressSFX());
		} else {
			//Super
			Result = super.update(keys, touches);
		}
		
		//Return
		return Result;
	}

	//Data
	protected int m_Selected;
}
