//
//  AppDelegate.m
//  Ark Framework
//
//  Created by LegACy on 4/6/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

//Import
#import "AppDelegate.h"
#import "ARKViewController.h"
#import "ARKStateManager.h"
#import "HolesStateFactory.h"
#import "ARKUtilities.h"

//Class
@implementation AppDelegate

- (int)	getFPS						{ return 60;				}
- (int)	getBaseWidth				{ return 320;				}
- (int)	getBaseHeight				{ return -480;				}
- (NSString*) getApplicationName	{ return @"Black Holes";	}
- (NSString*) getPressSFX			{ return nil;				}
- (NSString*) getCursorSFX			{ return nil;				}
- (NSString*) getReleaseSFX			{ return nil;				}
- (NSString*) getFontTexture		{ return @"pixel";			}
- (BOOL) isFontSmooth				{ return NO;				}
- (NSString*) getFont				{ return @"pixel";			}

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
	//Initialize
	[[ARKUtilities instance] setSystem:self];
	
	//Set view controller
	NSString* NibFile	= [[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone ? @"ViewController_iPhone" : @"ViewController_iPad";
	self.viewController = [[ARKViewController alloc] initWithNibName:NibFile bundle:nil];
	
	//Create window
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    self.window.rootViewController = self.viewController;
    [self.window makeKeyAndVisible];
	
	//Start ark state manager
	[[ARKStateManager instance] setupWithFactory:[[HolesStateFactory alloc] init]];
	
	//Return okay
    return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

@end
