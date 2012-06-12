/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ark.framework.system.j2me.canvas;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
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
	}
	
	//Accessor
	public Graphics getGraphics() { return super.getGraphics(); }

	public int[] getKeys() {		
		//Get key state
		int CanvasKeys = getKeyStates();

		//Check
		if ((CanvasKeys & GameCanvas.UP_PRESSED) != 0)		keyPressed(GameCanvas.UP_PRESSED);
		if ((CanvasKeys & GameCanvas.DOWN_PRESSED) != 0)	keyPressed(GameCanvas.DOWN_PRESSED);
		if ((CanvasKeys & GameCanvas.LEFT_PRESSED) != 0)	keyPressed(GameCanvas.LEFT_PRESSED);
		if ((CanvasKeys & GameCanvas.RIGHT_PRESSED) != 0)	keyPressed(GameCanvas.RIGHT_PRESSED);
		if ((CanvasKeys & GameCanvas.FIRE_PRESSED) != 0)	keyPressed(GameCanvas.FIRE_PRESSED);
		
		//Save keys
		int[] Keys	= m_Keys;
		m_Keys		= new int[] {};
		
		//Return
		return Keys;
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
}
