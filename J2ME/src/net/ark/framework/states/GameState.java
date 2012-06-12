package net.ark.framework.states;

import javax.microedition.lcdui.Graphics;
import net.ark.framework.components.Drawable;
import net.ark.framework.system.input.AccelerometerInfo;
import net.ark.framework.system.input.TouchInfo;
import net.ark.framework.system.images.Image;

public class GameState {
	public GameState(int id) {
		//No background
		this(id, (String)null);
	}
	
	public GameState(int id, Drawable background) {
		//Default
		this(id, (String)null);
		
		//Set background
		m_Background = background;
	}

	public GameState(int id, String background) {
		//Initialize variable
		m_ID				= id;
		m_Active			= false;
		m_Running			= false;
		m_DrawPrevious		= false;
		m_UpdatePrevious	= false;
		m_Background		= null;

		//Load background if exist
		if (background != null) m_Background = Image.create(background);
	}

	public void initialize() {
		//Set active
		m_Active = true;
	}

	//Accessors
	public int getID()					{	return m_ID;				}
	public boolean isActive()			{	return m_Active;			}
	public boolean runsInBackground()	{	return m_Running;			}
	public boolean drawPrevious()		{	return m_DrawPrevious;		}
	public boolean updatePrevious()		{	return m_UpdatePrevious;	}

	public void onEnter()			{}
	public void onRemove()			{}
	public void onExit()			{}
	public void keyPressed(int key)	{}
	public void update(long time, int[] keys, TouchInfo[] touches, AccelerometerInfo accel) {}
	
	public void draw(Graphics g) {
		//Draw background if exist
		if (m_Background != null) m_Background.draw(g);
	}

	//Members
	protected int		m_ID;
	protected boolean	m_Active;
	protected boolean	m_Running;
	protected boolean	m_UpdatePrevious;
	protected boolean	m_DrawPrevious;
	protected Drawable	m_Background;
}
