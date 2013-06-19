package net.ark.framework.system.images.android;

import net.ark.framework.system.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.SparseArray;

public class BitmapChar {
	protected BitmapChar() {
		//Initialize
		m_Advance		= 0;
		m_Vertices		= null;
		m_Coordinates	= null;
		m_Kerning		= new SparseArray<Long>();
	}
	
	public BitmapChar(JSONObject json, SparseArray<Long> kernings, float width, float height) {
		//Initialize
		this();
		
		//Initialize
		float X 		= 0;
		float Y			= 0;
		float Width		= 0;
		float Height	= 0;
		float OffsetX	= 0;
		float OffsetY	= 0;
		
		//Save kerning
		if (kernings!= null) m_Kerning = kernings;
		
		try {
			//Read json
			X 			= json.getInt(KEY_X);
			Y 			= json.getInt(KEY_Y);
			Width		= json.getInt(KEY_WIDTH);
			Height		= json.getInt(KEY_HEIGHT);
			OffsetX		= json.getInt(KEY_OFFSETX);
			OffsetY		= json.getInt(KEY_OFFSETY);
			m_Advance	= json.getInt(KEY_ADVANCE) * Utilities.instance().getScale();
		} catch (JSONException e) {}
		
		//Create vertex
		m_Vertices = new float[8];
		m_Vertices[0]	= OffsetX;			m_Vertices[1] = -OffsetY; 				//Top left
		m_Vertices[2]	= OffsetX;			m_Vertices[3] = -(OffsetY + Height);	//Bottom left
		m_Vertices[4]	= OffsetX + Width;	m_Vertices[5] = -OffsetY;				//Top right
		m_Vertices[6]	= OffsetX + Width;	m_Vertices[7] = -(OffsetY + Height);	//Bottom right
		for (int i = 0; i < m_Vertices.length; i++) m_Vertices[i] *= Utilities.instance().getScale();
				
		//Create coordinates
		m_Coordinates 		= new float[8];
		m_Coordinates[0]	= X / width;			m_Coordinates[1] = Y / height;				//Top left
		m_Coordinates[2]	= X / width;			m_Coordinates[3] = (Y + Height) / height;	//Bottom left
		m_Coordinates[4]	= (X + Width) / width;	m_Coordinates[5] = Y / height;				//Top right
		m_Coordinates[6]	= (X + Width) / width;	m_Coordinates[7] = (Y + Height) / height;	//Bottom right
	}

	//Accessors
	public float[] getTextureCoordinates() 	{	return m_Coordinates;					}
	public float[] getVertices() 			{	return m_Vertices;						}
	public float getAdvance()				{	return m_Advance;						}
	public float getBottom()				{	return m_Vertices[3]; 					}
	public float getHeight()				{	return m_Vertices[1] - m_Vertices[3]; 	}
	public float getWidth()					{	return m_Vertices[4] - m_Vertices[0]; 	}
	public float getTop()					{	return m_Vertices[1]; 					}
	
	public float getAdvance(char character) {
		//Get advance
		float Advance 	= getAdvance();
		Long Kerning	= m_Kerning.get(character);
		
		//if not null
		if (Kerning != null) Advance += Kerning.floatValue();
		
		//Return
		return Advance;
	}
	
	//Constants
	public final static String KEY_X		= "x";
	public final static String KEY_Y		= "y";
	public final static String KEY_WIDTH	= "width";
	public final static String KEY_HEIGHT	= "height";
	public final static String KEY_OFFSETX	= "xoffset";
	public final static String KEY_OFFSETY	= "yoffset";
	public final static String KEY_ADVANCE	= "xadvance";
	
	//Data
	protected float 			m_Advance;
	protected float[] 			m_Vertices;
	protected float[] 			m_Coordinates;
	protected SparseArray<Long>	m_Kerning;
}
