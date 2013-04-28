//
//  RecordManager.m
//  ARK Framework Example
//
//  Created by LegACy on 4/28/13.
//  Copyright (c) 2013 Raka Mahesa. All rights reserved.
//

#import <ARKFramework/Framework.h>
#import "RecordManager.h"
#import "LoaderLatest.h"

@implementation RecordManager

//Synthesize
@synthesize version		= m_Version;
@synthesize highscore	= m_Highscore;
@synthesize tutorial	= m_Tutorial;

- (id)init {
	//Super
	self = [super init];
	if (self) {
		//Create writer
		m_Writer = [ARKRecordWriter create];
		
		//Initialize data
		m_Version	= 1;
		m_Tutorial	= NO;
		m_Highscore	= 0;
	}
	
	//Return
	return self;
}

+ (RecordManager*)instance {
	//Static objects
	static dispatch_once_t Token	= 0;
	static RecordManager* Instance	= nil;
	
	//Only create object once
	dispatch_once(&Token, ^{
		Instance = [[self alloc] init];
	});
	
	//Return
	return Instance;
}

- (void)save {
	//Create JSON
	NSMutableDictionary* JSON = [NSMutableDictionary dictionary];
	[JSON setObject:[NSNumber numberWithInt:m_Version] forKey:LOADER_KEY_VERSION];
	[JSON setObject:[NSNumber numberWithInt:m_Highscore] forKey:LOADER_KEY_HIGHSCORE];
	if (m_Tutorial) [JSON setObject:[NSNumber numberWithInt:m_Version] forKey:LOADER_KEY_TUTORIAL];
	
	//Save json
	[m_Writer save:JSON];
}

- (void)load {
	//Get record
	NSDictionary* Result = [m_Writer load];
	if (Result) {
		//Create loader
		NSObject<RecordLoader>* Loader = [[LoaderLatest alloc] init];
		
		//Load
		m_Version	= [Loader loadVersionFromJSON:Result];
		m_Tutorial	= [Loader loadTutorialFromJSON:Result];
		m_Highscore	= [Loader loadHighscoreFromJSON:Result];
	}
}

//Setters
- (void)showTutorial				{ m_Tutorial = YES;			}
- (void)setHighscore:(int)highscore	{ m_Highscore = highscore;	}

@end
