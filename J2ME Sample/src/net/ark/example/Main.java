package net.ark.example;

import net.ark.framework.system.StateManager;
import net.ark.example.states.ExampleStateFactory;
import net.ark.example.system.ExampleUtilities;
import net.ark.framework.system.j2me.midlets.GameMidlet;
import net.ark.framework.system.System;
import net.ark.framework.system.Utilities;

public class Main extends GameMidlet implements System  {
    protected void initialize() {
		//Super
		super.initialize();
		
		//Set factory
		StateManager.instance().setup(new ExampleStateFactory());
    }
	
	public int getFPS() 				{ return 30;									}
	public int getBaseWidth() 			{ return -320;									}
	public int getBaseHeight() 			{ return 240;									}
	public boolean isFontSmooth()		{ return true;									}
	public String getReleaseSFX() 		{ return null;									}
	public String getFontTexture() 		{ return null;									}
	public String getApplicationName() 	{ return "J2ME Sample";							}
	public String getPressSFX() 		{ return Utilities.SFX_FOLDER + "cursor.wav";	}
	public String getFont() 			{ return ExampleUtilities.MAIN_FONT;			}
	
}
