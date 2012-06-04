/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pedongi.kite.states;

import com.pedongi.framework.components.Drawable;
import com.pedongi.framework.components.buttons.Button;
import com.pedongi.framework.components.buttons.ButtonContainer;
import com.pedongi.framework.states.GameState;
import com.pedongi.framework.system.RecordManager;
import com.pedongi.framework.system.SoundManager;
import com.pedongi.framework.system.StateManager;
import com.pedongi.framework.system.input.TouchInfo;
import com.pedongi.framework.system.Utilities;
import com.pedongi.framework.system.images.Image;
import com.pedongi.framework.system.input.AccelerometerInfo;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author LegACy
 */
public class StateTitle extends GameState {
	public StateTitle() {
		//Super
		super(GameState.TITLE, Utilities.BACKGROUND_FOLDER + "title.png");
		
		//Create kites
		m_KiteY		= new float[0];
		m_KiteX		= new float[0];
		m_KiteSpeed	= new float[0];
		m_Kites		= new Image[0];
	
		//1 kite
		createKite();
		if (m_KiteX[0] == 0)	m_KiteX[0] = Utilities.instance().getRandom(0, (int)Utilities.instance().getWidth() / 2);
		if (m_KiteX[0] > 0)		m_KiteY[0] = Utilities.instance().getRandom((int)Utilities.instance().getHeight() / 2, (int)Utilities.instance().getHeight());
		
		//Create images
		m_Title		= Image.create(Utilities.INTERFACE_FOLDER + "title.png");
		m_Version	= Image.create(Utilities.INTERFACE_FOLDER + "version.png");
		m_Version.setPosition(Utilities.instance().getWidth(), 0, Drawable.ANCHOR_RIGHT);
		m_Title.setPosition(Utilities.instance().getWidth() / 2f, 44, Drawable.ANCHOR_HCENTER);

		//Create components
		m_Buttons			= new ButtonContainer();
		Button ExitButton	= m_Buttons.addButton(BUTTON_EXIT, Utilities.BUTTON_FOLDER + "exit.png", null);
		Button HelpButton	= m_Buttons.addButton(BUTTON_HELP, Utilities.BUTTON_FOLDER + "help.png", null);
		Button MuteButton	= m_Buttons.addButton(BUTTON_MUTE, Utilities.BUTTON_FOLDER + "sound.png", null);
		Button UnmuteButton	= m_Buttons.addButton(BUTTON_UNMUTE, Utilities.BUTTON_FOLDER + "mute.png", null);
		Button CreditButton	= m_Buttons.addButton(BUTTON_CREDIT, Utilities.BUTTON_FOLDER + "credit.png", null);
		Button PlayButton	= m_Buttons.addButton(BUTTON_PLAY, Utilities.BUTTON_FOLDER + "button.png", "PLAY");
		
		//Set sound visibility
		SoundManager.instance().setMute(RecordManager.instance().isMute());
		UnmuteButton.Visible	= SoundManager.instance().isMute();
		MuteButton.Visible		= !SoundManager.instance().isMute();

		//Set buttons positions
		MuteButton.setPosition(4, 4);
		UnmuteButton.setPosition(4, 4);
		HelpButton.setPosition(4, Utilities.instance().getHeight() - 2, Drawable.ANCHOR_LEFT, Drawable.ANCHOR_BOTTOM);
		PlayButton.setPosition(Utilities.instance().getWidth() / 2, Utilities.instance().getHeight() * 0.6f, Drawable.ANCHOR_HCENTER, Drawable.ANCHOR_TOP);
		ExitButton.setPosition(Utilities.instance().getWidth() - 2, Utilities.instance().getHeight() - 2, Drawable.ANCHOR_RIGHT, Drawable.ANCHOR_BOTTOM);
		CreditButton.setPosition(2 + HelpButton.getOriginalWidth() + 6, Utilities.instance().getHeight() - 2, Drawable.ANCHOR_LEFT, Drawable.ANCHOR_BOTTOM);
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

		//Check back
		boolean Back = false;
		for (int i = 0; i < keys.length; i++) if (keys[i] == Utilities.instance().getBackButton()) Back = true;

		//If back
		if (Back) {
			//Done
			SoundManager.instance().playSFX(Utilities.SFX_FOLDER + "cancel.wav");
			m_Active = false;
		} else {
			//Next state exist?
			if (m_NextState >= 0) {
				//Change state
				StateManager.instance().goTo(m_NextState, null, false);
				
				//If upgrade
				if (m_NextState == GameState.UPGRADE) {
					//If has not been played
					if (!RecordManager.instance().isInitialized() || StateGame.Tutorial <= 1) {
						//Initialize
						RecordManager.instance().initialized();
						RecordManager.instance().save();
						
						//Go to game
						StateManager.instance().goTo(GameState.STORY, null, false);
					}
				}
			} else {
				//Check
				checkInput(keys, touches);
				checkKites(time);
			}
		}
	}

	protected void checkInput(int[] keys, TouchInfo[] touches) {
		//Get pressed button
		int Pressed = m_Buttons.update(keys, touches);
		if (Pressed != ButtonContainer.NO_BUTTON) {
			//Check
			switch (Pressed) {
			case BUTTON_PLAY:
				//Set next state
				m_NextState = GameState.UPGRADE;
				break;

			case BUTTON_MUTE:
			case BUTTON_UNMUTE:
				//Set mute
				SoundManager.instance().setMute(Pressed == BUTTON_MUTE);
				RecordManager.instance().mute(SoundManager.instance().isMute());
				RecordManager.instance().save();

				//Switch
				m_Buttons.getButton(-1, BUTTON_MUTE).Visible	= !SoundManager.instance().isMute();
				m_Buttons.getButton(-1, BUTTON_UNMUTE).Visible	= SoundManager.instance().isMute();
				break;

			case BUTTON_CREDIT:
				//Go to credit
				m_NextState = GameState.CREDIT;
				break;

			case BUTTON_HELP:
				//Go to help
				m_NextState = GameState.HELP;
				break;

			case BUTTON_EXIT:
				//Kill
				m_Active = false;
				break;
			}
		}

		//if there's a next state, don't draw button
		if (m_NextState >= 0) m_DrawButton = false;
	}

