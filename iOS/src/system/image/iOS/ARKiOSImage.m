//
//  ARKiOSImage.m
//  Ark Framework
//
//  Created by LegACy on 4/6/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

//Header files
#import <GLKit/GLKit.h>
#import "ARKiOSImage.h"
#import "ARKResourceManager.h"
#import "ARKUtilities.h"
#import "ARKiOSDevice.h"
#import "ARKTexture.h"

//Sizes
const int IMAGE_COLOR_SIZE 		= 4;
const int IMAGE_VERTEX_SIZE 	= 2;
const int IMAGE_COORDINATE_SIZE = 2;
const int IMAGE_DATA_SIZE		= IMAGE_VERTEX_SIZE + IMAGE_COORDINATE_SIZE + IMAGE_COLOR_SIZE;
const int IMAGE_VERTEX_COUNT	= 4;

//Edge constants
const int IMAGE_EDGE_TOP	= 0;
const int IMAGE_EDGE_LEFT 	= 1;
const int IMAGE_EDGE_RIGHT 	= 3;
const int IMAGE_EDGE_BOTTOM = 2;

//Index constants: 0 top, 1 left, 2 bottom, 3 right
const int IMAGE_COORDINATES_NORMAL[8]		=  { IMAGE_EDGE_RIGHT, IMAGE_EDGE_TOP,		IMAGE_EDGE_LEFT, IMAGE_EDGE_TOP,		IMAGE_EDGE_RIGHT, IMAGE_EDGE_BOTTOM,	IMAGE_EDGE_LEFT, IMAGE_EDGE_BOTTOM	};
const int IMAGE_COORDINATES_HMIRROR[8]		=  { IMAGE_EDGE_LEFT, IMAGE_EDGE_TOP,		IMAGE_EDGE_RIGHT, IMAGE_EDGE_TOP,		IMAGE_EDGE_LEFT, IMAGE_EDGE_BOTTOM,		IMAGE_EDGE_RIGHT, IMAGE_EDGE_BOTTOM	};
const int IMAGE_COORDINATES_VMIRROR[8]		=  { IMAGE_EDGE_RIGHT, IMAGE_EDGE_BOTTOM,	IMAGE_EDGE_LEFT, IMAGE_EDGE_BOTTOM,		IMAGE_EDGE_LEFT, IMAGE_EDGE_TOP,		IMAGE_EDGE_RIGHT, IMAGE_EDGE_TOP 	};
const int IMAGE_COORDINATES_BOTHMIRROR[8]	=  { IMAGE_EDGE_LEFT, IMAGE_EDGE_BOTTOM,	IMAGE_EDGE_RIGHT, IMAGE_EDGE_BOTTOM,	IMAGE_EDGE_RIGHT, IMAGE_EDGE_TOP,		IMAGE_EDGE_LEFT, IMAGE_EDGE_TOP		};

//Private stuff
@interface ARKiOSImage ()

//Private functions
- (void)configureColors;
- (void)configureVertices;
- (void)configureCoordinates;
- (const int*)getTemplateFromMirror:(int)mirror;
- (NSArray*)getEdges;

@end

@implementation ARKiOSImage

- (id)init {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		m_Top		= 0;
		m_Left		= 0;
		m_Texture	= nil;
		for (int i = 0; i < sizeof(m_Attributes) / sizeof(m_Attributes[0]); i++) m_Attributes[i] = 0;
	}
	
	//Return
	return self;
}

- (id)initFromFile:(NSString *)file atX:(float)x atY:(float)y {
	//Return using JSON
	return [self initFromJSON:[[ARKResourceManager instance] getJSONWithName:file] atX:x atY:y];
}

