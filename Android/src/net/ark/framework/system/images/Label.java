package net.ark.framework.system.images;

import javax.microedition.khronos.opengles.GL10;

import net.ark.framework.components.Drawable;
import net.ark.framework.system.Utilities;
import net.ark.framework.system.images.android.AndroidLabel;

public abstract class Label extends Drawable {	
	protected Label(String text, float x, float y) {
		//Init
		super();
		
		//Initialize
		m_Text = text;
		if (m_Text == null) m_Text = "";
		setRegion(0, 0, m_OriginalWidth, m_OriginalHeight);
		setPosition(x, y);
	}

	//Creation functions
	public static Label create(String text) 													{ return Label.create(text, Utilities.instance().getSystemFont(), 0, 0); 										}
	public static Label createInteger(int number) 												{ return Label.create("" + number);																				}
	public static Label create(String text, String font) 										{ return Label.create(text, font, 0, 0); 																		}
	public static Label createInteger(int number, String font) 									{ return Label.create("" + number, font);																		}
	public static Label create(String text, String font, float x, float y) 						{ return new AndroidLabel(text, font, x, y);																	}
	public static Label createInteger(int number, String font, float x, float y)				{ return Label.create("" + number, font, x, y);																	}
	public static Label createFloat(float number, int decimal, String font) 					{ return Label.create(Utilities.instance().writeFloat(number, decimal), font);									}
	public static Label createFloat(float number, int decimal, String font, float x, float y)	{ return Label.create(Utilities.instance().writeFloat(number, decimal), font, x, y);							}
	public static Label createFloat(float number, int decimal) 									{ return Label.create(Utilities.instance().writeFloat(number, decimal), Utilities.instance().getSystemFont());	}
	
	//Accessors
	public String getText()					{ return m_Text;					}
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
	
	@Override
	public abstract void draw(GL10 gl);
	
	//Note: RegionX and Y is relative to position
	
	//Data
	protected String	m_Text;
	protected float		m_RegionX;
	protected float		m_RegionY;
	protected float		m_RegionWidth;
	protected float		m_RegionHeight;
	protected float		m_OriginalRegionX;
	protected float		m_OriginalRegionY;
	protected float		m_OriginalRegionWidth;
	protected float		m_OriginalRegionHeight;
}
