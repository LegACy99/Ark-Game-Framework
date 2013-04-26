//
//  ARKiOSLabel.m
//  Ark Framework
//
//  Created by LegACy on 4/17/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

//Header files
#import <GLKit/GLKit.h>
#import "ARKiOSLabel.h"
#import "ARKResourceManager.h"
#import "ARKBitmapFont.h"
#import "ARKBitmapChar.h"
#import "ARKUtilities.h"
#import "ARKiOSDevice.h"
#import "ARKTexture.h"

//Constants
const int LABEL_COLOR_SIZE			= 4;
const int LABEL_VERTEX_SIZE			= 2;
const int LABEL_COORDINATE_SIZE		= 2;
const int LABEL_QUAD_COLORS			= LABEL_COLOR_SIZE * 4;
const int LABEL_QUAD_VERTICES		= LABEL_VERTEX_SIZE * 4;
const int LABEL_QUAD_COORDINATES	= LABEL_COORDINATE_SIZE * 4;
const short LABEL_BASE_INDICES[]	= { 0, 1, 2, 2, 1, 3 };
const int LABEL_QUAD_INDICES		= 6;

@interface ARKiOSLabel ()

- (void)calculateSize;
- (void)releaseBuffer;

@end

@implementation ARKiOSLabel

- (id)initWithText:(NSString *)text withFont:(NSString *)font { return [self initWithText:text withFont:font atX:0 atY:0]; }
- (id)initWithText:(NSString *)text withFont:(NSString *)font atX:(float)x atY:(float)y {
	//Super
	self = [super initWithText:text atX:x atY:y];
	if (self) {
		//Initialize
		m_Size		= 0;
		m_Font		= nil;
		m_Buffer	= nil;
		m_Indices	= nil;
		m_Texture	= nil;
		
		//Set font
		if (font) {
			//Get font
			m_Font = [[ARKResourceManager instance] getFontWithName:font];
			if (m_Font) m_Texture = [[ARKResourceManager instance] getTextureWithName:[m_Font getTexture]];
		}
		
		//Calculate size and region
		[self calculateSize];
		[self setRegionFromX:0 fromY:0 withWidth:m_OriginalWidth withHeight:m_OriginalHeight];
	}
	
	//Return
	return self;
}

- (void)dealloc {
	//Release
	[self releaseBuffer];
}

- (void)releaseBuffer {
	//Release
	if (m_Buffer)	free(m_Buffer);
	if (m_Indices)	free(m_Indices);
}

- (void)calculateSize {
	//Initialize
	m_Width 			= 0;
	m_Height			= 0;
	m_OriginalWidth		= 0;
	m_OriginalHeight	= 0;
	
	//Skip if no text
	if (!m_Text || !m_Font || [m_Text length] <= 0) return;
	
	//For each character
	float Cursor = 0;
	for (int i = 0; i < [m_Text length]; i++) {
		//Get bitmap character
		ARKBitmapChar* Char = [m_Font getCharForCharacter:[m_Text characterAtIndex:i]];
		if (Char) {
			//Next if more
			if (i + 1 < [m_Text length]) 	Cursor += [Char getAdvanceForChar:[m_Text characterAtIndex:(i + 1)]];
			else							Cursor += [Char getAdvance];
		}
	}
	
	//Set size
	m_Width 			= Cursor;
	m_OriginalHeight	= [m_Font getHeight];
	m_OriginalWidth		= m_Width / [[ARKUtilities instance] getScale];
	m_Height			= m_OriginalHeight * [[ARKUtilities instance] getScale];
}

