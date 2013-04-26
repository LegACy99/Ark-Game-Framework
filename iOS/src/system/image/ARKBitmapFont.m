//
//  ARKBitmapFont.m
//  Ark Framework
//
//  Created by LegACy on 4/15/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKBitmapFont.h"
#import "ARKiOSBitmapFont.h"
#import "ARKUtilities.h"

//Character constants
const NSString* FONT_KEY_CHAR		= @"char";
const NSString* FONT_KEY_CHARS		= @"chars";
const NSString* FONT_KEY_CHAR_INDEX	= @"id";

//Kerning constants
const NSString* FONT_KEY_KERNING_FIRST	= @"first";
const NSString* FONT_KEY_KERNING_SECOND	= @"second";
const NSString* FONT_KEY_KERNING_OFFSET	= @"amount";
const NSString* FONT_KEY_KERNINGS		= @"kernings";
const NSString* FONT_KEY_KERNING		= @"kerning";

//Pages constants
const NSString* FONT_KEY_FILE	= @"file";
const NSString* FONT_KEY_PAGE	= @"page";
const NSString* FONT_KEY_PAGES	= @"pages";

//Others
const NSString* FONT_KEY_FONT	= @"font";
const NSString* FONT_KEY_INFO	= @"info";
const NSString* FONT_KEY_SIZE	= @"size";
const NSString* FONT_KEY_WIDTH	= @"scalew";
const NSString* FONT_KEY_HEIGHT	= @"scaleh";
const NSString* FONT_KEY_LAYOUT	= @"common";
const NSString* FONT_KEY_LINE	= @"lineheight";

@implementation ARKBitmapFont

//Synthesize properties
@synthesize height	= m_Height;
@synthesize texture = m_Texture;

- (id)init {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		m_Size		= 0;
		m_Height	= 0;
		m_Texture	= nil;
	}
	
	//Return
	return self;
}

- (id)initWithJSON:(NSDictionary *)json {
	//INitialize
	self = [self init];
	if (self && json) {
		//Read json
		NSDictionary* JSONFont = [json objectForKey:FONT_KEY_FONT];
		if (JSONFont) {
			//Read common layout
			NSDictionary* JSONInfo = [JSONFont objectForKey:FONT_KEY_INFO];
			NSDictionary* JSONData = [JSONFont objectForKey:FONT_KEY_LAYOUT];
			if (JSONData) m_Height = [[JSONData objectForKey:FONT_KEY_LINE] intValue];
			if (JSONInfo) m_Size = [[JSONInfo objectForKey:FONT_KEY_SIZE] intValue];
			
			//Get pages data
			NSDictionary* JSONPages = [JSONFont objectForKey:FONT_KEY_PAGES];
			if (JSONPages) {
				//Get texture name
				NSDictionary* JSONPage	= [JSONPages objectForKey:FONT_KEY_PAGE];
				if (JSONPage) m_Texture = [UTILITIES_FONT_TEXTURES stringByAppendingString:[JSONPage objectForKey:FONT_KEY_FILE]];
			}
		}
	}
	
	//Return
	return self;
}

+ (ARKBitmapFont*)createFromJSON:(NSDictionary *)json {
	return [[ARKiOSBitmapFont alloc] initWithJSON:json];
}

- (ARKBitmapChar*)getCharForCharacter:(char)character {
	//Don't use, abstract
	return nil;
}

@end
