//
//  ARKButton.m
//  Ark Framework
//
//  Created by LegACy on 4/25/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKButton.h"
#import "ARKUtilities.h"
#import "ARKResourceManager.h"
#import "ARKLabel.h"
#import "ARKImage.h"

//Constants
const int BUTTON_STATE_NORMAL	= 0;
const int BUTTON_STATE_PRESSED	= 1;
const int BUTTON_STATE_INACTIVE	= 2;

@implementation ARKButton

//Synthesize
@synthesize active;
@synthesize visible;
@synthesize pressedSFX	= m_PressedSFX;
@synthesize releasedSFX	= m_ReleasedSFX;
@synthesize ID			= m_ID;

- (id)init {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		m_ID			= 0;
		m_InputX		= 0;
		m_InputY		= 0;
		m_InputWidth	= 0;
		m_InputHeight	= 0;
		m_ReleasedSFX	= nil;
		m_PressedSFX	= nil;
		m_Images		= nil;
		m_Labels		= nil;
		m_State			= BUTTON_STATE_NORMAL;
		self.active		= YES;
		self.visible	= YES;
	}
	
	//Return
	return self;
}

- (id)initWithID:(int)ID withResource:(NSString*)resource {
	return [self initWithID:ID withResource:resource withText:nil];
}

- (id)initWithID:(int)ID withImages:(NSArray*)images {
	return [self initWithID:ID withImages:images withText:nil];
}

- (id)initWithID:(int)ID withResource:(NSString*)resource withText:(NSString*)text {
	return [self initWithID:ID withResource:resource withText:text withFont:[[ARKUtilities instance] getSystemFont] atX:0 atY:0];
}

- (id)initWithID:(int)ID withImages:(NSArray*)images withText:(NSString*)text {	
	return [self initWithID:ID withImages:images withText:text withFont:[[ARKUtilities instance] getSystemFont] atX:0 atY:0];
}

- (id)initWithID:(int)ID withResource:(NSString*)resource withText:(NSString*)text withFont:(NSString*)font {
	return [self initWithID:ID withResource:resource withText:text withFont:font atX:0 atY:0];
}

- (id)initWithID:(int)ID withImages:(NSArray*)images withText:(NSString*)text withFont:(NSString*)font {
	return [self initWithID:ID withImages:images withText:text withFont:font atX:0 atY:0];
}

- (id)initWithID:(int)ID withResource:(NSString*)resource atX:(float)x atY:(float)y {
	return [self initWithID:ID withResource:resource withText:nil withFont:nil atX:x atY:y];
}

- (id)initWithID:(int)ID withImages:(NSArray*)images atX:(float)x atY:(float)y {
	return [self initWithID:ID withImages:images withText:nil withFont:nil atX:x atY:y];
}

- (id)initWithID:(int)ID withResource:(NSString*)resource withText:(NSString*)text withFont:(NSString*)font atX:(float)x atY:(float)y {
	return [self initWithID:ID withResource:resource withText:text withFont:font andFont:font andFont:font atX:x atY:y];
}

- (id)initWithID:(int)ID withImages:(NSArray*)images withText:(NSString*)text withFont:(NSString*)font atX:(float)x atY:(float)y {
	return [self initWithID:ID withImages:images withText:text withFont:font andFont:font andFont:font atX:x atY:y];
}

- (id)initWithID:(int)ID withResource:(NSString*)resource withText:(NSString*)text withFont:(NSString*)font1 andFont:(NSString*)font2 andFont:(NSString*)font3 atX:(float)x atY:(float)y {
	return [self initWithID:ID withImages:[[ARKResourceManager instance] getTexturesWithName:resource] withText:text withFont:font1 andFont:font2 andFont:font3 atX:x atY:y];
}

