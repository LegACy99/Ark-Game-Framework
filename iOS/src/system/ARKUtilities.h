//
//  Utilities.h
//  Ark Framework
//
//  Created by LegACy on 4/12/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import <Foundation/Foundation.h>

//Forward declaration
@protocol ARKSystem;

//Class declaration
@interface ARKUtilities : NSObject {
	//Protected variables
	NSString*	m_Name;
	NSString*	m_Font;
	NSString*	m_PressSFX;
	NSString*	m_CursorSFX;
	NSString*	m_ReleaseSFX;
	NSString*	m_FontTexture;
	BOOL		m_HeightAsBase;
	BOOL		m_FontSmooth;
	int			m_BaseHeight;
	int			m_BaseWidth;
	int			m_FPS;
}

//Properties
@property (readonly, getter = getSystemFont)			NSString* font;
@property (readonly, getter = getApplicationName)		NSString* name;
@property (readonly, getter = getSystemPressSFX)		NSString* pressSFX;
@property (readonly, getter = getSystemCursorSFX)		NSString* cursorSFX;
@property (readonly, getter = getSystemReleaseSFX)		NSString* releaseSFX;
@property (readonly, getter = getFontTexture)			NSString* fontTexture;
@property (readonly, getter = isSystemBasedOnHeight)	BOOL heightAsBase;
@property (readonly, getter = isSystemFontSmooth)		BOOL fontSmooth;
@property (readonly, getter = getBaseHeight)			int baseHeight;
@property (readonly, getter = getBaseWidth)				int baseWidth;
@property (readonly, getter = getSystemFPS)				int fps;

//Singleton instance
+ (ARKUtilities*)instance;

//Functions
- (void)setSystem:(id<ARKSystem>)system;

//Display abstracts
- (int)getRow;
- (int)getColumn;
- (float)getScale;
- (float)getWidth;
- (float)getHeight;

//Utilities
- (int)getARandomNumberBetween:(int)from to:(int)to;
- (NSString*)writeFloat:(float)number withDecimalDigit:(int)decimal;
- (NSString*)writeVersion:(NSArray*)version limitedIn:(NSArray*)digits;
- (NSString*)writeVersion:(NSArray*)version;

//Operating system functions
- (void)openURL:(NSString*)url;
- (void)openURL:(NSString*)url inBrowser:(BOOL)browser;
- (void)openURL:(NSString*)url inBrowser:(BOOL)browser withTitle:(NSString*)title;
- (void)openURL:(NSString *)url inBrowser:(BOOL)browser withTitle:(NSString *)title withLoading:(NSString*)loading;
- (void)openAppPage:(NSString*)app;

//Math stuff
- (int)getEuclideanDistanceFromX:(int)x1 fromY:(int)y1 toX:(int)x2 toY:(int)y2;
- (NSArray*)getDigitsFromNumber:(int)number limitedTo:(int)length;
- (NSArray*)getDigitsFromNumber:(int)number;

@end

//Constants
extern const NSString* UTILITIES_DATA_FOLDER;
extern const NSString* UTILITIES_AUDIO_FOLDER;
extern const NSString* UTILITIES_IMAGE_FOLDER;
extern const NSString* UTILITIES_TEXTURE_FOLDER;
extern const NSString* UTILITIES_FONT_TEXTURES;
extern const NSString* UTILITIES_STRING_FOLDER;
extern const NSString* UTILITIES_FONT_FOLDER;
extern const NSString* UTILITIES_BGM_FOLDER;
extern const NSString* UTILITIES_SFX_FOLDER;
