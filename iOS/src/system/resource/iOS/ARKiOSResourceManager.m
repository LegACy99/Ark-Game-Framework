//
//  ARKiOSResourceManager.m
//  Ark Framework
//
//  Created by LegACy on 4/11/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKiOSResourceManager.h"
#import "ARKiOSUtilities.h"
#import "ARKBitmapFont.h"
#import "ARKLoadable.h"
#import "ARKTexture.h"
#import "ARKImage.h"

//Constants
const NSString* RESOURCEMANAGER_KEY_SHEET			= @"Sheet";
const NSString* RESOURCEMANAGER_KEY_IMAGES			= @"Images";
const NSString* RESOURCEMANAGER_KEY_SHEET_OFFSET	= @"Offset";
const NSString* RESOURCEMANAGER_KEY_SHEET_GRID		= @"Grid";
const NSString* RESOURCEMANAGER_KEY_SHEET_SIZE		= @"Size";
const NSString* RESOURCEMANAGER_KEY_SHEET_CELL		= @"Cell";
const NSString* RESOURCEMANAGER_KEY_SHEET_GAP		= @"Gap";

@implementation ARKiOSResourceManager

- (id)init {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		m_Destroys	= [NSMutableArray array];
		m_Textures	= [NSMutableArray array];
		m_Loadables	= [NSMutableArray array];
		m_Resources	= [NSMutableDictionary dictionary];
		
		//Load system resources
		if ([[ARKUtilities instance] getSystemFont])		[self addFontFromFile:[[ARKUtilities instance] getSystemFont]];
		if ([[ARKUtilities instance] getSystemPressSFX]) 	[self addSFXFromFile:[[ARKUtilities instance] getSystemPressSFX]];
		if ([[ARKUtilities instance] getSystemCursorSFX]) 	[self addSFXFromFile:[[ARKUtilities instance] getSystemCursorSFX]];
		if ([[ARKUtilities instance] getSystemReleaseSFX]) 	[self addSFXFromFile:[[ARKUtilities instance] getSystemReleaseSFX]];
		if ([[ARKUtilities instance] getSystemFontTexture])	[self addTextureFromFile:[[ARKUtilities instance] getSystemFontTexture]
																	   withAntiAlias:[[ARKUtilities instance] isSystemFontSmooth]];
	}
	
	//Return
	return self;
}

+ (ARKiOSResourceManager*)instance {
	//Static objects
	static dispatch_once_t Token			= 0;
	static ARKiOSResourceManager* Instance	= nil;
	
	//Only create object once
	dispatch_once(&Token, ^{
		Instance = [[self alloc] init];
	});
	
	//Return
	return Instance;
}
- (void)addLanguage:(int)language			{ if (![self isLoading]) [m_Loadables addObject:[ARKLoadable createLanguage:language]];		}
- (void)addNumberFromFont:(int)font			{ if (![self isLoading]) [m_Loadables addObject:[ARKLoadable createNumberFromFont:font]];	}
- (void)addBGMFromFile:(NSString*)file		{ if (![self isLoading]) [m_Loadables addObject:[ARKLoadable createBGMFromFile:file]];		}
- (void)addSFXFromFile:(NSString*)file		{ if (![self isLoading]) [m_Loadables addObject:[ARKLoadable createSFXFromFile:file]];		}
- (void)addJSONFromFile:(NSString*)file		{ if (![self isLoading]) [m_Loadables addObject:[ARKLoadable createJSONFromFile:file]];		}
- (void)addFontFromFile:(NSString*)file		{ if (![self isLoading]) [m_Loadables addObject:[ARKLoadable createFontFromFile:file]];		}
- (void)addImageFromFile:(NSString*)file	{ if (![self isLoading]) [m_Loadables addObject:[ARKLoadable createJSONFromFile:file]];		}
- (void)addTextureFromFile:(NSString*)file	{ if (![self isLoading]) [m_Loadables addObject:[ARKLoadable createTextureFromFile:file]];	}

- (void)addTextureFromFile:(NSString*)file withAntiAlias:(BOOL)antialias {
	//If not loading
	if (![self isLoading]) {
		//Add
		[m_Loadables addObject:[ARKLoadable createTextureFromFile:file withAntiAlias:antialias]];
	}
}

//Not used
- (id)getImageWithName:(NSString *)name			{ return nil; }
- (NSArray*)getImagesWithName:(NSString *)name	{ return nil; }

- (id)getTextureWithName:(NSString *)name {
	//Initialize
	ARKTexture* Result = nil;
	
	//Get
	id Object = [m_Resources objectForKey:name];
	if (Object && [Object isKindOfClass:[ARKTexture class]]) Result = Object;
	
	//Return
	return Result;
}

