package net.ark.example;

import net.ark.example.states.KiteStateFactory;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import net.ark.framework.system.StateManager;
import net.ark.framework.system.j2me.J2MEDevice;
import javax.microedition.io.ConnectionNotFoundException;

public class Main extends MIDlet {	
    public Main() {
    	//Do nothing
    }

	public void openURL(String url) {
		try {
			//Open the URL
			platformRequest(url);
		} catch (ConnectionNotFoundException ex) {}
	}

	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
    	//Do nothing
	}

	protected void pauseApp() {
    	//Do nothing
	}

	protected void startApp() throws MIDletStateChangeException {
		//Set midlet
		Midlet = this;
		
		//Set display
		Display.getDisplay(this).setCurrent(J2MEDevice.instance());
		StateManager.instance().setup(new KiteStateFactory());
		new Thread(StateManager.instance()).start();
	}
	
	public static Main Midlet = null;
}
