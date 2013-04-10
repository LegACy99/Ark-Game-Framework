//
//  ARKImage.h
//  Ark Framework
//
//  Created by LegACy on 4/6/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

//Header imports
#import "ARKCroppable.h"

//Class declaration
@interface ARKImage : ARKCroppable {
	//Data
	float	m_Flip;
	float	m_Rotation;
	float	m_ColorRed;
	float	m_ColorBlue;
	float	m_ColorGreen;
	float	m_ColorAlpha;
	float	m_PivotX;
	float	m_PivotY;
	int		m_Mirror;
}

//Init functions
- (id)initFromFile:(NSString*)file atX:(float)x atY:(float)y;
- (id)initFromJSON:(id)json atX:(float)x atY:(float)y;

//Static functions
+ (ARKImage*)createFromPath:(NSString*)resource;
+ (ARKImage*)createFromJSON:(NSDictionary*)json;
+ (ARKImage*)createFromPath:(NSString*)resource atX:(float)x atY:(float)y;
+ (ARKImage*)createFromJSON:(NSDictionary*)json atX:(float)x atY:(float)y;

//Rotation functions
- (void)setRotationWithAngle:(float)angle;
- (void)setRotationWithAngle:(float)angle increasedBy:(float)increase;
- (void)setRotationWithAngle:(float)angle pivotAtX:(float)x pivotAtY:(float)y;
- (void)setRotationWithAngle:(float)angle increasedBy:(float)increase pivotAtX:(float)x pivotAtY:(float)y;

//Tint functions
- (void)setTintWithRed:(int)red withGreen:(int)green withBlue:(int)blue;
- (void)setTintWithRedF:(float)red withGreenF:(float)green withBlueF:(float)blue;
- (void)setTintWithRed:(int)red withGreen:(int)green withBlue:(int)blue withAlpha:(int)alpha;
- (void)setTintWithRedF:(float)red withGreenF:(float)green withBlueF:(float)blue withAlphaF:(float)alpha;

//Other functions
- (void)setFlipWithAngle:(float)angle;
- (void)setFlipWithAngle:(float)angle increasedBy:(float)increase;
- (void)setMirrorHorizontally:(BOOL)horizontal andVertically:(BOOL)vertical;
- (void)setRegionfromX:(float)x fromY:(float)y withWidth:(float)width withHeight:(float)height forced:(BOOL)force;

//Constants
extern const NSString* IMAGE_KEY_RECT;
extern const NSString* IMAGE_KEY_RECT_TOP;
extern const NSString* IMAGE_KEY_RECT_LEFT;
extern const NSString* IMAGE_KEY_RECT_RIGHT;
extern const NSString* IMAGE_KEY_RECT_BOTTOM;
extern const NSString* IMAGE_KEY_RECT_HEIGHT;
extern const NSString* IMAGE_KEY_RECT_WIDTH;
extern const NSString* IMAGE_KEY_TEXTURE;

@end
