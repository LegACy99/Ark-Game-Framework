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
		m_Font				= null;
		m_Colors			= null;
		m_Texture 			= null;
		m_Indices			= null;
		m_Vertices			= null;
		m_Coordinates		= null;
		
		//Set font
		if (font != null) {
			//Get font
			m_Font 		= ResourceManager.instance().getFont(font);
			m_Texture 	= (Texture) ResourceManager.instance().getTexture(m_Font.getTexture());
		}
		
		//Calculate size and region
		calculateSize();
		setRegion(0, 0, m_OriginalWidth, m_OriginalHeight);
	}
	
	protected void calculateSize() {
		//Initialize
		m_Width 			= 0;
		m_Height			= 0;
		m_OriginalWidth		= 0;
		m_OriginalHeight	= 0;
		
		//Skip if no text
		if (m_Text == null || m_Font == null) return;
		
		//For each character
		float Cursor 		= 0;
		for (int i = 0; i < m_Text.length(); i++) {
			//Get bitmap character
			BitmapChar Char = m_Font.getChar(m_Text.charAt(i));
			
			//If exist
			if (Char != null) {					
				//Next if more
				if (i + 1 < m_Text.length()) 	Cursor += Char.getAdvance(m_Text.charAt(i + 1));
				else							Cursor += Char.getWidth();
			}
		}
		
		//Set size
		m_OriginalWidth		= Cursor;
		m_OriginalHeight	= m_Font.getHeight();
		m_Height			= m_OriginalHeight * Utilities.instance().getScale();
		m_Width				= m_OriginalWidth * Utilities.instance().getScale();
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
		
		//Skip if same no text or no font
		if (m_Text == null || m_Font == null) 																										return;
		if (m_OriginalRegionX == OldX && m_OriginalRegionY == OldY && m_OriginalRegionWidth == OldWidth && m_OriginalRegionHeight == OldHeight) 	return;
		
		//Initialize data
		int End					= -1;
		int Start 				= -1;
		float Cursor 			= 0;
		float OffsetEnd			= 0;
		float OffsetStart		= 0;
		float[] OffsetTops		= new float[m_Text.length()];
		float[] OffsetBottoms	= new float[m_Text.length()];
		BitmapChar[] Chars		= new BitmapChar[m_Text.length()];
		boolean[] Drawns		= new boolean[m_Text.length()];
		
		//For each character
		for (int i = 0; i < m_Text.length(); i++) {
			//Initialize
			OffsetTops[i] 		= 0;
			OffsetBottoms[i]	= 0;
			Drawns[i]			= false;
			Chars[i] 			= m_Font.getChar(m_Text.charAt(i));
			
			//If character exist
			if (Chars[i] != null) {				
				//Calculate vertical region
				if (m_RegionY > -Chars[i].getTop()) 					OffsetTops[i] = m_RegionY - (-Chars[i].getTop());
				if (m_RegionY + m_RegionHeight < -Chars[i].getBottom()) OffsetBottoms[i] = (-Chars[i].getBottom()) - m_RegionY - m_RegionHeight;
				
				//Check start if nothing
				if (Start < 0) {
					//If in region
					if (Cursor + Chars[i].getWidth() >= m_RegionX) {
						//Set start
						Start 		= i;
						OffsetStart = m_RegionX - Cursor;
					}
				}
				
				//Drawn?
				Drawns[i] = Start >= 0 && End < 0;
				if (Drawns[i]) if (m_RegionY > -Chars[i].getBottom() || m_RegionY + m_RegionHeight < -Chars[i].getTop()) Drawns[i] = false;
				
				//If no end yet
				if (Start >= 0 && End < 0) {
					//If in region
					if (Cursor + Chars[i].getWidth() >= m_RegionX + m_RegionWidth) {
						//Set end
						End 		= i;
						OffsetEnd 	= Cursor + Chars[i].getWidth() - m_RegionX - m_RegionWidth;
					}
				}
				
				//Move cursor
				if (i + 1 < m_Text.length()) 	Cursor += Chars[i].getAdvance(m_Text.charAt(i + 1));
				else							Cursor += Chars[i].getAdvance();				
			}
		}
		
		//Calculate size
		int Size = 0;
		for (int i = 0; i < Drawns.length; i++) if (Drawns[i]) Size++;
		
		//Create array
		float[] Colors		= new float[Size * QUAD_COLORS];
		short[] Indices 	= new short[Size * QUAD_INDICES];
		float[] Vertices 	= new float[Size * QUAD_VERTICES];
		float[] Coordinates = new float[Size * QUAD_COORDINATES];

		//For each character
		Cursor 		= 0;
		int Index	= 0;
		for (int i = 0; i < m_Text.length(); i++) {			
			//If drawn
			if (Drawns[i]) {				
				//Get character vertices
				float[] CharVertices = Chars[i].getVertices();
				for (int j = 0; j < CharVertices.length; j++) {
					//Set vertex
					int VIndex = (Index * QUAD_VERTICES) + j;
					Vertices[VIndex] = CharVertices[j];
					
					//If X, move
					if (j % 2 == 0) Vertices[VIndex] += Cursor;
					else {
						//If Y
						if (j == 1) 		Vertices[VIndex] -= OffsetTops[i];
						else if (j == 5)	Vertices[VIndex] -= OffsetTops[i];
						else if (j == 3) 	Vertices[VIndex] += OffsetBottoms[i];
						else if (j == 7) 	Vertices[VIndex] += OffsetBottoms[i];
					}
				}
				
				//Get coordinates
				float[] CharCoordinates = Chars[i].getTextureCoordinates();
				float Height			= CharCoordinates[3] - CharCoordinates[1];
				for (int j = 0; j < CharCoordinates.length; j++) {
					//Set coordiate
					int CIndex = (Index * QUAD_COORDINATES) + j;
					Coordinates[CIndex] = CharCoordinates[j];
					
					//Set vertical region
					if (j == 1) 		Coordinates[CIndex] += OffsetTops[i] / Chars[i].getHeight() * Height;
					else if (j == 5)	Coordinates[CIndex] += OffsetTops[i] / Chars[i].getHeight() * Height;
					else if (j == 3) 	Coordinates[CIndex] -= OffsetBottoms[i] / Chars[i].getHeight() * Height;
					else if (j == 7) 	Coordinates[CIndex] -= OffsetBottoms[i] / Chars[i].getHeight() * Height;
				}
				
				//If start
				if (i == Start) {
					//Configure vertex
					Vertices[(Index * QUAD_VERTICES) + 0] += OffsetStart;
					Vertices[(Index * QUAD_VERTICES) + 2] += OffsetStart;
					
					//COnfigure texture
					float Width				= CharCoordinates[4] - CharCoordinates[0];
					float CoordinateOffset	= OffsetStart / Chars[i].getWidth() * Width;
					Coordinates[(Index * QUAD_COORDINATES) + 0] += CoordinateOffset;
					Coordinates[(Index * QUAD_COORDINATES) + 2] += CoordinateOffset;
				}
				
				//If end
				if (i == End) {
					//Move
					Vertices[(Index * QUAD_VERTICES) + 4] -= OffsetEnd;
					Vertices[(Index * QUAD_VERTICES) + 6] -= OffsetEnd;
					
					//COnfigure texture
					float Width				= CharCoordinates[4] - CharCoordinates[0];
					float CoordinateOffset	= OffsetEnd / Chars[i].getWidth() * Width;
					Coordinates[(Index * QUAD_COORDINATES) + 4] -= CoordinateOffset;
					Coordinates[(Index * QUAD_COORDINATES) + 6] -= CoordinateOffset;
				}
				
				//Set colors and indices
				for (int j = 0; j < QUAD_COLORS; j++)	Colors[(Index * QUAD_COLORS) + j] = 1f;
				for (int j = 0; j < QUAD_INDICES; j++) 	Indices[(Index * QUAD_INDICES) + j] = (short) ((Index * QUAD_VERTICES / 2) + BASE_INDICES[j]);
				
				//Next
				Index++;
			}
			
			//If char exist
			if (Chars[i] != null) {
				//Next if more
				if (i + 1 < m_Text.length()) 	Cursor += Chars[i].getAdvance(m_Text.charAt(i + 1));
				else							Cursor += Chars[i].getAdvance();
			}
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
	
	//Data
	protected BitmapFont	m_Font;
	protected FloatBuffer	m_Colors;
	protected ShortBuffer	m_Indices;
	protected FloatBuffer 	m_Vertices;
	protected FloatBuffer 	m_Coordinates;
	protected Texture 		m_Texture;
}
