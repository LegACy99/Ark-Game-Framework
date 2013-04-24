//
//  ARKiOSDevice.h
//  Ark Framework
//
//  Created by LegACy on 4/20/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKDevice.h"

//Forward declaration
@class GLKBaseEffect, ARKViewController;

@interface ARKiOSDevice : ARKDevice {
	//Data
	ARKViewController*	m_ViewController;
}

//Factory
+ (ARKiOSDevice*)instance;

//Getters and setters
- (GLKBaseEffect*) getGL;
- (void)setupViewController:(ARKViewController*)controller;
- (void)setSizeWithWidth:(float)width withHeight:(float)height;

@end
