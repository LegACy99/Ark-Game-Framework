//
//  ARKBitmapChar.m
//  Ark Framework
//
//  Created by LegACy on 4/15/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKBitmapChar.h"
#import "ARKUtilities.h"

//Constants
const NSString* CHAR_KEY_X			= @"x";
const NSString* CHAR_KEY_Y			= @"y";
const NSString* CHAR_KEY_WIDTH		= @"width";
const NSString* CHAR_KEY_HEIGHT		= @"height";
const NSString* CHAR_KEY_OFFSETX	= @"xoffset";
const NSString* CHAR_KEY_OFFSETY	= @"yoffset";
const NSString* CHAR_KEY_ADVANCE	= @"xadvance";

@implementation ARKBitmapChar

//Synthesize
@synthesize advance = m_Advance;

- (id)init {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		m_Advance = 0;
		m_Kerning = [NSDictionary dictionary];
		for (int i = 0; i < sizeof(m_Vertices); i++)	m_Vertices[i] = 0;
		for (int i = 0; i < sizeof(m_Coordinates); i++) m_Coordinates[i] = 0;
	}
	
	//Return
	return self;
}

- (id)initWithJSON:(NSDictionary *)json withKernings:(NSDictionary *)kernings withWidth:(float)width withHeight:(float)height {
	//Default
	self = [self init];
	if (self) {
		//Initialize
		float X 		= 0;
		float Y			= 0;
		float Width		= 0;
		float Height	= 0;
		float OffsetX	= 0;
		float OffsetY	= 0;
		
		//Save kerning
		if (kernings) m_Kerning = [NSDictionary dictionaryWithDictionary:kernings];
		
		//If json exist
		if (json) {
			//Read JSON
			X 			= [[json objectForKey:CHAR_KEY_X] intValue];
			Y 			= [[json objectForKey:CHAR_KEY_Y] intValue];
			Width		= [[json objectForKey:CHAR_KEY_WIDTH] intValue];
			Height		= [[json objectForKey:CHAR_KEY_HEIGHT] intValue];
			OffsetX		= [[json objectForKey:CHAR_KEY_OFFSETX] intValue];
			OffsetY		= [[json objectForKey:CHAR_KEY_OFFSETY] intValue];
			m_Advance	= [[json objectForKey:CHAR_KEY_ADVANCE] intValue];// * [[ARKUtilities instance] getScale];
		}
		
		//Create vertex
		m_Vertices[0]	= OffsetX;			m_Vertices[1] = -OffsetY; 				//Top left
		m_Vertices[2]	= OffsetX;			m_Vertices[3] = -(OffsetY + Height);	//Bottom left
		m_Vertices[4]	= OffsetX + Width;	m_Vertices[5] = -OffsetY;				//Top right
		m_Vertices[6]	= OffsetX + Width;	m_Vertices[7] = -(OffsetY + Height);	//Bottom right
		//for (int i = 0; i < sizeof(m_Vertices); i++) m_Vertices[i] *= [[ARKUtilities instance] getScale];
		
		//Create coordinates
		m_Coordinates[0]	= X / width;			m_Coordinates[1] = Y / height;				//Top left
		m_Coordinates[2]	= X / width;			m_Coordinates[3] = (Y + Height) / height;	//Bottom left
		m_Coordinates[4]	= (X + Width) / width;	m_Coordinates[5] = Y / height;				//Top right
		m_Coordinates[6]	= (X + Width) / width;	m_Coordinates[7] = (Y + Height) / height;	//Bottom right
	}
	
	//Return
	return self;
}

//Getters
- (float*)getVertices				{ return m_Vertices;					}
- (float*)getTextureCoordinates		{ return m_Coordinates;					}
- (float)getBottom					{ return m_Vertices[3];					}
- (float)getHeight					{ return m_Vertices[1] - m_Vertices[3];	}
- (float)getWidth					{ return m_Vertices[4] - m_Vertices[0];	}
- (float)getTop						{ return m_Vertices[1];					}

- (float)getAdvanceForChar:(char)character {
	//Get advance
	float Advance	= [self getAdvance];
	id Kerning		= [m_Kerning objectForKey:[NSNumber numberWithChar:character]];
	
	//if not nil
	if (Kerning && [Kerning isKindOfClass:[NSNumber class]]) Advance += [Kerning intValue];
	
	//Return
	return Advance;
}

@end
