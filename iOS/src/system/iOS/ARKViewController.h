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
	NSArray*				m_Touches;
	NSMutableDictionary*	m_UITouches;
	ARKAccelerometerInfo*	m_Accelerometer;
}

//Properties
@property (readonly, getter = getTouches)		NSArray* touches;
@property (readonly, getter = getAccelerometer) ARKAccelerometerInfo* accelerometer;

@end