- (void)setRegionFromX:(float)x fromY:(float)y withWidth:(float)width withHeight:(float)height {
	//Initialize
	float OldX		= m_OriginalRegionX;
	float OldY		= m_OriginalRegionY;
	float OldWidth	= m_OriginalRegionWidth;
	float OldHeight	= m_OriginalRegionHeight;
	
	//Super
	[super setRegionFromX:x fromY:y withWidth:width withHeight:height];
	
	//Skip if same or no text
	if (!m_Text || !m_Font || [m_Text length] <= 0)																							return;
	if (m_OriginalRegionX == OldX && m_OriginalRegionY == OldY && m_OriginalRegionWidth == OldWidth && m_OriginalRegionHeight == OldHeight)	return;
	
	//Initialize data
	int End							= -1;
	int Start						= -1;
	float Cursor					= 0;
	float OffsetEnd					= 0;
	float OffsetStart				= 0;
	NSMutableArray* Drawns			= [NSMutableArray arrayWithCapacity:[m_Text length]];
	NSMutableArray* Chars			= [NSMutableArray arrayWithCapacity:[m_Text length]];
	NSMutableArray* OffsetTops		= [NSMutableArray arrayWithCapacity:[m_Text length]];
	NSMutableArray* OffsetBottoms	= [NSMutableArray arrayWithCapacity:[m_Text length]];
	
	//For each character
	for (int i = 0; i < [m_Text length]; i++) {
		//Initialize
		[Drawns addObject:[NSNumber numberWithBool:NO]];
		[OffsetTops addObject:[NSNumber numberWithFloat:0]];
		[OffsetBottoms addObject:[NSNumber numberWithFloat:0]];
		[Chars addObject:[m_Font getCharForCharacter:[m_Text characterAtIndex:i]]];
		
		//If character exist
		ARKBitmapChar* Chara = [Chars objectAtIndex:i];
		if (Chara) {
			//Calculate vertical region
			if (m_RegionY > -[Chara getTop])
				[OffsetTops replaceObjectAtIndex:i withObject:[NSNumber numberWithFloat:m_RegionY - (-[Chara getTop])]];
			if (m_RegionY + m_RegionHeight < -[Chara getBottom])
				[OffsetBottoms replaceObjectAtIndex:i withObject:[NSNumber numberWithFloat:(-[Chara getBottom]) - m_RegionY - m_RegionHeight]];
			
			//Check start if nothing
			if (Start < 0) {
				//If in region
				if (Cursor + [Chara getWidth] >= m_RegionX) {
					//Set start
					Start 		= i;
					OffsetStart = m_RegionX - Cursor;
				}
			}
			
			//Drawn?
			[Drawns replaceObjectAtIndex:i withObject:[NSNumber numberWithBool:(Start >= 0 && End < 0)]];
			if ([[Drawns objectAtIndex:i] boolValue]) if (m_RegionY > -[Chara getBottom] || m_RegionY + m_RegionHeight < -[Chara getTop])
				[Drawns replaceObjectAtIndex:i withObject:[NSNumber numberWithBool:NO]];
			
			//If no end yet
			if (Start >= 0 && End < 0) {
				//If in region
				if (Cursor + [Chara getWidth] >= m_RegionX + m_RegionWidth) {
					//Set end
					End 		= i;
					OffsetEnd 	= Cursor + [Chara getWidth] - m_RegionX - m_RegionWidth;
				}
			}
			
			//Move cursor
			if (i + 1 < [m_Text length]) 	Cursor += [Chara getAdvanceForChar:[m_Text characterAtIndex:i + 1]];
			else							Cursor += [Chara getAdvance];
		}
	}
	
	//Calculate size
	int Size = 0;
	for (int i = 0; i < [Drawns count]; i++) if ([[Drawns objectAtIndex:i] boolValue]) Size++;
	
	//Release buffers
	[self releaseBuffer];
	
	//Create index
	m_Size		= Size * LABEL_QUAD_INDICES;
	m_Indices	= malloc(m_Size * sizeof(GLshort));
	
	//Create more arrays
	NSMutableArray* Colors		= [NSMutableArray arrayWithCapacity:Size * LABEL_QUAD_COLORS];
	NSMutableArray* Vertices 	= [NSMutableArray arrayWithCapacity:Size * LABEL_QUAD_VERTICES];
	NSMutableArray* Coordinates = [NSMutableArray arrayWithCapacity:Size * LABEL_QUAD_COORDINATES];
	for (int i = 0; i < Size * LABEL_QUAD_COORDINATES; i++) [Coordinates addObject:[NSNull null]];
	for (int i = 0; i < Size * LABEL_QUAD_VERTICES; i++)	[Vertices addObject:[NSNull null]];
	for (int i = 0; i < Size * LABEL_QUAD_COLORS; i++)		[Colors addObject:[NSNull null]];
	
	//For each character
	Cursor 		= 0;
	int Index	= 0;
	for (int i = 0; i < [m_Text length]; i++) {
		//If drawn
		if ([[Drawns objectAtIndex:i] boolValue]) {
			//Get character vertices
			NSArray* CharVertices = [[Chars objectAtIndex:i] getVertices];
			for (int j = 0; j < [CharVertices count]; j++) {
				//Set vertex
				int VIndex		= (Index * LABEL_QUAD_VERTICES) + j;
				float Vertex	= [[CharVertices objectAtIndex:j] floatValue];
				
				//If X, move
				if (j % 2 == 0) Vertex += Cursor;
				else {
					//If Y
					if (j == 1) 		Vertex -= [[OffsetTops objectAtIndex:i] floatValue];
					else if (j == 5)	Vertex -= [[OffsetTops objectAtIndex:i] floatValue];
					else if (j == 3) 	Vertex += [[OffsetBottoms objectAtIndex:i] floatValue];
					else if (j == 7) 	Vertex += [[OffsetBottoms objectAtIndex:i] floatValue];
				}
				
				//Save vertex
				[Vertices replaceObjectAtIndex:VIndex withObject:[NSNumber numberWithFloat:Vertex]];
			}
			
			//Get coordinates
			float CharHeight			= [[Chars objectAtIndex:i] getHeight];
			NSArray* CharCoordinates	= [[Chars objectAtIndex:i] getTextureCoordinates];
			float Height				= [[CharCoordinates objectAtIndex:3] floatValue] - [[CharCoordinates objectAtIndex:1] floatValue];
			for (int j = 0; j < [CharCoordinates count]; j++) {
				//Set coordiate
				int CIndex			= (Index * LABEL_QUAD_COORDINATES) + j;
				float Coordinate	= [[CharCoordinates objectAtIndex:j] floatValue];
				
				//Set vertical region
				if (j == 1) 		Coordinate += [[OffsetTops objectAtIndex:i] floatValue] / CharHeight * Height;
				else if (j == 5)	Coordinate += [[OffsetTops objectAtIndex:i] floatValue] / CharHeight * Height;
				else if (j == 3) 	Coordinate -= [[OffsetBottoms objectAtIndex:i] floatValue] / CharHeight * Height;
				else if (j == 7) 	Coordinate -= [[OffsetBottoms objectAtIndex:i] floatValue] / CharHeight * Height;
				
				//Save
				[Coordinates replaceObjectAtIndex:CIndex withObject:[NSNumber numberWithFloat:Coordinate]];
			}
			
			//If start
			if (i == Start) {
				//Configure vertex
				float Vertex0 = [[Vertices objectAtIndex:(Index * LABEL_QUAD_VERTICES) + 0] floatValue] + OffsetStart;
				float Vertex2 = [[Vertices objectAtIndex:(Index * LABEL_QUAD_VERTICES) + 2] floatValue] + OffsetStart;
				[Vertices replaceObjectAtIndex:(Index * LABEL_QUAD_VERTICES) + 0 withObject:[NSNumber numberWithFloat:Vertex0]];
				[Vertices replaceObjectAtIndex:(Index * LABEL_QUAD_VERTICES) + 2 withObject:[NSNumber numberWithFloat:Vertex2]];
				
				//Calculate coordinate stuff
				float Width				= [[CharCoordinates objectAtIndex:4] floatValue] - [[CharCoordinates objectAtIndex:0] floatValue];
				float CoordinateOffset	= OffsetStart / [[Chars objectAtIndex:i] getWidth] * Width;
				
				//Configure texture
				float Coordinate0 = [[Coordinates objectAtIndex:(Index * LABEL_QUAD_COORDINATES) + 0] floatValue] + CoordinateOffset;
				float Coordinate2 = [[Coordinates objectAtIndex:(Index * LABEL_QUAD_COORDINATES) + 2] floatValue] + CoordinateOffset;
				[Coordinates replaceObjectAtIndex:(Index * LABEL_QUAD_COORDINATES) + 0 withObject:[NSNumber numberWithFloat:Coordinate0]];
				[Coordinates replaceObjectAtIndex:(Index * LABEL_QUAD_COORDINATES) + 2 withObject:[NSNumber numberWithFloat:Coordinate2]];
			}
			
			//If end
			if (i == End) {
				//Move
				float Vertex4 = [[Vertices objectAtIndex:(Index * LABEL_QUAD_VERTICES) + 4] floatValue] - OffsetEnd;
				float Vertex6 = [[Vertices objectAtIndex:(Index * LABEL_QUAD_VERTICES) + 6] floatValue] - OffsetEnd;
				[Vertices replaceObjectAtIndex:(Index * LABEL_QUAD_VERTICES) + 4 withObject:[NSNumber numberWithFloat:Vertex4]];
				[Vertices replaceObjectAtIndex:(Index * LABEL_QUAD_VERTICES) + 6 withObject:[NSNumber numberWithFloat:Vertex6]];
				
				//Calculate coordinate stuff
				float Width				= [[CharCoordinates objectAtIndex:4] floatValue] - [[CharCoordinates objectAtIndex:0] floatValue];
				float CoordinateOffset	= OffsetEnd / [[Chars objectAtIndex:i] getWidth] * Width;
				
				//Configure texture
				float Coordinate4 = [[Coordinates objectAtIndex:(Index * LABEL_QUAD_COORDINATES) + 4] floatValue] - CoordinateOffset;
				float Coordinate6 = [[Coordinates objectAtIndex:(Index * LABEL_QUAD_COORDINATES) + 6] floatValue] - CoordinateOffset;
				[Coordinates replaceObjectAtIndex:(Index * LABEL_QUAD_COORDINATES) + 4 withObject:[NSNumber numberWithFloat:Coordinate4]];
				[Coordinates replaceObjectAtIndex:(Index * LABEL_QUAD_COORDINATES) + 6 withObject:[NSNumber numberWithFloat:Coordinate6]];
			}
			
			//Set colors and indices
			for (int j = 0; j < LABEL_QUAD_COLORS; j++)	[Colors replaceObjectAtIndex:(Index * LABEL_QUAD_COLORS) + j withObject:[NSNumber numberWithFloat:1]];
			for (int j = 0; j < LABEL_QUAD_INDICES; j++) m_Indices[(Index * LABEL_QUAD_INDICES) + j] = (short)((Index * LABEL_QUAD_VERTICES / 2) + LABEL_BASE_INDICES[j]);
			
			//Next
			Index++;
		}
		
		//If char exist
		if ([Chars objectAtIndex:i]) {
			//Next if more
			if (i + 1 < [m_Text length]) 	Cursor += [[Chars objectAtIndex:i] getAdvanceForChar:[m_Text characterAtIndex:i + 1]];
			else							Cursor += [[Chars objectAtIndex:i] getAdvance];
		}
	}
	
	//Allocate
	int BufferSize	= [Vertices count] + [Colors count] + [Coordinates count];
	m_Buffer		= malloc(BufferSize * sizeof(GLfloat));
	
	//For each vertex
	int VertexSize = LABEL_VERTEX_SIZE + LABEL_COLOR_SIZE + LABEL_COORDINATE_SIZE;
	for (int i = 0; i < BufferSize / VertexSize; i++) {
		//Save position data
		int VertexIndex = i * VertexSize;
		for (int j = 0; j < LABEL_VERTEX_SIZE; j++) m_Buffer[VertexIndex + j] = [[Vertices objectAtIndex:(i * LABEL_VERTEX_SIZE) + j] floatValue];
		
		//Save coordinate data
		VertexIndex += LABEL_VERTEX_SIZE;
		for (int j = 0; j < LABEL_COORDINATE_SIZE; j++) m_Buffer[VertexIndex + j] = [[Coordinates objectAtIndex:(i * LABEL_COORDINATE_SIZE) + j] floatValue];
		
		//Save color data
		VertexIndex += LABEL_COORDINATE_SIZE;
		for (int j = 0; j < LABEL_COLOR_SIZE; j++) m_Buffer[VertexIndex + j]	= [[Colors objectAtIndex:(i * LABEL_COLOR_SIZE) + j] floatValue];
	}
	
}

