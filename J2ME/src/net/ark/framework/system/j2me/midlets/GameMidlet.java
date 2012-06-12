/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ark.framework.system.j2me.midlets;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import net.ark.framework.system.StateManager;
import net.ark.framework.system.j2me.J2MEDevice;
import net.ark.framework.system.j2me.canvas.ArkCanvas;

/**
 *
 * @author LegACy
 */
public abstract class GameMidlet extends MIDlet {
	protected void startApp() throws MIDletStateChangeException {
		//Create canvas
		m_Canvas = new ArkCanvas();
		Display.getDisplay(this).setCurrent(m_Canvas);
		
		//Setup
		initialize();		
		
		//Start statemanager
		new Thread(StateManager.instance()).start();
	}
    
    protected void initialize() {
        //Save midlet
        J2MEDevice.Midlet = this;
    }

	//Unused
	protected void pauseApp() {}
	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {}
	
	//Accessor
	public ArkCanvas getCanvas() { 
		return m_Canvas;
	}
	
	public void openURL(String url) {
		try {
			//Open the URL
			platformRequest(url);
		} catch (ConnectionNotFoundException ex) {}
	}
	
	//Data
	protected ArkCanvas m_Canvas;
}
