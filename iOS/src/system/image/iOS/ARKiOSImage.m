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
#import "ARKTexture.h"

//Sizes
const int COLOR_SIZE 		= 4;
const int VERTEX_SIZE 		= 2;
const int COORDINATE_SIZE 	= 2;
const int DATA_SIZE			= VERTEX_SIZE + COORDINATE_SIZE + COLOR_SIZE;

//Edge constants
const int EDGE_TOP		= 0;
const int EDGE_LEFT 	= 1;
const int EDGE_RIGHT 	= 3;
const int EDGE_BOTTOM 	= 2;

//Index constants: 0 top, 1 left, 2 bottom, 3 right
const int COORDINATES_NORMAL[8]		=  { EDGE_RIGHT, EDGE_TOP,		EDGE_LEFT, EDGE_TOP,		EDGE_RIGHT, EDGE_BOTTOM,	EDGE_LEFT, EDGE_BOTTOM	};
const int COORDINATES_HMIRROR[8] 	=  { EDGE_LEFT, EDGE_TOP,		EDGE_RIGHT, EDGE_TOP,		EDGE_LEFT, EDGE_BOTTOM,		EDGE_RIGHT, EDGE_BOTTOM	};
const int COORDINATES_VMIRROR[8] 	=  { EDGE_RIGHT, EDGE_BOTTOM,	EDGE_LEFT, EDGE_BOTTOM,		EDGE_LEFT, EDGE_TOP,		EDGE_RIGHT, EDGE_TOP 	};
const int COORDINATES_BOTHMIRROR[8]	=  { EDGE_LEFT, EDGE_BOTTOM,	EDGE_RIGHT, EDGE_BOTTOM,	EDGE_RIGHT, EDGE_TOP,		EDGE_LEFT, EDGE_TOP		};

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
		for (int i = 0; i < sizeof(m_Attributes); i++) m_Attributes[i] = 0;
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
					m_Left = [Top floatValue];
					
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
			m_Width		= m_OriginalWidth;// * Utilities.instance().getScale();
			m_Height	= m_OriginalHeight;// * Utilities.instance().getScale();
			m_PivotY	= m_Height / 2;
			m_PivotX	= m_Width / 2;

			
			//Create drawing rect
			[self setRegionfromX:0 fromY:0 withWidth:m_OriginalWidth withHeight:m_OriginalHeight];
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

- (void)setRegionfromX:(float)x fromY:(float)y withWidth:(float)width withHeight:(float)height {
	//Set region not forced
	[self setRegionfromX:x fromY:y withWidth:width withHeight:height forced:NO];
}

- (void)setRegionfromX:(float)x fromY:(float)y withWidth:(float)width withHeight:(float)height forced:(BOOL)force {
	//Initialize
	BOOL Valid		= force;
	float OldX		= m_OriginalRegionX;
	float OldY		= m_OriginalRegionY;
	float OldWidth	= m_OriginalRegionWidth;
	float OldHeight	= m_OriginalRegionHeight;
	
	//Super
	[super setRegionfromX:x fromY:y withWidth:width withHeight:height];
	
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
	m_Attributes[0 * DATA_SIZE] = -(m_Width / 2) + m_RegionX + m_RegionWidth;	m_Attributes[(0 * DATA_SIZE) + 1] = (m_Height / 2) - m_RegionY;
	m_Attributes[1 * DATA_SIZE] = -(m_Width / 2) + m_RegionX;					m_Attributes[(1 * DATA_SIZE) + 1] = (m_Height / 2) - m_RegionY;
	m_Attributes[2 * DATA_SIZE] = -(m_Width / 2) + m_RegionX + m_RegionWidth;	m_Attributes[(2 * DATA_SIZE) + 1] = (m_Height / 2) - m_RegionY - m_RegionHeight;
	m_Attributes[3 * DATA_SIZE] = -(m_Width / 2) + m_RegionX;					m_Attributes[(3 * DATA_SIZE) + 1] = (m_Height / 2) - m_RegionY - m_RegionHeight;
}

- (void)configureCoordinates {
	//Initialize
	NSArray* Edges		= [self getEdges];
	const int* Template = [self getTemplateFromMirror:m_Mirror];
	
	//For each data
	int Offset = VERTEX_SIZE;
	for (int i = 0; i < 4; i++) {
		//Set attribute
		for (int j = 0; j < 2; j++) m_Attributes[(i * DATA_SIZE) + Offset + j] = [(NSNumber*)[Edges objectAtIndex:Template[(i * 2) + j]] floatValue];
	}
}

- (NSArray*)getEdges {
	//Calculate
	NSArray* Edges = [NSArray arrayWithObjects:
					  [NSNumber numberWithFloat:(m_Top + m_OriginalRegionY) / 128],//m_Texture.getHeight()],
					  [NSNumber numberWithFloat:(m_Left + m_OriginalRegionX) / 128],//m_Texture.getWidth()],
					  [NSNumber numberWithFloat:(m_Left + m_OriginalRegionX + m_OriginalRegionWidth) / 128],//m_Texture.getWidth()],
					  [NSNumber numberWithFloat:(m_Top + m_OriginalRegionY + m_OriginalRegionHeight) / 128],//m_Texture.getHeight()],
					  nil];
	
	//Return
	return Edges;
}

- (const int*)getTemplateFromMirror:(int)mirror {
	//Get the correct pattern
	if (mirror == DRAWABLE_MIRROR_VERTICAL)			return COORDINATES_VMIRROR;
	else if (mirror == DRAWABLE_MIRROR_HORIZONTAL) 	return COORDINATES_HMIRROR;
	else if (mirror == DRAWABLE_MIRROR_BOTH)		return COORDINATES_BOTHMIRROR;
	else											return COORDINATES_NORMAL;
}

- (void)configureColors {
	//For each vertex
	int Offset = VERTEX_SIZE + COORDINATE_SIZE;
	for (int i = 0; i < 4; i++) {
		//Set color
		m_Attributes[(i * DATA_SIZE) + Offset + 0] = m_ColorRed;
		m_Attributes[(i * DATA_SIZE) + Offset + 1] = m_ColorGreen;
		m_Attributes[(i * DATA_SIZE) + Offset + 2] = m_ColorBlue;
		m_Attributes[(i * DATA_SIZE) + Offset + 3] = m_ColorAlpha;
	}
}

- (void)drawWithGL:(GLKBaseEffect*)gl {
	//Set vertex attributes
	glVertexAttribPointer(GLKVertexAttribPosition, VERTEX_SIZE, GL_FLOAT, GL_FALSE, DATA_SIZE * 4, &m_Attributes);
	glVertexAttribPointer(GLKVertexAttribColor, COLOR_SIZE, GL_FLOAT, GL_FALSE, DATA_SIZE * 4, &(m_Attributes[VERTEX_SIZE + COORDINATE_SIZE]));
	glVertexAttribPointer(GLKVertexAttribTexCoord0, COORDINATE_SIZE, GL_FLOAT, GL_FALSE, DATA_SIZE * 4, &(m_Attributes[VERTEX_SIZE]));
	
    //Set texture
	if (m_Texture) gl.texture2d0.name = [m_Texture getID];
	
	//Create matrix
    GLKMatrix4 ViewMatrix = GLKMatrix4MakeLookAt(0, 0, 240, 0, 0, 0, 0, 1, 0);
    gl.transform.modelviewMatrix = ViewMatrix;
	
	//Render
    [gl prepareToDraw];
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
}

@end
