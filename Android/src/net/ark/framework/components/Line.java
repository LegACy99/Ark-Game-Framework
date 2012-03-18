package net.ark.framework.components;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import net.ark.framework.system.Device;
import net.ark.framework.system.Utilities;

public class Line extends Drawable {
	protected Line() {
		//Super
		super();
		
		//Initialize
		m_EndX 		= 0;
		m_EndY 		= 0;
		m_StartX 	= 0;
		m_StartY 	= 0;
		m_OffsetX	= 0;
		m_OffsetY	= 0;
		m_Thickness	= 2f * Utilities.instance().getScale();
		m_Color		= new float[] { 0.5f, 0.4f, 0.15f, 1f };
	}
	
	public Line(float startX, float startY, float endX, float endY) {
		//Default
		this();
		
		//Save
		m_EndX 		= endX;
		m_EndY 		= endY;
		m_StartX 	= startX;
		m_StartY	= startY;
		
		//Calculate
		calculateSize();
		createVertices();
	}
	
	public void setColor(float r, float g, float b, float a) {
		//Set color
		m_Color = new float[] {	r, g, b, a };
	}
	
	public void setStart(float startX, float startY) {
		//Set
		m_StartX = startX;
		m_StartY = startY;
		
		//Recalculate
		calculateSize();
		createVertices();
	}
	
	public void setEnd(float x, float y) {
		//Set
		m_EndX = x;
		m_EndY = y;
		
		//Recalculate
		calculateSize();
		createVertices();
	}
	
	protected void calculateSize() {		
		//Get width and height
		m_OriginalWidth		= Math.abs(m_EndX - m_StartX);
		m_OriginalHeight	= Math.abs(m_EndY - m_StartY);
		m_Height			= m_OriginalHeight * Utilities.instance().getScale();
		m_Width				= m_OriginalWidth * Utilities.instance().getScale();
	}
	
	protected void createVertices() {
		//Calculate
		float StartX 	= 0;
		float StartY 	= 0;
		float EndX		= m_EndX > m_StartX ? m_Width : -m_Width;
		float EndY		= m_EndY > m_StartY ? -m_Height : m_Height;
		
		//Create vertex
		float[] Vertices = new float[] { StartX, StartY, EndX, EndY };
		
		//Create buffer
		ByteBuffer VertexBuffer = ByteBuffer.allocateDirect(Vertices.length * Utilities.FLOAT_SIZE);
		VertexBuffer.order(ByteOrder.nativeOrder());
		m_Vertices = VertexBuffer.asFloatBuffer();
		m_Vertices.put(Vertices);
		m_Vertices.position(0);		
		
		//Calculate offset
		m_OffsetX = (m_StartX * Utilities.instance().getScale()) - (Device.instance().getWidth() / 2f);
		m_OffsetY = (Device.instance().getHeight() / 2f) - (m_StartY * Utilities.instance().getScale());
	}

	@Override
	public void draw(GL10 gl) {
		//Save matrix
		gl.glPushMatrix();
		
		//Set data
		gl.glLineWidth(m_Thickness);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, m_Vertices);
		gl.glColor4f(m_Color[0], m_Color[1], m_Color[2], m_Color[3]);

		//Translate and draw
		gl.glTranslatef(m_OffsetX, m_OffsetY, 0);
		gl.glDrawArrays(GL10.GL_LINES, 0, m_Vertices.capacity() / 2);
		
		//Restore matrix
		gl.glPopMatrix();
	}

	//Data
	protected float			m_EndX;
	protected float			m_EndY;
	protected float			m_StartX;
	protected float			m_StartY;
	protected float			m_OffsetX;
	protected float			m_OffsetY;
	protected float			m_Thickness;
	protected FloatBuffer	m_Vertices;
	protected float[] 		m_Color;
}
