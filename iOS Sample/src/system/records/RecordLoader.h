//
//  RecordLoader.h
//  ARK Framework Example
//
//  Created by LegACy on 4/28/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol RecordLoader <NSObject>

//Loading interface
- (int)loadVersionFromJSON:(NSDictionary*)json;
- (int)loadHighscoreFromJSON:(NSDictionary*)json;
- (BOOL)loadTutorialFromJSON:(NSDictionary*)json;

@end

//Constants
extern const NSString* LOADER_KEY_VERSION;
extern const NSString* LOADER_KEY_TUTORIAL;
extern const NSString* LOADER_KEY_HIGHSCORE;
