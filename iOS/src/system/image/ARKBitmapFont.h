//
//  ARKBitmapFont.h
//  Ark Framework
//
//  Created by LegACy on 4/15/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import <Foundation/Foundation.h>

//Forward declaration
@class ARKBitmapChar;

@interface ARKBitmapFont : NSObject {
	//Data
	float		m_Size;
	float		m_Height;
	NSString*	m_Texture;
}

//Properties
@property (readonly, getter = getHeight) float height;
@property (readonly, getter = getTexture) NSString* texture;

//Factory function
+ (ARKBitmapFont*)createFromJSON:(NSDictionary*)json;

//Functions
- (id)initWithJSON:(NSDictionary*)json;
- (ARKBitmapChar*)getCharForCharacter:(char)character;

@end

//Constants
extern const NSString* FONT_KEY_CHAR;
extern const NSString* FONT_KEY_CHARS;
extern const NSString* FONT_KEY_CHAR_INDEX;
extern const NSString* FONT_KEY_KERNING_FIRST;
extern const NSString* FONT_KEY_KERNING_SECOND;
extern const NSString* FONT_KEY_KERNING_OFFSET;
extern const NSString* FONT_KEY_KERNINGS;
extern const NSString* FONT_KEY_KERNING;
extern const NSString* FONT_KEY_FILE;
extern const NSString* FONT_KEY_PAGE;
extern const NSString* FONT_KEY_PAGES;
extern const NSString* FONT_KEY_FONT;
extern const NSString* FONT_KEY_INFO;
extern const NSString* FONT_KEY_SIZE;
extern const NSString* FONT_KEY_WIDTH;
extern const NSString* FONT_KEY_HEIGHT;
extern const NSString* FONT_KEY_LAYOUT;
extern const NSString* FONT_KEY_LINE;
