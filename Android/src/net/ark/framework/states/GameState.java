package net.ark.framework.states;

import javax.microedition.khronos.opengles.GL10;

import net.ark.framework.components.Drawable;
import net.ark.framework.system.android.input.AccelerometerInfo;
import net.ark.framework.system.android.input.TouchInfo;
import net.ark.framework.system.images.Image;
import net.ark.framework.system.resource.ResourceManager;

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
		if (background != null) m_Background = Image.create(ResourceManager.instance().getJSON(background));
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

	public void onEnter()	{}
	public void onRemove()	{}
	public void onExit()	{}
	public void onResume()	{}
	public void update(long time, int[] keys, TouchInfo[] touches, AccelerometerInfo accel) {}
	
	public void draw(GL10 gl) {
		//Draw background if exist
		if (m_Background != null) m_Background.draw(gl);
	}

	//Members
	protected int		m_ID;
	protected boolean	m_Active;
	protected boolean	m_Running;
	protected boolean	m_UpdatePrevious;
	protected boolean	m_DrawPrevious;
	protected Drawable	m_Background;
}
