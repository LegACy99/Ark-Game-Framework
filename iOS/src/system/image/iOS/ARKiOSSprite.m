//
//  ARKiOSSPrite.m
//  Ark Framework
//
//  Created by LegACy on 4/14/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKiOSSprite.h"
#import "ARKResourceManager.h"
#import "ARKImage.h"

@implementation ARKiOSSprite

- (id)initWithPath:(NSString *)path AtX:(float)x atY:(float)y withDelay:(long)delay {
	//Super
	self = [super initAtX:x atY:y withDelay:delay];
	if (self) {
		//Create images
		NSArray* Data			= [[ARKResourceManager instance] getTexturesWithName:path];
		if (Data) {
			NSMutableArray* Images	= [NSMutableArray array];
			for (int i = 0; i < [Data count]; i++) [Images addObject:[ARKImage createFromJSON:[Data objectAtIndex:i] atX:x atY:y]];
			m_Images = Images;
			
			//Get all frame
			m_Total	= [m_Images count];
			if (m_Total > 0) {
				//Set data
				m_Width				= [[m_Images objectAtIndex:0] getWidth];
				m_Height			= [[m_Images objectAtIndex:0] getHeight];
				m_OriginalWidth		= [[m_Images objectAtIndex:0] getOriginalWidth];
				m_OriginalHeight	= [[m_Images objectAtIndex:0] getOriginalHeight];
				[self setStraightAnimation];
			}
		}
	}
	
	//Return
	return self;
}

- (void)setRegionFromX:(float)x fromY:(float)y withWidth:(float)width withHeight:(float)height {
	//Super
	[super setRegionFromX:x fromY:y withWidth:width withHeight:height];
	
	//Set all region
	for (int i = 0; i < [m_Images count]; i++) [[m_Images objectAtIndex:i] setRegionFromX:x fromY:y withWidth:width withHeight:height];
}

- (void)setPositionAtX:(float)x atY:(float)y horizontallyAlignedTo:(int)horizontal verticallyAlignedTo:(int)vertical {
	//Super
	[super setPositionAtX:x atY:y horizontallyAlignedTo:horizontal verticallyAlignedTo:vertical];
	
	//Position all
	for (int i = 0; i < [m_Images count]; i++) [[m_Images objectAtIndex:i] setPositionAtX:x atY:y horizontallyAlignedTo:horizontal verticallyAlignedTo:vertical];
}

- (void)setRotationWithAngle:(float)angle increasedBy:(float)increase pivotAtX:(float)x pivotAtY:(float)y {
	//Rotate all
	for (int i = 0; i < [m_Images count]; i++) [[m_Images objectAtIndex:i] setRotationWithAngle:angle increasedBy:increase pivotAtX:x pivotAtY:y];
}

- (void)setTintWithRedF:(float)red withGreenF:(float)green withBlueF:(float)blue withAlphaF:(float)alpha {
	//Tint all
	for (int i = 0; i < [m_Images count]; i++) [[m_Images objectAtIndex:i] setTintWithRedF:red withGreenF:green withBlueF:blue withAlphaF:alpha];
}

- (void)setMirrorHorizontally:(BOOL)horizontal andVertically:(BOOL)vertical {
	//Mirror all
	for (int i = 0; i < [m_Images count]; i++) [[m_Images objectAtIndex:i] setMirrorHorizontally:horizontal andVertically:vertical];
}

- (void)drawWithGL:(GLKBaseEffect *)gl {
	//Draw the correct frame
	[[m_Images objectAtIndex:m_Frame] drawWithGL:gl];
}

@end
