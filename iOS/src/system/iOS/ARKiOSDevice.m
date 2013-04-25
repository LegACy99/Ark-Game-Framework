//
//  ARKiOSDevice.m
//  Ark Framework
//
//  Created by LegACy on 4/20/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKiOSDevice.h"
#import "ARKUtilities.h"
#import "ARKViewController.h"

@implementation ARKiOSDevice

//Synthesize
@synthesize viewMatrix = m_ViewMatrix;

- (id)init {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		m_ViewController	= nil;
		m_ViewMatrix		= GLKMatrix4Identity;
	}
	
	//Return
	return self;
}

+ (ARKDevice*)instance {
	//Static objects
	static dispatch_once_t Token	= 0;
	static ARKiOSDevice* Instance	= nil;
	
	//Only create object once
	dispatch_once(&Token, ^{
		Instance = [[self alloc] init];
	});
	
	//Return
	return Instance;
}

- (long)getDeltaTime {
	//Check
	if (m_ViewController)	return [m_ViewController getTime];
	else					return 0;
}

- (GLKBaseEffect*)getGL {
	//Check
	if (m_ViewController)	return [m_ViewController getGL];
	else					return nil;
}

- (void)setupViewController:(ARKViewController *)controller {
	//Save
	m_ViewController = controller;
	if (controller) {
		//Get stuff
		m_Touches		= [m_ViewController getTouches];
		m_Accelerometer	= [m_ViewController getAccelerometer];
	}
}

- (void)setSizeWithWidth:(float)width withHeight:(float)height {
	//if no initial value
	if (m_Width == 0 || m_Height == 0) {
		//Save size
		m_Width		= width;
		m_Height	= height;
	}
	
	//No column / row
	m_Row		= 1;
	m_Column	= 1;
	
	//Calculate scale
	if ([[ARKUtilities instance] isSystemBasedOnHeight]) 	m_Scale = m_Height / [[ARKUtilities instance] getBaseHeight];
	else													m_Scale = m_Width / [[ARKUtilities instance] getBaseWidth];
	
	//Calculate matrix
	m_ViewMatrix = GLKMatrix4MakeLookAt(0, 0, m_Height / 2, 0, 0, 0, 0, 1, 0);
}

- (void)openURL:(NSString *)url inBrowser:(BOOL)browser withTitle:(NSString *)title withLoading:(NSString *)loading {
	//Open in browser
	[[UIApplication sharedApplication] openURL:[NSURL URLWithString:url]];
}

- (void)openAppPage:(NSString *)app {
	//Open iTUnes
	[[UIApplication sharedApplication] openURL:[NSURL URLWithString:app]];
}

@end
