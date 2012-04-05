package net.ark.framework.system.images;

import net.ark.framework.components.Drawable;
import net.ark.framework.system.images.android.AndroidImage;
import net.ark.framework.system.resource.ResourceManager;

import org.json.JSONObject;

public abstract class Image extends Drawable {
	protected Image() {
		//Super
		super();
		
		//Initialize variables
		m_Top	 		= 0;
		m_Left			= 0;	
		m_Flip			= 0;
		m_Rotation		= 0;
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
		
		//Reset rect
		setRect(0, 0, m_OriginalWidth, m_OriginalHeight);
	}
	
	public abstract void setRect(float x, float y, float width, float height);
	
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
	protected int	m_Mirror;
	
	//Rect
	protected float		m_Top;
	protected float		m_Left;
	protected float		m_OriginalTop;
	protected float		m_OriginalLeft;
}
