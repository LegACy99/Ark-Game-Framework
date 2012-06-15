/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ark.framework.system.j2me.canvas;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import net.ark.framework.system.Device;
import net.ark.framework.system.Utilities;
import net.ark.framework.system.input.AccelerometerInfo;
import net.ark.framework.system.input.TouchInfo;
import net.ark.framework.system.j2me.J2MEDevice;

/**
 *
 * @author LegACy
 */
public class ArkCanvas extends GameCanvas {
	public ArkCanvas() {
		//Super
		super(false);
		
		//Fullscreen
		setFullScreenMode(true);
		
		//Initialize
		m_Keys			= new int[0];
		m_Touch			= new TouchInfo();
		m_Accelerometer = new AccelerometerInfo();
		m_TimerUp		= 0;
		m_TimerDown		= 0;
		m_TimerLeft		= 0;
		m_TimerRight	= 0;
    	m_Suppressed	= true;
		m_BlockRight	= false;
		m_BlockLeft		= false;
		m_BlockFire		= false;
		m_BlockDown		= false;
		m_BlockUp		= false;
	}
	
	//Accessor
	public Graphics getGraphics() { return super.getGraphics(); }

	public int[] getKeys() {		
		//Get key state
		int CanvasKeys = getKeyStates();

		//If up
		if ((CanvasKeys & Device.instance().getUpButton()) != 0) {
			//If not blocked or isn't suppressed
			if (!m_BlockUp || !m_Suppressed) {
				//Register key
				keyPressed(Device.instance().getUpButton());
				
				//If suppressed
				if (m_Suppressed) {
					//Block
					m_BlockUp	= true;
					m_TimerUp	= Utilities.BUTTON_DELAY;

					//Free opposite button
					m_BlockDown	= false;
					m_TimerDown = 0;
				}
			}
		}
		
		//If down
		if ((CanvasKeys & Device.instance().getDownButton()) != 0) {
			//If not blocked or isn't suppressed
			if (!m_BlockDown || !m_Suppressed) {
				//Register key
				keyPressed(Device.instance().getDownButton());
				
				//If suppressed
				if (m_Suppressed) {
					//Block
					m_BlockDown = true;
					m_TimerDown = Utilities.BUTTON_DELAY;

					//Free up button
					m_BlockUp = false;
					m_TimerUp = 0;
				}
			}
		}
		
		//If left
		if ((CanvasKeys & Device.instance().getLeftButton()) != 0) {
			//If not blocked or isn't suppressed
			if (!m_BlockLeft || !m_Suppressed) {
				//Register key
				keyPressed(Device.instance().getLeftButton());
				
				//If suppressed
				if (m_Suppressed) {
					//Block
					m_BlockLeft	= true;
					m_TimerLeft	= Utilities.BUTTON_DELAY;

					//Free right button
					m_BlockRight = false;
					m_TimerRight = 0;
				}
			}
		}
		
		//If right
		if ((CanvasKeys & Device.instance().getRightButton()) != 0) {
			//If not blocked or isn't suppressed
			if (!m_BlockRight || !m_Suppressed) {
				//Register key
				keyPressed(Device.instance().getRightButton());
				
				//If suppressed
				if (m_Suppressed) {
					//Block
					m_BlockRight	= true;
					m_TimerRight	= Utilities.BUTTON_DELAY;

					//Free left button
					m_BlockLeft	= false;
					m_TimerLeft	= 0;
				}
			}			
		}
		
		//If fire
		if ((CanvasKeys & Device.instance().getFireButton()) != 0) {
			//If not blocked or isn't suppressed
			if (!m_BlockFire || !m_Suppressed) {
				//Register and block
				keyPressed(Device.instance().getFireButton());
				m_BlockFire = true;
			}
		} else m_BlockFire = false;
		
		//Save keys
		int[] Keys	= m_Keys;
		m_Keys		= new int[] {};
		
		//Return
		return Keys;
	}
	
	public void updateTime(long time) {
		//Set timer
		if (m_TimerUp > 0)		m_TimerUp -= time;
		if (m_TimerDown > 0)	m_TimerDown -= time;
		if (m_TimerLeft > 0)	m_TimerLeft -= time;
		if (m_TimerRight > 0)	m_TimerRight -= time;
		
		//Check timer
		if (m_TimerUp <= 0 && m_BlockUp)		m_BlockUp = false;
		if (m_TimerDown <= 0 && m_BlockDown)	m_BlockDown = false;
		if (m_TimerLeft <= 0 && m_BlockLeft)	m_BlockLeft = false;
		if (m_TimerRight <= 0 && m_BlockRight)	m_BlockRight = false;
	}
	
	public void keyPressed(int keyCode) {
		//Super
		super.keyPressed(keyCode);

		//Check key
		boolean Ignore = false;
		for (int i = 0; i < J2MEDevice.IGNORED_KEYS.length; i++) if (keyCode == J2MEDevice.IGNORED_KEYS[i]) Ignore = true;

		//If not ignored
		if (!Ignore) {
			//Add
			int[] Keys = new int[m_Keys.length + 1];
			System.arraycopy(m_Keys, 0, Keys, 0, m_Keys.length);
			Keys[m_Keys.length]	= keyCode;
			m_Keys				= Keys;
		}
	}
	
	public TouchInfo getTouch() {
		return m_Touch;
	}

	public synchronized void pointerPressed(int x, int y) {
		m_Touch.pressed(x, y);
	}

	public synchronized void pointerDragged(int x, int y) {
		m_Touch.dragged(x, y);
	}

	public synchronized void pointerReleased(int x, int y) {
		m_Touch.released(x, y);
	}
	
	//Data
	protected int[]				m_Keys;
	protected TouchInfo			m_Touch;
	protected AccelerometerInfo m_Accelerometer;
	protected boolean   		m_Suppressed;
	protected boolean   		m_BlockRight;
	protected boolean			m_BlockLeft;
	protected boolean			m_BlockFire;
	protected boolean			m_BlockDown;
	protected boolean			m_BlockUp;
	protected long				m_TimerUp;
	protected long				m_TimerDown;
	protected long				m_TimerLeft;
	protected long				m_TimerRight;
}
