//
//  ARKAccelerometerInfo.m
//  Ark Framework
//
//  Created by LegACy on 4/14/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

//Header files
#import <math.h>
#import "ARKAccelerometerInfo.h"

//Constants
const int ACCELEROMETER_TILT_UP			= 0;
const int ACCELEROMETER_TILT_DOWN		= 1;
const int ACCELEROMETER_TILT_LEFT		= 2;
const int ACCELEROMETER_TILT_RIGHT		= 3;
const int ACCELEROMETER_TILT_UNKNOWN	= -1;
const float ACCELEROMETER_STAND_LIMIT	= 4.0;

@implementation ARKAccelerometerInfo

//Synthesize
@synthesize referenceX;
@synthesize referenceY;
@synthesize referenceZ;

- (id)init {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		m_AccelX		= 0;
		m_AccelY		= 0;
		m_AccelZ		= 0;
		m_ReferenceX	= 0;
		m_ReferenceY	= 0;
		m_ReferenceZ	= 0;
	}
	
	//Return
	return self;
}
//Accessors
- (float)getX { return m_AccelX - m_ReferenceX; }
- (float)getY { return m_AccelY - m_ReferenceY; }
- (float)getZ { return m_AccelZ - m_ReferenceZ; }

- (int)getTiltDirectionWithHorizontalOrientation:(BOOL)horizontal {
	//Initialize
	int Tilt = ACCELEROMETER_TILT_UNKNOWN;
	
	//Find standing axis
	if (m_ReferenceX > ACCELEROMETER_STAND_LIMIT) {
		//If horizontal
		if (horizontal) {
			//Check Y
			if ([self getY] < 0)	Tilt = ACCELEROMETER_TILT_LEFT;
			else					Tilt = ACCELEROMETER_TILT_RIGHT;
		} else {
			//Check Z
			if ([self getZ] < 0)	Tilt = ACCELEROMETER_TILT_DOWN;
			else					Tilt = ACCELEROMETER_TILT_UP;
		}
	} else if (m_ReferenceY > ACCELEROMETER_STAND_LIMIT) {
		//If horizontal
		if (horizontal) {
			//Check X
			if ([self getX] < 0)	Tilt = ACCELEROMETER_TILT_RIGHT;
			else					Tilt = ACCELEROMETER_TILT_LEFT;
		} else {
			//Check Z
			if ([self getZ] < 0)	Tilt = ACCELEROMETER_TILT_DOWN;
			else					Tilt = ACCELEROMETER_TILT_UP;
		}
	} else if (m_ReferenceX < -ACCELEROMETER_STAND_LIMIT) {
		//If horizontal
		if (horizontal) {
			//Check Y
			if ([self getY] < 0)	Tilt = ACCELEROMETER_TILT_RIGHT;
			else					Tilt = ACCELEROMETER_TILT_LEFT;
		} else {
			//Check Z
			if ([self getZ] < 0)	Tilt = ACCELEROMETER_TILT_UP;
			else					Tilt = ACCELEROMETER_TILT_DOWN;
		}
	} else if (m_ReferenceY < -ACCELEROMETER_STAND_LIMIT) {
		//If horizontal
		if (horizontal) {
			//Check X
			if ([self getX] < 0)	Tilt = ACCELEROMETER_TILT_LEFT;
			else					Tilt = ACCELEROMETER_TILT_RIGHT;
		} else {
			//Check Z
			if ([self getZ] < 0)	Tilt = ACCELEROMETER_TILT_DOWN;
			else					Tilt = ACCELEROMETER_TILT_UP;
		}
	}
	
	//Return
	return Tilt;
}

- (float)getTiltWithHorizontalOrientation:(BOOL)horizontal { return [self getTiltOnDirection:[self getTiltDirectionWithHorizontalOrientation:horizontal]]; }
- (float)getTiltOnDirection:(int)direction {
	//Initialize
	float Tilt = 0;
	
	//If unknown
	if (direction == ACCELEROMETER_TILT_UNKNOWN) {
		//Find the bigger one
		Tilt = [self getY];
		if (abs([self getX]) > abs(Tilt)) Tilt = [self getX];
	} else {
		//Check standing
		if (m_ReferenceX > ACCELEROMETER_STAND_LIMIT) {
			//Check direction
			if (direction == ACCELEROMETER_TILT_LEFT || direction == ACCELEROMETER_TILT_RIGHT) 	Tilt = [self getY];
			else																				Tilt = -[self getZ];
		} else if (m_ReferenceY > ACCELEROMETER_STAND_LIMIT) {
			//Check direction
			if (direction == ACCELEROMETER_TILT_LEFT || direction == ACCELEROMETER_TILT_RIGHT) 	Tilt = -[self getX];
			else																				Tilt = -[self getZ];
		} else if (m_ReferenceX < -ACCELEROMETER_STAND_LIMIT) {
			//Check direction
			if (direction == ACCELEROMETER_TILT_LEFT || direction == ACCELEROMETER_TILT_RIGHT) 	Tilt = -[self getY];
			else																				Tilt = [self getZ];
		} else if (m_ReferenceY < -ACCELEROMETER_STAND_LIMIT) {
			//Check direction
			if (direction == ACCELEROMETER_TILT_LEFT || direction == ACCELEROMETER_TILT_RIGHT) 	Tilt = [self getX];
			else																				Tilt = [self getZ];
		}
	}
	
	//Return
	return Tilt;
}

- (void)setAccelerationX:(float)x accelerationY:(float)y accelerationZ:(float)z {
	//Set
	m_AccelX = x;
	m_AccelY = y;
	m_AccelZ = z;
}

- (void)setReference {
	//Save
	m_ReferenceX = m_AccelX;
	m_ReferenceY = m_AccelY;
	m_ReferenceZ = m_AccelZ;
}

@end
