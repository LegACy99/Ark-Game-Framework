//
//  FloatingLabel.m
//  ARK Framework Example
//
//  Created by LegACy on 4/28/13.
//  Copyright (c) 2013 Raka Mahesa. All rights reserved.
//

#import "FloatingLabel.h"
#import "HoleUtilities.h"
#import <math.h>

@implementation FloatingLabel

- (id)init { return nil; }
- (id)initWithText1:(NSString *)text1 withText2:(NSString *)text2 withDuration:(long)duration atX:(float)x atY:(float)y {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		NSString* Text	= [text1 uppercaseStringWithLocale:[[NSLocale alloc] initWithLocaleIdentifier:@"en_US"]];
		m_Label2		= [ARKLabel createWithText:text2 withFont:[HoleUtilities FONT_PIXEL_SMALL]];
		m_Label1		= [ARKLabel createWithText:Text withFont:[HoleUtilities FONT_PIXEL_SMALL]];
		m_Duration		= duration;
		m_InitialY		= y;
		m_Timer			= 0;
		
		//Set size
		m_Width 			= MAX([m_Label1 getWidth], [m_Label2 getWidth]);
		m_OriginalWidth 	= MAX([m_Label1 getOriginalWidth], [m_Label2 getOriginalWidth]);
		m_OriginalHeight	= [m_Label1 getOriginalHeight] + [m_Label2 getOriginalHeight] - 4;
		m_Height			= m_OriginalHeight * [[ARKUtilities instance] getScale];
		
		//Set position
		[self setPositionAtX:x atY:y horizontallyAlignedTo:DRAWABLE_ANCHOR_HCENTER verticallyAlignedTo:DRAWABLE_ANCHOR_BOTTOM];
	}
	
	//Return
	return self;
}

- (void)setPositionAtX:(float)x atY:(float)y horizontallyAlignedTo:(int)horizontal verticallyAlignedTo:(int)vertical {
	//Super
	[super setPositionAtX:x atY:y horizontallyAlignedTo:horizontal verticallyAlignedTo:vertical];
	
	//Calculate
	float X = m_X / [[ARKUtilities instance] getScale];
	float Y = m_Y / [[ARKUtilities instance] getScale];
	
	//Set label positions
	[m_Label1 setPositionAtX:X + (m_OriginalWidth / 2) atY:Y horizontallyAlignedTo:DRAWABLE_ANCHOR_HCENTER];
	[m_Label2 setPositionAtX:X + (m_OriginalWidth / 2) atY:Y + [m_Label1 getOriginalHeight] - 4 horizontallyAlignedTo:DRAWABLE_ANCHOR_HCENTER];
}

- (BOOL)isDead {
	//Is more than duration?
	return m_Timer >= m_Duration;
}

- (void)updateWithDeltaTime:(long)time {
	//Update
	m_Timer += time;
	
	//Set position
	float Y = m_InitialY - (12.0 * (float)m_Timer / 1000.0);
	[self setPositionAtX:m_X / [[ARKUtilities instance] getScale] atY:Y horizontallyAlignedTo:DRAWABLE_ANCHOR_LEFT verticallyAlignedTo:DRAWABLE_ANCHOR_BOTTOM];
}

- (void)drawWithGL:(GLKBaseEffect *)gl {
	//Draw
	[m_Label2 drawWithGL:gl];
	[m_Label1 drawWithGL:gl];
}

@end
