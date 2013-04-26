//
//  ARKRectangle.m
//  Ark Framework
//
//  Created by LegACy on 4/26/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKRectangle.h"
#import "ARKUtilities.h"
#import "ARKiOSRectangle.h"

//Constants
const int RECTANGLE_INDEX_RED 	= 0;
const int RECTANGLE_INDEX_BLUE 	= 2;
const int RECTANGLE_INDEX_GREEN = 1;
const int RECTANGLE_INDEX_ALPHA = 3;

@implementation ARKRectangle

- (id)init {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		m_Flip		= 0;
		m_Rotation	= 0;
		for (int i = 0; i < sizeof(m_Color) / sizeof(m_Color[0]); i++) m_Color[i] = 0;
	}
	
	//Return
	return self;
}

+ (ARKRectangle*)createWithWidth:(float)width withHeight:(float)height { return [ARKRectangle createFromX:0 fromY:0 withWidth:width withHeight:height]; }
+ (ARKRectangle*)createFromX:(float)x fromY:(float)y withWidth:(float)width withHeight:(float)height {
	return [ARKRectangle createFromX:x fromY:y withWidth:width withHeight:height withRed:1 withGreen:1 withBlue:1 withAlpha:1];
}

+ (ARKRectangle*)createFromX:(float)x fromY:(float)y withWidth:(float)width withHeight:(float)height withRed:(float)red withGreen:(float)green withBlue:(float)blue withAlpha:(float)alpha {
	//Create rectangle
	return [[ARKiOSRectangle alloc] initFromX:x fromY:y withWidth:width withHeight:height withRed:red withGreen:green withBlue:blue withAlpha:alpha];
}

- (float)getRedComponent	{ return m_Color[RECTANGLE_INDEX_RED];		}
- (float)getBlueComponent	{ return m_Color[RECTANGLE_INDEX_BLUE];		}
- (float)getGreenComponent	{ return m_Color[RECTANGLE_INDEX_GREEN];	}
- (float)getAlphaComponent	{ return m_Color[RECTANGLE_INDEX_ALPHA];	}

- (void)setRectWithWidth:(float)width withHeight:(float)height {
	//Set size
	m_OriginalWidth		= width;
	m_OriginalHeight	= height;
	m_Height			= m_OriginalHeight * [[ARKUtilities instance] getScale];
	m_Width				= m_OriginalWidth * [[ARKUtilities instance] getScale];
}

- (void)setColorWithRed:(float)red withGreen:(float)green withBlue:(float)blue withAlpha:(float)alpha {
	//Set color
	m_Color[RECTANGLE_INDEX_RED]	= red;
	m_Color[RECTANGLE_INDEX_BLUE]	= blue;
	m_Color[RECTANGLE_INDEX_GREEN]	= green;
	m_Color[RECTANGLE_INDEX_ALPHA]	= alpha;
}

@end
