package com.ark.example.states;

import javax.microedition.khronos.opengles.GL10;

import net.ark.framework.components.Drawable;
import net.ark.framework.system.StateManager;
import net.ark.framework.system.Utilities;
import net.ark.framework.system.android.input.AccelerometerInfo;
import net.ark.framework.system.android.input.TouchInfo;
import net.ark.framework.system.images.Image;
import net.ark.framework.system.resource.ResourceManager;

public class StateSplash extends ExampleState {
	public StateSplash() {
		super(ExampleState.SPLASH);
		
		//Load splash resource
		ResourceManager.instance().addTexture(Utilities.TEXTURE_FOLDER + "splash.png");
		ResourceManager.instance().addImage(Utilities.BACKGROUND_FOLDER + "splash.json");
		ResourceManager.instance().addImage(Utilities.INTERFACE_FOLDER + "wheel.json");
		ResourceManager.instance().start();
		while (!ResourceManager.instance().isFinished()) ResourceManager.instance().update();
				
		//Load musics
		ResourceManager.instance().addSFX(Utilities.SFX_FOLDER + "cursor.wav");
		ResourceManager.instance().addSFX(Utilities.SFX_FOLDER + "cancel.wav");
		
		//Load resource
		ResourceManager.instance().addRecordLoading();
		ResourceManager.instance().addTexture(Utilities.TEXTURE_FOLDER + "title.png");
		ResourceManager.instance().addImage(Utilities.BACKGROUND_FOLDER + "title.json");
		ResourceManager.instance().addImage(Utilities.INTERFACE_FOLDER + "button.json");
		ResourceManager.instance().addTexture(Utilities.FONT_TEXTURES + "font.png");
		ResourceManager.instance().addFont(Utilities.FONT_FOLDER + "font.json");
		
		//Start
		ResourceManager.instance().start();
		
		//Reset time
		m_Time = 0;
		
		//Create images
		m_Title = new Image(ResourceManager.instance().getJSON(Utilities.BACKGROUND_FOLDER + "splash.json"));
		m_Wheel	= new Image(ResourceManager.instance().getJSON(Utilities.INTERFACE_FOLDER + "wheel.json"));
		m_Wheel.setPosition(Utilities.instance().getWidth() / 2f, Utilities.instance().getHeight() / 2f, Drawable.ANCHOR_HCENTER, Drawable.ANCHOR_VCENTER);
	}

	@Override
	public void update(long time, int[] keys, TouchInfo[] touches, AccelerometerInfo accel) {
		//Increase time
		m_Time += time;
		
		//Check resource manager
		if (ResourceManager.instance().isFinished()) {
			//Check time			
			if (m_Time >= MIN_TIME)	StateManager.instance().goTo(ExampleState.TITLE, null, true);
		} else ResourceManager.instance().update();
		
		//Set rotation
		m_Wheel.setRotation(0, WHEEL_SPEED);
	}
	
	@Override
	public void draw(GL10 gl) {		
		//Draw images
		m_Title.draw(gl);
		m_Wheel.draw(gl);
	}
	
	//Constants
	protected final static long MIN_TIME 		= 3000;
	protected final static float WHEEL_SPEED 	= 8;
	
	//Members
	protected long 	m_Time;
	protected Image	m_Wheel;
	protected Image m_Title;
}
