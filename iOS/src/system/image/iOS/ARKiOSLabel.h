//
//  ARKiOSLabel.h
//  Ark Framework
//
//  Created by LegACy on 4/17/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKLabel.h"

@class ARKBitmapFont, ARKTexture;

@interface ARKiOSLabel : ARKLabel {
	//Data
	GLfloat*		m_Buffer;
	GLshort*		m_Indices;
	ARKTexture*		m_Texture;
	ARKBitmapFont*	m_Font;
}

- (id)initWithText:(NSString *)text withFont:(NSString*)font;
- (id)initWithText:(NSString *)text withFont:(NSString*)font atX:(float)x atY:(float)y;

@end
