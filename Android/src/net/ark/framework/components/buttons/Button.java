package net.ark.framework.components.buttons;

import javax.microedition.khronos.opengles.GL10;

import org.json.JSONObject;

import net.ark.framework.components.Drawable;
import net.ark.framework.system.Device;
import net.ark.framework.system.Utilities;
import net.ark.framework.system.images.Image;
import net.ark.framework.system.images.Label;
import net.ark.framework.system.resource.ResourceManager;

public class Button extends Drawable {
	protected Button() {
		//Super
		super();
		
		//Initialize
		m_ID			= 0;
		m_InputX		= 0;
		m_InputY		= 0;
		m_InputWidth	= 0;
		m_InputHeight	= 0;
		m_Images		= null;
		m_Labels		= null;
		m_Active		= true;
		Visible			= true;
		m_State			= STATE_NORMAL;
	}
	
	public Button(int id, String images) {
		this(id, images, "", 0f, 0f);
	}
	
	public Button(int id, JSONObject[] images) {
		this(id, images, null);
	}
	
	public Button(int id, String images, String text) {
		this(id, images, text, 0f, 0f);
	}
	
	public Button(int id, JSONObject[] images, String text) {
		this(id, images, text, 0f, 0f);
	}

	public Button(int id, String images, float x, float y) {
		this(id, images, null, x, y);
	}

	public Button(int id, JSONObject[] images, float x, float y) {
		this(id, images, null, x, y);
	}
	
	public Button(int id, String images, String text, float x, float y) {
		this(id, (JSONObject[]) ResourceManager.instance().getTextures(images), text, x, y);
	}
	
	public Button(int id, JSONObject[] images, String text, float x, float y) {
		//Default
		this();
		
		//Save
		m_ID = id;
		
		//Create image if exist
		if (images != null) {
			m_Images = new Image[images.length];
			for (int i = 0; i < m_Images.length; i++) m_Images[i] = Image.create(images[i]);
			
			//Get data
			m_Width 			= m_Images[0].getWidth();
			m_Height 			= m_Images[0].getHeight();
			m_OriginalWidth 	= m_Images[0].getOriginalWidth();
			m_OriginalHeight	= m_Images[0].getOriginalHeight();
		}
		
		//If text exist
		if (text != null) {
			//Create label
			m_Labels 				= new Label[m_Images.length];
			m_Labels[STATE_NORMAL] 	= Label.create(text, FONT_NORMAL);
			m_Labels[STATE_PRESSED] = Label.create(text, FONT_PRESSED); 
			if (m_Labels.length > STATE_INACTIVE) m_Labels[STATE_INACTIVE] = Label.create(text, FONT_INACTIVE);
		}
		
		//Set position and size
		setPosition(x, y);
		setSize(m_X, m_Y, m_Width, m_Height, false, false);
	}
	
	//Accessors
	public int getID()			{	return m_ID;		}
	public boolean isActive()	{	return m_Active;	}
	
	public void setPosition(float x, float y, int horizontal, int vertical) {
		//Super
		super.setPosition(x, y, horizontal, vertical);
		
		//Set components position
		if (m_Images != null) for (int i = 0; i < m_Images.length; i++) m_Images[i].setPosition(x, y, horizontal, vertical);
		if (m_Labels != null)
			for (int i = 0; i < m_Labels.length; i++) 
				m_Labels[i].setPosition((m_X + (m_Width / 2f)) / Utilities.instance().getScale(), (m_Y + (m_Height / 2f)) / Utilities.instance().getScale(), Drawable.ANCHOR_HCENTER, Drawable.ANCHOR_VCENTER);
	}
	
	public void setInactiveImage(JSONObject json) {				
		//Create new images
		Image[] Images 			= new Image[STATE_INACTIVE + 1];
		Images[STATE_NORMAL]	= m_Images[STATE_NORMAL];
		Images[STATE_PRESSED]	= m_Images[STATE_PRESSED];
		Images[STATE_INACTIVE]	= Image.create(json);
		m_Images				= Images;
		
		//If text exist
		if (m_Labels != null) {
			//Create label
			Label[] Labels 				= new Label[m_Images.length];
			m_Labels[STATE_INACTIVE] 	= Label.create(m_Labels[0].getText(), FONT_INACTIVE);
			Labels[STATE_PRESSED] 		= m_Labels[STATE_PRESSED];
			Labels[STATE_NORMAL] 		= m_Labels[STATE_NORMAL];
			m_Labels 					= Labels;
		}
	}
	
	public void setSize(float x, float y, float width, float height) {
		//Scale
		setSize(x, y, width, height, true, true);
	}
	
	public void setSize(float x, float y, float width, float height, boolean scaleOffset, boolean scaleSize) {
		//Set offset
		m_InputX = x;
		m_InputY = y;
		if (scaleOffset) m_InputX *= Device.instance().getScale();
		if (scaleOffset) m_InputY *= Device.instance().getScale();
		
		//Set size
		m_InputWidth 	= width;
		m_InputHeight	= height;
		if (scaleSize) m_InputWidth		*= Device.instance().getScale();
		if (scaleSize) m_InputHeight	*= Device.instance().getScale();
	}
	
	public void setActive(boolean active) {
		//Set
		m_Active = active;
	}
	
	public void setState(int state) {		
		//Set state
		m_State  = state;
		if (m_Images != null) m_State %= m_Images.length;
	}
	
	public boolean isInside(float x, float y) {
		//Init variables
		float Top 	= m_Y + m_InputY;
		float Left	= m_X + m_InputX;		
		
		//Check for false
		if (y < Top) 					return false;
		if (x < Left)					return false;
		if (y > Top + m_InputHeight)	return false;
		if (x > Left + m_InputWidth)	return false;
		
		//If passed, inside
		return true;
	}
	
	@Override
	public void draw(GL10 gl) {
		//Skip if not visible
		if (!Visible) return;
		
		//Get state
		int State = m_State;
		if (!isActive()) State = STATE_INACTIVE;
		
		//Draw button
		if (m_Images != null) m_Images[State].draw(gl);
		if (m_Labels != null) m_Labels[State].draw(gl);
	}
	
	//Constants
	public static final int STATE_NORMAL		= 0;
	public static final int STATE_PRESSED		= 1;
	protected static final int STATE_INACTIVE	= 2;
	protected static final String FONT_NORMAL	= Utilities.FONT_FOLDER + "font.json";
	protected static final String FONT_PRESSED	= Utilities.FONT_FOLDER + "font.json";
	protected static final String FONT_INACTIVE	= Utilities.FONT_FOLDER + "font.json";
	
	//Size
	protected float m_InputX;
	protected float m_InputY;
	protected float m_InputWidth;
	protected float m_InputHeight;
	
	//Data
	protected int 		m_ID;
	protected int 		m_State;
	protected Label[]	m_Labels;
	protected Image[] 	m_Images;
	protected boolean	m_Active;
	public boolean		Visible;
}
