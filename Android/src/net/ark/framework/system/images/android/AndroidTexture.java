package net.ark.framework.system.images.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import net.ark.framework.system.Device;
import net.ark.framework.system.android.AndroidDevice;
import net.ark.framework.system.images.Texture;

public class AndroidTexture extends Texture {
	protected AndroidTexture() {
		//Super
		super();
	}
	
	public AndroidTexture(String name, boolean antialias, boolean external) {
		//Initialize
		this();
		
		//Save
		m_Name 		= name;
		m_External	= external;
		m_AntiAlias	= antialias;
		
		//Load
		load();
	}
	
	@Override
	public void load() {		
		try {
			//Load image
			Bitmap Loaded = null;
			if (!m_External) 	Loaded = BitmapFactory.decodeStream(((AndroidDevice)Device.instance()).getAssets().open(m_Name));
			else {
				//Load
				FileInputStream Stream	= new FileInputStream(new File(m_Name));
				Loaded 					= BitmapFactory.decodeStream(Stream);
			}
			
			//Get data
			m_Width 	= Loaded.getWidth();
			m_Height 	= Loaded.getHeight();
			
			//Generate texture
			GL10 OpenGL = ((AndroidDevice)Device.instance()).getGL();
			OpenGL.glGenTextures(1, m_IDs, 0);

			//Bind texture
			OpenGL.glBindTexture(GL10.GL_TEXTURE_2D, m_IDs[0]);
			OpenGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
			OpenGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, m_AntiAlias ? GL10.GL_LINEAR : GL10.GL_NEAREST);
			OpenGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
			OpenGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
			
			//Create texture
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, Loaded, 0);
			
			//Release resources
			OpenGL.glBindTexture(GL10.GL_TEXTURE_2D, 0);
			Loaded.recycle();
		} catch (IOException e) {}
	}

	@Override
	public void destroy() {
		//If ID exist
		if (getID() >= 0) {
			//Unload from memory
			AndroidDevice.instance().getGL().glDeleteTextures(1, m_IDs, 0);
			m_IDs = new int[] { -1 };
		}
	}
}
