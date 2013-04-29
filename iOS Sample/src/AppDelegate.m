//
//  AppDelegate.m
//  iOS Sample
//
//  Created by LegACy on 4/27/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "AppDelegate.h"
#import "HoleUtilities.h"
#import "HoleStateFactory.h"

@implementation AppDelegate

- (int)	getFPS						{ return 60;															}
- (int)	getBaseWidth				{ return -480;															}
- (int)	getBaseHeight				{ return 320;															}
- (NSString*) getApplicationName	{ return @"Black Holes";												}
- (NSString*) getPressSFX			{ return [UTILITIES_SFX_FOLDER stringByAppendingString:@"cursor.aiff"];	}
- (NSString*) getCursorSFX			{ return nil;															}
- (NSString*) getReleaseSFX			{ return nil;															}
- (NSString*) getFontTexture		{ return [HoleUtilities FONT_PIXEL_BOLD_TEXTURE];						}
- (BOOL) isFontSmooth				{ return NO;															}
- (NSString*) getFont				{ return [HoleUtilities FONT_PIXEL_BOLD];								}

- (void)createViewController {
	//Initialize
	[[ARKUtilities instance] setSystem:self];
	
	//Set view controller
	NSString* NibFile	= [[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone ? @"ViewController_iPhone" : @"ViewController_iPad";
	self.viewController = [[ARKViewController alloc] initWithNibName:NibFile bundle:nil];
}

- (void)initialize {
	//Start state manager
	[[ARKStateManager instance] setupWithFactory:[[HoleStateFactory alloc] init]];
}

@end
