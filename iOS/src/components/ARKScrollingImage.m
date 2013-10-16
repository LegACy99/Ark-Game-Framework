//
//  ARKScrollingImage.m
//  Ark Framework
//
//  Created by LegACy on 4/26/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import <math.h>
#import "ARKScrollingImage.h"
#import "ARKResourceManager.h"
#import "ARKUtilities.h"
#import "ARKImage.h"

//Constants
const int SCROLLING_MAIN_IMAGE			= 0;
const int SCROLLING_VERTICAL_IMAGE		= 1;
const int SCROLLING_HORIZONTAL_IMAGE	= 2;
const int SCROLLING_DIAGONAL_IMAGE		= 3;

@interface ARKScrollingImage ()

- (int)signumOf:(int)number;

@end

@implementation ARKScrollingImage

- (id)init {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		m_Time		= 0;
		m_ScrollX	= 0;
		m_ScrollY	= 0;
		m_OffsetX	= 0;
		m_OffsetY	= 0;
	}
	
	//Return
	return self;
}

- (id)initWithJSON:(NSDictionary*)json		{ return [self initWithJSON:json scrolledByX:0 byY:0];			}
- (id)initWithResource:(NSString*)resource	{ return [self initWithResource:resource scrolledByX:0 byY:0];	}
- (id)initWithJSON:(NSDictionary*)json scrolledByX:(float)scrollX byY:(float)scrollY {
	return [self initWithJSON:json atX:0 atY:0 scrolledByX:scrollX byY:scrollY];
}

- (id)initWithResource:(NSString*)resource scrolledByX:(float)scrollX byY:(float)scrollY {
	return [self initWithResource:resource atX:0 atY:0 scrolledByX:scrollX byY:scrollY];
}

- (id)initWithResource:(NSString *)resource atX:(float)x atY:(float)y scrolledByX:(float)scrollX byY:(float)scrollY {
	return [self initWithJSON:[[ARKResourceManager instance] getJSONWithName:resource] atX:x atY:y scrolledByX:scrollX byY:scrollY];
}

- (id)initWithJSON:(NSDictionary *)json atX:(float)x atY:(float)y scrolledByX:(float)scrollX byY:(float)scrollY {
	//Default
	self = [self init];
	if (self) {
		//Create image
		for (int i = 0; i < sizeof(m_Images) / sizeof(m_Images[0]); i++) m_Images[i] = [ARKImage createFromJSON:json];
		
		//Save data
		m_Width				= [m_Images[SCROLLING_MAIN_IMAGE] getWidth];
		m_Height			= [m_Images[SCROLLING_MAIN_IMAGE] getHeight];
		m_OriginalWidth	 	= [m_Images[SCROLLING_MAIN_IMAGE] getOriginalWidth];
		m_OriginalHeight	= [m_Images[SCROLLING_MAIN_IMAGE] getOriginalHeight];
		m_ScrollX 			= scrollX;
		m_ScrollY 			= scrollY;
		
		//Set position
		[self setPositionAtX:x atY:y];
	}
	
	//Return
	return self;
}

- (void)setPositionAtX:(float)x atY:(float)y horizontallyAlignedTo:(int)horizontal verticallyAlignedTo:(int)vertical {
	//Set position then scroll
	[super setPositionAtX:x atY:y horizontallyAlignedTo:horizontal verticallyAlignedTo:vertical];
	[self scrollByX:0 byY:0];
}

- (void)setTintWithRed:(int)red withGreen:(int)green withBlue:(int)blue							{ [self setTintWithRed:red withGreen:green withBlue:blue withAlpha:255];																		}
- (void)setTintWithRedF:(float)red withGreenF:(float)green withBlueF:(float)blue				{ [self setTintWithRedF:red withGreenF:green withBlueF:blue withAlphaF:1.0f];																	}
- (void)setTintWithRed:(int)red withGreen:(int)green withBlue:(int)blue withAlpha:(int)alpha	{ [self setTintWithRedF:(float)red / 255.0f withGreenF:(float)green / 255.0f withBlueF:(float)blue / 255.0f withAlphaF:(float)alpha / 255.0f];	}
- (void)setTintWithRedF:(float)red withGreenF:(float)green withBlueF:(float)blue withAlphaF:(float)alpha {
	//Set all images tint
	for (int i = 0; i < sizeof(m_Images) / sizeof(m_Images[0]); i++) [m_Images[i] setTintWithRedF:red withGreenF:green withBlueF:blue withAlphaF:alpha];
}

- (int)signumOf:(int)number { return (number < 0) ? -1 : (number > 0) ? +1 : 0; }

- (void)scrollByX:(float)x byY:(float)y {
	//Scroll
	m_OffsetX += x;
	m_OffsetY += y;
	m_OffsetX = ((int)fabs(m_OffsetX) % (int)m_OriginalWidth) * [self signumOf:m_OffsetX];
	m_OffsetY = ((int)fabs(m_OffsetY) % (int)m_OriginalHeight) * [self signumOf:m_OffsetY];
	
	//Get original position
	float OriginalX = m_X / [[ARKUtilities instance] getScale];
	float OriginalY = m_Y / [[ARKUtilities instance] getScale];
	
	//Set images position
	[m_Images[SCROLLING_MAIN_IMAGE] setPositionAtX:OriginalX + m_OffsetX atY:OriginalY + m_OffsetY];
	[m_Images[SCROLLING_VERTICAL_IMAGE] setPositionAtX:OriginalX + m_OffsetX atY:OriginalY + m_OffsetY - ((m_OriginalHeight - 1) * [self signumOf:m_OffsetY])];
	[m_Images[SCROLLING_HORIZONTAL_IMAGE] setPositionAtX:OriginalX + m_OffsetX - ((m_OriginalWidth - 1) * [self signumOf:m_OffsetX]) atY:OriginalY + m_OffsetY];
	[m_Images[SCROLLING_DIAGONAL_IMAGE] setPositionAtX:OriginalX + m_OffsetX - ((m_OriginalWidth - 1) * [self signumOf:m_OffsetX]) atY:OriginalY + m_OffsetY - ((m_OriginalHeight - 1) * [self signumOf:m_OffsetY])];
}

- (void)updateWithDeltaTime:(long)time {
	//Increase time
	m_Time += time;
	
	//Scroll
	m_OffsetX = 0;
	m_OffsetY = 0;
	[self scrollByX:(float)m_Time / 1000.0 * m_ScrollX byY:(float)m_Time / 1000.0 * m_ScrollY];
}

- (void)drawWithGL:(GLKBaseEffect *)gl {
	//Draw only the relevant image
	[m_Images[SCROLLING_MAIN_IMAGE] drawWithGL:gl];
	if (m_OffsetX != 0) 					[m_Images[SCROLLING_HORIZONTAL_IMAGE] drawWithGL:gl];
	if (m_OffsetY != 0) 					[m_Images[SCROLLING_VERTICAL_IMAGE] drawWithGL:gl];
	if (m_OffsetX != 0 && m_OffsetX != 0) 	[m_Images[SCROLLING_DIAGONAL_IMAGE] drawWithGL:gl];
}

@end
