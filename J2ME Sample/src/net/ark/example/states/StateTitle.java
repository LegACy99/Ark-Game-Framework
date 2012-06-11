/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ark.example.states;

import net.ark.framework.components.Drawable;
import net.ark.framework.components.buttons.Button;
import net.ark.framework.components.buttons.ButtonContainer;
import net.ark.framework.states.GameState;
import net.ark.framework.system.RecordManager;
import net.ark.framework.system.SoundManager;
import net.ark.framework.system.StateManager;
import net.ark.framework.system.input.TouchInfo;
import net.ark.framework.system.Utilities;
import net.ark.framework.system.input.AccelerometerInfo;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

/**
 *
 * @author LegACy
 */
public class StateTitle extends GameState {
	public StateTitle() {
		//Super
		super(GameState.TITLE, Utilities.BACKGROUND_FOLDER + "title.png");

		//Create components
		m_Buttons			= new ButtonContainer();
		Button ExitButton	= m_Buttons.addButton(BUTTON_EXIT, Utilities.BUTTON_FOLDER + "exit.png", null);
		ExitButton.setPosition(Utilities.instance().getWidth() - 2, Utilities.instance().getHeight() - 2, Drawable.ANCHOR_RIGHT, Drawable.ANCHOR_BOTTOM);
		
		//Set sound visibility
		SoundManager.instance().setMute(RecordManager.instance().isMute());
	}

	public void onEnter() {
		//Reset next state
		m_NextState		= -1;
		m_DrawButton	= true; 

		//Play BGM
		SoundManager.instance().loadBGM("title.mid");
		SoundManager.instance().playBGM();
	}
	
	public void update(long time, int[] keys, TouchInfo[] touches, AccelerometerInfo accel) {
		//Super
		super.update(time, keys, touches, accel);

		//Check buttons
		boolean Fire = false;
		boolean Back = false;
		boolean Left = false;
		for (int i = 0; i < keys.length; i++) {
			if (keys[i] == Utilities.instance().getBackButton())		Back = true;
			else if (keys[i] == Utilities.instance().getMenuButton())	Left = true;
			else if (keys[i] == GameCanvas.FIRE_PRESSED)				Fire = true;			
		}
		
		//If back
		if (Back) {
			//Done
			SoundManager.instance().playSFX(Utilities.SFX_FOLDER + "cancel.wav");
			m_Active = false;
		} else if (Left) {
		} else if (Fire) {
		} else {
			//Next state exist?
			if (m_NextState >= 0) {
				//Change state
				StateManager.instance().goTo(m_NextState, null, false);
			} else {
				//Check
				checkInput(keys, touches);
			}
		}
	}

	protected void checkInput(int[] keys, TouchInfo[] touches) {
		//Get pressed button
		int Pressed = m_Buttons.update(keys, touches);
		if (Pressed != ButtonContainer.NO_BUTTON) {
			//Check
			switch (Pressed) {
			case BUTTON_EXIT:
				//Kill
				m_Active = false;
				break;
			}
		}

		//if there's a next state, don't draw button
		if (m_NextState >= 0) m_DrawButton = false;
	}
	
	public void draw(Graphics g) {		
		//Super
		super.draw(g);

		//Draw buttons
		if (m_DrawButton) m_Buttons.draw(g);
	}
	
	//Constants
	protected static final int BUTTON_EXIT = 2;
	
	//Data
	protected int				m_NextState;
	protected boolean			m_DrawButton;
	protected ButtonContainer	m_Buttons;
}
