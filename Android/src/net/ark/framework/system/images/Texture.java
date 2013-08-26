package net.ark.framework.system.images;

import net.ark.framework.system.images.android.AndroidTexture;


public abstract class Texture {
	protected Texture() {
		//Initialize
		m_IDs 		= new int[] { -1 };
		m_Name		= null;
		m_AntiAlias	= true;
		m_External	= false;
		m_Height	= 0f;
		m_Width		= 0f;
	}
	
	//Creator
	public static Texture create(String name, boolean antialias, boolean external) 	{ return new AndroidTexture(name, antialias, external);	}
	
	//Abstract functions
	public abstract void load();
	public abstract void destroy();
	
	//Accessors
	public int getID()			{ return m_IDs[0];	}
	public float getHeight()	{ return m_Height;	}
	public float getWidth()		{ return m_Width;	}

	//Data
	protected int[]	m_IDs;
	protected float 	m_Width;
	protected float 	m_Height;
	protected boolean	m_AntiAlias;
	protected boolean	m_External;
	protected String	m_Name;
}
