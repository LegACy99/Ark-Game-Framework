package net.ark.framework.states;

import javax.microedition.khronos.opengles.GL10;

import net.ark.framework.system.android.input.AccelerometerInfo;
import net.ark.framework.system.android.input.TouchInfo;
import net.ark.framework.system.images.Image;
import net.ark.framework.system.resource.ResourceManager;

public class GameState {
	//State ID
	public static final int SPLASH 	= 1;
	public static final int TITLE 	= 2;
	
	public GameState(int id) {
		//No background
		this(id, (String)null);
	}
	
	public GameState(int id, Image background) {
		//Default
		this(id, (String)null);
		
		//Set background
		m_Background = background;
	}

	public GameState(int id, String background) {
		//Initialize variable
		m_ID			= id;
		m_Active		= false;
		m_Running		= false;
		m_Background	= null;
        m_Colors        = null;

		//Load background if exist
		if (background != null) m_Background = new Image(ResourceManager.instance().getJSON(background));
	}
	

	public void initialize() {
		//Set active
		m_Active = true;
	}

	//Accessors
	public int getID()					{	return m_ID;		}
	public boolean isActive()			{	return m_Active;	}
	public boolean runsInBackground()	{	return m_Running;	}

	public void onEnter()			{}
	public void onRemove()			{}
	public void onExit()			{}
	public void keyPressed(int key)	{}
	public void update(long time, int[] keys, TouchInfo[] touches, AccelerometerInfo accel) {}
	
	public void draw(GL10 gl) {
		//Draw background if exist
		if (m_Background != null) m_Background.draw(gl);
	}

	//Members
	protected int		m_ID;
	protected boolean	m_Active;
	protected boolean	m_Running;
	protected Image		m_Background;
    protected int[]     m_Colors;
}
