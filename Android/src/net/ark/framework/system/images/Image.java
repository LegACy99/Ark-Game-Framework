package net.ark.framework.system.images;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.json.JSONException;
import org.json.JSONObject;

import net.ark.framework.components.Drawable;
import net.ark.framework.system.Device;
import net.ark.framework.system.Utilities;
import net.ark.framework.system.resource.ResourceManager;

public class Image extends Drawable {
	protected Image() {
		//Super
		super();
		
		//Initialize variables
		m_Top	 		= 0;
		m_Left			= 0;	
		m_Flip			= 0;
		m_Rotation		= 0;
		m_Vertices		= null;
		m_Coordinates	= null;
		m_Texture		= null;
		m_Mirror		= MIRROR_NONE;
	}
	
	public Image(String file) {
		this(ResourceManager.instance().getJSON(file));
	}
	
	public Image(JSONObject json) {
		//Initialize
		this(json, 0, 0);
	}
	
	public Image(String file, int x, int y) {
		this(ResourceManager.instance().getJSON(file), x, y);
	}
	
	public Image(JSONObject json, int x, int y) {
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
					m_OriginalLeft = RectJSON.getLong(KEY_RECT_LEFT);
					
					//Get width
					if (!RectJSON.isNull(KEY_RECT_RIGHT)) 	m_OriginalWidth = RectJSON.getLong(KEY_RECT_RIGHT) - m_OriginalLeft;
					else									m_OriginalWidth = RectJSON.getLong(KEY_RECT_WIDTH);
				}
				else {
					//Get width
					m_OriginalWidth = RectJSON.getLong(KEY_RECT_WIDTH);
					m_OriginalLeft	= RectJSON.getLong(KEY_RECT_RIGHT) - m_Width;
				}
				
				//Get vertical size
				if (!RectJSON.isNull(KEY_RECT_TOP)) {
					//Get top
					m_OriginalTop = RectJSON.getLong(KEY_RECT_TOP);
					
					//Get height
					if (!RectJSON.isNull(KEY_RECT_BOTTOM)) 	m_OriginalHeight = RectJSON.getLong(KEY_RECT_BOTTOM) - m_OriginalTop;
					else									m_OriginalHeight = RectJSON.getLong(KEY_RECT_HEIGHT);
				}
				else {
					//Get height
					m_OriginalHeight	= RectJSON.getLong(KEY_RECT_HEIGHT);
					m_OriginalTop		= RectJSON.getLong(KEY_RECT_BOTTOM) - m_OriginalHeight;
				}
			}
			
			//Scale
			m_Top 		= m_OriginalTop * Utilities.instance().getScale();
			m_Left 		= m_OriginalLeft * Utilities.instance().getScale();
			m_Width		= m_OriginalWidth * Utilities.instance().getScale();
			m_Height	= m_OriginalHeight * Utilities.instance().getScale();

			//Create drawing rect
			setRect(0, 0, m_OriginalWidth, m_OriginalHeight);
			m_Colors = getWhiteColors();
		} catch (JSONException e) {}
	}
	
	protected static FloatBuffer getWhiteColors() {
		//Check
		if (s_White == null) {
			//Create float
			float[] Colors = new float[] {
				1f, 1f, 1f, 1f,
				1f, 1f, 1f, 1f,
				1f, 1f, 1f, 1f,
				1f, 1f, 1f, 1f
			};
			
			//Create buffer
			ByteBuffer ColorBuffer = ByteBuffer.allocateDirect(Colors.length * Utilities.FLOAT_SIZE);
			ColorBuffer.order(ByteOrder.nativeOrder());			
			s_White = ColorBuffer.asFloatBuffer();
			s_White.put(Colors);
			s_White.position(0);
		}
			
		//Return
		return s_White;
	}
	
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
	
	public void setRect(float x, float y, float width, float height) {		
		//Get vertex and coordinates
		float[] Coordinates = createCoordinates(x, y, width, height);
		float[] Vertices	= createVertices(width * Device.instance().getScale(), height * Device.instance().getScale());
		
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
	
	protected float[] createVertices(float width, float height) {
		//Create vertices
		return new float[] {
			width - (m_Width / 2f), m_Height / 2f,
			-m_Width / 2f,			m_Height / 2f,
			width - (m_Width / 2f),	(m_Height / 2f) - height,
			-m_Width / 2f,			(m_Height / 2f) - height
		};
	}
	
	protected float[] createCoordinates(float x, float y, float width, float height) {
		//Initialize
		float[] Edges 	= getEdges(x, y, width, height);
		int[] Template 	= getTemplate(m_Mirror);

		//Set coordinates
		float[] Coordinates = new float[Template.length];
		for (int i = 0; i < Coordinates.length; i++) Coordinates[i] = Edges[Template[i]];
		
		//Return
		return Coordinates;
	}
	
	protected float[] getEdges(float x, float y, float width, float height) {
		//Calculate
		float[] Edges		= new float[4];
		Edges[EDGE_TOP] 	= (m_OriginalTop + y) / m_Texture.getHeight();
		Edges[EDGE_LEFT]	= (m_OriginalLeft + x) / m_Texture.getWidth();
		Edges[EDGE_RIGHT]	= (m_OriginalLeft + x + width) / m_Texture.getWidth();
		Edges[EDGE_BOTTOM] 	= (m_OriginalTop + y + height) / m_Texture.getHeight();
		
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
	
	protected void setIndices() {
		//
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
		
		//Translate and draw
		gl.glTranslatef(((m_Width - Device.instance().getWidth()) / 2f) + m_X, ((Device.instance().getHeight() - m_Height) / 2) - m_Y, 0);
		gl.glRotatef(m_Rotation, 0, 0, -1);
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
	
	//Color
	protected static FloatBuffer s_White = null;
	
	//Data
	protected float m_Flip;
	protected float m_Rotation;
	protected int	m_Mirror;
	
	//Rect
	protected float		m_Top;
	protected float		m_Left;
	protected float		m_OriginalTop;
	protected float		m_OriginalLeft;
	
	//GL stuff
	protected FloatBuffer 	m_Colors;
	protected FloatBuffer 	m_Vertices;
	protected FloatBuffer 	m_Coordinates;
	protected Texture 		m_Texture;
}
