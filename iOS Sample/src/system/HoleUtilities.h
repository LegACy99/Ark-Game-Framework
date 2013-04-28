//
//  HoleUtilities.h
//  ARK Framework Example
//
//  Created by LegACy on 4/28/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface HoleUtilities : NSObject

//Font
+ (NSString*)FONT_COIN;
+ (NSString*)FONT_SCORE;
+ (NSString*)FONT_PIXEL;
+ (NSString*)FONT_PIXEL_BOLD;
+ (NSString*)FONT_PIXEL_SMALL;
+ (NSString*)FONT_CAPS_SMALL;

//Font textures
+ (NSString*)FONT_COIN_TEXTURE;
+ (NSString*)FONT_SCORE_TEXTURE;
+ (NSString*)FONT_PIXEL_TEXTURE;
+ (NSString*)FONT_PIXEL_BOLD_TEXTURE;
+ (NSString*)FONT_PIXEL_SMALL_TEXTURE;
+ (NSString*)FONT_CAPS_SMALL_TEXTURE;

@end

//Constants
extern const NSString* UTIL_GAME_IMAGES;
extern const NSString* UTIL_INTERFACE_IMAGES;
