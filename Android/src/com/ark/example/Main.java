package com.ark.example;

import net.ark.framework.R;
import net.ark.framework.system.StateManager;
import net.ark.framework.system.System;
import net.ark.framework.system.Utilities;
import net.ark.framework.system.android.AndroidDevice;
import net.ark.framework.system.android.GameActivity;

import com.ark.example.states.ExampleStateFactory;

public class Main extends GameActivity implements System {
	@Override
	protected void initialize() {
		//Super
		super.initialize();
		
		//Initialize
        Utilities.instance().setSystem(this);
        AndroidDevice.instance().setSystem(this);
        StateManager.instance().setup(new ExampleStateFactory());
	}
	
	@Override public int getFPS() 					{ return 30;									}
	@Override public int getBaseWidth() 			{ return -1;									}
	@Override public int getBaseHeight() 			{ return 720;									}
	@Override public boolean isFontSmooth()			{ return true;									}
	@Override public String getReleaseSFX() 		{ return null;									}
	@Override public String getApplicationName() 	{ return getString(R.string.app_name);			}
	@Override public String getFontTexture() 		{ return Utilities.FONT_TEXTURES + "font.png";	}
	@Override public String getPressSFX() 			{ return Utilities.SFX_FOLDER + "cursor.wav";	}
	@Override public String getFont() 				{ return Utilities.FONT_FOLDER + "font.json";	}
}