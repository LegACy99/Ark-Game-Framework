//
//  ARKBitmapChar.h
//  Ark Framework
//
//  Created by LegACy on 4/15/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ARKBitmapChar : NSObject {
	//Data
	float			m_Advance;
	NSArray*		m_Vertices;
	NSArray*		m_Coordinates;
	NSDictionary*	m_Kerning;
}

//Properties
@property (readonly, getter = getAdvance)				float advance;
@property (readonly, getter = getVertices)				NSArray* vertices;
@property (readonly, getter = getTextureCoordinates)	NSArray* coordinates;

//Constructor
- (id)initWithJSON:(NSDictionary*)json withKernings:(NSDictionary*)kernings withWidth:(float)width withHeight:(float)height;

//Getter
- (float)getBottom;
- (float)getHeight;
- (float)getWidth;
- (float)getTop;

- (float)getAdvanceForChar:(char)character;

@end