- (NSDictionary*)getJSONWithName:(NSString *)name {
	//Initialize
	NSDictionary* Result = nil;
	
	//Get
	id Object = [m_Resources objectForKey:name];
	if (Object && [Object isKindOfClass:[NSDictionary class]]) Result = Object;
	
	//Return
	return Result;
}

- (ARKBitmapFont*)getFontWithName:(NSString *)name {
	//Initialize
	ARKBitmapFont* Result = nil;
	
	//Get
	id Object = [m_Resources objectForKey:name];
	if (Object && [Object isKindOfClass:[ARKBitmapFont class]]) Result = Object;
	
	//Return
	return Result;
}

- (NSArray*)getTexturesWithName:(NSString *)name {
	//Initialize
	NSMutableArray* Result	= [NSMutableArray array];
	NSDictionary* JSON		= [self getJSONWithName:name];
	
	//If exist
	if (JSON) {
		//Check for object inside
		id Sheet	= [JSON objectForKey:RESOURCEMANAGER_KEY_SHEET];
		id Images	= [JSON objectForKey:RESOURCEMANAGER_KEY_IMAGES];
		if (Images && [Images isKindOfClass:[NSArray class]]) {
			//Add images
			NSArray* ImageArray = Images;
			for (int i = 0; i < [ImageArray count]; i++) [Result addObject:[NSMutableDictionary dictionaryWithDictionary:[ImageArray objectAtIndex:i]]];
		} else if (Sheet && [Sheet isKindOfClass:[NSDictionary class]]) {
			//Save
			NSDictionary* SheetData = Sheet;
			
			//Initialize
			int GapX 		= 0;
			int GapY 		= 0;
			int Rows	 	= 0;
			int Columns		= 0;
			int OffsetX		= 0;
			int OffsetY		= 0;
			int CellWidth	= 0;
			int CellHeight	= 0;
			
			//If offset exist
			id SheetOffset = [SheetData objectForKey:RESOURCEMANAGER_KEY_SHEET_OFFSET];
			if (SheetOffset && [SheetOffset isKindOfClass:[NSArray class]]) {
				//Get data
				NSArray* Offset	= SheetOffset;
				OffsetX			= [(NSNumber*)[Offset objectAtIndex:0] intValue];
				OffsetY			= [(NSNumber*)[Offset objectAtIndex:2] intValue];
			}
			
			//If gap exist
			id SheetGap = [SheetData objectForKey:RESOURCEMANAGER_KEY_SHEET_GAP];
			if (SheetGap && [SheetGap isKindOfClass:[NSArray class]]) {
				//Get gap
				NSArray* Gap	= SheetGap;
				GapX			= [(NSNumber*)[Gap objectAtIndex:0] intValue];
				GapY			= [(NSNumber*)[Gap objectAtIndex:1] intValue];
			}
			
			//If grid exist
			id SheetGrid = [SheetData objectForKey:RESOURCEMANAGER_KEY_SHEET_GRID];
			id SheetCell = [SheetData objectForKey:RESOURCEMANAGER_KEY_SHEET_CELL];
			if (SheetGrid && [SheetGrid isKindOfClass:[NSArray class]]) {
				//Get data
				NSArray* Grid 	= SheetGrid;
				Columns			= [(NSNumber*)[Grid objectAtIndex:0] intValue];
				Rows			= [(NSNumber*)[Grid objectAtIndex:1] intValue];
				
				//Check data type
				id GridCell	= [SheetData objectForKey:RESOURCEMANAGER_KEY_SHEET_CELL];
				id GridSize	= [SheetData objectForKey:RESOURCEMANAGER_KEY_SHEET_SIZE];
				if (GridCell && [GridCell isKindOfClass:[NSArray class]]) {
					//Get data
					NSArray* Cell 	= GridCell;
					CellWidth		= [(NSNumber*)[Cell objectAtIndex:0] intValue];
					CellHeight		= [(NSNumber*)[Cell objectAtIndex:1] intValue];
				} else if (GridSize && [GridSize isKindOfClass:[NSArray class]]) {
					//Get data
					NSArray* Size	= GridSize;
					CellWidth		= [(NSNumber*)[Size objectAtIndex:0] intValue] / Columns;
					CellHeight		= [(NSNumber*)[Size objectAtIndex:1] intValue] / Rows;
				}
			} else if (SheetCell && [SheetCell isKindOfClass:[NSArray class]]) {
				//Get data
				NSArray* Cell 	= SheetCell;
				CellWidth		= [(NSNumber*)[Cell objectAtIndex:0] intValue];
				CellHeight		= [(NSNumber*)[Cell objectAtIndex:1] intValue];
				
				//If size exist
				id CellSize = [SheetData objectForKey:RESOURCEMANAGER_KEY_SHEET_SIZE];
				if (CellSize && [CellSize isKindOfClass:[NSArray class]]) {
					//Get data
					NSArray* Size	= CellSize;
					CellWidth		= [(NSNumber*)[Size objectAtIndex:0] intValue] / Columns;
					CellHeight		= [(NSNumber*)[Size objectAtIndex:1] intValue] / Rows;
				}
			}
			
			//Create result
			for (int y = 0; y < Rows; y++) {
				for (int x = 0; x < Columns; x++) {
					//Create rect
					NSMutableDictionary* Data = [NSMutableDictionary dictionary];
					NSMutableDictionary* Rect = [NSMutableDictionary dictionary];
					[Rect setObject:[NSNumber numberWithInt:CellWidth] forKey:IMAGE_KEY_RECT_WIDTH];
					[Rect setObject:[NSNumber numberWithInt:CellHeight] forKey:IMAGE_KEY_RECT_HEIGHT];
					[Rect setObject:[NSNumber numberWithInt:(OffsetX + ((CellWidth + GapX) * x))] forKey:IMAGE_KEY_RECT_LEFT];
					[Rect setObject:[NSNumber numberWithInt:(OffsetY + ((CellWidth + GapY) * y))] forKey:IMAGE_KEY_RECT_TOP];
				
					//Add rect data
					[Data setObject:Rect forKey:IMAGE_KEY_RECT];
					[Result addObject:Data];
				}
			}
		}
		
		//If there's a texture
		id Texture = [JSON objectForKey:IMAGE_KEY_TEXTURE];
		if (Texture && [Texture isKindOfClass:[NSString class]]) {
			//Get texture
			NSString* TextureName = Texture;
			
			//For each result
			for (int i = 0; i < [Result count]; i++) {
				//Add texture if doesn't exist
				id ExistingTexture = [(NSDictionary*)[Result objectAtIndex:i] objectForKey:IMAGE_KEY_TEXTURE];
				if (!ExistingTexture) [(NSMutableDictionary*)[Result objectAtIndex:i] setObject:TextureName forKey:IMAGE_KEY_TEXTURE];
			}
		}
	}
	
	//Return
	return Result;
}

