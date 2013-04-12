//
//  Utilities.m
//  Ark Framework
//
//  Created by LegACy on 4/12/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import <math.h>
#import "ARKUtilities.h"
#import "ARKiOsUtilities.h"
#import "ARKSystem.h"

//Folder constants
const NSString* UTILITIES_DATA_FOLDER		= @"data/";
const NSString* UTILITIES_AUDIO_FOLDER		= @"audio/";
const NSString* UTILITIES_IMAGE_FOLDER		= @"images/";
const NSString* UTILITIES_TEXTURE_FOLDER	= @"textures/";
const NSString* UTILITIES_FONT_TEXTURES		= @"textures/fonts/";
const NSString* UTILITIES_STRING_FOLDER		= @"data/strings/";
const NSString* UTILITIES_FONT_FOLDER		= @"data/fonts/";
const NSString* UTILITIES_BGM_FOLDER		= @"audio/bgm/";
const NSString* UTILITIES_SFX_FOLDER		= @"audio/sfx/";

@implementation ARKUtilities

//Synthesize properties
@synthesize font			= m_Font;
@synthesize name			= m_Name;
@synthesize pressSFX		= m_PressSFX;
@synthesize cursorSFX		= m_CursorSFX;
@synthesize releaseSFX		= m_ReleaseSFX;
@synthesize fontTexture		= m_FontTexture;
@synthesize heightAsBase	= m_HeightAsBase;
@synthesize fontSmooth		= m_FontSmooth;
@synthesize baseHeight		= m_BaseHeight;
@synthesize baseWidth		= m_BaseWidth;
@synthesize fps				= m_FPS;

- (id)init {
	//Super
	self = [super init];
	if (self) {
		m_FPS			= 1;
		m_BaseWidth		= 1;
		m_BaseHeight	= 1;
		m_FontSmooth	= true;
		m_HeightAsBase	= false;
		m_FontTexture	= nil;
		m_ReleaseSFX	= nil;
		m_CursorSFX		= nil;
		m_PressSFX		= nil;
		m_Font			= nil;
		m_Name			= @"";
	}
	
	//Return
	return self;
}

+ (ARKUtilities*)instance {
	return [ARKiOSUtilities instance];
}

- (void)setSystem:(id<ARKSystem>)system {
	//If exist
	if (system) {
		//Save
		m_FPS			= [system getFPS];
		m_Font			= [system getFont];
		m_Name			= [system getApplicationName];
		m_FontTexture	= [system getFontTexture];
		m_ReleaseSFX	= [system getReleaseSFX];
		m_FontSmooth	= [system isFontSmooth];
		m_CursorSFX		= [system getCursorSFX];
		m_PressSFX		= [system getPressSFX];
		
		//Get base size
		m_BaseWidth		= [system getBaseWidth];
		m_BaseHeight	= [system getBaseHeight];
		if (m_BaseWidth < 0) 			m_BaseWidth *= -1;
		if (m_BaseHeight < 0) 			m_BaseHeight *= -1;
		if ([system getBaseHeight] > 0) m_HeightAsBase = YES;
	}
}

//Abstracts
- (int)getRow															{ return 1;		}
- (int)getColumn;														{ return 1;		}
- (float)getScale;														{ return 1;		}
- (float)getWidth;														{ return 1;		}
- (float)getHeight;														{ return 1;		}
- (int)getARandomNumberBetween:(int)from to:(int)to						{ return 1;		}
- (NSString*)writeFloat:(float)number withDecimalDigit:(int)decimal;	{ return nil;	}
- (NSString*)writeVersion:(NSArray*)version limitedIn:(NSArray*)digits;	{ return nil;	}
- (NSString*)writeVersion:(NSArray*)version;							{ return nil;	}

//Open URL
- (void)openURL:(NSString*)url;														{ [self openURL:url inBrowser:YES];										}
- (void)openURL:(NSString*)url inBrowser:(BOOL)browser;								{ [self openURL:url inBrowser:browser withTitle:nil];					}
- (void)openURL:(NSString*)url inBrowser:(BOOL)browser withTitle:(NSString*)title;	{ [self openURL:url inBrowser:browser withTitle:title withLoading:nil];	}
- (void)openURL:(NSString *)url inBrowser:(BOOL)browser withTitle:(NSString *)title withLoading:(NSString*)loading {
	//Do nothing (yet)
}

- (void)openAppPage:(NSString*)app {
	//Do nothing (yet)
}

- (int)getEuclideanDistanceFromX:(int)x1 fromY:(int)y1 toX:(int)x2 toY:(int)y2 {
	//Calculate
	return abs(x2 - x1) +abs(y2 - y1);
}

- (NSArray*)getDigitsFromNumber:(int)number limitedTo:(int)length {
	//Default
	NSMutableArray* Digits = [NSMutableArray arrayWithCapacity:length];
	
	//For each digit
	int Number = number;
	for (int i = length - 1; i >= 0; i--) {
		//Calculate
		[Digits replaceObjectAtIndex:i withObject:[NSNumber numberWithInt:(Number % 10)]];
		Number = (Number - [[Digits objectAtIndex:i] intValue]) / 10;
	}
	
	//Return
	return [NSArray arrayWithArray:Digits];
}

- (NSArray*)getDigitsFromNumber:(int)number {
	//If number is 0, return 0
	if (number <= 0) return [NSArray arrayWithObject:[NSNumber numberWithInt:0]];
	
	//Default
	NSMutableArray* Digits = [NSMutableArray array];
	
	//While not all
	int Number = number;
	while (Number > 0) {
		//Get current
		int Current = Number % 10;
		[Digits addObject:[NSNumber numberWithInt:Current]];
		
		//Change number
		Number = (Number - Current) / 10;
	}
	
	//Return
	return [NSArray arrayWithArray:Digits];
}

@end
