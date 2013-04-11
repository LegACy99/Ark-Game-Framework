//
//  ARKLoadable.m
//  Ark Framework
//
//  Created by LegACy on 4/11/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

//Import
#import "ARKTexture.h"
#import "ARKLoadable.h"
#import "ARKLoadableTexture.h"
#import "ARKResourceManager.h"

//Constants
const int LOADABLE_BGM		= 1;
const int LOADABLE_SFX		= 2;
const int LOADABLE_JSON		= 3;
const int LOADABLE_FONT		= 4;
const int LOADABLE_NUMBER	= 5;
const int LOADABLE_STRING	= 6;
const int LOADABLE_TEXTURE	= 7;

@implementation ARKLoadable

//Synthesize properties
@synthesize name = m_Name;

- (id)init {
	//Don't use
	return nil;
}

- (id)initResourceType:(int)type { return [self initResourceType:type withName:@""]; }
- (id)initResourceType:(int)type withName:(NSString*)name {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		m_Name		= name;
		m_Resource	= type;
	}
	
	//Return
	return self;
}

+ (ARKLoadable*)createLanguage:(int)language											{ return [[ARKLoadable alloc] initResourceType:LOADABLE_STRING withName:nil];		}
+ (ARKLoadable*)createNumberFromFont:(int)font;											{ return [[ARKLoadable alloc] initResourceType:LOADABLE_NUMBER withName:nil];		}
+ (ARKLoadable*)createBGMFromFile:(NSString*)file;										{ return [[ARKLoadable alloc] initResourceType:LOADABLE_BGM withName:file];			}
+ (ARKLoadable*)createSFXFromFile:(NSString*)file;										{ return [[ARKLoadable alloc] initResourceType:LOADABLE_SFX withName:file];			}
+ (ARKLoadable*)createJSONFromFile:(NSString*)file;										{ return [[ARKLoadable alloc] initResourceType:LOADABLE_JSON withName:file];		}
+ (ARKLoadable*)createFontFromFile:(NSString*)file;										{ return [[ARKLoadable alloc] initResourceType:LOADABLE_FONT withName:file];		}
+ (ARKLoadable*)createTextureFromFile:(NSString*)file withAntiAlias:(BOOL)antialias;	{ return [[ARKLoadableTexture alloc] initWithName:file withAntiAlias:antialias];	}
+ (ARKLoadable*)createTextureFromFile:(NSString*)file;									{ return [[ARKLoadableTexture alloc] initWithName:file];							}

- (id)load {
	//Initialize
	id Result = nil;
	
	//Check type
	if (m_Resource == LOADABLE_TEXTURE) {
		//Create texture
		Result = [ARKTexture createFromFile:m_Name];
	} else if (m_Resource == LOADABLE_JSON) {
		//Store json object
		Result = [[ARKResourceManager instance] readJSONFromFile:m_Name];
	} else if (m_Resource == LOADABLE_FONT) {
		//Load font
		//
	} else if (m_Resource == LOADABLE_SFX) {
		//Load SFX
	} else if (m_Resource == LOADABLE_BGM) {
		//Load BGM
	} else if (m_Resource == LOADABLE_NUMBER) {
		//Not used
	} else if (m_Resource == LOADABLE_STRING) {
		//Not used
	}
	
	//Return
	return Result;
}

@end
