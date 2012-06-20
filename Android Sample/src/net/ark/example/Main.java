package net.ark.example;

import net.ark.example.states.ExampleStateFactory;
import net.ark.framework.system.StateManager;
import net.ark.framework.system.System;
import net.ark.framework.system.Utilities;
import net.ark.framework.system.android.AndroidDevice;
import net.ark.framework.system.android.activities.GameActivity;
import net.ark.framework.system.android.activities.WebActivity;
import net.ark.sample.R;
import android.content.Intent;

public class Main extends GameActivity implements System {
	@Override
	protected void initialize() {
		//Super
		super.initialize();
		
		//Initialize
        Utilities.instance().setSystem(this);
        StateManager.instance().setup(new ExampleStateFactory());
	}
	
	@Override public int getFPS() 					{ return 30;									}
	@Override public int getBaseWidth() 			{ return -1;									}
	@Override public int getBaseHeight() 			{ return 720;									}
	@Override public boolean isFontSmooth()			{ return true;									}
	@Override public String getReleaseSFX() 		{ return null;									}
	@Override public String getApplicationName() 	{ return getString(R.string.app_name);			}
	@Override public String getFontTexture() 		{ return Utilities.FONT_TEXTURES + "font.png";	}
	@Override public String getCursorSFX() 			{ return Utilities.SFX_FOLDER + "cursor.ogg";	}
	@Override public String getPressSFX() 			{ return Utilities.SFX_FOLDER + "cursor.ogg";	}
	@Override public String getFont() 				{ return Utilities.FONT_FOLDER + "font.json";	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		//Super
		super.onNewIntent(intent);
		
		//Ensure intent exist
		if (intent != null) {
			//Create intent
			Intent NewIntent = null;
			if (intent.hasExtra(AndroidDevice.EXTRA_URL)) {
				//Create web intent
				NewIntent = new Intent(this, WebActivity.class);
				NewIntent.putExtra(AndroidDevice.EXTRA_URL, intent.getStringExtra(AndroidDevice.EXTRA_URL));
				
				//Add title and loading
				if (intent.hasExtra(AndroidDevice.EXTRA_TITLE)) 	NewIntent.putExtra(AndroidDevice.EXTRA_TITLE, intent.getStringExtra(AndroidDevice.EXTRA_TITLE));
				if (intent.hasExtra(AndroidDevice.EXTRA_LOADING)) 	NewIntent.putExtra(AndroidDevice.EXTRA_LOADING, intent.getStringExtra(AndroidDevice.EXTRA_LOADING));
			}
			
			//Execute
			if (NewIntent != null) startActivity(NewIntent);
		}
	}
}