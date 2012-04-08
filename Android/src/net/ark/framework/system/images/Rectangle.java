package net.ark.framework.system.images;

import net.ark.framework.components.Drawable;
import net.ark.framework.system.Utilities;
import net.ark.framework.system.images.android.AndroidRectangle;

public abstract class Rectangle extends Drawable {
	protected Rectangle() {
		//Super
		super();
		
		//Initialize
		m_Flip		= 0;
		m_Rotation	= 0;
	}

	public static Rectangle create(float width, float height)														{ return Rectangle.create(0, 0, width, height);					}
	public static Rectangle create(float x, float y, float width, float height)										{ return Rectangle.create(x, y, width, height, 1f, 1f, 1f, 1f);	}
	public static Rectangle create(float x, float y, float width, float height, float r, float g, float b, float a)	{ return new AndroidRectangle(x, y, width, height, r, g, b, a);	}
	
	public void setRect(float width, float height) {
		//Set size
		m_OriginalWidth		= width;
		m_OriginalHeight	= height;
		m_Height			= m_OriginalHeight * Utilities.instance().getScale();
		m_Width				= m_OriginalWidth * Utilities.instance().getScale();
	}
	
	//Data
	protected float m_Flip;
	protected float m_Rotation;
}
