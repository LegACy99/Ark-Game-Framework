//
//  AppDelegate.m
//  Ark Framework
//
//  Created by LegACy on 4/6/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

//Import
#import "ARKGameDelegate.h"
#import "ARKViewController.h"

//Class
@implementation ARKGameDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
	//Create view controller
	[self createViewController];
	
	//Create window
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    self.window.rootViewController = self.viewController;
    [self.window makeKeyAndVisible];
	
	//Initialize
	[self initialize];
	
	//Return okay
    return YES;
}

- (void)createViewController {
	//Do nothing
}

- (void)initialize {
	//Do nothing
}

@end