- (id)initFromJSON:(NSDictionary*)json atX:(float)x atY:(float)y {
	//Init
	self = [self init];
	if (self) {
		//Set position
		[self setPositionAtX:x atY:y];
		
		//If json exist
		if (json) {
			//Get texture
			NSString* TextureName	= [json objectForKey:IMAGE_KEY_TEXTURE];
			m_Texture				= [[ARKResourceManager instance] getTextureWithName:TextureName];
			
			//Get rect
			NSDictionary* RectDictionary = [json objectForKey:IMAGE_KEY_RECT];
			if (RectDictionary) {
				//Get horizontal size
				NSNumber* Left = [RectDictionary objectForKey:IMAGE_KEY_RECT_LEFT];
				if (Left) {
					//Get left
					m_Left = [Left floatValue];
					
					//Get width
					NSNumber* Right = [RectDictionary objectForKey:IMAGE_KEY_RECT_RIGHT];
					NSNumber* Width = [RectDictionary objectForKey:IMAGE_KEY_RECT_WIDTH];
					if (Right)		m_OriginalWidth = [Right floatValue] - m_Left;
					else if (Width)	m_OriginalWidth = [Width floatValue];
				} else {
					//Get width
					NSNumber* Right = [RectDictionary objectForKey:IMAGE_KEY_RECT_RIGHT];
					NSNumber* Width = [RectDictionary objectForKey:IMAGE_KEY_RECT_WIDTH];
					if (Right) m_Left = [Right floatValue] - m_OriginalWidth;
					if (Width) m_OriginalWidth = [Width floatValue];
				}
				
				//Get vertical size
				NSNumber* Top = [RectDictionary objectForKey:IMAGE_KEY_RECT_TOP];
				if (Top) {
					//Get top
					m_Top = [Top floatValue];
					
					//Get height
					NSNumber* Bottom = [RectDictionary objectForKey:IMAGE_KEY_RECT_BOTTOM];
					NSNumber* Height = [RectDictionary objectForKey:IMAGE_KEY_RECT_HEIGHT];
					if (Bottom)			m_OriginalHeight = [Bottom floatValue] - m_Top;
					else if (Height)	m_OriginalHeight = [Height floatValue];
				} else {
					//Get height
					NSNumber* Bottom = [RectDictionary objectForKey:IMAGE_KEY_RECT_BOTTOM];
					NSNumber* Height = [RectDictionary objectForKey:IMAGE_KEY_RECT_HEIGHT];
					if (Bottom) m_Top = [Bottom floatValue] - m_OriginalHeight;
					if (Height) m_OriginalHeight = [Height floatValue];
				}
			}
			
			//Scale
			m_Width		= m_OriginalWidth * [[ARKUtilities instance] getScale];
			m_Height	= m_OriginalHeight * [[ARKUtilities instance] getScale];
			m_PivotY	= m_Height / 2;
			m_PivotX	= m_Width / 2;
			
			//Create drawing rect
			[self setRegionFromX:0 fromY:0 withWidth:m_OriginalWidth withHeight:m_OriginalHeight];
			[self configureColors];
		}
	}
	
	//Return
	return self;
}

- (void)setTintWithRedF:(float)red withGreenF:(float)green withBlueF:(float)blue withAlphaF:(float)alpha {
	//Super
	[super setTintWithRedF:red withGreenF:green withBlueF:blue withAlphaF:alpha];
	
	//Manage attribute
	[self configureColors];
}

- (void)setRegionFromX:(float)x fromY:(float)y withWidth:(float)width withHeight:(float)height {
	//Set region not forced
	[self setRegionFromX:x fromY:y withWidth:width withHeight:height forced:NO];
}

- (void)setRegionFromX:(float)x fromY:(float)y withWidth:(float)width withHeight:(float)height forced:(BOOL)force {
	//Initialize
	BOOL Valid		= force;
	float OldX		= m_OriginalRegionX;
	float OldY		= m_OriginalRegionY;
	float OldWidth	= m_OriginalRegionWidth;
	float OldHeight	= m_OriginalRegionHeight;
	
	//Super
	[super setRegionFromX:x fromY:y withWidth:width withHeight:height];
	
	//If not valid
	if (!Valid) {
		//Valid if there's a difference
		if (m_OriginalRegionX != OldX) 					Valid = YES;
		else if (m_OriginalRegionY != OldY) 			Valid = YES;
		else if (m_OriginalRegionWidth != OldWidth) 	Valid = YES;
		else if (m_OriginalRegionHeight != OldHeight) 	Valid = YES;
	}
	
	//If valid
	if (Valid) {
		//Configure vertex attributes
		[self configureVertices];
		[self configureCoordinates];
	}
}

- (void)configureVertices {
	//Set vertex attributes
	m_Attributes[0 * IMAGE_DATA_SIZE] = -(m_Width / 2) + m_RegionX + m_RegionWidth;	m_Attributes[(0 * IMAGE_DATA_SIZE) + 1] = (m_Height / 2) - m_RegionY;
	m_Attributes[1 * IMAGE_DATA_SIZE] = -(m_Width / 2) + m_RegionX;					m_Attributes[(1 * IMAGE_DATA_SIZE) + 1] = (m_Height / 2) - m_RegionY;
	m_Attributes[2 * IMAGE_DATA_SIZE] = -(m_Width / 2) + m_RegionX + m_RegionWidth;	m_Attributes[(2 * IMAGE_DATA_SIZE) + 1] = (m_Height / 2) - m_RegionY - m_RegionHeight;
	m_Attributes[3 * IMAGE_DATA_SIZE] = -(m_Width / 2) + m_RegionX;					m_Attributes[(3 * IMAGE_DATA_SIZE) + 1] = (m_Height / 2) - m_RegionY - m_RegionHeight;
}

