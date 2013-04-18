//
//  ARKSprite.m
//  Ark Framework
//
//  Created by LegACy on 4/14/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKSprite.h"
#import "ARKiOSSprite.h"

@implementation ARKSprite

//Synthesize properties
@synthesize frame = m_Frame;
@synthesize total = m_Total;

- (id)init {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		m_Frame		= 0;
		m_Total		= 0;
		m_Delay		= 0;
		m_Timer		= 0;
		m_Animating	= false;
	}
	
	//Return
	return self;
}

- (id)initAtX:(float)x atY:(float)y withDelay:(long)delay {
	//Default
	self = [self init];
	if (self) {
		//Save
		m_X	= x;
		m_Y	= y;
		
		//Set delay
		m_Delay = delay;
		if (m_Delay > 0) m_Animating = true;
	}
	
	//Return
	return self;
}

//Creators
+ (ARKSprite*)createFromPath:(NSString*)path							{ return [ARKSprite createFromPath:path withDelay:0];					}
+ (ARKSprite*)createFromPath:(NSString*)path withDelay:(long)delay		{ return [ARKSprite createFromPath:path atX:0 atY:0 withDelay:delay];	}
+ (ARKSprite*)createFromPath:(NSString*)path atX:(float)x atY:(float)y	{ return [ARKSprite createFromPath:path atX:x atY:y withDelay:0];		}
+ (ARKSprite*)createFromPath:(NSString*)path atX:(float)x atY:(float)y withDelay:(long)delay {
	return [[ARKiOSSprite alloc] initWithPath:path AtX:x atY:y withDelay:delay];
}

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
	//Don't use (abstract)
	[self doesNotRecognizeSelector:_cmd];
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
	//Don't use (abstract)
	[self doesNotRecognizeSelector:_cmd];
}

- (void)setMirrorHorizontally:(BOOL)horizontal andVertically:(BOOL)vertical {
	//Don't use (abstract)
	[self doesNotRecognizeSelector:_cmd];
}

- (void)setAnimating:(BOOL)animating {
	//Set
	m_Animating = animating;
}

- (void)nextFrame {
	//Increase
	m_Frame++;
	if (m_Frame >= m_Total) m_Frame = 0;
}

- (void)setFrame:(int)frame {
	//Set frame
	m_Frame = frame;
	
	//Correct
	if (m_Frame >= m_Total) m_Frame = m_Total - 1;
	else if (m_Frame < 0)	m_Frame = 0;
}

- (void)updateAfter:(long)time {
	//If animating
	if (m_Animating) {
		//Increase timer
		m_Timer += time;
		
		//If more than delay
		if (m_Timer >= m_Delay) {
			//Reset
			m_Timer -= m_Delay;
			
			//Next frame
			[self nextFrame];
		}
	}
}

@end
