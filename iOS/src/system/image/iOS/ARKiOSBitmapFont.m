//
//  ARKiOSBitmapFont.m
//  Ark Framework
//
//  Created by LegACy on 4/15/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKiOSBitmapFont.h"
#import "ARKBitmapChar.h"

@implementation ARKiOSBitmapFont

- (id)initWithJSON:(NSDictionary *)json {
	//Super
	self = [super initWithJSON:json];
	if (self && json) {
		//Create
		NSMutableDictionary* Characters = [NSMutableDictionary dictionary];
		
		//Read json
		NSDictionary* JSONFont = [json objectForKey:FONT_KEY_FONT];
		if (JSONFont) {
			//Read layout
			float Width				= 0;
			float Height			= 0;
			NSDictionary* JSONData = [JSONFont objectForKey:FONT_KEY_LAYOUT];
			if (JSONData) {
				//Get texture size
				Width	= [[JSONData objectForKey:FONT_KEY_WIDTH] intValue];
				Height	= [[JSONData objectForKey:FONT_KEY_HEIGHT] intValue];
			}
			
			//Create kerning map
			NSMutableDictionary* Kernings	= [NSMutableDictionary dictionary];
			NSDictionary* JSONKernings		= [JSONFont objectForKey:FONT_KEY_KERNINGS];
			if (JSONKernings) {
				//Get kerning data
				NSArray* KerningsArray = [JSONKernings objectForKey:FONT_KEY_KERNING];
				if (KerningsArray) {
					//For each kerning
					for (int i = 0; i < [KerningsArray count]; i++) {
						//Get data
						NSDictionary* JSONKerning 	= [KerningsArray objectAtIndex:i];
						NSNumber* SecondChar		= [JSONKerning objectForKey:FONT_KEY_KERNING_FIRST];
						NSNumber* FirstChar			= [JSONKerning objectForKey:FONT_KEY_KERNING_SECOND];
						NSNumber* Offset			= [JSONKerning objectForKey:FONT_KEY_KERNING_OFFSET];
						
						//Get map
						NSMutableDictionary* Kerning = [Kernings objectForKey:FirstChar];
						if (!Kerning) {
							//Add to map
							Kerning	= [NSMutableDictionary dictionary];
							[Kernings setObject:Kerning forKey:FirstChar];
						}
						
						//Add to map
						[Kerning setObject:Offset forKey:SecondChar];
					}
				}
			}
			
			//Get font data
			NSDictionary* JSONCharacters = [JSONFont objectForKey:FONT_KEY_CHARS];
			if (JSONCharacters) {
				//Get characters
				NSArray* CharactersArray = [JSONCharacters objectForKey:FONT_KEY_CHAR];
				if (CharactersArray) {
					//For each character
					for (int i = 0; i < [CharactersArray count]; i++) {
						//Get index
						NSDictionary* JSONChar	= [CharactersArray objectAtIndex:i];
						NSNumber *Index			= [JSONChar objectForKey:FONT_KEY_CHAR_INDEX];
						
						//Create character
						ARKBitmapChar* Char = [[ARKBitmapChar alloc] initWithJSON:JSONChar withKernings:[Kernings objectForKey:Index] withWidth:Width withHeight:Height];
						[Characters setObject:Char forKey:Index];
						
						//Check height
						float CharHeight = [Char getHeight] + [Char getOffsetY];
						if (CharHeight > m_Height) m_Height = CharHeight;
					}
				}
			}
		}
		
		//Save dictionary
		m_Characters = Characters;
	}
	
	//Return
	return self;
}

- (ARKBitmapChar*)getCharForCharacter:(char)character {
	//Return character
	return [m_Characters objectForKey:[NSNumber numberWithChar:character]];
}

@end