- (void)configureCoordinates {
	//Initialize
	NSArray* Edges		= [self getEdges];
	const int* Template = [self getTemplateFromMirror:m_Mirror];
	
	//For each data
	int Offset = IMAGE_VERTEX_SIZE;
	for (int i = 0; i < IMAGE_VERTEX_COUNT; i++) {
		//Set attribute
		for (int j = 0; j < 2; j++) m_Attributes[(i * IMAGE_DATA_SIZE) + Offset + j] = [(NSNumber*)[Edges objectAtIndex:Template[(i * 2) + j]] floatValue];
	}
}

- (NSArray*)getEdges {
	//Calculate
	NSArray* Edges = [NSArray arrayWithObjects:
					  [NSNumber numberWithFloat:(m_Top + m_OriginalRegionY) / [m_Texture getHeight]],
					  [NSNumber numberWithFloat:(m_Left + m_OriginalRegionX) / [m_Texture getWidth]],
					  [NSNumber numberWithFloat:(m_Top + m_OriginalRegionY + m_OriginalRegionHeight) / [m_Texture getHeight]],
					  [NSNumber numberWithFloat:(m_Left + m_OriginalRegionX + m_OriginalRegionWidth) / [m_Texture getWidth]],
					  nil];
	
	//Return
	return Edges;
}

- (const int*)getTemplateFromMirror:(int)mirror {
	//Get the correct pattern
	if (mirror == DRAWABLE_MIRROR_VERTICAL)			return IMAGE_COORDINATES_VMIRROR;
	else if (mirror == DRAWABLE_MIRROR_HORIZONTAL) 	return IMAGE_COORDINATES_HMIRROR;
	else if (mirror == DRAWABLE_MIRROR_BOTH)		return IMAGE_COORDINATES_BOTHMIRROR;
	else											return IMAGE_COORDINATES_NORMAL;
}

- (void)configureColors {
	//For each vertex
	int Offset = IMAGE_VERTEX_SIZE + IMAGE_COORDINATE_SIZE;
	for (int i = 0; i < IMAGE_VERTEX_COUNT; i++) {
		//Set color
		m_Attributes[(i * IMAGE_DATA_SIZE) + Offset + 0] = m_ColorRed;
		m_Attributes[(i * IMAGE_DATA_SIZE) + Offset + 1] = m_ColorGreen;
		m_Attributes[(i * IMAGE_DATA_SIZE) + Offset + 2] = m_ColorBlue;
		m_Attributes[(i * IMAGE_DATA_SIZE) + Offset + 3] = m_ColorAlpha;
	}
}

- (void)drawWithGL:(GLKBaseEffect*)gl {
	//Skip if no gl
	if (!gl) return;
	
	//Set vertex attributes
	int Size = IMAGE_VERTEX_COUNT * IMAGE_DATA_SIZE;
	glVertexAttribPointer(GLKVertexAttribPosition, IMAGE_VERTEX_SIZE, GL_FLOAT, GL_FALSE, Size, &m_Attributes);
	glVertexAttribPointer(GLKVertexAttribColor, IMAGE_COLOR_SIZE, GL_FLOAT, GL_FALSE, Size, &(m_Attributes[IMAGE_VERTEX_SIZE + IMAGE_COORDINATE_SIZE]));
	glVertexAttribPointer(GLKVertexAttribTexCoord0, IMAGE_COORDINATE_SIZE, GL_FLOAT, GL_FALSE, Size, &(m_Attributes[IMAGE_VERTEX_SIZE]));
	
    //Set texture
	if (m_Texture) gl.texture2d0.name = [m_Texture getID];
	
	//Calculate stuff
	float PivotX		= (m_Width / 2) - m_PivotX;
	float PivotY		= m_PivotY - (m_Height / 2);
	float TranslationX	= ((m_Width - [[ARKDevice instance] getWidth]) / 2) + m_X - PivotX;
	float TranslationY	= (([[ARKDevice instance] getHeight] - m_Height) / 2) - m_Y - PivotY;
	
	//Create matrix
    GLKMatrix4 ViewMatrix	= [[ARKiOSDevice instance] getViewMatrix];
	ViewMatrix				= GLKMatrix4Translate(ViewMatrix, TranslationX, TranslationY, 0);
	ViewMatrix				= GLKMatrix4Rotate(ViewMatrix, m_Rotation, 0, 0, -1);
	ViewMatrix				= GLKMatrix4Translate(ViewMatrix, PivotX, PivotY, 0);
	ViewMatrix				= GLKMatrix4Rotate(ViewMatrix, m_Flip, -1, 0, 0);
    gl.transform.modelviewMatrix = ViewMatrix;

	//Render
    [gl prepareToDraw];
    glDrawArrays(GL_TRIANGLE_STRIP, 0, IMAGE_VERTEX_COUNT);
}

@end
