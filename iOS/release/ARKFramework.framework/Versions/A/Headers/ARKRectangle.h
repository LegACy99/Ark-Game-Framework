//
//  ARKRectangle.h
//  Ark Framework
//
//  Created by LegACy on 4/26/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKCroppable.h"

@interface ARKRectangle : ARKCroppable {
	//Data
	float 	m_Flip;
	float 	m_Rotation;
	float	m_Color[4];
}

//Factory method
+ (ARKRectangle*)createWithWidth:(float)width withHeight:(float)height;
+ (ARKRectangle*)createFromX:(float)x fromY:(float)y withWidth:(float)width withHeight:(float)height;
+ (ARKRectangle*)createFromX:(float)x fromY:(float)y withWidth:(float)width withHeight:(float)height withRed:(float)red withGreen:(float)green withBlue:(float)blue withAlpha:(float)alpha;

//Accessors
- (float)getRedComponent;
- (float)getBlueComponent;
- (float)getGreenComponent;
- (float)getAlphaComponent;

//Setters
- (void)setRectWithWidth:(float)width withHeight:(float)height;
- (void)setColorWithRed:(float)red withGreen:(float)green withBlue:(float)blue withAlpha:(float)alpha;

@end

//Constants
extern const int RECTANGLE_INDEX_RED;
extern const int RECTANGLE_INDEX_BLUE;
extern const int RECTANGLE_INDEX_GREEN;
extern const int RECTANGLE_INDEX_ALPHA;
