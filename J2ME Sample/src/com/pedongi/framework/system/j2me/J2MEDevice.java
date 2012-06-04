/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pedongi.framework.system.j2me;

import com.pedongi.framework.system.Device;
import com.pedongi.framework.system.input.TouchInfo;
import com.pedongi.framework.system.Utilities;
import com.pedongi.framework.system.input.AccelerometerInfo;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

/**
 *
 * @author LegACy
 */
public class J2MEDevice extends Device {
    protected J2MEDevice() {
        //Super
        super();

		//Full screen
		setFullScreenMode(true);
    	
    	//Initialize
		m_Keys			= new int[0];
		m_Touches 		= new TouchInfo[10];
		m_Accelerometer	= new AccelerometerInfo();
		for (int i = 0; i < m_Touches.length; i++) m_Touches[i] = new TouchInfo();
		
		//Calculate column
		m_Column	= (getWidth()  + Utilities.instance().getTileWidth()  - 1) / Utilities.instance().getTileWidth();
		m_Row		= (getHeight() + Utilities.instance().getTileHeight() - 1) / Utilities.instance().getTileHeight();
    }
	
	public synchronized static Device instance() {
		//Create state manager if doesn't exist
		if (s_Instance == null) s_Instance = new J2MEDevice();
		return s_Instance;
	}

    //Accessor
	public Graphics getGraphic() {	return getGraphics();	};

	public int[] getKeys() {
		//Get key state
		int CanvasKeys = getKeyStates();

		//Check
		if ((CanvasKeys & GameCanvas.UP_PRESSED) != 0)		keyPressed(GameCanvas.UP_PRESSED);
		if ((CanvasKeys & GameCanvas.DOWN_PRESSED) != 0)	keyPressed(GameCanvas.DOWN_PRESSED);
		if ((CanvasKeys & GameCanvas.LEFT_PRESSED) != 0)	keyPressed(GameCanvas.LEFT_PRESSED);
		if ((CanvasKeys & GameCanvas.RIGHT_PRESSED) != 0)	keyPressed(GameCanvas.RIGHT_PRESSED);
		if ((CanvasKeys & GameCanvas.FIRE_PRESSED) != 0)	keyPressed(GameCanvas.FIRE_PRESSED);

		//Return
		return super.getKeys();
	}

	public void keyPressed(int keyCode) {
		//Super
		super.keyPressed(keyCode);

		//Check key
		boolean Ignore = false;
		for (int i = 0; i < IGNORED_KEYS.length; i++) if (keyCode == IGNORED_KEYS[i]) Ignore = true;

		//If not ignored
		if (!Ignore) {
			//Add
			int[] Keys = new int[m_Keys.length + 1];
			System.arraycopy(m_Keys, 0, Keys, 0, m_Keys.length);
			Keys[m_Keys.length]	= keyCode;
			m_Keys				= Keys;
		}
	}

	public synchronized void pointerPressed(int x, int y) {
		if (m_Touches != null) m_Touches[0].pressed(x, y);
	}

	public synchronized void pointerDragged(int x, int y) {
		if (m_Touches != null) m_Touches[0].dragged(x, y);
	}

	public synchronized void pointerReleased(int x, int y) {
		if (m_Touches != null) m_Touches[0].released(x, y);
	}
	
	//The only instance
	private static J2MEDevice s_Instance = null;

	//Constants
	protected final int AXIS_X 			= 0;
	protected final int AXIS_Y 			= 1;
	protected final int AXIS_Z 			= 2;
	protected final int[] IGNORED_KEYS 	=  {  };
	
	//Data
	/*protected long				m_TimerUp;
	protected long				m_TimerDown;
	protected long				m_TimerLeft;
	protected long				m_TimerRight;
	protected boolean			m_Suppressed;
	protected boolean			m_HoldRight;
	protected boolean			m_HoldLeft;
	protected boolean			m_HoldFire;
	protected boolean			m_HoldDown;
	protected boolean			m_HoldUp;*/
}
