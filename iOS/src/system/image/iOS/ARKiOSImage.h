//
//  ARKiOSImage.h
//  Ark Framework
//
//  Created by LegACy on 4/6/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import <GLKit/GLKit.h>
#import "ARKImage.h"

//Forward declaration
@class ARKTexture;

//Class declaration
@interface ARKiOSImage : ARKImage {
	//Variables
	float		m_Top;
	float		m_Left;
	float		m_FlipRadian;
	float		m_RotationRadian;
	GLfloat		m_Attributes[32];
	ARKTexture*	m_Texture;
}

//Constructors
- (id)initFromFile:(NSString *)file atX:(float)x atY:(float)y;
- (id)initFromJSON:(NSDictionary*)json atX:(float)x atY:(float)y;

@end
