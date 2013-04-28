//
//  Wormhole.m
//  ARK Framework Example
//
//  Created by LegACy on 4/28/13.
//  Copyright (c) 2013 Raka Mahesa. All rights reserved.
//

#import "Wormhole.h"
#import "HoleUtilities.h"
#import <math.h>

//Constants
const int HOLE_BLACKHOLE 		= 1;
const int HOLE_WHITEHOLE 		= 2;
const float HOLE_MIN_DISTANCE 	= 250;
const float HOLE_FORCE		 	= 75000;

@implementation Wormhole

//Synthesize
@synthesize type = m_Type;

- (id)init { return nil; }
- (id)initWithType:(int)type atX:(float)x atY:(float)y {
	//Super
	self = [super init];
	if (self) {
		//Create
		m_Type 		= type;
		m_Duration	= 3000;
		m_Image		= [ARKImage createFromPath:[UTIL_GAME_IMAGES stringByAppendingString:(type == HOLE_BLACKHOLE ? @"blackhole.json" : @"whitehole.json")]];
		
		//Calculate width
		m_Width				= [m_Image getWidth];
		m_Height			= [m_Image getHeight];
		m_OriginalWidth 	= [m_Image getOriginalWidth];
		m_OriginalHeight 	= [m_Image getOriginalHeight];
		
		//Set position
		[self setPositionAtX:x atY:y horizontallyAlignedTo:DRAWABLE_ANCHOR_HCENTER verticallyAlignedTo:DRAWABLE_ANCHOR_VCENTER];
	}
	
	//Return
	return self;
}

- (void)setPositionAtX:(float)x atY:(float)y horizontallyAlignedTo:(int)horizontal verticallyAlignedTo:(int)vertical {
	//Set position
	[super setPositionAtX:x atY:y horizontallyAlignedTo:horizontal verticallyAlignedTo:vertical];
	[m_Image setPositionAtX:x atY:y horizontallyAlignedTo:horizontal verticallyAlignedTo:vertical];
}

- (BOOL)isAlive {
	//Has duration?
	return m_Duration > 0;
}

- (float)calculateXForceAtX:(float)x atY:(float)y {
	//Initialize
	float Result = 0;
	
	//Get distance
	float CenterX 		= (m_X / [[ARKUtilities instance] getScale]) + (m_OriginalWidth / 2);
	float CenterY 		= (m_Y /  [[ARKUtilities instance] getScale]) + (m_OriginalHeight / 2);
	float DistanceX		= CenterX - x;
	float DistanceY		= CenterY - y;
	float DistanceSq	= (DistanceX * DistanceX) + (DistanceY * DistanceY);
	
	//Get distance
	float Distance 	= sqrtf(DistanceSq);
	float Power		= HOLE_FORCE / DistanceSq;
	if (Power > 200) Power = 200;
	Result			= DistanceX / Distance * Power;
	
	//Return
	return Result;
}

- (float)calculateYForceAtX:(float)x atY:(float)y {
	//Initialize
	float Result = 0;
	
	//Get distance
	float CenterX 		= (m_X / [[ARKUtilities instance] getScale]) + (m_OriginalWidth / 2);
	float CenterY 		= (m_Y /  [[ARKUtilities instance] getScale]) + (m_OriginalHeight / 2);
	float DistanceX		= CenterX - x;
	float DistanceY		= CenterY - y;
	float DistanceSq	= (DistanceX * DistanceX) + (DistanceY * DistanceY);
	
	//Get distance
	float Distance 	= sqrtf(DistanceSq);
	float Power		= HOLE_FORCE / DistanceSq;
	if (Power > 200) Power = 200;
	Result			= DistanceY / Distance * Power;
	
	//Return
	return Result;
}

- (void)updateWithDeltaTime:(long)time {
	//Update duration
	m_Duration -= time;
}

- (void)drawWithGL:(GLKBaseEffect *)gl {
	//Draw
	if (m_Duration > 1200 || (m_Duration < 1000 && m_Duration > 600) || m_Duration < 400) [m_Image drawWithGL:gl];
}

@end