- (void)drawWithGL:(GLKBaseEffect *)gl {
	//Skip if nothing
	if (!m_Texture || m_Size <= 0 || !m_Buffer || !m_Indices || !gl) return;
	
	//Set vertex attributes
	int Size = sizeof(GLfloat) * (LABEL_VERTEX_SIZE + LABEL_COLOR_SIZE + LABEL_COORDINATE_SIZE);
	glVertexAttribPointer(GLKVertexAttribPosition, LABEL_VERTEX_SIZE, GL_FLOAT, GL_FALSE, Size, m_Buffer);
	glVertexAttribPointer(GLKVertexAttribColor, LABEL_COLOR_SIZE, GL_FLOAT, GL_FALSE, Size, &(m_Buffer[LABEL_VERTEX_SIZE + LABEL_COORDINATE_SIZE]));
	glVertexAttribPointer(GLKVertexAttribTexCoord0, LABEL_COORDINATE_SIZE, GL_FLOAT, GL_FALSE, Size, &(m_Buffer[LABEL_VERTEX_SIZE]));
	
    //Set texture
	gl.texture2d0.name = [m_Texture getID];
	
	//Create matrix
	float TranslationX				= m_X - ([[ARKDevice instance] getWidth] / 2);
	float TranslationY				= ([[ARKDevice instance] getHeight] / 2) - m_Y;
	gl.transform.modelviewMatrix	= GLKMatrix4Translate([[ARKiOSDevice instance] getViewMatrix], TranslationX,  TranslationY, 0);
	
	//Render
    [gl prepareToDraw];
	glDrawElements(GL_TRIANGLES, m_Size, GL_UNSIGNED_SHORT, m_Indices);
}

@end
