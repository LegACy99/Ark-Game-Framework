//
//  ARKTouchInfo.h
//  Ark Framework
//
//  Created by LegACy on 4/14/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ARKTouchInfo : NSObject {
	//Properties
	float m_X;
	float m_Y;
	float m_LastX;
	float m_LastY;
	float m_StartX;
	float m_StartY;
	
	//State
	BOOL m_Pressed;
	BOOL m_WasPressed;
	
	//Getsture
	int m_Swipe;
}

//Properties
@property (readonly, getter = getX)			float x;
@property (readonly, getter = getY)			float y;
@property (readonly, getter = getStartX)	float startX;
@property (readonly, getter = getStartY)	float startY;
@property (readonly, getter = isPressed)	BOOL pressed;
@property (readonly, getter = wasPressed)	BOOL wasPressed;

//Accessors
- (float)getOffsetX;
- (float)getOffsetY;
- (int)getSwipe;

//Functions
- (void)reset;
- (void)removed;
- (void)pressedAtX:(float)x atY:(float)y;
- (void)draggedToX:(float)x toY:(float)y;
- (void)releasedAtX:(float)x atY:(float)y;
- (void)addSwipeFromX:(float)x1 fromY:(float)y1 toX:(float)x2 toY:(float)y2;

@end

//Constant
extern const int TOUCH_NO_SWIPE;
extern const int TOUCH_SWIPE_NORTH;
extern const int TOUCH_SWIPE_NORTHEAST;
extern const int TOUCH_SWIPE_EAST;
extern const int TOUCH_SWIPE_SOUTHEAST;
extern const int TOUCH_SWIPE_SOUTH;
extern const int TOUCH_SWIPE_SOUTHWEST;
extern const int TOUCH_SWIPE_WEST;
extern const int TOUCH_SWIPE_NORTHWEST;