- (id)initWithID:(int)ID withImages:(NSArray*)images withText:(NSString*)text withFont:(NSString*)font1 andFont:(NSString*)font2 andFont:(NSString*)font3 atX:(float)x atY:(float)y {
	//Default
	self = [self init];
	if (self) {
		//Save
		m_ID = ID;
		
		//If image exist
		if (images && [images count] > 0) {
			//Create image
			NSMutableArray* Images = [NSMutableArray array];
			for (int i = 0; i < [images count]; i++) [Images addObject:[ARKImage createFromJSON:[images objectAtIndex:i]]];
			m_Images = [NSArray arrayWithArray:Images];
			
			//Get data
			m_Width				= [[m_Images objectAtIndex:0] getWidth];
			m_Height			= [[m_Images objectAtIndex:0] getHeight];
			m_OriginalWidth		= [[m_Images objectAtIndex:0] getOriginalWidth];
			m_OriginalHeight	= [[m_Images objectAtIndex:0] getOriginalHeight];
		}
		
		//If text exist
		if (text) {
			//Create label
			m_Labels = [NSArray arrayWithObjects:
						[ARKLabel createWithText:text withFont:font1],
						[ARKLabel createWithText:text withFont:font2],
						[ARKLabel createWithText:text withFont:font3],
						 nil];
		}
		
		//Set SFX
		[self setSFXForPress:[[ARKUtilities instance] getSystemPressSFX] forRelease:[[ARKUtilities instance] getSystemReleaseSFX]];
		
		//Set position and size
		[self setPositionAtX:0 atY:0];
		[self setRegionFromX:0 fromY:0 withWidth:m_OriginalWidth withHeight:m_OriginalHeight];
	}
	
	//Return
	return self;
}

- (BOOL)isInsideX:(float)x Y:(float)y {
	//Init variables
	float Top	= m_Y + m_InputY;
	float Left	= m_X + m_InputX;
	
	//Check for false
	if (y < Top) 					return NO;
	if (x < Left)					return NO;
	if (y > Top + m_InputHeight)	return NO;
	if (x > Left + m_InputWidth)	return NO;
	
	//If passed, inside
	return YES;
}

- (void)setPositionAtX:(float)x atY:(float)y horizontallyAlignedTo:(int)horizontal verticallyAlignedTo:(int)vertical {
	//Super
	[super setPositionAtX:x atY:y horizontallyAlignedTo:horizontal verticallyAlignedTo:vertical];
	
	//Set components position
	if (m_Images) for (int i = 0; i < [m_Images count]; i++) [[m_Images objectAtIndex:i] setPositionAtX:x atY:y horizontallyAlignedTo:horizontal verticallyAlignedTo:vertical];
	if (m_Labels)
		for (int i = 0; i < [m_Labels count]; i++)
			[[m_Labels objectAtIndex:i] setPositionAtX:(m_X + (m_Width / 2)) / [[ARKUtilities instance] getScale]
												   atY:(m_Y + (m_Height / 2)) / [[ARKUtilities instance] getScale]
								 horizontallyAlignedTo:DRAWABLE_ANCHOR_HCENTER
								   verticallyAlignedTo:DRAWABLE_ANCHOR_VCENTER];
}

