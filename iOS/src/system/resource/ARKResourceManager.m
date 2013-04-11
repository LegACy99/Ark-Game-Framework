//
//  ResourceManager.m
//  Ark Framework
//
//  Created by LegACy on 4/11/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

//Header file
#import "ARKResourceManager.h"
#import "ARKiOsResourceManager.h"

@implementation ARKResourceManager

//Synthesize properties
@synthesize finished = m_Finished;

- (id)init {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		m_Added		= 0;
		m_Loaded	= 0;
		m_Started	= NO;
		m_Finished	= NO;
	}
	
	//Return
	return self;
}

+ (ARKResourceManager*)instance {
	//Return implemented instance
	return [ARKiOSResourceManager instance];
}

//Resource loaders
- (void)addLanguage:(int)language											{ [self doesNotRecognizeSelector:_cmd]; }
- (void)addNumberFromFont:(int)font											{ [self doesNotRecognizeSelector:_cmd]; }
- (void)addBGMFromFile:(NSString*)file										{ [self doesNotRecognizeSelector:_cmd]; }
- (void)addSFXFromFile:(NSString*)file										{ [self doesNotRecognizeSelector:_cmd]; }
- (void)addJSONFromFile:(NSString*)file										{ [self doesNotRecognizeSelector:_cmd]; }
- (void)addFontFromFile:(NSString*)file										{ [self doesNotRecognizeSelector:_cmd]; }
- (void)addImageFromFile:(NSString*)file									{ [self doesNotRecognizeSelector:_cmd]; }
- (void)addTextureFromFile:(NSString*)file									{ [self doesNotRecognizeSelector:_cmd]; }
- (void)addTextureFromFile:(NSString*)file withAntiAlias:(BOOL)antialias	{ [self doesNotRecognizeSelector:_cmd]; }

//Resource accessors
- (id)getImageWithName:(NSString*)name				{ return nil; }
- (id)getTextureWithName:(NSString*)name;			{ return nil; }
- (NSArray*)getImagesWithName:(NSString*)name;		{ return nil; }
- (NSArray*)getTexturesWithName:(NSString*)name;		{ return nil; }
- (NSDictionary*)getJSONWithName:(NSString*)name;	{ return nil; }
- (NSDictionary*)readJSONFromFile:(NSString*)file;	{ return nil; }
- (id)getFontWithName:(NSString*)name;				{ return nil; }

- (BOOL)isLoading {
	//Loading if started and not finished yet
	return m_Started && !m_Finished;
}

- (float)getProgresss {
	//Special case
	if ([self isFinished])	return 1;
	if (m_Added <= 0)		return 0;
	
	//Return percentage
	return (float)m_Loaded / (float)m_Added;
}

- (void)start {
	//Skip if no loading or no loadable
	if ([self isLoading])	return;
	if (m_Added <= 0)		return;
	
	//Start
	m_Loaded	= 0;
	m_Started	= YES;
	m_Finished	= NO;
}

- (void)update {
	//If loading
	if ([self isLoading]) {
		//Next
		m_Loaded++;
		if (m_Loaded >= m_Added) m_Finished = YES;
	}
}

//Abstract functions
- (void)reload										{ [self doesNotRecognizeSelector:_cmd]; }
- (void)destroy										{ [self doesNotRecognizeSelector:_cmd];	}
- (void)destroyResourceWithName:(NSString*)name;	{ [self doesNotRecognizeSelector:_cmd];	}

@end
