//
//  ARKiOSImage.h
//  Ark Framework
//
//  Created by LegACy on 4/6/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKImage.h"

//Forward declaration
@class GLKTextureInfo;

//Class declaration
@interface ARKiOSImage : ARKImage {
	//Variables
	GLKTextureInfo*	m_Texture;
	GLfloat			m_Attributes[48];
}

//
- (id)initFromFile:(NSString *)file atX:(float)x atY:(float)y;
- (id)initFromJSON:(id)json atX:(float)x atY:(float)y;

@end