	protected void checkKites(long time) {
		//Create kite if not enough
		if (m_Kites.length < KITE_COUNT) createKite();

		//Initialize
		int Dead		= 0;
		boolean[] Died	= new boolean[m_Kites.length];

		//Update kites
		for (int i = 0; i < m_Kites.length; i++) {
			//Update position
			m_KiteX[i] += KITE_SPEEDX * m_KiteSpeed[i] * (float)time / 1000f;
			m_KiteY[i] -= KITE_SPEEDY * m_KiteSpeed[i] * (float)time / 1000f;

			//Check death
			if (m_KiteX[i] > Utilities.instance().getWidth() || m_KiteY[i] + m_Kites[i].getOriginalHeight() < 0) {
				//Died
				Dead++;
				Died[i] = true;
			} else Died[i] = false;

			//Update kite
			m_Kites[i].setPosition(m_KiteX[i], m_KiteY[i]);
		}

		//If there's a dead kite
		if (Dead > 0) {
			//Initialize
			int Current		= 0;
			float[]	KiteX	= new float[m_Kites.length - Dead];
			float[]	KiteY	= new float[m_Kites.length - Dead];
			float[]	Speed	= new float[m_Kites.length - Dead];
			Image[] Kites	= new Image[m_Kites.length - Dead];

			//Kill dead kites
			for (int i = 0; i < m_Kites.length; i++) {
				//If alive
				if (!Died[i]) {
					//Add
					KiteX[Current] = m_KiteX[i];
					KiteY[Current] = m_KiteY[i];
					Kites[Current] = m_Kites[i];
					Speed[Current] = m_KiteSpeed[i];
					Current++;
				}
			}

			//Update
			m_KiteX		= KiteX;
			m_KiteY		= KiteY;
			m_Kites		= Kites;
			m_KiteSpeed	= Speed;
		}
	}
	
	protected void createKite() {
		//Create new kite
		int Side		= Utilities.instance().getRandom(0, 2);
		Image Kite		= Image.create(Utilities.INTERFACE_FOLDER + "title-kite" + Utilities.instance().getRandom(1, KITE_TYPES + 1) + ".png");
		float KiteY		= Side == 0 ? Utilities.instance().getRandom(1, (int)Utilities.instance().getHeight()) : Utilities.instance().getHeight();
		float KiteX		= Side == 0 ? -Kite.getOriginalWidth() : Utilities.instance().getRandom(1, (int)(Utilities.instance().getWidth() - Kite.getOriginalWidth()));
		float KiteSpeed	= Kite.getOriginalWidth() / 72f;
		
		//Copy kites
		float[] NewX		= new float[m_Kites.length + 1];
		float[] NewY		= new float[m_Kites.length + 1];
		float[] NewSpeed	= new float[m_Kites.length + 1];
		Image[] NewKites	= new Image[m_Kites.length + 1];
		System.arraycopy(m_KiteX, 0, NewX, 0, m_KiteX.length);
		System.arraycopy(m_KiteY, 0, NewY, 0, m_KiteY.length);
		System.arraycopy(m_Kites, 0, NewKites, 0, m_Kites.length);
		System.arraycopy(m_KiteSpeed, 0, NewSpeed, 0, m_KiteSpeed.length);
		
		//Add new
		NewX[m_Kites.length]		= KiteX;
		NewY[m_Kites.length]		= KiteY;
		NewSpeed[m_Kites.length]	= KiteSpeed;
		NewKites[m_Kites.length]	= Kite;
		
		//Set
		m_KiteX		= NewX;
		m_KiteY		= NewY;
		m_Kites		= NewKites;
		m_KiteSpeed	= NewSpeed;
	}
	
	public void draw(Graphics g) {		
		//Super
		super.draw(g);
		
		//Draw kites
		for (int i = 0; i < m_Kites.length; i++) m_Kites[i].draw(g);
		
		//Draw components
		m_Title.draw(g);
		m_Version.draw(g);

		//Draw buttons
		if (m_DrawButton) m_Buttons.draw(g);
	}
	
	//Constants
	protected static final int KITE_TYPES		= 5;
	protected static final int KITE_COUNT		= 3;
	protected static final int BUTTON_PLAY		= 1;
	protected static final int BUTTON_EXIT		= 2;
	protected static final int BUTTON_MUTE		= 3;
	protected static final int BUTTON_UNMUTE	= 4;
	protected static final int BUTTON_CREDIT	= 5;
	protected static final int BUTTON_HELP		= 6;
	protected static final float KITE_SPEEDX	= 30;
	protected static final float KITE_SPEEDY	= 60;
	
	//Data
	protected float[]	m_KiteX;
	protected float[]	m_KiteY;
	protected float[]	m_KiteSpeed;
	protected boolean	m_DrawButton;
	protected int		m_NextState;
	
	//Components
	protected Image				m_Title;
	protected Image				m_Version;
	protected ButtonContainer	m_Buttons;
	protected Image[]			m_Kites;
}
