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
	float			m_Vertices[8];
	float			m_Coordinates[8];
	NSDictionary*	m_Kerning;
}

//Properties
@property (readonly, getter = getAdvance) float advance;

//Constructor
- (id)initWithJSON:(NSDictionary*)json withKernings:(NSDictionary*)kernings withWidth:(float)width withHeight:(float)height;

//Getter
- (float*)getVertices;
- (float*)getTextureCoordinates;
- (float)getBottom;
- (float)getHeight;
- (float)getWidth;
- (float)getTop;

- (float)getAdvanceForChar:(char)character;

@end
