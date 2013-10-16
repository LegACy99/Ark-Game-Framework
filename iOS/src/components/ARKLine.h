//
//  ARKLine.h
//  ARK Framework
//
//  Created by LegACy on 10/16/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import <GLKit/GLKit.h>
#import "ARKDrawable.h"

@interface ARKLine : ARKDrawable {
	//Data
	float		m_EndX;
	float		m_EndY;
	float		m_StartX;
	float		m_StartY;
	float		m_OffsetX;
	float		m_OffsetY;
	float		m_Thickness;
	GLfloat		m_Vertices[4];
	GLKVector4	m_Color;
}

//Constructors
- (id)init;
- (id)initStartingAtX:(float)startX atY:(float)startY toX:(float)endX toY:(float)endY;

//Setters
- (void)setThickness:(float)thickness;
- (void)setEndAtX:(float)x atY:(float)y;
- (void)setStartAtX:(float)x atY:(float)y;
- (void)setColorWithRed:(float)red withGreen:(float)green withBlue:(float)blue withAlpha:(float)alpha;

@end
