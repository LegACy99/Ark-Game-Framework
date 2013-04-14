//
//  ARKRecordWriter.m
//  Ark Framework
//
//  Created by LegACy on 4/14/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKRecordWriter.h"
#import "ARKiOSRecordWriter.h"

@implementation ARKRecordWriter

//Creator
+ (ARKRecordWriter*)create {
	//Return record writer for iOS
	return [[ARKiOSRecordWriter alloc] init];
}

//Save and load
- (void)save:(NSDictionary*)json	{ [self doesNotRecognizeSelector:_cmd]; }
- (NSDictionary*)load				{ return nil;							}

@end
