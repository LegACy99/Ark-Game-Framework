package net.ark.framework.system.images.android;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import net.ark.framework.system.Device;
import net.ark.framework.system.Utilities;
import net.ark.framework.system.images.Image;
import net.ark.framework.system.images.Texture;
import net.ark.framework.system.resource.ResourceManager;

import org.json.JSONException;
import org.json.JSONObject;

public class AndroidImage extends Image {
	protected AndroidImage() {
		//Super
		super();
		
		//Initialize variables
		m_Top			= 0;
		m_Left			= 0;
		m_Colors		= null;
		m_Vertices		= null;
		m_Coordinates	= null;
		m_Texture		= null;
	}
	
	public AndroidImage(String file, int x, int y) {
		this(ResourceManager.instance().getJSON(file), x, y);
	}
	
	public AndroidImage(JSONObject json, int x, int y) {
		//Initialize
		this();
		
		//Save position
		setPosition(x, y);
		
		try {
			//Get texture
			String TextureName 	= Utilities.TEXTURE_FOLDER + json.getString(KEY_TEXTURE);
			m_Texture			= (Texture) ResourceManager.instance().getTexture(TextureName);
			
			//Get rect
			JSONObject RectJSON = json.getJSONObject(KEY_RECT);
			if (RectJSON != null) {
				//Get horizontal size
				if (!RectJSON.isNull(KEY_RECT_LEFT)) {
					//Get left
					m_Left = RectJSON.getLong(KEY_RECT_LEFT);
					
					//Get width
					if (!RectJSON.isNull(KEY_RECT_RIGHT)) 	m_OriginalWidth = RectJSON.getLong(KEY_RECT_RIGHT) - m_Left;
					else									m_OriginalWidth = RectJSON.getLong(KEY_RECT_WIDTH);
				}
				else {
					//Get width
					m_OriginalWidth = RectJSON.getLong(KEY_RECT_WIDTH);
					m_Left	= RectJSON.getLong(KEY_RECT_RIGHT) - m_Width;
				}
				
				//Get vertical size
				if (!RectJSON.isNull(KEY_RECT_TOP)) {
					//Get top
					m_Top = RectJSON.getLong(KEY_RECT_TOP);
					
					//Get height
					if (!RectJSON.isNull(KEY_RECT_BOTTOM)) 	m_OriginalHeight = RectJSON.getLong(KEY_RECT_BOTTOM) - m_Top;
					else									m_OriginalHeight = RectJSON.getLong(KEY_RECT_HEIGHT);
				}
				else {
					//Get height
					m_OriginalHeight	= RectJSON.getLong(KEY_RECT_HEIGHT);
					m_Top		= RectJSON.getLong(KEY_RECT_BOTTOM) - m_OriginalHeight;
				}
			}
			
			//Scale
			m_Width		= m_OriginalWidth * Utilities.instance().getScale();
			m_Height	= m_OriginalHeight * Utilities.instance().getScale();
			m_PivotY	= m_Height / 2f;
			m_PivotX	= m_Width / 2f;

			//Create drawing rect
			setRegion(0, 0, m_OriginalWidth, m_OriginalHeight);
			m_Colors = createColors();
		} catch (JSONException e) {}
	}
	
	@Override
	public void setTint(float red, float green, float blue, float alpha) {
		//Super
		super.setTint(red, green, blue, alpha);
		
		//Create buffer
		m_Colors = createColors();
	}
	
	@Override
	public void setRegion(float x, float y, float width, float height) {
		//Set region not forced
		setRegion(x, y, width, height, false);
	}
	
