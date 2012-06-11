package net.ark.framework.system.images;

import net.ark.framework.system.Utilities;
import net.ark.framework.system.images.j2me.J2MEImage;
import net.ark.framework.components.Drawable;

public abstract class Image extends Drawable {
	protected Image() {
		//Super
		super();
		
		//Initialize variables
		m_Flip			= 0;
		m_Rotation		= 0;
		m_RegionTop		= 0;
		m_RegionLeft	= 0;
		m_RegionWidth	= 0;
		m_RegionHeight	= 0;
		m_Mirror		= MIRROR_NONE;
	}
	
	protected Image(String resource, float x, float y) {
		//Initialize
		this();
		
		//Save position
		setPosition(x, y);
	}

	//Create
	public static Image create(String resource)						{	return create(resource, 0, 0);			}
	public static Image create(String resource, float x, float y)	{	return new J2MEImage(resource, x, y);	}
	
	public void setRotation(float angle) {
		//Initialize
		int Add 	= 0;
		float Angle	= angle;
		
		//Set angle
		if (Angle == 0) {
			Add 	= -1;
			Angle 	= 1;
		}
		setRotation(Angle, Add);
	}
	
	public void setRotation(float angle, float addition) {
		//Set rotation
		if (angle != 0) m_Rotation = angle;
		m_Rotation += addition;
	}
	
	public void setFlip(float angle) {
		//Initialize
		int Add 	= 0;
		float Angle	= angle;
		
		//Set angle
		if (Angle == 0) {
			Add 	= -1;
			Angle 	= 1;
		}
		setFlip(Angle, Add);
	}
	
	public void setFlip(float angle, float addition) {
		//Set flip
		if (angle != 0) m_Flip = angle;
		m_Flip += addition;
	}
	
	public void setMirror(boolean horizontal, boolean vertical) {
		//Set mirror
		if (!horizontal && !vertical) 		m_Mirror = MIRROR_NONE;
		else if (horizontal && !vertical) 	m_Mirror = MIRROR_HORIZONTAL;
		else if (!horizontal && vertical) 	m_Mirror = MIRROR_VERTICAL;
		else 								m_Mirror = MIRROR_BOTH;
	}

	public void setRegion(float width, float height) {
		setRegion(0, 0, width, height);
	}

	public void setRegion(float left, float top, float right, float bottom) {
		//Set region
		m_RegionTop		= top * Utilities.instance().getScale();
		m_RegionLeft	= left * Utilities.instance().getScale();
		m_RegionWidth	= (right - left) * Utilities.instance().getScale();
		m_RegionHeight	= (bottom - top) * Utilities.instance().getScale();
	}
	
	//Data
	protected float m_Flip;
	protected float m_Rotation;
	protected int	m_Mirror;

	//Region
	protected float m_RegionTop;
	protected float m_RegionLeft;
	protected float m_RegionWidth;
	protected float m_RegionHeight;
}
