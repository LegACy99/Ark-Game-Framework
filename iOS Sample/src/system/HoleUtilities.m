//
//  HoleUtilities.m
//  ARK Framework Example
//
//  Created by LegACy on 4/28/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "HoleUtilities.h"
#import <ARKFramework/ARKFramework.h>

//Folders
const NSString* UTIL_GAME_IMAGES		= @"images/game/";
const NSString* UTIL_INTERFACE_IMAGES	= @"images/interface/";

@implementation HoleUtilities

//No constructor
- (id)init { return nil; }

//Constants
+ (NSString*)FONT_COIN					{ return [UTILITIES_FONT_FOLDER stringByAppendingString:@"coin.json"];			}
+ (NSString*)FONT_SCORE					{ return [UTILITIES_FONT_FOLDER stringByAppendingString:@"score.json"];			}
+ (NSString*)FONT_PIXEL					{ return [UTILITIES_FONT_FOLDER stringByAppendingString:@"pixel.json"];			}
+ (NSString*)FONT_PIXEL_BOLD			{ return [UTILITIES_FONT_FOLDER stringByAppendingString:@"pixel_bold.json"];	}
+ (NSString*)FONT_PIXEL_SMALL			{ return [UTILITIES_FONT_FOLDER stringByAppendingString:@"pixel-small.json"];	}
+ (NSString*)FONT_CAPS_SMALL			{ return [UTILITIES_FONT_FOLDER stringByAppendingString:@"caps-small.json"];	}
+ (NSString*)FONT_COIN_TEXTURE			{ return [UTILITIES_FONT_TEXTURES stringByAppendingString:@"coin.png"];			}
+ (NSString*)FONT_SCORE_TEXTURE			{ return [UTILITIES_FONT_TEXTURES stringByAppendingString:@"score.png"];		}
+ (NSString*)FONT_PIXEL_TEXTURE			{ return [UTILITIES_FONT_TEXTURES stringByAppendingString:@"pixel.png"];		}
+ (NSString*)FONT_PIXEL_BOLD_TEXTURE	{ return [UTILITIES_FONT_TEXTURES stringByAppendingString:@"pixel-bold.png"];	}
+ (NSString*)FONT_PIXEL_SMALL_TEXTURE	{ return [UTILITIES_FONT_TEXTURES stringByAppendingString:@"pixel-small.png"];	}
+ (NSString*)FONT_CAPS_SMALL_TEXTURE	{ return [UTILITIES_FONT_TEXTURES stringByAppendingString:@"caps-small.png"];	}

@end
