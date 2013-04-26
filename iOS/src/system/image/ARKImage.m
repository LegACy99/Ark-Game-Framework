//
//  ARKImage.m
//  Ark Framework
//
//  Created by LegACy on 4/6/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKImage.h"
#import "ARKiOSImage.h"
#import "ARKUtilities.h"

//Constants
const NSString* IMAGE_KEY_RECT			= @"Rect";
const NSString* IMAGE_KEY_RECT_TOP		= @"Top";
const NSString* IMAGE_KEY_RECT_LEFT		= @"Left";
const NSString* IMAGE_KEY_RECT_RIGHT	= @"Right";
const NSString* IMAGE_KEY_RECT_BOTTOM	= @"Bottom";
const NSString* IMAGE_KEY_RECT_HEIGHT	= @"Height";
const NSString* IMAGE_KEY_RECT_WIDTH	= @"Width";
const NSString* IMAGE_KEY_TEXTURE		= @"Texture";

@implementation ARKImage

- (id)init {
	//Init
	self = [super init];
	if (self) {
		//Initialize
		m_Flip			= 0;
		m_PivotX		= 0;
		m_PivotY		= 0;
		m_Rotation		= 0;
		m_ColorRed		= 1;
		m_ColorBlue		= 1;
		m_ColorGreen	= 1;
		m_ColorAlpha	= 1;
		m_Mirror		= DRAWABLE_MIRROR_NONE;
	}
	
	//Return
	return self;
}

- (id)initFromFile:(NSString*)file atX:(float)x atY:(float)y {
	//Init
	self = [self init];
	if (self) {
		//Position
		[self setPositionAtX:x atY:y];
	}
	
	//Return
	return self;
	
}

- (id)initFromJSON:(id)json atX:(float)x atY:(float)y {
	//Init
	self = [self init];
	if (self) {
		//Position
		[self setPositionAtX:x atY:y];
	}
	
	//Return
	return self;
}

+ (ARKImage*)createFromJSON:(NSDictionary*)json								{ return [ARKImage createFromJSON:json atX:0 atY:0];				}
+ (ARKImage*)createFromPath:(NSString*)resource								{ return [ARKImage createFromPath:resource atX:0 atY:0];			}
+ (ARKImage*)createFromJSON:(NSDictionary*)json atX:(float)x atY:(float)y	{ return [[ARKiOSImage alloc] initFromJSON:json atX:x atY:y];		}
+ (ARKImage*)createFromPath:(NSString*)resource atX:(float)x atY:(float)y	{ return [[ARKiOSImage alloc] initFromFile:resource atX:x atY:y];	}

- (void)setRotationWithAngle:(float)angle {
	//Set rotation
	[self setRotationWithAngle:angle pivotAtX:[self getOriginalWidth] / 2 pivotAtY:[self getOriginalHeight] / 2];
}

- (void)setRotationWithAngle:(float)angle increasedBy:(float)increase {
	//Set rotation
	[self setRotationWithAngle:angle increasedBy:increase pivotAtX:[self getOriginalWidth] / 2 pivotAtY:[self getOriginalHeight] / 2];
}

- (void)setRotationWithAngle:(float)angle pivotAtX:(float)x pivotAtY:(float)y {
	//Initialize
	int Add 	= 0;
	float Angle	= angle;
	
	//Set angle
	if (Angle == 0) {
		Add 	= -1;
		Angle 	= 1;
	}
	
	//Set rotation
	[self setRotationWithAngle:Angle increasedBy:Add pivotAtX:x pivotAtY:y];
}

- (void)setRotationWithAngle:(float)angle increasedBy:(float)increase pivotAtX:(float)x pivotAtY:(float)y {
	//Set rotation
	if (angle != 0) m_Rotation = angle;
	m_Rotation += increase;
	
	//Set pivot
	m_PivotX = x * [[ARKUtilities instance] getScale];
	m_PivotY = y * [[ARKUtilities instance] getScale];
}

- (void)setTintWithRed:(int)red withGreen:(int)green withBlue:(int)blue {
	//Set color
	[self setTintWithRed:red withGreen:green withBlue:blue withAlpha:255];
}

- (void)setTintWithRedF:(float)red withGreenF:(float)green withBlueF:(float)blue {
	//Set color
	[self setTintWithRedF:red withGreenF:green withBlueF:blue withAlphaF:1];
}

- (void)setTintWithRed:(int)red withGreen:(int)green withBlue:(int)blue withAlpha:(int)alpha { 
	[self setTintWithRedF:red/255 withGreenF:green/255 withBlueF:blue/255 withAlphaF:alpha/255];
}

- (void)setTintWithRedF:(float)red withGreenF:(float)green withBlueF:(float)blue withAlphaF:(float)alpha {
	//Save
	m_ColorRed		= red;
	m_ColorBlue		= blue;
	m_ColorGreen	= green;
	m_ColorAlpha	= alpha;
}

- (void)setFlipWithAngle:(float)angle {
	//Initialize
	int Add 	= 0;
	float Angle	= angle;
	
	//Set angle
	if (Angle == 0) {
		Add 	= -1;
		Angle 	= 1;
	}
	
	//Flip
	[self setFlipWithAngle:Angle increasedBy:Add];
}

- (void)setFlipWithAngle:(float)angle increasedBy:(float)increase {
	//Set flip
	if (angle != 0) m_Flip = angle;
	m_Flip += increase;
}

- (void)setMirrorHorizontally:(BOOL)horizontal andVertically:(BOOL)vertical {
	//Set mirror
	if (!horizontal && !vertical) 		m_Mirror = DRAWABLE_MIRROR_NONE;
	else if (horizontal && !vertical) 	m_Mirror = DRAWABLE_MIRROR_HORIZONTAL;
	else if (!horizontal && vertical) 	m_Mirror = DRAWABLE_MIRROR_VERTICAL;
	else 								m_Mirror = DRAWABLE_MIRROR_BOTH;
	
	//Reset rect
	[self setRegionFromX:0 fromY:0 withWidth:m_OriginalWidth withHeight:m_OriginalHeight forced:YES];
}

- (void)setRegionFromX:(float)x fromY:(float)y withWidth:(float)width withHeight:(float)height forced:(BOOL)force {
	//Invalid
	[self doesNotRecognizeSelector:_cmd];
}

@end
