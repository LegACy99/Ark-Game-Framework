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
import com.pedongi.kite.game.UpgradeManager;

public class StateSplash extends GameState {
	public StateSplash() {
		//Super
		super(GameState.SPLASH);
				
		//Initialize
		m_Timer 	= 0;
		m_Present	= null;
		m_Loading	= null;
		m_DrawnOnce	= false;
		m_Drawn		= false;

		//Create developer image
		m_Developer	= Image.create(Utilities.INTERFACE_FOLDER + "developer.png");
		m_Developer.setPosition(Utilities.instance().getWidth() / 2, Utilities.instance().getHeight() / 2, Drawable.ANCHOR_HCENTER, Drawable.ANCHOR_VCENTER);
		
		//Load language
		ResourceManager.instance().addString(StringManager.Language.ENGLISH.getID());
		
		//Load main font
		ResourceManager.instance().addFont(BitmapFont.MAIN);
		ResourceManager.instance().addFont(BitmapFont.THICK);
		
		//Load data
		ResourceManager.instance().addRecordLoading();
		
		//Load sound resources
		ResourceManager.instance().addSFX("star.wav");
		ResourceManager.instance().addSFX("powerup.wav");
		ResourceManager.instance().addSFX("cancel.wav");
		ResourceManager.instance().addSFX("cursor.wav");
		
		//Load buttons
		ResourceManager.instance().addImages(Utilities.BUTTON_FOLDER + "back.png", 2, 1);
		ResourceManager.instance().addImages(Utilities.BUTTON_FOLDER + "exit.png", 2, 1);
		ResourceManager.instance().addImages(Utilities.BUTTON_FOLDER + "help.png", 2, 1);
		ResourceManager.instance().addImages(Utilities.BUTTON_FOLDER + "menu.png", 2, 1);
		ResourceManager.instance().addImages(Utilities.BUTTON_FOLDER + "mute.png", 2, 1);
		ResourceManager.instance().addImages(Utilities.BUTTON_FOLDER + "pause.png", 2, 1);
		ResourceManager.instance().addImages(Utilities.BUTTON_FOLDER + "retry.png", 2, 1);
		ResourceManager.instance().addImages(Utilities.BUTTON_FOLDER + "sound.png", 2, 1);
		ResourceManager.instance().addImages(Utilities.BUTTON_FOLDER + "credit.png", 2, 1);
		ResourceManager.instance().addImages(Utilities.BUTTON_FOLDER + "button.png", 1, 2);
				
		//Load title resources
		ResourceManager.instance().addImage(Utilities.INTERFACE_FOLDER + "title.png");
		ResourceManager.instance().addImage(Utilities.INTERFACE_FOLDER + "version.png");
		ResourceManager.instance().addImage(Utilities.INTERFACE_FOLDER + "title-kite1.png");
		ResourceManager.instance().addImage(Utilities.INTERFACE_FOLDER + "title-kite2.png");
		ResourceManager.instance().addImage(Utilities.INTERFACE_FOLDER + "title-kite3.png");
		ResourceManager.instance().addImage(Utilities.INTERFACE_FOLDER + "title-kite4.png");
		ResourceManager.instance().addImage(Utilities.INTERFACE_FOLDER + "title-kite5.png");
		ResourceManager.instance().addImages(Utilities.INTERFACE_FOLDER + "circle.png", 1, 2);
		ResourceManager.instance().addImage(Utilities.BACKGROUND_FOLDER + "title.png");
		
		//Load tutorial
		ResourceManager.instance().addImage(Utilities.TUTORIAL_FOLDER + "1.png");
		ResourceManager.instance().addImage(Utilities.TUTORIAL_FOLDER + "2.png");
		ResourceManager.instance().addImage(Utilities.TUTORIAL_FOLDER + "3.png");
		ResourceManager.instance().addImage(Utilities.TUTORIAL_FOLDER + "4.png");
		
		//Load story
		ResourceManager.instance().addImage(Utilities.STORY_FOLDER + "1.png");
		ResourceManager.instance().addImage(Utilities.STORY_FOLDER + "2.png");
		ResourceManager.instance().addImage(Utilities.STORY_FOLDER + "3.png");
		
		//Load menu resources
		ResourceManager.instance().addImage(Utilities.INTERFACE_FOLDER + "menu-top.png");
		ResourceManager.instance().addImage(Utilities.INTERFACE_FOLDER + "menu-list.png");
		ResourceManager.instance().addImage(Utilities.INTERFACE_FOLDER + "menu-cover.png");
		ResourceManager.instance().addImage(Utilities.INTERFACE_FOLDER + "menu-points.png");
		ResourceManager.instance().addImage(Utilities.INTERFACE_FOLDER + "menu-shadow.png");
		ResourceManager.instance().addImage(Utilities.INTERFACE_FOLDER + "menu-shadow2.png");
		ResourceManager.instance().addImage(Utilities.INTERFACE_FOLDER + "menu-exp_bar.png");
		ResourceManager.instance().addImage(Utilities.INTERFACE_FOLDER + "panel-result.png");
		ResourceManager.instance().addImage(Utilities.INTERFACE_FOLDER + "menu-exp_background.png");
		ResourceManager.instance().addImages(Utilities.INTERFACE_FOLDER + "button-status.png", 2, 1);

		//Load achievements
		ResourceManager.instance().addImage(Utilities.ACHIEVEMENT_FOLDER + "medal.png");

		//Load upgrades
		ResourceManager.instance().addJSON(Utilities.DATA_FOLDER + "upgrades.json");
		ResourceManager.instance().addImages(Utilities.UPGRADE_FOLDER + "energy.png", 2, 1);
		ResourceManager.instance().addImages(Utilities.UPGRADE_FOLDER + "agility.png", 2, 1);
		ResourceManager.instance().addImages(Utilities.UPGRADE_FOLDER + "control.png", 2, 1);
		ResourceManager.instance().addImages(Utilities.UPGRADE_FOLDER + "duration.png", 2, 1);
		ResourceManager.instance().addImages(Utilities.UPGRADE_FOLDER + "endurance.png", 2, 1);
		
		//Load more fonts
		ResourceManager.instance().addFont(BitmapFont.SMALL);		
		ResourceManager.instance().addFont(BitmapFont.SMALL_BOLD);		
		ResourceManager.instance().addFont(BitmapFont.HUGE);
		ResourceManager.instance().addFont(BitmapFont.TINY);
		
		//Load kites
		ResourceManager.instance().addImages(Utilities.KITE_FOLDER + "kite1.png", 4, 1);
		ResourceManager.instance().addImages(Utilities.KITE_FOLDER + "kite2.png", 4, 1);
		ResourceManager.instance().addImages(Utilities.KITE_FOLDER + "kite3.png", 4, 1);
		
		//Load game resources
		ResourceManager.instance().addImage(Utilities.PICKUP_FOLDER + "jump.png");
		ResourceManager.instance().addImage(Utilities.PICKUP_FOLDER + "boost.png");
		ResourceManager.instance().addImage(Utilities.PICKUP_FOLDER + "energy.png");
		ResourceManager.instance().addImage(Utilities.BACKGROUND_FOLDER + "sky1.png");
		ResourceManager.instance().addImage(Utilities.BACKGROUND_FOLDER + "sky2.png");
		ResourceManager.instance().addImage(Utilities.BACKGROUND_FOLDER + "sky3.png");
		ResourceManager.instance().addImage(Utilities.BACKGROUND_FOLDER + "sky4.png");
		ResourceManager.instance().addImage(Utilities.BACKGROUND_FOLDER + "sky5.png");
		ResourceManager.instance().addImage(Utilities.BACKGROUND_FOLDER + "sky6.png");
		ResourceManager.instance().addImage(Utilities.BACKGROUND_FOLDER + "monas.png");
		ResourceManager.instance().addImage(Utilities.BACKGROUND_FOLDER + "clouds.png");
		ResourceManager.instance().addImage(Utilities.BACKGROUND_FOLDER + "buildings.png");
		ResourceManager.instance().addImage(Utilities.INTERFACE_FOLDER + "game-kite.png");
		ResourceManager.instance().addImage(Utilities.INTERFACE_FOLDER + "game-line.png");
		ResourceManager.instance().addImage(Utilities.INTERFACE_FOLDER + "game-frame.png");
		ResourceManager.instance().addImage(Utilities.INTERFACE_FOLDER + "game-height.png");
		ResourceManager.instance().addImage(Utilities.INTERFACE_FOLDER + "game-record.png");
		ResourceManager.instance().addImage(Utilities.INTERFACE_FOLDER + "game-levelup.png");
		ResourceManager.instance().addImage(Utilities.INTERFACE_FOLDER + "game-bar-red.png");
		ResourceManager.instance().addImage(Utilities.INTERFACE_FOLDER + "game-bar-green.png");
		ResourceManager.instance().addImage(Utilities.INTERFACE_FOLDER + "game-bar-yellow.png");
		ResourceManager.instance().addImages(Utilities.INTERFACE_FOLDER + "game-arrow.png", 6, 1);
		ResourceManager.instance().addImages(Utilities.INTERFACE_FOLDER + "button-pause.png", 2, 1);
		ResourceManager.instance().addImages(Utilities.PICKUP_FOLDER + "energy-firework.png", 4, 1);
		ResourceManager.instance().addImages(Utilities.PICKUP_FOLDER + "boost-firework.png", 4, 1);
		ResourceManager.instance().addImages(Utilities.PICKUP_FOLDER + "jump-firework.png", 4, 1);
		
		//Load shadow
		ResourceManager.instance().addShadow();
		ResourceManager.instance().addDarkBackground();
		
		//Load numbers
		//ResourceManager.instance().addNumber(BitmapFont.TINY);
		ResourceManager.instance().addNumber(BitmapFont.MAIN);
		ResourceManager.instance().addNumber(BitmapFont.SMALL);
		//ResourceManager.instance().addNumber(BitmapFont.HUGE);
		ResourceManager.instance().addNumber(BitmapFont.THICK);
		ResourceManager.instance().addNumber(BitmapFont.SMALL_BOLD);		
		
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
		
		//if there's no loading text
		if (m_Present == null) {		
			//Get string
			String Present = StringManager.instance().getString("present");
			if (Present != null && BitmapFont.getFont(BitmapFont.MAIN) != null) {
				//Create label
				m_Present = Label.create(Present, BitmapFont.MAIN);
				m_Present.setPosition(Utilities.instance().getWidth() / 2, (Utilities.instance().getHeight() + m_Developer.getOriginalHeight()) / 2, Drawable.ANCHOR_HCENTER);
			}
		}
		
		//Increase timer
		m_Timer += time;		
		
		//Is loader finished?
		if (ResourceManager.instance().isFinished()) {
			//If time limit passed
			if (m_Timer >= MIN_TIMER) {
				//go to title
				UpgradeManager.instance();
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

				//Draw developer
				m_Developer.draw(g);
				
				//Drawn once
				m_DrawnOnce = true;
			}
			
			//Draw texts
			if (m_Loading != null) m_Loading.draw(g);
			if (m_Present != null) m_Present.draw(g);
			if (m_Loading != null && m_Present != null) m_Drawn = true;
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
	
	//Components
	protected Label m_Loading;
	protected Label m_Present;
	protected Image m_Developer;
}
