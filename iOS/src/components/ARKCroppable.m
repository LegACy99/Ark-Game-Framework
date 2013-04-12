//
//  ARKCroppable.m
//  Ark Framework
//
//  Created by LegACy on 4/6/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

//Header file
#import "ARKCroppable.h"
#import "ARKUtilities.h"

//Implementation
@implementation ARKCroppable

//Synthesize properties
@synthesize originalRegionX			= m_OriginalRegionX;
@synthesize originalRegionY			= m_OriginalRegionY;
@synthesize originalRegionWidth		= m_OriginalRegionWidth;
@synthesize originalRegionHeight	= m_OriginalRegionHeight;

- (id)init {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		m_RegionX				= 0;
		m_RegionY				= 0;
		m_RegionWidth			= 0;
		m_RegionHeight			= 0;
		m_OriginalRegionX		= 0;
		m_OriginalRegionY		= 0;
		m_OriginalRegionWidth	= 0;
		m_OriginalRegionHeight	= 0;
	}
	
	//Return
	return self;
}

- (void)setRegionFromX:(float)left fromY:(float)top {
	//Set region
	[self setRegionfromX:left fromY:top withWidth:m_OriginalWidth - left withHeight:m_OriginalHeight - top];
}

- (void)setRegionFromX:(float)left fromY:(float)top toX:(float)right toY:(float)bottom {
	//Set region
	[self setRegionfromX:left fromY:top withWidth:right - left withHeight:bottom - top];
}

- (void)setRegionWithWidth:(float)width withHeight:(float)height {
	//Set region
	[self setRegionfromX:0 fromY:0 withWidth:width withHeight:height];
}

- (void)setRegionfromX:(float)x fromY:(float)y withWidth:(float)width withHeight:(float)height {
	//Set data
	m_OriginalRegionX		= x;
	m_OriginalRegionY		= y;
	m_OriginalRegionWidth	= width;
	m_OriginalRegionHeight	= height;
	
	//Validate
	if (m_OriginalRegionX < 0) 											m_OriginalRegionX = 0;
	if (m_OriginalRegionY < 0) 											m_OriginalRegionY = 0;
	if (m_OriginalRegionWidth < 0) 										m_OriginalRegionWidth = 0;
	if (m_OriginalRegionHeight < 0) 									m_OriginalRegionHeight = 0;
	if (m_OriginalRegionX > m_OriginalWidth)							m_OriginalRegionX = m_OriginalWidth;
	if (m_OriginalRegionY > m_OriginalHeight)							m_OriginalRegionY = m_OriginalHeight;
	if (m_OriginalRegionWidth + m_OriginalRegionX > m_OriginalWidth) 	m_OriginalRegionWidth = m_OriginalWidth - m_OriginalRegionX;
	if (m_OriginalRegionHeight + m_OriginalRegionY > m_OriginalHeight)	m_OriginalRegionHeight = m_OriginalHeight - m_OriginalRegionY;
	
	//Scale
	m_RegionX		= m_OriginalRegionX * [[ARKUtilities instance] getScale];
	m_RegionY		= m_OriginalRegionY * [[ARKUtilities instance] getScale];
	m_RegionWidth	= m_OriginalRegionWidth * [[ARKUtilities instance] getScale];
	m_RegionHeight	= m_OriginalRegionHeight * [[ARKUtilities instance] getScale];
}

@end
