package net.ark.framework.components;

import javax.microedition.khronos.opengles.GL10;

import net.ark.framework.system.Utilities;
import net.ark.framework.system.images.Image;
import net.ark.framework.system.resource.ResourceManager;

import org.json.JSONObject;

public class ScrollingImage extends Drawable {
	protected ScrollingImage() {
		//Super
		super();
		
		//Initialize
		m_Time		= 0;
		m_ScrollX	= 0;
		m_ScrollY	= 0;
		m_OffsetX	= 0;
		m_OffsetY	= 0;
		m_Images 	= new Image[4];
	}

	public ScrollingImage(String resource) {
		this(resource, 0, 0);
	}
	
	public ScrollingImage(JSONObject json) {
		this(json, 0, 0);
	}
	
	public ScrollingImage(String resource, float scrollX, float scrollY) {
		this(resource, 0, 0, scrollX, scrollY);
	}
	
	public ScrollingImage(JSONObject json, float scrollX, float scrollY) {
		this(json, 0, 0, scrollX, scrollY);
	}
	
	public ScrollingImage(String resource, float x, float y, float scrollX, float scrollY) {
		this(ResourceManager.instance().getJSON(resource), x, y, scrollX, scrollY);
	}
	
	public ScrollingImage(JSONObject json, float x, float y, float scrollX, float scrollY) {
		//Initialize
		this();
		
		//Create image
		for (int i = 0; i < m_Images.length; i++) m_Images[i] = Image.create(json);
		
		//Save data
		m_Width				= m_Images[MAIN_IMAGE].getWidth();
		m_Height			= m_Images[MAIN_IMAGE].getHeight();
		m_OriginalWidth	 	= m_Images[MAIN_IMAGE].getOriginalWidth();
		m_OriginalHeight	= m_Images[MAIN_IMAGE].getOriginalHeight();
		m_ScrollX 			= scrollX;
		m_ScrollY 			= scrollY;
		
		//Set position
		setPosition(x, y);
	}
	
	@Override
	public void setPosition(float x, float y, int horizontal, int vertical) {
		//Set position then scrol
		super.setPosition(x, y, horizontal, vertical);
		scroll(0, 0);
	}

	public void setTint(int red, int green, int blue) { setTint(red, green, blue, 255); }
	public void setTint(float red, float green, float blue) { setTint(red, green, blue, 1f); }
	public void setTint(int red, int green, int blue, int alpha) { setTint((float)red / 255f, (float)green / 255f, (float)blue / 255f); }
	public void setTint(float red, float green, float blue, float alpha) {
		//Set all images tint
		for (int i = 0; i < m_Images.length; i++) m_Images[i].setTint(red, green, blue, alpha);
	}
	
	public void scroll(float x, float y) {
		////Scroll
		m_OffsetX += x;
		m_OffsetY += y;
		m_OffsetX = (Math.abs(m_OffsetX) % m_OriginalWidth) * Math.signum(m_OffsetX);
		m_OffsetY = (Math.abs(m_OffsetY) % m_OriginalHeight) * Math.signum(m_OffsetY);
		
		//Get original position
		float OriginalX 	= m_X / Utilities.instance().getScale();
		float OriginalY 	= m_Y / Utilities.instance().getScale();
		
		//Set images position
		m_Images[MAIN_IMAGE].setPosition(OriginalX + m_OffsetX, OriginalY + m_OffsetY);
		m_Images[VERTICAL_IMAGE].setPosition(OriginalX + m_OffsetX, OriginalY + m_OffsetY - ((m_OriginalHeight - 1) * Math.signum(m_OffsetY)));
		m_Images[HORIZONTAL_IMAGE].setPosition(OriginalX + m_OffsetX - ((m_OriginalWidth - 1) * Math.signum(m_OffsetX)), OriginalY + m_OffsetY);
		m_Images[DIAGONAL_IMAGE].setPosition(OriginalX + m_OffsetX - ((m_OriginalWidth - 1) * Math.signum(m_OffsetX)), OriginalY + m_OffsetY - ((m_OriginalHeight - 1) * Math.signum(m_OffsetY)));
	}
	
	public void update(long time) {
		//Increase time
		m_Time += time;
		
		//Scroll
		m_OffsetX = 0;
		m_OffsetY = 0;
		scroll((float)m_Time / 1000f * m_ScrollX, (float)m_Time / 1000f * m_ScrollY);
	}
	
	@Override
	public void draw(GL10 gl) {
		//Draw only the relevant image
		m_Images[MAIN_IMAGE].draw(gl);
		if (m_OffsetX != 0) 					m_Images[HORIZONTAL_IMAGE].draw(gl);
		if (m_OffsetY != 0) 					m_Images[VERTICAL_IMAGE].draw(gl);
		if (m_OffsetX != 0 && m_OffsetX != 0) 	m_Images[DIAGONAL_IMAGE].draw(gl);
	}
	
	//Constants
	protected static final int MAIN_IMAGE 		= 0;
	protected static final int VERTICAL_IMAGE 	= 1;
	protected static final int HORIZONTAL_IMAGE = 2;
	protected static final int DIAGONAL_IMAGE 	= 3;
	
	//Data
	protected long 		m_Time;
	protected float 	m_ScrollX;
	protected float 	m_ScrollY;
	protected float 	m_OffsetX;
	protected float 	m_OffsetY;
	protected Image[] 	m_Images;
}
