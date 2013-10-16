//
//  ARKSprite.h
//  Ark Framework
//
//  Created by LegACy on 4/14/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKCroppable.h"

@interface ARKSprite : ARKCroppable {
	//Data
	int		m_Frame;
	int		m_Total;
	BOOL	m_Animating;
	long	m_Delay;
	long	m_Timer;
}

//Properties
@property (readonly, getter = getFrame)		int frame;
@property (readonly, getter = getMaxFrame)	int total;

//Constructor
- (id)initAtX:(float)x atY:(float)y withDelay:(long)delay;

//Creators
+ (ARKSprite*)createFromPath:(NSString*)path;
+ (ARKSprite*)createFromPath:(NSString*)path withDelay:(long)delay;
+ (ARKSprite*)createFromPath:(NSString*)path atX:(float)x atY:(float)y;
+ (ARKSprite*)createFromPath:(NSString*)path atX:(float)x atY:(float)y withDelay:(long)delay;

//Rotation setters
- (void)setRotationWithAngle:(float)angle;
- (void)setRotationWithAngle:(float)angle increasedBy:(float)increase;
- (void)setRotationWithAngle:(float)angle pivotAtX:(float)x pivotAtY:(float)y;
- (void)setRotationWithAngle:(float)angle increasedBy:(float)increase pivotAtX:(float)x pivotAtY:(float)y;

//Tint functions
- (void)setTintWithRed:(int)red withGreen:(int)green withBlue:(int)blue;
- (void)setTintWithRedF:(float)red withGreenF:(float)green withBlueF:(float)blue;
- (void)setTintWithRed:(int)red withGreen:(int)green withBlue:(int)blue withAlpha:(int)alpha;
- (void)setTintWithRedF:(float)red withGreenF:(float)green withBlueF:(float)blue withAlphaF:(float)alpha;

- (void)setMirrorHorizontally:(BOOL)horizontal andVertically:(BOOL)vertical;

//Animation setters
- (void)setAnimating:(BOOL)animating;
- (void)setDelay:(long)delay;
- (void)setFrame:(int)frame;
- (void)nextFrame;

//Update
- (void)updateAfter:(long)time;

@end
