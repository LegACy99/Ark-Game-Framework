//
//  ARKTouchInfo.m
//  Ark Framework
//
//  Created by LegACy on 4/14/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

//Header files
#import <math.h>
#import "ARKTouchInfo.h"

//Constants
const int TOUCH_NO_SWIPE	 	= 0;
const int TOUCH_SWIPE_NORTH 	= 1;
const int TOUCH_SWIPE_NORTHEAST = 2;
const int TOUCH_SWIPE_EAST 		= 3;
const int TOUCH_SWIPE_SOUTHEAST = 4;
const int TOUCH_SWIPE_SOUTH 	= 5;
const int TOUCH_SWIPE_SOUTHWEST = 6;
const int TOUCH_SWIPE_WEST 		= 7;
const int TOUCH_SWIPE_NORTHWEST = 8;
const float TOUCH_MIN_DISTANCE	= 200;
const double TOUCH_TAN_22		= 0.4142135623731; //tan(22.5)

@implementation ARKTouchInfo

//Synthesize properties
@synthesize x			= m_X;
@synthesize y			= m_Y;
@synthesize startX		= m_StartX;
@synthesize startY		= m_StartY;
@synthesize pressed		= m_Pressed;
@synthesize wasPressed	= m_WasPressed;

- (id)init {
	//Super
	self = [super init];
	if (self) [self reset];
	
	//Return
	return self;
}

- (void)reset {
	//Reset data
	m_X			= -1;
	m_Y			= -1;
	m_LastX		= -1;
	m_LastY		= -1;
	m_StartX	= -1;
	m_StartY	= -1;
	m_Swipe		= TOUCH_NO_SWIPE;
	
	//Reset state
	m_Pressed		= NO;
	m_WasPressed	= NO;
}

- (float)getOffsetX {
	//Get offset
	float Offset 	= m_X - m_LastX;
	m_LastX			= m_X;
	
	//Return
	return Offset;
}

- (float)getOffsetY {
	//Get offset
	float Offset 	= m_Y - m_LastY;
	m_LastY			= m_Y;
	
	//Return
	return Offset;
}

- (int)getSwipe {
	//Extract swipe
	int Swipe	= m_Swipe;
	m_Swipe		= TOUCH_NO_SWIPE;
	
	//Return
	return Swipe;
}

- (void)pressedAtX:(float)x atY:(float)y {
	//Skip if already pressed
	if (m_Pressed) return;
	
	//Set data
	m_X	= x;
	m_Y	= y;
	
	//Set other data
	m_LastX		= m_X;
	m_LastY		= m_Y;
	m_StartX 	= m_X;
	m_StartY 	= m_Y;
	
	//Pressed
	m_Pressed = YES;
	
}

- (void)draggedToX:(float)x toY:(float)y {
	//Skip if not pressed
	if (!m_Pressed) return;
	
	//Was pressed
	m_WasPressed = YES;
	
	//Set current position
	m_X	= x;
	m_Y	= y;
}

- (void)releasedAtX:(float)x atY:(float)y {
	//Skip if not pressed
	if (!m_Pressed) return;
	
	//Set current position
	m_X	= x;
	m_Y	= y;
	
	//Released
	m_Pressed = NO;
}

- (void)removed {
	//Remove
	if (m_Pressed) m_Pressed = NO;
	m_WasPressed = NO;
}

- (void)addSwipeFromX:(float)x1 fromY:(float)y1 toX:(float)x2 toY:(float)y2 {
	//Validate swipe
	float DistanceX = (x2 - x1);
	float DistanceY = (y2 - y1);
	if ((DistanceX * DistanceX) + (DistanceY * DistanceY) < (TOUCH_MIN_DISTANCE * TOUCH_MIN_DISTANCE)) return;
	
	//Initialize
	int Swipe = TOUCH_NO_SWIPE;
	
	//Get Ttan
	double TanXY = abs(DistanceX) / abs(DistanceY);
	double TanYX = abs(DistanceY) / abs(DistanceX);
	
	//If up
	if (DistanceY <= 0) {
		//Is north?
		if (TanXY <= TOUCH_TAN_22) Swipe = TOUCH_SWIPE_NORTH;
		else if (TanYX > TOUCH_TAN_22) {
			//Check diagonal
			if (DistanceX >= 0) Swipe = TOUCH_SWIPE_NORTHEAST;
			else 				Swipe = TOUCH_SWIPE_NORTHWEST;
		} else {
			//Check horizontal
			if (DistanceX >= 0) Swipe = TOUCH_SWIPE_EAST;
			else				Swipe = TOUCH_SWIPE_WEST;
		}
	} else {
		//Is south?
		if (TanXY <= TOUCH_TAN_22) Swipe = TOUCH_SWIPE_SOUTH;
		else if (TanYX > TOUCH_TAN_22) {
			//Check diagonal
			if (DistanceX >= 0) Swipe = TOUCH_SWIPE_SOUTHEAST;
			else 				Swipe = TOUCH_SWIPE_SOUTHWEST;
		} else {
			//Check horizontal
			if (DistanceX >= 0) Swipe = TOUCH_SWIPE_EAST;
			else				Swipe = TOUCH_SWIPE_WEST;
		}
	}
	
	//If direction got
	if (Swipe != TOUCH_NO_SWIPE) m_Swipe = Swipe;
}

@end
