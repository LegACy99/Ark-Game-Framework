//
//  ARKiOSRectangle.h
//  Ark Framework
//
//  Created by LegACy on 4/26/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import <GLKit/GLKit.h>
#import "ARKRectangle.h"

@interface ARKiOSRectangle : ARKRectangle {
	//Data
	GLfloat m_Attributes[24];
}

//Constructor
- (id)initFromX:(float)x fromY:(float)y withWidth:(float)width withHeight:(float)height withRed:(float)red withGreen:(float)green withBlue:(float)blue withAlpha:(float)alpha;

@end
