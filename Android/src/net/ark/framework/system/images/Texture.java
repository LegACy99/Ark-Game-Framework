package net.ark.framework.system.images;

import net.ark.framework.system.images.android.AndroidTexture;


public abstract class Texture {
	protected Texture() {
		//Initialize
		m_ID 		= -1;
		m_Name		= null;
		m_AntiAlias	= true;
		m_Height	= 0f;
		m_Width		= 0f;
	}
	
	//Creator
	public static Texture create(String name) 						{ return create(name, true);					}
	public static Texture create(String name, boolean antialias) 	{ return new AndroidTexture(name, antialias);	}
	
	//Abstract function
	public abstract void load();
	
	//Accessors
	public int getID()			{ return m_ID;		}
	public float getWidth()		{ return m_Width;	}
	public float getHeight()	{ return m_Height;	}

	//Data
	protected int		m_ID;
	protected float 	m_Width;
	protected float 	m_Height;
	protected boolean	m_AntiAlias;
	protected String	m_Name;
}
