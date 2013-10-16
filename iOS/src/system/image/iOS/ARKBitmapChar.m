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
@synthesize advance		= m_Advance;
@synthesize vertices	= m_Vertices;
@synthesize coordinates	= m_Coordinates;

- (id)init {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		m_Advance	= 0;
		m_Kerning	= [NSDictionary dictionary];
		m_Vertices	= [NSArray arrayWithObjects:
					   [NSNumber numberWithFloat:0], [NSNumber numberWithFloat:0], [NSNumber numberWithFloat:0], [NSNumber numberWithFloat:0],
					   [NSNumber numberWithFloat:0], [NSNumber numberWithFloat:0], [NSNumber numberWithFloat:0], [NSNumber numberWithFloat:0], nil];
		m_Coordinates = [NSArray arrayWithObjects:
						 [NSNumber numberWithFloat:0], [NSNumber numberWithFloat:0], [NSNumber numberWithFloat:0], [NSNumber numberWithFloat:0],
						 [NSNumber numberWithFloat:0], [NSNumber numberWithFloat:0], [NSNumber numberWithFloat:0], [NSNumber numberWithFloat:0], nil];
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
			m_Advance	= [[json objectForKey:CHAR_KEY_ADVANCE] intValue] * [[ARKUtilities instance] getScale];
		}
		
		//Create vertex
		float Vertices[8];
		Vertices[0]	= OffsetX;			Vertices[1] = -OffsetY; 			//Top left
		Vertices[2]	= OffsetX;			Vertices[3] = -(OffsetY + Height);	//Bottom left
		Vertices[4]	= OffsetX + Width;	Vertices[5] = -OffsetY;				//Top right
		Vertices[6]	= OffsetX + Width;	Vertices[7] = -(OffsetY + Height);	//Bottom right
		m_Vertices = [NSArray arrayWithObjects:
					  [NSNumber numberWithFloat:Vertices[0] * [[ARKUtilities instance] getScale]],
					  [NSNumber numberWithFloat:Vertices[1] * [[ARKUtilities instance] getScale]],
					  [NSNumber numberWithFloat:Vertices[2] * [[ARKUtilities instance] getScale]],
					  [NSNumber numberWithFloat:Vertices[3] * [[ARKUtilities instance] getScale]],
					  [NSNumber numberWithFloat:Vertices[4] * [[ARKUtilities instance] getScale]],
					  [NSNumber numberWithFloat:Vertices[5] * [[ARKUtilities instance] getScale]],
					  [NSNumber numberWithFloat:Vertices[6] * [[ARKUtilities instance] getScale]],
					  [NSNumber numberWithFloat:Vertices[7] * [[ARKUtilities instance] getScale]],
					  nil];
		
		//Create coordinates
		m_Coordinates = [NSArray arrayWithObjects:
						 [NSNumber numberWithFloat:X / width],				[NSNumber numberWithFloat:Y / height],				//Top left
						 [NSNumber numberWithFloat:X / width],				[NSNumber numberWithFloat:(Y + Height) / height],	//Bottom left
						 [NSNumber numberWithFloat:(X + Width) / width],	[NSNumber numberWithFloat:Y / height],				//Top right
						 [NSNumber numberWithFloat:(X + Width) / width],	[NSNumber numberWithFloat:(Y + Height) / height],	//Bottom right
						 nil];
	}
	
	//Return
	return self;
}

//Getters
- (float)getBottom	{ return [[m_Vertices objectAtIndex:3] floatValue];												}
- (float)getOffsetY	{ return -[[m_Vertices objectAtIndex:1] floatValue];											}
- (float)getHeight	{ return [[m_Vertices objectAtIndex:1] floatValue] - [[m_Vertices objectAtIndex:3] floatValue];	}
- (float)getWidth	{ return [[m_Vertices objectAtIndex:4] floatValue] - [[m_Vertices objectAtIndex:0] floatValue];	}
- (float)getTop		{ return [[m_Vertices objectAtIndex:1] floatValue];												}

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