	@Override
	protected void setRegion(float x, float y, float width, float height, boolean force) {
		//Initialize
		boolean Valid 	= force;
		float OldX		= m_OriginalRegionX;
		float OldY		= m_OriginalRegionY;
		float OldWidth	= m_OriginalRegionWidth;
		float OldHeight	= m_OriginalRegionHeight;
		
		//Super
		super.setRegion(x, y, width, height);
		
		//If not valid
		if (!Valid) {
			//Valid if there's a difference
			if (m_OriginalRegionX != OldX) 					Valid = true;
			else if (m_OriginalRegionY != OldY) 			Valid = true;
			else if (m_OriginalRegionWidth != OldWidth) 	Valid = true;
			else if (m_OriginalRegionHeight != OldHeight) 	Valid = true;
		}
		
		//Proceed if valid
		if (Valid) {
			//Get vertex and coordinates
			float[] Coordinates = createCoordinates();
			float[] Vertices	= createVertices();
			
			//Create buffers
			ByteBuffer VertexBuffer 	= ByteBuffer.allocateDirect(Vertices.length * Utilities.FLOAT_SIZE);
			ByteBuffer CoordinateBuffer = ByteBuffer.allocateDirect(Coordinates.length * Utilities.FLOAT_SIZE);
			CoordinateBuffer.order(ByteOrder.nativeOrder());
			VertexBuffer.order(ByteOrder.nativeOrder());
			
			//Save vertices
			m_Vertices = VertexBuffer.asFloatBuffer();
			m_Vertices.put(Vertices);
			m_Vertices.position(0);
			
			//Save coordinate
			m_Coordinates = CoordinateBuffer.asFloatBuffer();
			m_Coordinates.put(Coordinates);
			m_Coordinates.position(0);	
		}
	}
	
	protected float[] createVertices() {		
		//Create vertices
		return new float[] {
			-(m_Width / 2f) + m_RegionX + m_RegionWidth, 	(m_Height / 2f) - m_RegionY,					//Top right
			-(m_Width / 2f) + m_RegionX,					(m_Height / 2f) - m_RegionY,					//Top left
			-(m_Width / 2f) + m_RegionX + m_RegionWidth,	(m_Height / 2f) - m_RegionY - m_RegionHeight,	//Bottom right
			-(m_Width / 2f) + m_RegionX,					(m_Height / 2f) - m_RegionY - m_RegionHeight 	//Bottom left
		};
	}
	
	protected float[] createCoordinates() {
		//Initialize
		float[] Edges 	= getEdges();
		int[] Template 	= getTemplate(m_Mirror);

		//Set coordinates
		float[] Coordinates = new float[Template.length];
		for (int i = 0; i < Coordinates.length; i++) Coordinates[i] = Edges[Template[i]];
		
		//Return
		return Coordinates;
	}
	
	protected FloatBuffer createColors() {
		//Initialize
		FloatBuffer ColorBuffer = null;
		float[] Colors = new float[] {
			m_ColorRed, m_ColorGreen, m_ColorBlue, m_ColorAlpha,
			m_ColorRed, m_ColorGreen, m_ColorBlue, m_ColorAlpha,
			m_ColorRed, m_ColorGreen, m_ColorBlue, m_ColorAlpha,
			m_ColorRed, m_ColorGreen, m_ColorBlue, m_ColorAlpha
		};
		
		//Create buffer
		ByteBuffer Buffer = ByteBuffer.allocateDirect(Colors.length * Utilities.FLOAT_SIZE);
		Buffer.order(ByteOrder.nativeOrder());			
		ColorBuffer = Buffer.asFloatBuffer();
		ColorBuffer.put(Colors);
		ColorBuffer.position(0);
			
		//Return
		return ColorBuffer;
	}
	
	protected float[] getEdges() {
		//Calculate
		float[] Edges		= new float[4];
		Edges[EDGE_TOP] 	= (m_Top + m_OriginalRegionY) / m_Texture.getHeight();
		Edges[EDGE_LEFT]	= (m_Left + m_OriginalRegionX) / m_Texture.getWidth();
		Edges[EDGE_RIGHT]	= (m_Left + m_OriginalRegionX + m_OriginalRegionWidth) / m_Texture.getWidth();
		Edges[EDGE_BOTTOM] 	= (m_Top + m_OriginalRegionY + m_OriginalRegionHeight) / m_Texture.getHeight();
		
		//Return
		return Edges;
	}
	
