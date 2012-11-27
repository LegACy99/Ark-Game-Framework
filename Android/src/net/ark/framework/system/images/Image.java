package net.ark.framework.system.images;

import net.ark.framework.components.Croppable;
import net.ark.framework.system.Utilities;
import net.ark.framework.system.images.android.AndroidImage;
import net.ark.framework.system.resource.ResourceManager;

import org.json.JSONObject;

public abstract class Image extends Croppable {
	protected Image() {
		//Super
		super();
		
		//Initialize variables
		m_Flip			= 0;
		m_PivotX		= 0;
		m_PivotY		= 0;
		m_Rotation		= 0;
		m_ColorRed		= 1f;
		m_ColorBlue		= 1f;
		m_ColorGreen	= 1f;
		m_ColorAlpha	= 1f;
		m_Mirror		= MIRROR_NONE;
	}
	
	protected Image(String file, int x, int y) {
		this(ResourceManager.instance().getJSON(file), x, y);
	}
	
	protected Image(JSONObject json, int x, int y) {
		//Initialize
		this();
		
		//Save position
		setPosition(x, y);
	}

	//Create
	public static Image create(JSONObject json)					{	return create(json, 0, 0);					}
	public static Image create(String resource)					{	return create(resource, 0, 0);				}
	public static Image create(JSONObject json, int x, int y)	{	return new AndroidImage(json, x, y);		}
	public static Image create(String resource, int x, int y)	{	return new AndroidImage(resource, x, y);	}
	
	protected abstract void setRegion(float x, float y, float width, float height, boolean force);
	
	public void setRotation(float angle, float x, float y) {
		//Initialize
		int Add 	= 0;
		float Angle	= angle;
		
		//Set angle
		if (Angle == 0) {
			Add 	= -1;
			Angle 	= 1;
		}
		setRotation(Angle, Add, x, y);
	}
	
	public void setRotation(float angle) { setRotation(angle, getOriginalWidth() / 2f, getOriginalHeight() / 2f); }
	public void setRotation(float angle, float addition) { setRotation(angle, addition, getOriginalWidth() / 2f, getOriginalHeight() / 2f); }
	public void setRotation(float angle, float addition, float x, float y) {
		//Set rotation
		if (angle != 0) m_Rotation = angle;
		m_Rotation += addition;
		
		//Set pivot
		m_PivotX = x * Utilities.instance().getScale();
		m_PivotY = y * Utilities.instance().getScale();
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
		
		//Reset rect
		setRegion(0, 0, m_OriginalWidth, m_OriginalHeight, true);
	}

	public void setTint(int red, int green, int blue) { setTint(red, green, blue, 255); }
	public void setTint(float red, float green, float blue) { setTint(red, green, blue, 1f); }
	public void setTint(int red, int green, int blue, int alpha) { setTint((float)red / 255f, (float)green / 255f, (float)blue / 255f); }
	public void setTint(float red, float green, float blue, float alpha) {
		//Save
		m_ColorRed		= red;
		m_ColorBlue		= blue;
		m_ColorGreen	= green;
		m_ColorAlpha	= alpha;
	}
	
	//Constants
	public final static String KEY_RECT			= "Rect";
	public final static String KEY_RECT_TOP		= "Top";
	public final static String KEY_RECT_LEFT	= "Left";
	public final static String KEY_RECT_RIGHT	= "Right";
	public final static String KEY_RECT_BOTTOM	= "Bottom";
	public final static String KEY_RECT_HEIGHT	= "Height";
	public final static String KEY_RECT_WIDTH	= "Width";
	public final static String KEY_TEXTURE		= "Texture";
	
	//Data
	protected float m_Flip;
	protected float m_Rotation;
	protected float m_ColorRed;
	protected float m_ColorBlue;
	protected float m_ColorGreen;
	protected float m_ColorAlpha;
	protected float m_PivotX;
	protected float m_PivotY;
	protected int	m_Mirror;
}
