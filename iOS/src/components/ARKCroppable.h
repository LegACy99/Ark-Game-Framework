//
//  ARKCroppable.h
//  Ark Framework
//
//  Created by LegACy on 4/6/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKDrawable.h"

@interface ARKCroppable : ARKDrawable {
	//Variables
	//Note: RegionX and Y is relative to position
	float m_RegionX;
	float m_RegionY;
	float m_RegionWidth;
	float m_RegionHeight;
	float m_OriginalRegionX;
	float m_OriginalRegionY;
	float m_OriginalRegionWidth;
	float m_OriginalRegionHeight;
}

//Properties
@property (readonly, getter = getOriginalRegionX)		float originalRegionX;
@property (readonly, getter = getOriginalRegionY)		float originalRegionY;
@property (readonly, getter = getOriginalRegionWidth)	float originalRegionWidth;
@property (readonly, getter = getOriginalRegionHeight)	float originalRegionHeight;

//Public functions
- (void)setRegionFromX:(float)left fromY:(float)top;
- (void)setRegionFromX:(float)left fromY:(float)top toX:(float)right toY:(float)bottom;
- (void)setRegionFromX:(float)x fromY:(float)y withWidth:(float)width withHeight:(float)height;
- (void)setRegionWithWidth:(float)width withHeight:(float)height;

@end
