//
//  LoaderLatest.m
//  ARK Framework Example
//
//  Created by LegACy on 4/28/13.
//  Copyright (c) 2013 Raka Mahesa. All rights reserved.
//

#import "LoaderLatest.h"

@implementation LoaderLatest

- (int)loadVersionFromJSON:(NSDictionary *)json {
	//Initialize
	int Result = 0;
	if (json) {
		//Load
		id Version = [json objectForKey:LOADER_KEY_VERSION];
		if (Version) Result = [Version intValue];
	}
	
	//Return
	return Result;
}

- (int)loadHighscoreFromJSON:(NSDictionary *)json {
	//Initialize
	int Result = 0;
	if (json) {
		//Load
		id Highscore = [json objectForKey:LOADER_KEY_HIGHSCORE];
		if (Highscore) Result = [Highscore intValue];
	}
	
	//Return
	return Result;
}

- (BOOL)loadTutorialFromJSON:(NSDictionary *)json {
	//Load
	BOOL Result = false;
	if (json && [json objectForKey:LOADER_KEY_TUTORIAL]) Result = true;
	
	//Return
	return Result;
}

@end
