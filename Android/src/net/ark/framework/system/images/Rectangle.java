package net.ark.framework.system.images;

import net.ark.framework.components.Croppable;
import net.ark.framework.system.Utilities;
import net.ark.framework.system.images.android.AndroidRectangle;

public abstract class Rectangle extends Croppable {
	protected Rectangle() {
		//Super
		super();
		
		//Initialize
		m_Flip		= 0;
		m_Rotation	= 0;
		m_Color	= new float[] { 0, 0, 0, 0 };
	}

	public static Rectangle create(float width, float height)														{ return Rectangle.create(0, 0, width, height);					}
	public static Rectangle create(float x, float y, float width, float height)										{ return Rectangle.create(x, y, width, height, 1f, 1f, 1f, 1f);	}
	public static Rectangle create(float x, float y, float width, float height, float r, float g, float b, float a)	{ return new AndroidRectangle(x, y, width, height, r, g, b, a);	}
	
	//Accessors
	public float getRedComponent() 		{ return m_Color[INDEX_RED];	}
	public float getBlueComponent() 	{ return m_Color[INDEX_BLUE];	}
	public float getGreenComponent() 	{ return m_Color[INDEX_GREEN];	}
	public float getAlphaComponent() 	{ return m_Color[INDEX_ALPHA];	}
	
	public void setRect(float width, float height) {
		//Set size
		m_OriginalWidth		= width;
		m_OriginalHeight	= height;
		m_Height			= m_OriginalHeight * Utilities.instance().getScale();
		m_Width				= m_OriginalWidth * Utilities.instance().getScale();
	}
	
	public void setColor(float r, float g, float b, float a) {
		//Set color
		m_Color = new float[] { r, g, b, a };
	}
	
	//Constants
	protected static final int INDEX_RED 	= 0;
	protected static final int INDEX_BLUE 	= 2;
	protected static final int INDEX_GREEN 	= 1;
	protected static final int INDEX_ALPHA 	= 3;
	
	//Data
	protected float 	m_Flip;
	protected float 	m_Rotation;
	protected float[]	m_Color;
}
