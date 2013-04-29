//
//  Bullet.m
//  ARK Framework Example
//
//  Created by LegACy on 4/28/13.
//  Copyright (c) 2013 Raka Mahesa. All rights reserved.
//

#import "Bullet.h"
#import "HoleUtilities.h"
#import "Wormhole.h"

//Constants
const int BULLET_TRAIL_MAX 	= 10;
const long BULLET_TRAIL_GAP = 100;

@interface Bullet ()

//Private function
- (void)calculateAngle;

@end

@implementation Bullet

//Synthesize properties
@synthesize missX	= m_MissX;
@synthesize missY	= m_MissY;
@synthesize labelX	= m_LabelX;
@synthesize labelY	= m_LabelY;

- (id)init { return nil; }
- (id)initAtX:(float)x atY:(float)y withXVelocity:(float)velocityX withYVelocity:(float)velocityY {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		m_MissX		= -1;
		m_MissY		= -1;
		m_MissTime	= 0;
		m_NearMiss	= false;
		m_VelocityX	= velocityX;
		m_VelocityY	= velocityY;
		m_TrailTime	= BULLET_TRAIL_GAP;
		m_Trails	= [NSMutableArray array];
		m_Image		= [ARKImage createFromPath:[UTIL_GAME_IMAGES stringByAppendingString:@"bullet.json"]];
		
		//Calculate width
		m_Width				= [m_Image getWidth];
		m_Height			= [m_Image getHeight];
		m_OriginalWidth 	= [m_Image getOriginalWidth];
		m_OriginalHeight 	= [m_Image getOriginalHeight];
		
		//Set position
		[self setPositionAtX:x atY:y horizontallyAlignedTo:DRAWABLE_ANCHOR_HCENTER verticallyAlignedTo:DRAWABLE_ANCHOR_VCENTER];
		[self calculateAngle];
	}
	
	//Return
	return self;
}

- (void)setPositionAtX:(float)x atY:(float)y horizontallyAlignedTo:(int)horizontal verticallyAlignedTo:(int)vertical {
	//Super
	[super setPositionAtX:x atY:y horizontallyAlignedTo:horizontal verticallyAlignedTo:vertical];
	[m_Image setPositionAtX:x atY:y horizontallyAlignedTo:horizontal verticallyAlignedTo:vertical];
}

- (void)calculateAngle {
	//Calculate
	float Angle = [[ARKUtilities instance] getDegreeFromRadian:atan2f(m_VelocityY, m_VelocityX)];
	[m_Image setRotationWithAngle:Angle];
}

- (BOOL)isDead {
	//Calculate
	float X = m_X / [[ARKUtilities instance] getScale];
	float Y = m_Y / [[ARKUtilities instance] getScale];
	
	//Check border
	if (X < -20)											return YES;
	else if (Y < -20)										return YES;
	else if (X > [[ARKUtilities instance] getWidth]+ 20) 	return YES;
	else if (Y > [[ARKUtilities instance] getHeight] + 20)	return YES;
	
	//Alive
	return NO;
}

- (BOOL)isNearMiss {
	//Get near miss
	BOOL NearMiss 	= m_NearMiss;
	m_NearMiss 		= NO;
	
	//Return
	return NearMiss;
}

- (BOOL)doesCollideWithRectFromX:(float)x fromY:(float)y withWidth:(float)width withHeight:(float)height {
	//Check
	if (m_X > x + width) 	return NO;
	if (m_Y > y + height) 	return NO;
	if (m_X + m_Width < x) 	return NO;
	if (m_Y + m_Height < y) return NO;
	
	//Passed the check
	return YES;
}

