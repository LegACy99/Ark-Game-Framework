//
//  ARKiOSUtilities.h
//  Ark Framework
//
//  Created by LegACy on 4/12/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKUtilities.h"

@interface ARKiOSUtilities : ARKUtilities

//Singleton
+ (ARKiOSUtilities*)instance;

//File processor
- (NSArray*)splitFilePath:(NSString*)path;
- (NSString*)getResourcePath:(NSString*)path;

@end