- (NSDictionary*) readJSONFromFile:(NSString *)file {
	//Initialize
	NSDictionary* Result = nil;

	//Open JSON file
	NSString* Path			= [[ARKiOSUtilities instance] getResourcePath:file];
	NSInputStream* Stream	= [NSInputStream inputStreamWithFileAtPath:Path];
	if (Stream) {
		//Create JSON from stream
		[Stream open];
		id JSON = [NSJSONSerialization JSONObjectWithStream:Stream options:0 error:nil];
		[Stream close];
		
		//If valid, save
		if (JSON && [JSON isKindOfClass:[NSDictionary class]]) Result = JSON;
	}
	
	//Return
	return Result;
}

- (void)start {
	//Initialize
	m_Added = [m_Loadables count];
	[m_Destroys removeAllObjects];
	
	//Super
	[super start];
}

- (void)reload {
	//Reload textures
	for (int i = 0; i < [m_Textures count]; i++) [(ARKTexture*)[m_Textures objectAtIndex:i] load];
}

- (void)destroy {
	//Destroy all destructibles
	for (int i = 0; i < [m_Destroys count]; i++) [self destroyResourceWithName:[m_Destroys objectAtIndex:i]];
	[m_Destroys removeAllObjects];
}

- (void)destroyResourceWithName:(NSString *)name {
	//If loading
	if ([self isLoading]) {
		//Check if going ot be loaded
		BOOL Loaded = NO;
		//TODO
		//for (int i = 0; i < [m_Loadables count] && !Loaded; i++) if (m_Loadables.get(i).getName().equals(resource)) Loaded = true;
		
		//If isn't going to be loaded, add
		if (!Loaded) [m_Destroys addObject:name];
	} else {
		//If exist
		id Removed = [m_Resources objectForKey:name];
		if (Removed) {
			//Remove
			[m_Resources removeObjectForKey:name];
			
			//If texture
			if ([Removed isKindOfClass:[ARKTexture class]]) {
				//Destroy
				[m_Textures removeObject:Removed];
				[(ARKTexture*)Removed destroy];
			}
		}
	}
}

- (void)update {
	//If loading
	if ([self isLoading]) {
		//Get object
		ARKLoadable* Current	= [m_Loadables objectAtIndex:m_Loaded];
		NSString* Name			= [Current getName];
		
		//If doesn't exist
		id Resource = [m_Resources objectForKey:Name];
		if (!Resource) {
			//Load
			id Loaded = [Current load];
			if (Loaded) {
				//Add
				[m_Resources setObject:Loaded forKey:[Current getName]];
				if ([Loaded isKindOfClass:[ARKTexture class]]) [m_Textures addObject:Loaded];
			}
		}
		
		//Update
		[super update];
		
		//If finished
		if (m_Finished) {
			//Reset
			m_Added = 0;
			[m_Loadables removeAllObjects];
		}
	}
}

@end
