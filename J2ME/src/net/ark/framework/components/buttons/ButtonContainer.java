/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ark.framework.components.buttons;

import net.ark.framework.system.SoundManager;
import net.ark.framework.system.Utilities;
import net.ark.framework.system.input.TouchInfo;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author LegACy
 */
public class ButtonContainer {
	public ButtonContainer() {
		//Initialize
		m_Pressed = -1;
		m_Buttons = new Button[0];
	}

	public Button addButton(int id, String images, String text) {
		//Create and add button
		return addButton(new Button(id, images, text));
	}

	public Button addButton(Button button) {
		//Add
		Button[] Buttons = new Button[m_Buttons.length + 1];
		System.arraycopy(m_Buttons, 0, Buttons, 0, m_Buttons.length);
		Buttons[m_Buttons.length]	= button;
		m_Buttons					= Buttons;

		//Return the button
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
		if (index >= 0 && index < m_Buttons.length) Result = m_Buttons[index];
		else {
			//Find from id
			for (int i = 0; i < m_Buttons.length; i++)
				if (m_Buttons[i].getID() == id) Result = m_Buttons[i];
		}

		//Return
		return Result;
	}

	public void removeButtons() {
		//Destroy buttons
		m_Buttons = new Button[0];
	}

	public int update(int[] keys, TouchInfo[] touches) {
		//Initialize
		int Result = NO_BUTTON;

		//Check
		if (m_Buttons.length <= m_Pressed) m_Pressed = -1;

		//If pressed
		if (touches[0].isPressed()) {
			//If no button is pressed
			if (m_Pressed < 0) {
				//For each button
				for (int i = 0; i < m_Buttons.length; i++) {
					//If pressed
					if (m_Buttons[i].Visible && m_Buttons[i].isActive() && m_Buttons[i].isInside(touches[0].getStartX(), touches[0].getStartY())) {
						//Set pressed button
						m_Pressed = i;
						m_Buttons[i].setState(Button.STATE_PRESSED);

						//SFX
						SoundManager.instance().playSFX(Utilities.SFX_FOLDER + "cursor.wav");
					}
				}
			} else {
				//Set state
				Button Pressed = m_Buttons[m_Pressed];
				if (Pressed.isInside(touches[0].getCurrentX(), touches[0].getCurrentY())) 	Pressed.setState(Button.STATE_PRESSED);
				else																		Pressed.setState(Button.STATE_NORMAL);
			}
		} else {
			//If there's a pressed button
			if (m_Pressed >= 0) {
				//If released inside set as result
				if (m_Buttons[m_Pressed].isInside(touches[0].getCurrentX(), touches[0].getCurrentY()))	Result = m_Buttons[m_Pressed].getID();

				//Reset button state
				m_Buttons[m_Pressed].setState(Button.STATE_NORMAL);
			}

			//Nothing pressed
			m_Pressed = -1;
		}

		//Return
		return Result;
	}

	public void draw(Graphics g) {
		//Draw all buttons
		for (int i = 0; i < m_Buttons.length; i++) m_Buttons[i].draw(g);
	}

	//Constants
	public static final int NO_BUTTON = -1;

	//Data
	protected int		m_Pressed;
	protected Button[]	m_Buttons;
}
