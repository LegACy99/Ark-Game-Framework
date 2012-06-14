package net.ark.framework.components;

import net.ark.framework.system.Utilities;

public abstract class Croppable extends Drawable {
	protected Croppable() {
		//Super
		super();
		
		//Initialize
		m_RegionX				= 0;
		m_RegionY				= 0;
		m_RegionWidth			= 0;
		m_RegionHeight			= 0;
		m_OriginalRegionX		= 0;
		m_OriginalRegionY		= 0;
		m_OriginalRegionWidth	= 0;
		m_OriginalRegionHeight	= 0;
	}
	
	//Accessors
	public float getOriginalRegionX()		{ return m_OriginalRegionX;			}
	public float getOriginalRegionY()		{ return m_OriginalRegionY;			}
	public float getOriginalRegionWidth()	{ return m_OriginalRegionWidth;		}
	public float getOriginalRegionHeight()	{ return m_OriginalRegionHeight;	}
	
	//Setters
	public void setRegionSides(float left, float top, float right, float bottom) { setRegion(left, top, right - left, bottom - top); }
	public void setRegion(float width, float height) { setRegion(0, 0, width, height); }
	public void setRegion(float x, float y, float width, float height) {
		//Set data
		m_OriginalRegionX		= x;
		m_OriginalRegionY		= y;
		m_OriginalRegionWidth	= width;
		m_OriginalRegionHeight	= height;
		
		//Validate
		if (m_OriginalRegionX < 0) 											m_OriginalRegionX = 0;
		if (m_OriginalRegionY < 0) 											m_OriginalRegionY = 0;
		if (m_OriginalRegionWidth < 0) 										m_OriginalRegionWidth = 0;
		if (m_OriginalRegionHeight < 0) 									m_OriginalRegionHeight = 0;
		if (m_OriginalRegionX > m_OriginalWidth)							m_OriginalRegionX = m_OriginalWidth;
		if (m_OriginalRegionY > m_OriginalHeight)							m_OriginalRegionY = m_OriginalHeight;
		if (m_OriginalRegionWidth - m_OriginalRegionX > m_OriginalWidth) 	m_OriginalRegionWidth = m_OriginalWidth - m_OriginalRegionX;
		if (m_OriginalRegionHeight - m_OriginalRegionY > m_OriginalHeight)	m_OriginalRegionHeight = m_OriginalHeight - m_OriginalRegionY;
		
		//Scale
		m_RegionX		= m_OriginalRegionX * Utilities.instance().getScale();
		m_RegionY		= m_OriginalRegionY * Utilities.instance().getScale();
		m_RegionWidth	= m_OriginalRegionWidth * Utilities.instance().getScale();
		m_RegionHeight	= m_OriginalRegionHeight * Utilities.instance().getScale();
	}
	
	//Data
	//Note: RegionX and Y is relative to position
	protected float		m_RegionX;
	protected float		m_RegionY;
	protected float		m_RegionWidth;
	protected float		m_RegionHeight;
	protected float		m_OriginalRegionX;
	protected float		m_OriginalRegionY;
	protected float		m_OriginalRegionWidth;
	protected float		m_OriginalRegionHeight;
}
