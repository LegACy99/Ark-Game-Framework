//
//  ARKViewController.h
//  Ark Framework
//
//  Created by LegACy on 4/20/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import <GLKit/GLKit.h>

//Class
@class ARKAccelerometerInfo;

@interface ARKViewController : GLKViewController<GLKViewControllerDelegate> {
	//Data
	BOOL					m_Size;
	NSArray*				m_Touches;
	EAGLContext*			m_Context;
	NSMutableDictionary*	m_UITouches;
	ARKAccelerometerInfo*	m_Accelerometer;
	GLKBaseEffect*			m_OpenGL;
}

//Properties
@property (readonly, getter = getTouches)		NSArray* touches;
@property (readonly, getter = getAccelerometer) ARKAccelerometerInfo* accelerometer;
@property (readonly, getter = getGL)			GLKBaseEffect* gl;

@end
