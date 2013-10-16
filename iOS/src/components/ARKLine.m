//
//  ARKLine.m
//  ARK Framework
//
//  Created by LegACy on 10/16/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKLine.h"
#import "ARKUtilities.h"
#import "ARKiOSDevice.h"

//Sizes
const int LINE_VERTEX_SIZE 	= 2;
const int LINE_VERTEX_COUNT	= 2;
const int LINE_DATA_SIZE	= LINE_VERTEX_SIZE;

@interface ARKLine ()

//Private functions
- (void)calculateSize;
- (void)createVertices;

@end

@implementation ARKLine

- (id)init { return nil; }
- (id)initStartingAtX:(float)startX atY:(float)startY toX:(float)endX toY:(float)endY {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		m_OffsetX	= 0;
		m_OffsetY	= 0;
		m_Color		= GLKVector4Make(1.0f, 1.0f, 1.0f, 1.0f);
		m_Thickness	= 1.0f * [[ARKUtilities instance] getScale];
		for (int i = 0; i < sizeof(m_Vertices) / sizeof(m_Vertices[0]); i++)	m_Vertices[i] = 0;
		
		//Save
		m_EndX 		= endX;
		m_EndY 		= endY;
		m_StartX 	= startX;
		m_StartY	= startY;
		
		//Calculate
		[self calculateSize];
		[self createVertices];
	}
	
	//Return
	return self;
}

- (void)setColorWithRed:(float)red withGreen:(float)green withBlue:(float)blue withAlpha:(float)alpha {
	//Set color
	m_Color = GLKVector4Make(red, green, blue, alpha);
}

- (void)setThickness:(float)thickness {
	//Set thickness
	m_Thickness = thickness * [[ARKUtilities instance] getScale];
}

- (void)setStartAtX:(float)x atY:(float)y {
	//Set
	m_StartX = x;
	m_StartY = y;
	
	//Recalculate
	[self calculateSize];
	[self createVertices];
}

- (void)setEndAtX:(float)x atY:(float)y {
	//Set
	m_EndX = x;
	m_EndY = y;
	
	//Recalculate
	[self calculateSize];
	[self createVertices];
}

- (void)calculateSize {
	//Get width and height
	m_OriginalWidth		= abs(m_EndX - m_StartX);
	m_OriginalHeight	= abs(m_EndY - m_StartY);
	m_Height			= m_OriginalHeight * [[ARKUtilities instance] getScale];
	m_Width				= m_OriginalWidth * [[ARKUtilities instance] getScale];
}

- (void)createVertices {
	//Calculate
	float StartX 	= 0;
	float StartY 	= 0;
	float EndX		= m_EndX > m_StartX ? m_Width : -m_Width;
	float EndY		= m_EndY > m_StartY ? -m_Height : m_Height;
	
	//Create vertices
	m_Vertices[0] = StartX;
	m_Vertices[1] = StartY;
	m_Vertices[2] = EndX;
	m_Vertices[3] = EndY;
	
	//Calculate offset
	m_OffsetX = (m_StartX * [[ARKUtilities instance] getScale]) - ([[ARKDevice instance] getWidth] / 2.0f);
	m_OffsetY = ([[ARKDevice instance] getHeight] / 2.0f) - (m_StartY * [[ARKUtilities instance] getScale]);
}

- (void)drawWithGL:(GLKBaseEffect *)gl {
	//Skip if no gl
	if (!gl) return;
	
	//Set vertex attributes
	int Size = LINE_DATA_SIZE * sizeof(GLfloat);
	glVertexAttribPointer(GLKVertexAttribPosition, LINE_VERTEX_SIZE, GL_FLOAT, GL_FALSE, Size, &m_Vertices);
	glLineWidth(m_Thickness);
	
	//Configure effect
	gl.constantColor		= m_Color;
	gl.useConstantColor		= GL_TRUE;
	gl.texture2d0.enabled	= GL_FALSE;
	glDisableVertexAttribArray(GLKVertexAttribColor);
	
	//Create matrix
    GLKMatrix4 ViewMatrix			= [[ARKiOSDevice instance] getViewMatrix];
	ViewMatrix						= GLKMatrix4Translate(ViewMatrix, m_OffsetX, m_OffsetY, 0);
    gl.transform.modelviewMatrix	= ViewMatrix;
	
	//Render
    [gl prepareToDraw];
    glDrawArrays(GL_LINES, 0, LINE_VERTEX_COUNT);
	
	//Restore configuration
	gl.texture2d0.enabled	= GL_TRUE;
	gl.useConstantColor		= GL_FALSE;
	glEnableVertexAttribArray(GLKVertexAttribColor);
}

@end
