//
//  ARKAccelerometerInfo.h
//  Ark Framework
//
//  Created by LegACy on 4/14/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ARKAccelerometerInfo : NSObject {
	//Protected variables
	float m_AccelX;
	float m_AccelY;
	float m_AccelZ;
	float m_ReferenceX;
	float m_ReferenceY;
	float m_ReferenceZ;
}

//Properties
@property (readonly, getter = getReferenceX) float referenceX;
@property (readonly, getter = getReferenceY) float referenceY;
@property (readonly, getter = getReferenceZ) float referenceZ;

//Accessors
- (float)getX;
- (float)getY;
- (float)getZ;

//Tilt accessor
- (int)getTiltDirectionWithHorizontalOrientation:(BOOL)horizontal;
- (float)getTiltWithHorizontalOrientation:(BOOL)horizontal;
- (float)getTiltOnDirection:(int)direction;

//Setter
- (void)setAccelerationX:(float)x accelerationY:(float)y accelerationZ:(float)z;
- (void)setReference;

@end