	protected int[] getTemplate(int mirror) {
		//Get the correct pattern
		int[] Template = COORDINATES_NORMAL;
		if (mirror == MIRROR_BOTH)	 			Template = COORDINATES_BOTHMIRROR;
		else if (mirror == MIRROR_VERTICAL) 	Template = COORDINATES_VMIRROR;
		else if (mirror == MIRROR_HORIZONTAL) 	Template = COORDINATES_HMIRROR;
		
		//Return
		return Template;
	}

	@Override
	public void draw(GL10 gl) {
		//Save matrix
		gl.glPushMatrix();
		
		//Send data to GPU
		gl.glColorPointer(COLOR_SIZE, GL10.GL_FLOAT, 0, m_Colors);
		gl.glVertexPointer(VERTEX_SIZE, GL10.GL_FLOAT, 0, m_Vertices);
		gl.glTexCoordPointer(COORDINATE_SIZE, GL10.GL_FLOAT, 0, m_Coordinates);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, m_Texture.getID());
		
		//Calculate rotation pivot
		float PivotX = (m_Width / 2f) - m_PivotX;
		float PivotY = m_PivotY - (m_Height / 2f);
		
		//Translate and draw
		gl.glTranslatef(((m_Width - Device.instance().getWidth()) / 2f) + m_X - PivotX, ((Device.instance().getHeight() - m_Height) / 2) - m_Y - PivotY, 0);
		gl.glRotatef(m_Rotation, 0, 0, -1);
		gl.glTranslatef(PivotX, PivotY, 0);
		gl.glRotatef(m_Flip, -1, 0, 0);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, m_Vertices.capacity() / VERTEX_SIZE);
		
		//Restore matrix
		gl.glPopMatrix();
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
	
	//Sizes
	protected final static int COLOR_SIZE 		= 4;
	protected final static int VERTEX_SIZE 		= 2;
	protected final static int COORDINATE_SIZE 	= 2;
	
	//Edge constants
	protected final static int EDGE_TOP 	= 0;
	protected final static int EDGE_LEFT 	= 1;
	protected final static int EDGE_RIGHT 	= 3;
	protected final static int EDGE_BOTTOM 	= 2;	
	
	//Index constants: 0 top, 1 left, 2 bottom, 3 right
	protected final static int[] COORDINATES_NORMAL		=  { EDGE_RIGHT, EDGE_TOP,		EDGE_LEFT, EDGE_TOP,		EDGE_RIGHT, EDGE_BOTTOM,	EDGE_LEFT, EDGE_BOTTOM	};
	protected final static int[] COORDINATES_HMIRROR 	=  { EDGE_LEFT, EDGE_TOP,		EDGE_RIGHT, EDGE_TOP,		EDGE_LEFT, EDGE_BOTTOM,		EDGE_RIGHT, EDGE_BOTTOM	};
	protected final static int[] COORDINATES_VMIRROR 	=  { EDGE_RIGHT, EDGE_BOTTOM,	EDGE_LEFT, EDGE_BOTTOM,		EDGE_LEFT, EDGE_TOP,		EDGE_RIGHT, EDGE_TOP 	};
	protected final static int[] COORDINATES_BOTHMIRROR =  { EDGE_LEFT, EDGE_BOTTOM,	EDGE_RIGHT, EDGE_BOTTOM,	EDGE_RIGHT, EDGE_TOP,		EDGE_LEFT, EDGE_TOP		};
	
	//Data
	protected float	m_Top;
	protected float	m_Left;
	
	//GL stuff
	protected FloatBuffer 	m_Colors;
	protected FloatBuffer 	m_Vertices;
	protected FloatBuffer 	m_Coordinates;
	protected Texture 		m_Texture;
}
