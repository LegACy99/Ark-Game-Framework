//
//  ARKiOSDevice.m
//  Ark Framework
//
//  Created by LegACy on 4/20/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKiOSDevice.h"

@implementation ARKiOSDevice

- (id)init {
	//Super
	self = [super init];
	if (self) {
		//
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

- (void)openURL:(NSString *)url inBrowser:(BOOL)browser withTitle:(NSString *)title withLoading:(NSString *)loading {
	//Do nothing (yet)
}

- (void)openAppPage:(NSString *)app {
	//Do nothing (yet)
}

@end
