//
//  ARKLoadable.h
//  Ark Framework
//
//  Created by LegACy on 4/11/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

//Imports
#import <Foundation/Foundation.h>

//Class declaration
@interface ARKLoadable : NSObject {
	//Variables
	NSString*	m_Name;
	int			m_Resource;
}

//Properties
@property (readonly, getter = getName) NSString* name;

//Creators
+ (ARKLoadable*)createLanguage:(int)language;
+ (ARKLoadable*)createNumberFromFont:(int)font;
+ (ARKLoadable*)createBGMFromFile:(NSString*)file;
+ (ARKLoadable*)createSFXFromFile:(NSString*)file;
+ (ARKLoadable*)createJSONFromFile:(NSString*)file;
+ (ARKLoadable*)createFontFromFile:(NSString*)file;
+ (ARKLoadable*)createTextureFromFile:(NSString*)file;
+ (ARKLoadable*)createTextureFromFile:(NSString*)file withAntiAlias:(BOOL)antialias;

//Functions
- (id)initResourceType:(int)type;
- (id)initResourceType:(int)type withName:(NSString*)name;
- (id)load;

//Constant
extern const int LOADABLE_TEXTURE;

@end
