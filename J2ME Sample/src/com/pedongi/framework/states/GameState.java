package com.pedongi.framework.states;

import javax.microedition.lcdui.Graphics;

import com.pedongi.framework.system.input.TouchInfo;
import com.pedongi.framework.system.Utilities;
import com.pedongi.framework.system.images.Image;
import com.pedongi.framework.system.input.AccelerometerInfo;

public class GameState {
	//State ID
	public static final int SPLASH		= 0;
	public static final int TITLE		= 1;
	public static final int HELP		= 2;
	public static final int CREDIT		= 3;
	public static final int UPGRADE		= 4;
	public static final int ACHIEVEMENT	= 5;
	public static final int GAME		= 6;
	public static final int RESULT		= 7;
	public static final int PAUSE		= 8;
	public static final int STORY		= 9;
	
	public GameState(int id) {
		//No background
		this(id, (String)null);
	}

	public GameState(int id, String background) {
		//Initialize variable
		m_ID			= id;
		m_Active		= false;
		m_Running		= false;
		m_Background	= null;
        m_Colors        = null;

		//Load background if exist
		if (background != null) m_Background = Image.create(background);
	}

	public void initialize() {
		//Set active
		m_Active = true;
	}

	//Accessors
	public int getID()					{	return m_ID;		}
	public boolean isActive()			{	return m_Active;	}
	public boolean runsInBackground()	{	return m_Running;	}

	public void onEnter() {}
	public void onRemove() {}
	public void onExit() {}
	public void keyPressed(int key) {}
	public void update(long time, int[] keys, TouchInfo[] touches, AccelerometerInfo accel) {}
	
	public void draw(Graphics g) {
		//Defaulting to black
		g.setColor(0x000000);

		//Draw background if exist
		if (m_Background != null)	m_Background.draw(g);
		else						g.fillRect(0, 0, (int)Utilities.instance().getWidth(), (int)Utilities.instance().getHeight());
	}

	//Members
	protected int		m_ID;
	protected boolean	m_Active;
	protected boolean	m_Running;
	protected Image		m_Background;
    protected int[]     m_Colors;
}