- (void)setRegionFromX:(float)x fromY:(float)y withWidth:(float)width withHeight:(float)height {
	//Super
	[super setRegionFromX:x fromY:y withWidth:width withHeight:height];
	
	//Set size
	[self setSizeWithX:m_RegionX withY:m_RegionY withWidth:m_RegionWidth withHeight:m_RegionHeight offsetScaled:NO sizeScaled:NO];
	
	//Set images region
	if (m_Images) for (int i = 0; i < [m_Images count]; i++) [[m_Images objectAtIndex:i] setRegionFromX:x fromY:y withWidth:width withHeight:height];
	
	//If label exist
	float Scale = [[ARKUtilities instance] getScale];
	if (m_Labels) for (int i = 0; i < [m_Labels count]; i++) {
		//Calculate region
		float Top 		= m_Y + m_RegionY > [[m_Labels objectAtIndex:i] getY] ? (m_Y + m_RegionY) - [[m_Labels objectAtIndex:i] getY] : 0;
		float Left 		= m_X + m_RegionX > [[m_Labels objectAtIndex:i] getX] ? (m_X + m_RegionX) - [[m_Labels objectAtIndex:i] getX] : 0;
		float Width		= [[m_Labels objectAtIndex:i] getX] + [[m_Labels objectAtIndex:i] getWidth] > m_X + m_RegionX + m_RegionWidth ? m_X + m_RegionX + m_RegionWidth - Left - [[m_Labels objectAtIndex:i] getX] : [[m_Labels objectAtIndex:i] getWidth] - Left;
		float Height	= [[m_Labels objectAtIndex:i] getY] + [[m_Labels objectAtIndex:i] getHeight] > m_Y + m_RegionY + m_RegionHeight ? m_Y + m_RegionY + m_RegionHeight - Top - [[m_Labels objectAtIndex:i] getY] : [[m_Labels objectAtIndex:i] getHeight] - Top;
		
		//Set region
		[[m_Labels objectAtIndex:i] setRegionFromX:Left / Scale fromY:Top / Scale withWidth:Width / Scale withHeight:Height / Scale];
	}
}

- (void)setState:(int)state {
	//Set state
	m_State = state;
	if (m_Images) m_State %= [m_Images count];
}

- (void)setSFXForPress:(NSString *)pressed forRelease:(NSString *)released {
	//Save
	m_PressedSFX	= pressed;
	m_ReleasedSFX	= released;
}

- (void)setInactiveImage:(NSDictionary *)json {
	//Skip if no image
	if (!m_Images) return;
	
	//Create new images
	NSArray* Images	= [NSArray arrayWithObjects:
					   [m_Images objectAtIndex:BUTTON_STATE_NORMAL],
					   [m_Images objectAtIndex:BUTTON_STATE_PRESSED],
					   [ARKImage createFromJSON:json],
					   nil];
	m_Images = Images;
	
	//If text exist
	if (m_Labels) {
		//Create label
		NSArray* Labels = [NSArray arrayWithObjects:
						   [m_Labels objectAtIndex:BUTTON_STATE_NORMAL],
						   [m_Labels objectAtIndex:BUTTON_STATE_PRESSED],
						   [ARKLabel createWithText:[[m_Labels objectAtIndex:0] getText] withFont:m_FontInactive],
						   nil];
		m_Labels = Labels;
	}
}

- (void)setSizeWithX:(float)x withY:(float)y withWidth:(float)width withHeight:(float)height {
	//Scale
	[self setSizeWithX:x withY:y withWidth:width withHeight:height offsetScaled:YES sizeScaled:YES];
}

- (void)setSizeWithX:(float)x withY:(float)y withWidth:(float)width withHeight:(float)height offsetScaled:(BOOL)scaleOffset sizeScaled:(BOOL)scaleSize {
	//Set offset
	m_InputX = x;
	m_InputY = y;
	if (scaleOffset) m_InputX *= [[ARKUtilities instance] getScale];
	if (scaleOffset) m_InputY *= [[ARKUtilities instance] getScale];
	
	//Set size
	m_InputWidth 	= width;
	m_InputHeight	= height;
	if (scaleSize) m_InputWidth		*= [[ARKUtilities instance] getScale];
	if (scaleSize) m_InputHeight	*= [[ARKUtilities instance] getScale];
}

- (void)drawWithGL:(GLKBaseEffect *)gl {
	//Skip if not visible
	if (![self visible]) return;
	
	//Get state
	int State = m_State;
	if (![self active]) State = BUTTON_STATE_INACTIVE;
	
	//Draw
	if (m_Images) [[m_Images objectAtIndex:State] drawWithGL:gl];
	if (m_Labels) [[m_Labels objectAtIndex:State] drawWithGL:gl];
}

@end
