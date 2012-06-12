/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ark.framework.system.j2me;

import net.ark.framework.system.Device;
import net.ark.framework.system.input.TouchInfo;
import net.ark.framework.system.input.AccelerometerInfo;
import javax.microedition.lcdui.Graphics;
import net.ark.framework.system.j2me.midlets.GameMidlet;

/**
 *
 * @author LegACy
 */
public class J2MEDevice extends Device {
    protected J2MEDevice() {
        //Super
        super();
		
		//If midlet exist
		if (Midlet != null) {			
			//Get size
			m_Width		= Midlet.getCanvas().getWidth();
			m_Height	= Midlet.getCanvas().getHeight();
		
			//No column/row
			m_Row		= 1;
			m_Column	= 1;
			
			//Calculate column
			//m_Column	= (getWidth()  + Utilities.instance().getTileWidth()  - 1) / Utilities.instance().getTileWidth();
			//m_Row		= (getHeight() + Utilities.instance().getTileHeight() - 1) / Utilities.instance().getTileHeight();
		}
		
		
		//Initialize input
        m_Back			= -7;
        m_Menu			= -6;
		m_Keys			= null;
		m_Touches 		= new TouchInfo[10];
		m_Accelerometer = new AccelerometerInfo();
		for (int i = 0; i < m_Touches.length; i++) m_Touches[i] = new TouchInfo();
    }
	
	public synchronized static Device instance() {
		//Create state manager if doesn't exist
		if (s_Instance == null) s_Instance = new J2MEDevice();
		return s_Instance;
	}
	
	//Accessors
	public Graphics getGraphic() { 
		return Midlet.getCanvas().getGraphics();
	}

	public int[] getKeys() {
		//Initialize
		m_Keys = new int[] {};
		
		//If midlet exist
		if (Midlet != null) {
			//Copy keys
			int[] CanvasKeys	= Midlet.getCanvas().getKeys();
			m_Keys				= new int[CanvasKeys.length];
			System.arraycopy(CanvasKeys, 0, m_Keys, 0, m_Keys.length);
		}

		//Return
		return super.getKeys();
	}
	
	public TouchInfo[] getTouches()	{
		//If midlet exist
		if (Midlet != null) {
			//Set touch
			m_Touches[0] = Midlet.getCanvas().getTouch();
		}
		
		//Return super
		return super.getTouches();
	}
	
	public void openURL(String url, boolean browser, String title, String loading) {
		//Open URL if midlet and url exist
		if (Midlet != null && url != null) Midlet.openURL(url);
	}
	
	//Single instances
	public static GameMidlet	Midlet = null;
	private static J2MEDevice	s_Instance = null;

	//Constants
	protected final int AXIS_X					= 0;
	protected final int AXIS_Y					= 1;
	protected final int AXIS_Z					= 2;
	public static final int[] IGNORED_KEYS		=  {};
    public static final String EXTRA_LOADING 	= "loading";
    public static final String EXTRA_TITLE 		= "title";
	public static final String EXTRA_URL		= "url";
	
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
