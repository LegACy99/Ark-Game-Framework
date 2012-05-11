package net.ark.framework.system.images.android;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import net.ark.framework.system.Device;
import net.ark.framework.system.Utilities;
import net.ark.framework.system.images.BitmapFont;
import net.ark.framework.system.images.Label;
import net.ark.framework.system.images.Texture;
import net.ark.framework.system.resource.ResourceManager;

public class AndroidLabel extends Label {	
	public AndroidLabel(String text, String font) {
		this(text, font, 0, 0);
	}
	
	public AndroidLabel(String text, String font, float x, float y) {
		//Super
		super(text, x, y);
		
		//Initialize
		m_Colors			= null;
		m_Texture 			= null;
		m_Indices			= null;
		m_Vertices			= null;
		m_Coordinates		= null;
		float[] Colors		= new float[m_Text.length() * QUAD_COLORS];
		short[] Indices 	= new short[m_Text.length() * QUAD_INDICES];
		float[] Vertices 	= new float[m_Text.length() * QUAD_VERTICES];
		float[] Coordinates = new float[m_Text.length() * QUAD_COORDINATES];
		
		//if font exist
		if (font != null) {
			//Get font
			BitmapFont Font = ResourceManager.instance().getFont(font);
			m_Texture 		= (Texture) ResourceManager.instance().getTexture(Font.getTexture());

			//For each character
			float Cursor = 0;
			for (int i = 0; i < m_Text.length(); i++) {
				//Get bitmap character
				BitmapChar Char = Font.getChar(m_Text.charAt(i));
				
				//If exist
				if (Char != null) {
					//Get character vertices
					float[] CharVertices = Char.getVertices();
					for (int j = 0; j < CharVertices.length; j++) {
						//Set vertex
						int Index = (i * QUAD_VERTICES) + j;
						Vertices[Index] = CharVertices[j];
						
						//Add the X
						if (Index % 2 == 0) Vertices[Index] += Cursor;
					}
					
					//Set texture coordinates
					float[] CharCoordinates = Char.getTextureCoordinates();
					for (int j = 0; j < CharCoordinates.length; j++) Coordinates[(i * QUAD_COORDINATES) + j] = CharCoordinates[j];
					
					//Set colors and indices
					for (int j = 0; j < QUAD_COLORS; j++)	Colors[(i * QUAD_COLORS) + j] = 1f;
					for (int j = 0; j < QUAD_INDICES; j++) 	Indices[(i * QUAD_INDICES) + j] = (short) ((i * 4) + BASE_INDICES[j]);
					
					//Next if more
					if (i + 1 < m_Text.length()) 	Cursor += Char.getAdvance(m_Text.charAt(i + 1));
					else							Cursor += Char.getAdvance();
				}
			}
			
			//Set size
			m_Width 			= Cursor;
			m_OriginalHeight	= Font.getHeight();
			m_OriginalWidth		= m_Width / Utilities.instance().getScale();
			m_Height			= m_OriginalHeight * Utilities.instance().getScale();
		}
		
		//Create vertex buffer
		ByteBuffer VertexBuffer = ByteBuffer.allocateDirect(Vertices.length * Utilities.FLOAT_SIZE);
		VertexBuffer.order(ByteOrder.nativeOrder());
		m_Vertices = VertexBuffer.asFloatBuffer();
		m_Vertices.put(Vertices);
		m_Vertices.position(0);
		
		//Create index buffer
		ByteBuffer IndicesBuffer = ByteBuffer.allocateDirect(Indices.length * Utilities.SHORT_SIZE);
		IndicesBuffer.order(ByteOrder.nativeOrder());
		m_Indices = IndicesBuffer.asShortBuffer();
		m_Indices.put(Indices);
		m_Indices.position(0);

		//Create texture coordinate buffer
		ByteBuffer CoordinatesBuffer = ByteBuffer.allocateDirect(Coordinates.length * Utilities.FLOAT_SIZE);
		CoordinatesBuffer.order(ByteOrder.nativeOrder());
		m_Coordinates = CoordinatesBuffer.asFloatBuffer();
		m_Coordinates.put(Coordinates);
		m_Coordinates.position(0);
		
		//Create color buffer
		ByteBuffer ColorBuffer = ByteBuffer.allocateDirect(Colors.length * Utilities.FLOAT_SIZE);
		ColorBuffer.order(ByteOrder.nativeOrder());
		m_Colors = ColorBuffer.asFloatBuffer();
		m_Colors.put(Colors);
		m_Colors.position(0);
	}
	
	@Override
	public void draw(GL10 gl) {
		//Skip if no texture
		if (m_Texture == null) return;
		
		//Save matrix
		gl.glPushMatrix();
		
		//Send data to GPU
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, m_Colors);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, m_Vertices);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_Coordinates);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, m_Texture.getID());
		
		//Translate and draw
		gl.glTranslatef(m_X - (Device.instance().getWidth() / 2f), (Device.instance().getHeight() / 2f) - m_Y, 0);
		gl.glDrawElements(GL10.GL_TRIANGLES, m_Indices.capacity(), GL10.GL_UNSIGNED_SHORT, m_Indices);
		
		//Restore matrix
		gl.glPopMatrix();
	}
	
	//Constants
	protected final int QUAD_COLORS			= 16;
	protected final int QUAD_INDICES		= 6;
	protected final int QUAD_VERTICES		= 8;
	protected final int QUAD_COORDINATES	= 8;
	protected final short[] BASE_INDICES	= { 0, 1, 2, 2, 1, 3 };
	
	//Drawing stuff
	protected FloatBuffer	m_Colors;
	protected ShortBuffer	m_Indices;
	protected FloatBuffer 	m_Vertices;
	protected FloatBuffer 	m_Coordinates;
	protected Texture 		m_Texture;
}
