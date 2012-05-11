package com.ark.example.states;

import javax.microedition.khronos.opengles.GL10;

import com.ark.example.system.ExampleUtilities;

import net.ark.framework.components.Drawable;
import net.ark.framework.components.buttons.Button;
import net.ark.framework.components.buttons.ButtonContainer;
import net.ark.framework.system.Device;
import net.ark.framework.system.SoundManager;
import net.ark.framework.system.Utilities;
import net.ark.framework.system.android.input.AccelerometerInfo;
import net.ark.framework.system.android.input.TouchInfo;

public class StateTitle extends ExampleState {
	public StateTitle() {
		//Super
		super(ExampleState.TITLE, ExampleUtilities.BACKGROUND_FOLDER + "title.json");

		//Create containers
		m_Buttons = new ButtonContainer();
		Button Back = m_Buttons.addButton(BUTTON_BACK, ExampleUtilities.INTERFACE_FOLDER + "button.json", null);
		Back.setPosition(Utilities.instance().getWidth() - 20, Utilities.instance().getHeight() - 20, Drawable.ANCHOR_RIGHT, Drawable.ANCHOR_BOTTOM);
	}
	
	@Override
	public void onEnter() {		
		//Play BGM
		SoundManager.instance().loadBGM(Utilities.BGM_FOLDER + "theme.mp3");
		SoundManager.instance().playBGM();
	}

	@Override
	public void update(long time, int[] keys, TouchInfo[] touches, AccelerometerInfo accel) {
		//Check back
		boolean Back = false;
		for (int i = 0; i < keys.length; i++) if (keys[i] == Device.instance().getBackButton()) Back = true;
		
		//If back
		if (Back) {
			//Done
			SoundManager.instance().playSFX(Utilities.SFX_FOLDER + "cancel.wav");
			m_Active = false;
		} else {	
			//Check buttons
			int Pressed = m_Buttons.update(keys, touches);
			if (Pressed == BUTTON_BACK) m_Active = false;
		}
	}
	
	@Override
	public void draw(GL10 gl) {
		//Super
		super.draw(gl);
		m_Buttons.draw(gl);
	}
	
	//Button IDs
	protected static final int BUTTON_BACK		= 1;
	
	//Components
	protected ButtonContainer	m_Buttons;
}
