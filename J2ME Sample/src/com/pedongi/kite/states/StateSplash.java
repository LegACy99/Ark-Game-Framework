package com.pedongi.kite.states;

import com.pedongi.framework.components.Drawable;
import com.pedongi.framework.states.GameState;
import javax.microedition.lcdui.Graphics;

import com.pedongi.framework.system.StateManager;
import com.pedongi.framework.system.StringManager;
import com.pedongi.framework.system.input.TouchInfo;
import com.pedongi.framework.system.Utilities;
import com.pedongi.framework.system.images.Image;
import com.pedongi.framework.system.images.Label;
import com.pedongi.framework.system.input.AccelerometerInfo;
import com.pedongi.framework.system.images.j2me.BitmapFont;
import com.pedongi.framework.system.resource.ResourceManager;

public class StateSplash extends GameState {
	public StateSplash() {
		//Super
		super(GameState.SPLASH);
				
		//Initialize
		m_Timer 	= 0;
		m_Loading	= null;
		m_DrawnOnce	= false;
		m_Drawn		= false;

		//Create background
		m_Background = Image.create(Utilities.BACKGROUND_FOLDER + "splash.png");
		
		//Load language
		ResourceManager.instance().addString(StringManager.Language.ENGLISH.getID());
		
		//Load main font
		ResourceManager.instance().addFont(BitmapFont.THICK);
		ResourceManager.instance().addFont(BitmapFont.MAIN);
		
		//Load data
		ResourceManager.instance().addRecordLoading();
		
		//Load sound resources
		ResourceManager.instance().addSFX("cancel.wav");
		ResourceManager.instance().addSFX("cursor.wav");
		
		//Load title resources
		ResourceManager.instance().addImage(Utilities.BACKGROUND_FOLDER + "title.png");
		ResourceManager.instance().addImages(Utilities.BUTTON_FOLDER + "exit.png", 2, 1);
		
		//Load more fonts
		ResourceManager.instance().addFont(BitmapFont.SMALL);		
		ResourceManager.instance().addFont(BitmapFont.SMALL_BOLD);		
		ResourceManager.instance().addFont(BitmapFont.HUGE);
		ResourceManager.instance().addFont(BitmapFont.TINY);
		
		//Load shadow
		ResourceManager.instance().addShadow();
		ResourceManager.instance().addDarkBackground();	
		
		//Start loading
		ResourceManager.instance().start();
	}
	
	public void update(long time, int[] keys, TouchInfo[] touches, AccelerometerInfo accel) {
		//Super
		super.update(time, keys, touches, accel);
		
		//Skip if not drawn yet
		if (!m_DrawnOnce) return;
		
		//if there's no loading text
		if (m_Loading == null) {		
			//Get string
			String Loading = StringManager.instance().getString("loading");
			if (Loading != null && BitmapFont.getFont(BitmapFont.THICK) != null) {
				//Create label
				m_Loading = Label.create(Loading.toUpperCase(), BitmapFont.THICK);
				m_Loading.setPosition(BAR_OFFSETX * 2, Utilities.instance().getHeight() - BAR_OFFSETY, Drawable.ANCHOR_LEFT, Drawable.ANCHOR_BOTTOM);
			}
		}
		
		//Increase timer
		m_Timer += time;		
		
		//Is loader finished?
		if (ResourceManager.instance().isFinished()) {
			//If time limit passed
			if (m_Timer >= MIN_TIMER) {
				//go to title
				StateManager.instance().goTo(GameState.TITLE, null, true);
			}
		} else ResourceManager.instance().update();
	}
	
	public void draw(Graphics g) {
		//If not drawn yet
		if (!m_Drawn) {
			//If never drawn
			if (!m_DrawnOnce) {
				//Super
				super.draw(g);
				
				//Drawn once
				m_DrawnOnce = true;
			}
			
			//Draw texts
			if (m_Loading != null) m_Loading.draw(g);
			if (m_Loading != null) m_Drawn = true;
		}
		
		//Draw progress bar
        g.setColor(0xFFFFFFFF);
        float BarWidth = Utilities.instance().getWidth() - (BAR_OFFSETX * 2);
        g.fillRect(BAR_OFFSETX, (int)Utilities.instance().getHeight() - BAR_OFFSETY, (int)(ResourceManager.instance().getProgress() * BarWidth), BAR_HEIGHT);
	}
	
	//Constants
	protected final int BAR_OFFSETX = 12;
	protected final int BAR_OFFSETY = 24;
	protected final int BAR_HEIGHT 	= 12;
	protected final long MIN_TIMER 	= 2000;
	
	//Data
	protected long 		m_Timer;
	protected boolean	m_Drawn;
	protected boolean	m_DrawnOnce;
	protected Label		m_Loading;
}