- (void)updateForceByWormholes:(NSArray *)holes {
	//Skip if no holes
	if (!holes) return;
	
	//For each hole
	for (int i = 0; i < [holes count]; i++) {
		//Calculate force
		float X 		= (m_X / [[ARKUtilities instance] getScale]) + (m_OriginalWidth / 2);
		float Y 		= (m_Y / [[ARKUtilities instance] getScale]) + (m_OriginalHeight / 2);
		float XForce 	= [[holes objectAtIndex:i] calculateXForceAtX:X atY:Y];
		float YForce	= [[holes objectAtIndex:i] calculateYForceAtX:X atY:Y];
		
		//Add
		m_VelocityX += XForce;
		m_VelocityY += YForce;
		[self calculateAngle];
	}
}

- (void)calculateLabelPositionFromOldX:(float)oldX fromOldY:(float)oldY fromX:(float)x fromY:(float)y {
	//Check
	if (oldX > 0 && x < 0) {
		//Set
		m_LabelX = 20;
		m_LabelY = y;
	} else if (oldX < [[ARKUtilities instance] getWidth] && x > [[ARKUtilities instance] getWidth]) {
		//Set
		m_LabelX = [[ARKUtilities instance] getWidth] - 30;
		m_LabelY = y;
	} else if (oldY > 0 && y < 0) {
		//Set
		m_LabelX = x;
		m_LabelY = 50;
	} else if (oldY < [[ARKUtilities instance] getHeight] && y > [[ARKUtilities instance] getHeight]) {
		//Set
		m_LabelX = x;
		m_LabelY = [[ARKUtilities instance] getHeight];
	}
	
	//Correct
	if (m_LabelX < 30)												m_LabelX = 30;
	else if (m_LabelX > [[ARKUtilities instance] getWidth] - 30) 	m_LabelX = [[ARKUtilities instance] getWidth] - 30;
	if (m_LabelY < 50)												m_LabelY = 50;
}

- (void)updateWithDeltaTime:(long)time {
	//Calculate position
	float OldX	= m_X / [[ARKUtilities instance] getScale];
	float OldY	= m_Y / [[ARKUtilities instance] getScale];
	float X 	= OldX + (m_VelocityX * (float)time / 1000);
	float Y 	= OldY + ((m_VelocityY) * (float)time / 1000);
	[self calculateLabelPositionFromOldX:OldX fromOldY:OldY fromX:X fromY:Y];
	[self setPositionAtX:X atY:Y];
	
	//Check timer
	if (m_MissTime > 0) {
		//Manage
		m_MissTime -= time;
		if (m_MissTime <= 0) m_NearMiss = true;
	} else {
		//If no nearmiss yet
		if (m_MissX < 0 && m_MissY < 0) {
			//Check distance
			float DistanceX = X - ([[ARKUtilities instance] getWidth] / 2);
			float DistanceY = Y - ([[ARKUtilities instance] getHeight] / 2);
			if ((DistanceX * DistanceX) + (DistanceY * DistanceY) < 80 * 80) {
				//Get near miss
				m_MissX = X;
				m_MissY = Y;
				m_MissTime = 500;
			}
		}
	}
	
	//Manage trail
	m_TrailTime -= time;
	if (m_TrailTime <= 0) {
		//Create image
		ARKImage* Trail = [ARKImage createFromPath:[UTIL_GAME_IMAGES stringByAppendingString:@"trail.json"]];
		[Trail setPositionAtX:X + (m_OriginalWidth / 2) atY:Y + (m_OriginalHeight / 2) horizontallyAlignedTo:DRAWABLE_ANCHOR_HCENTER verticallyAlignedTo:DRAWABLE_ANCHOR_VCENTER];
		
		//Add trail
		[m_Trails addObject:Trail];
		if ([m_Trails count] > BULLET_TRAIL_MAX) [m_Trails removeObjectAtIndex:0];
		
		//Reset time
		m_TrailTime = BULLET_TRAIL_GAP;
	}
}

- (void)drawWithGL:(GLKBaseEffect *)gl {
	//Skip if no gl
	if (!gl) return;
	
	//Draw
	for (int i = 0; i < [m_Trails count]; i++) [[m_Trails objectAtIndex:i] drawWithGL:gl];
	[m_Image drawWithGL:gl];
}

@end
