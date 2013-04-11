//
//  ResourceManager.h
//  Ark Framework
//
//  Created by LegACy on 4/11/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

//Import
#import <Foundation/Foundation.h>

//Class delcaration
@interface ARKResourceManager : NSObject {
	//Protected variables
	int		m_Added;
	int		m_Loaded;
	BOOL	m_Started;
	BOOL	m_Finished;
}

//Properties
@property (readonly, getter = isFinished) BOOL finished;

//Singleton function
+ (ARKResourceManager*)instance;

//Abstract resource loaders
- (void)addLanguage:(int)language;
- (void)addNumberFromFont:(int)font;
- (void)addBGMFromFile:(NSString*)file;
- (void)addSFXFromFile:(NSString*)file;
- (void)addJSONFromFile:(NSString*)file;
- (void)addFontFromFile:(NSString*)file;
- (void)addImageFromFile:(NSString*)file;
- (void)addTextureFromFile:(NSString*)file;
- (void)addTextureFromFile:(NSString*)file withAntiAlias:(BOOL)antialias;

//Abstract resource accessors
- (id)getImageWithName:(NSString*)name;
- (id)getTextureWithName:(NSString*)name;
- (NSArray*)getImagesWithName:(NSString*)name;
- (NSArray*)getTexturesWithName:(NSString*)name;
- (NSDictionary*)getJSONWithName:(NSString*)name;
- (NSDictionary*)readJSONFromFile:(NSString*)file;
- (id)getFontWithName:(NSString*)name;

//Accessors
- (BOOL)isLoading;
- (float)getProgresss;

//Manager functions
- (void)start;
- (void)update;

//Abstract functions
- (void)destroy;
- (void)destroyResourceWithName:(NSString*)name;
- (void)reload;

@end
