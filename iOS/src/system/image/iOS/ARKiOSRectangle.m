//
//  ARKiOSRectangle.m
//  Ark Framework
//
//  Created by LegACy on 4/26/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKiOSRectangle.h"
#import "ARKiOSDevice.h"

//Sizes
const int RECTANGLE_COLOR_SIZE		= 4;
const int RECTANGLE_VERTEX_SIZE 	= 2;
const int RECTANGLE_VERTEX_COUNT	= 4;
const int RECTANGLE_DATA_SIZE		= RECTANGLE_COLOR_SIZE + RECTANGLE_VERTEX_SIZE;

@implementation ARKiOSRectangle

- (id)init {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		for (int i = 0; i < sizeof(m_Attributes) / sizeof(m_Attributes[0]); i++) m_Attributes[i] = 0;
	}
	
	//Return
	return self;
}

- (id)initFromX:(float)x fromY:(float)y withWidth:(float)width withHeight:(float)height withRed:(float)red withGreen:(float)green withBlue:(float)blue withAlpha:(float)alpha {
	//Default
	self = [self init];
	if (self) {
		//Set data
		[self setPositionAtX:x atY:y];
		[self setRectWithWidth:width withHeight:height];
		[self setColorWithRed:red withGreen:green withBlue:blue withAlpha:alpha];
		[self setRegionWithWidth:m_OriginalWidth withHeight:m_OriginalHeight];
	}
	
	//Return
	return self;
}

- (void)setRegionFromX:(float)x fromY:(float)y withWidth:(float)width withHeight:(float)height {
	//Initialize
	float OldX		= m_OriginalRegionX;
	float OldY		= m_OriginalRegionY;
	float OldWidth	= m_OriginalRegionWidth;
	float OldHeight	= m_OriginalRegionHeight;
	
	//Super
	[super setRegionFromX:x fromY:y withWidth:width withHeight:height];
	
	//Recreate if there's difference
	if (m_OriginalRegionX != OldX && m_OriginalRegionY == OldY && m_OriginalRegionWidth == OldWidth && m_OriginalRegionHeight == OldHeight) return;
	[self setRectWithWidth:m_OriginalWidth withHeight:m_OriginalHeight];
}

- (void)setColorWithRed:(float)red withGreen:(float)green withBlue:(float)blue withAlpha:(float)alpha {
	//Super
	[super setColorWithRed:red withGreen:green withBlue:blue withAlpha:alpha];
	
	//For each vertex
	int Offset = RECTANGLE_VERTEX_SIZE;
	for (int i = 0; i < RECTANGLE_VERTEX_COUNT; i++) {
		//Set color
		m_Attributes[(i * RECTANGLE_DATA_SIZE) + Offset + 0] = m_Color[RECTANGLE_INDEX_RED];
		m_Attributes[(i * RECTANGLE_DATA_SIZE) + Offset + 2] = m_Color[RECTANGLE_INDEX_BLUE];
		m_Attributes[(i * RECTANGLE_DATA_SIZE) + Offset + 1] = m_Color[RECTANGLE_INDEX_GREEN];
		m_Attributes[(i * RECTANGLE_DATA_SIZE) + Offset + 3] = m_Color[RECTANGLE_INDEX_ALPHA];
	}
}

- (void)setRectWithWidth:(float)width withHeight:(float)height {
	//Super
	[super setRectWithWidth:width withHeight:height];
	
	//Set position
	int Size = RECTANGLE_DATA_SIZE;
	m_Attributes[0 * Size] = -(m_Width / 2) + m_RegionX + m_RegionWidth;	m_Attributes[(0 * Size) + 1] = (m_Height / 2) - m_RegionY;					//Top right
	m_Attributes[1 * Size] = -(m_Width / 2) + m_RegionX;					m_Attributes[(1 * Size) + 1] = (m_Height / 2) - m_RegionY;					//Top left
	m_Attributes[2 * Size] = -(m_Width / 2) + m_RegionX + m_RegionWidth;	m_Attributes[(2 * Size) + 1] = (m_Height / 2) - m_RegionY - m_RegionHeight;	//Bottom right
	m_Attributes[3 * Size] = -(m_Width / 2) + m_RegionX;					m_Attributes[(3 * Size) + 1] = (m_Height / 2) - m_RegionY - m_RegionHeight;	//Bottom lest
}

- (void)drawWithGL:(GLKBaseEffect *)gl {
	//Skip if no gl
	if (!gl) return;
	
	//Set vertex attributes
	int Size = RECTANGLE_DATA_SIZE * sizeof(GLfloat);
	glVertexAttribPointer(GLKVertexAttribPosition, RECTANGLE_VERTEX_SIZE, GL_FLOAT, GL_FALSE, Size, &m_Attributes);
	glVertexAttribPointer(GLKVertexAttribColor, RECTANGLE_COLOR_SIZE, GL_FLOAT, GL_FALSE, Size, &(m_Attributes[RECTANGLE_VERTEX_SIZE]));
	
	//Disable texture
	gl.texture2d0.enabled = GL_FALSE;
	
	//Calculate stuff
	float TranslationX	= ((m_Width - [[ARKDevice instance] getWidth]) / 2) + m_X;
	float TranslationY	= (([[ARKDevice instance] getHeight] - m_Height) / 2) - m_Y;
	
	//Create matrix
    GLKMatrix4 ViewMatrix			= [[ARKiOSDevice instance] getViewMatrix];
	ViewMatrix						= GLKMatrix4Translate(ViewMatrix, TranslationX, TranslationY, 0);
	ViewMatrix						= GLKMatrix4Rotate(ViewMatrix, m_Rotation, 0, 0, -1);
	ViewMatrix						= GLKMatrix4Rotate(ViewMatrix, m_Flip, -1, 0, 0);
    gl.transform.modelviewMatrix	= ViewMatrix;
	
	//Render
    [gl prepareToDraw];
    glDrawArrays(GL_TRIANGLE_STRIP, 0, RECTANGLE_VERTEX_COUNT);
	
	//Enable texture
	gl.texture2d0.enabled = GL_TRUE;
}

@end
