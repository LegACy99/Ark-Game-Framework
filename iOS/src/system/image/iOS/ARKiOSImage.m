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

//Sizes
const int COLOR_SIZE 		= 4;
const int VERTEX_SIZE 		= 2;
const int COORDINATE_SIZE 	= 2;

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

const GLfloat DATA[54] = {
    -64, -128, 0,		0, 0,	1, 1, 1, 0.5f,
    -64, 128, 0,	0, 2,		1, 1, 1, 0.5f,
    0, -128, 0,		0.5f, 0,	1, 1, 1, 0.5f,
    0, -128, 0,		0.5f, 0,	1, 1, 1, 0.5f,
    -64, 128, 0,	0, 2,		1, 1, 1, 0.5f,
    0, 128, 0,		0.5f, 2,		1, 1, 1, 0.5f,
};

@implementation ARKiOSImage

- (id)init {
	//Do not use
	return nil;
}

- (id)initFromFile:(NSString *)file atX:(float)x atY:(float)y {
	//Init
	self = [super initFromFile:file atX:x atY:y];
	if (self) {
		//Create texture
		NSString* File			= [[NSBundle mainBundle] pathForResource:file ofType:@"png"];
		NSDictionary* Options	= [NSDictionary dictionaryWithObject:[NSNumber numberWithBool:YES] forKey:GLKTextureLoaderOriginBottomLeft];
		m_Texture				= [GLKTextureLoader textureWithContentsOfFile:File options:Options error:nil];
		
		//Set array
		glGenVertexArraysOES(1, &m_Array);
		glBindVertexArrayOES(m_Array);
		
		//Set buffer
		glGenBuffers(1, &m_Buffer);
		glBindBuffer(GL_ARRAY_BUFFER, m_Buffer);
		glBufferData(GL_ARRAY_BUFFER, sizeof(DATA), DATA, GL_STATIC_DRAW);
		
		//Set attributes
		glEnableVertexAttribArray(GLKVertexAttribColor);
		glEnableVertexAttribArray(GLKVertexAttribPosition);
		glEnableVertexAttribArray(GLKVertexAttribTexCoord0);
		
		//Segment vertext data
		glVertexAttribPointer(GLKVertexAttribColor, 4, GL_FLOAT, GL_FALSE, 36, (void*)20);
		glVertexAttribPointer(GLKVertexAttribPosition, 3, GL_FLOAT, GL_FALSE, 36, (void*)0);
		glVertexAttribPointer(GLKVertexAttribTexCoord0, 3, GL_FLOAT, GL_FALSE, 36, (void*)12);
		
		//Release
		glBindVertexArrayOES(0);
	}
	
	//Return
	return self;
}

- (id)initFromJSON:(id)json atX:(float)x atY:(float)y {
	//Do nothing
	return nil;
}

- (void)drawWithEffect:(GLKBaseEffect*)effect {
	//Bind vertext array
    glBindVertexArrayOES(m_Array);
	
	//Create matrix
    GLKMatrix4 ViewMatrix = GLKMatrix4MakeLookAt(0, 0, 240, 0, 0, 0, 0, 1, 0);
    effect.transform.modelviewMatrix = ViewMatrix;
    
    //Set texture
	if (m_Texture) effect.texture2d0.name = m_Texture.name;
	
	//Render
    [effect prepareToDraw];
    glDrawArrays(GL_TRIANGLES, 0, 6);
}

@end
