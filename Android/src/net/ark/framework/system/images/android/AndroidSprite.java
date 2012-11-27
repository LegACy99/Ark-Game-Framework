package net.ark.framework.system.images.android;

import javax.microedition.khronos.opengles.GL10;

import net.ark.framework.system.images.Image;
import net.ark.framework.system.images.Sprite;
import net.ark.framework.system.resource.ResourceManager;

import org.json.JSONObject;

public class AndroidSprite extends Sprite {
    protected AndroidSprite(int x, int y, long delay) {
		//Super
		super(x, y, delay);
		
		//Initialize
		m_Images = null;
	}

	public AndroidSprite(String resource, int x, int y, long delay) {
		//Default
		this(x, y, delay);
		
		//Create images
		JSONObject[] Data	= (JSONObject[]) ResourceManager.instance().getTextures(resource);
		m_Images			= new Image[Data.length];
		for (int i = 0; i < m_Images.length; i++) m_Images[i] = Image.create(Data[i], x, y);

		//Set data
		m_Total				= m_Images.length;
		m_Width				= m_Images[0].getWidth();
		m_Height			= m_Images[0].getHeight();
		m_OriginalWidth		= m_Images[0].getOriginalWidth();
		m_OriginalHeight	= m_Images[0].getOriginalHeight();
	}
	
	@Override
	public void setRegion(float x, float y, float width, float height) {
		//Super
		super.setRegion(x, y, width, height);
		
		//Set images region
		for (int i = 0; i < m_Images.length; i++) m_Images[i].setRegion(x, y, width, height);
	}
	
	@Override
	public void setPosition(float x, float y, int horizontal, int vertical) {
		//Super
		super.setPosition(x, y, horizontal, vertical);
		
		//Set all images
		for (int i = 0; i < m_Images.length; i++) m_Images[i].setPosition(x, y, horizontal, vertical);
	}
	
	@Override
	public void setRotation(float angle, float addition, float x, float y) {
		//Set all images
		for (int i = 0; i < m_Images.length; i++) m_Images[i].setRotation(angle, addition, x, y);
	}
	
	@Override
	public void setMirror(boolean horizontal, boolean vertical) {
		//Super
		super.setMirror(horizontal, vertical);
		
		//Mirror all images
		for (int i = 0; i < m_Images.length; i++) m_Images[i].setMirror(horizontal, vertical);
	}

	@Override
	public void draw(GL10 gl) {
		//Draw the correct frame
		m_Images[m_Frame].draw(gl);
	}
	
	//Variable
	protected Image[] m_Images;
}
