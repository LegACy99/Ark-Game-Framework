//
//  ARKiOSRecordWriter.m
//  Ark Framework
//
//  Created by LegACy on 4/14/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

//Header
#import "ARKiOSRecordWriter.h"

//Constant
const NSString* RECORD_SAVE_NAME = @"Records";

@implementation ARKiOSRecordWriter

- (id)init {
	//Super
	self = [super init];
	if (self) {
		//Get path
		m_SaveFile		= nil;
		NSArray* Paths	= NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
		if (Paths && [Paths objectAtIndex:0]) {
			NSString* FileName = [NSString stringWithFormat:@"%@.json", RECORD_SAVE_NAME];
			m_SaveFile = [[Paths objectAtIndex:0] stringByAppendingPathComponent:FileName];
		}
	}
	
	//Return
	return self;
}

- (void)save:(NSDictionary*)json {
	//Create JSON and save
	NSData* JSONData = [NSJSONSerialization dataWithJSONObject:json options:0 error:nil];
	if (JSONData && m_SaveFile) [JSONData writeToFile:m_SaveFile atomically:YES];
}

- (NSDictionary*)load {
	//Initialize
	NSDictionary* Result = nil;
	
	//If file exist
	if (m_SaveFile) {
		//Read
		NSData* JSONData = [NSData dataWithContentsOfFile:m_SaveFile];
		if (JSONData) Result = [NSJSONSerialization JSONObjectWithData:JSONData options:0 error:nil];
	}
	
	//Return
	return Result;
}

@end
