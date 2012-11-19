package net.ark.framework.system.images.android;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import net.ark.framework.system.Device;
import net.ark.framework.system.Utilities;
import net.ark.framework.system.images.Rectangle;

public class AndroidRectangle extends Rectangle {
	protected AndroidRectangle() {
		//Super
		super();
		
		//Initialize
		m_Colors	= null;
		m_Vertices	= null;
	}
	
	public AndroidRectangle(float x, float y, float width, float height, float r, float g, float b, float a) {
		//Default
		this();
		
		//Set data
		setPosition(x, y);
		setColor(r, g, b, a);
		setRect(width, height);
		setRegion(m_OriginalWidth, m_OriginalHeight);
	}
	
	@Override
	public void setRegion(float x, float y, float width, float height) {
		//Initialize
		float OldX		= m_OriginalRegionX;
		float OldY		= m_OriginalRegionY;
		float OldWidth	= m_OriginalRegionWidth;
		float OldHeight	= m_OriginalRegionHeight;
		
		//Super
		super.setRegion(x, y, width, height);
		
		//Recreate if there's a difference
		if (m_OriginalRegionX != OldX && m_OriginalRegionY == OldY && m_OriginalRegionWidth == OldWidth && m_OriginalRegionHeight == OldHeight) return;
		setRect(m_OriginalWidth, m_OriginalHeight);
	}
	
	@Override
	public void setColor(float r, float g, float b, float a) {
		//Super
		super.setColor(r, g, b, a);
		
		//Create float
		float[] Colors = new float[] {
			m_Color[INDEX_RED], m_Color[INDEX_GREEN], m_Color[INDEX_BLUE], m_Color[INDEX_ALPHA],
			m_Color[INDEX_RED], m_Color[INDEX_GREEN], m_Color[INDEX_BLUE], m_Color[INDEX_ALPHA],
			m_Color[INDEX_RED], m_Color[INDEX_GREEN], m_Color[INDEX_BLUE], m_Color[INDEX_ALPHA],
			m_Color[INDEX_RED], m_Color[INDEX_GREEN], m_Color[INDEX_BLUE], m_Color[INDEX_ALPHA]
		};
		
		//Create buffer
		ByteBuffer ColorBuffer = ByteBuffer.allocateDirect(Colors.length * Utilities.FLOAT_SIZE);
		ColorBuffer.order(ByteOrder.nativeOrder());			
		m_Colors = ColorBuffer.asFloatBuffer();
		m_Colors.put(Colors);
		m_Colors.position(0);
	}
	
	@Override
	public void setRect(float width, float height) {
		//Super
		super.setRect(width, height);
		
		//Create buffer
		float[] Vertices 		= createVertices();
		ByteBuffer VertexBuffer = ByteBuffer.allocateDirect(Vertices.length * Utilities.FLOAT_SIZE);
		VertexBuffer.order(ByteOrder.nativeOrder());
		
		//Save
		m_Vertices = VertexBuffer.asFloatBuffer();
		m_Vertices.put(Vertices);
		m_Vertices.position(0);
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
	
	@Override
	public void draw(GL10 gl) {
		//Save matrix
		gl.glPushMatrix();
		
		//Send data to GPU
		gl.glColorPointer(COLOR_SIZE, GL10.GL_FLOAT, 0, m_Colors);
		gl.glVertexPointer(VERTEX_SIZE, GL10.GL_FLOAT, 0, m_Vertices);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
		
		//Change blending function if not black
		if (getRedComponent() > 0 || getGreenComponent() > 0 || getBlueComponent() > 0) gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		//Translate and draw
		gl.glTranslatef(((m_Width - Device.instance().getWidth()) / 2f) + m_X, ((Device.instance().getHeight() - m_Height) / 2) - m_Y, 0);
		gl.glRotatef(m_Rotation, 0, 0, -1);
		gl.glRotatef(m_Flip, -1, 0, 0);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, m_Vertices.capacity() / VERTEX_SIZE);
		
		//Restore blending if not black
		if (getRedComponent() > 0 || getGreenComponent() > 0 || getBlueComponent() > 0) gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		//Restore matrix
		gl.glPopMatrix();
	}
	
	//Sizes
	protected final static int COLOR_SIZE 	= 4;
	protected final static int VERTEX_SIZE 	= 2;
	
	//Edge constants
	protected final static int EDGE_TOP 	= 0;
	protected final static int EDGE_LEFT 	= 1;
	protected final static int EDGE_RIGHT 	= 3;
	protected final static int EDGE_BOTTOM 	= 2;
	
	//Index constants: 0 top, 1 left, 2 bottom, 3 right
	protected final static int[] COORDINATES =  { EDGE_RIGHT, EDGE_TOP,	EDGE_LEFT, EDGE_TOP, EDGE_RIGHT, EDGE_BOTTOM, EDGE_LEFT, EDGE_BOTTOM	};
	
	//GL stuff
	protected FloatBuffer 	m_Colors;
	protected FloatBuffer 	m_Vertices;
}
