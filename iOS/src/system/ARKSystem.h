//
//  System.h
//  Ark Framework
//
//  Created by LegACy on 4/12/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol ARKSystem <NSObject>

//Game data
- (int)			getFPS;
- (int)			getBaseWidth;
- (int)			getBaseHeight;
- (NSString*)	getApplicationName;

//Game default resources
- (NSString*)	getPressSFX;
- (NSString*)	getCursorSFX;
- (NSString*)	getReleaseSFX;
- (NSString*)	getFontTexture;
- (BOOL)		isFontSmooth;
- (NSString*)	getFont;

@end
