//
//  Drawable.m
//  Ark Framework
//
//  Created by LegACy on 4/6/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import <GLKit/GLKit.h>
#import "ARKDrawable.h"
#import "ARKDevice.h"

//Anchor Constants
const int DRAWABLE_ANCHOR_LEFT		= 0;
const int DRAWABLE_ANCHOR_HCENTER	= 1;
const int DRAWABLE_ANCHOR_RIGHT		= 2;
const int DRAWABLE_ANCHOR_TOP		= 0;
const int DRAWABLE_ANCHOR_VCENTER	= 1;
const int DRAWABLE_ANCHOR_BOTTOM	= 2;

//Mirror Constants
const int DRAWABLE_MIRROR_NONE			= 0;
const int DRAWABLE_MIRROR_HORIZONTAL	= 1;
const int DRAWABLE_MIRROR_VERTICAL		= 2;
const int DRAWABLE_MIRROR_BOTH			= 3;

//Implementation
@implementation ARKDrawable

//Synthesize
@synthesize x				= m_X;
@synthesize y				= m_Y;
@synthesize width			= m_Width;
@synthesize height			= m_Height;
@synthesize originalWidth	= m_OriginalWidth;
@synthesize originalHeight	= m_OriginalHeight;

- (id)init {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		m_X					= 0;
		m_Y					= 0;
		m_Width				= 0;
		m_Height			= 0;
		m_OriginalWidth		= 0;
		m_OriginalHeight	= 0;
	}
	
	//Return
	return self;
}

- (void)setPositionAtX:(float)x atY:(float)y {
	//Set position
	[self setPositionAtX:x atY:y horizontallyAlignedTo:DRAWABLE_ANCHOR_LEFT verticallyAlignedTo:DRAWABLE_ANCHOR_TOP];
}

- (void)setPositionAtX:(float)x atY:(float)y horizontallyAlignedTo:(int)horizontal {
	//Set position
	[self setPositionAtX:x atY:y horizontallyAlignedTo:horizontal verticallyAlignedTo:DRAWABLE_ANCHOR_TOP];
}

- (void)setPositionAtX:(float)x atY:(float)y horizontallyAlignedTo:(int)horizontal verticallyAlignedTo:(int)vertical {
	//Initialize
	m_X = x * [[ARKDevice instance] getScale];
	m_Y = y * [[ARKDevice instance] getScale];
	
	//Set X
	if (horizontal == DRAWABLE_ANCHOR_RIGHT)		m_X -= m_Width;
	else if (horizontal == DRAWABLE_ANCHOR_HCENTER)	m_X -= (m_Width / 2);
	
	//Set Y
	if (vertical == DRAWABLE_ANCHOR_BOTTOM)			m_Y -= m_Height;
	else if (vertical == DRAWABLE_ANCHOR_VCENTER)	m_Y -= (m_Height / 2);
}

- (void)drawWithGL:(GLKBaseEffect*)gl {
	//Invalid
	[self doesNotRecognizeSelector:_cmd];
}

@end