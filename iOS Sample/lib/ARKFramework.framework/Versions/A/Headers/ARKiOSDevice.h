//
//  ARKiOSDevice.h
//  Ark Framework
//
//  Created by LegACy on 4/20/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKDevice.h"
#import <GLKit/GLKit.h>

//Forward declaration
@class ARKViewController;

@interface ARKiOSDevice : ARKDevice {
	//Data
	GLKMatrix4			m_ViewMatrix;
	ARKViewController*	m_ViewController;
}

//Property
@property (readonly, getter = getViewMatrix) GLKMatrix4 viewMatrix;

//Factory
+ (ARKiOSDevice*)instance;

//Getters
- (long) getDeltaTime;
- (GLKBaseEffect*) getGL;

//Setters
- (void)setupViewController:(ARKViewController*)controller;
- (void)setSizeWithWidth:(float)width withHeight:(float)height;

@end
