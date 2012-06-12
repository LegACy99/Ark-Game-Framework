package net.ark.example.states;

import net.ark.framework.components.Drawable;
import javax.microedition.lcdui.Graphics;
import net.ark.example.system.ExampleUtilities;

import net.ark.framework.system.StateManager;
import net.ark.framework.system.StringManager;
import net.ark.framework.system.input.TouchInfo;
import net.ark.framework.system.Utilities;
import net.ark.framework.system.images.Image;
import net.ark.framework.system.images.Label;
import net.ark.framework.system.input.AccelerometerInfo;
import net.ark.framework.system.images.j2me.BitmapFont;
import net.ark.framework.system.resource.ResourceManager;
import net.ark.framework.system.resource.j2me.J2MELoadableDark;

public class StateSplash extends ExampleState {
	public StateSplash() {
		//Super
		super(ExampleState.SPLASH);
				
		//Initialize
		m_Timer 	= 0;
		m_Loading	= null;
		m_DrawnOnce	= false;
		m_Drawn		= false;
		
		//Load splash resource
		ResourceManager.instance().addImage(ExampleUtilities.BACKGROUND_FOLDER + "splash.png");
		ResourceManager.instance().start();
		while (!ResourceManager.instance().isFinished()) ResourceManager.instance().update();

		//Create background
		m_Background = Image.create(ExampleUtilities.BACKGROUND_FOLDER + "splash.png");
		
		//Load language
		ResourceManager.instance().addString(StringManager.Language.ENGLISH.getID());
		
		//Load main font
		ResourceManager.instance().addFont(ExampleUtilities.THICK_FONT);
		ResourceManager.instance().addFont(ExampleUtilities.MAIN_FONT);
		
		//Load sound resources
		ResourceManager.instance().addSFX("cancel.wav");
		ResourceManager.instance().addSFX("cursor.wav");
		
		//Load title resources
		ResourceManager.instance().addImage(ExampleUtilities.BACKGROUND_FOLDER + "title.png");
		ResourceManager.instance().addImages(ExampleUtilities.BUTTON_FOLDER + "exit.png", 2, 1);
		
		//Load more fonts
		ResourceManager.instance().addFont(ExampleUtilities.SMALL_FONT);		
		ResourceManager.instance().addFont(ExampleUtilities.SMALL_BOLD_FONT);		
		ResourceManager.instance().addFont(ExampleUtilities.HUGE_FONT);
		ResourceManager.instance().addFont(ExampleUtilities.TINY_FONT);
		
		//Load shadow
		ResourceManager.instance().addShadow();
		ResourceManager.instance().addDarkBackground();	
		J2MELoadableDark.setBackground(ExampleUtilities.BACKGROUND_FOLDER + "splash.png");
		
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
			if (Loading != null && BitmapFont.getFont(ExampleUtilities.THICK_FONT) != null) {
				//Create label
				m_Loading = Label.create(Loading.toUpperCase(), ExampleUtilities.THICK_FONT);
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
				StateManager.instance().goTo(ExampleState.TITLE, null, true);
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
