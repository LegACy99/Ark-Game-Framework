//
//  ARKiOSUtilities.m
//  Ark Framework
//
//  Created by LegACy on 4/12/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import <stdlib.h>
#import "ARKDevice.h"
#import "ARKiOSUtilities.h"

@implementation ARKiOSUtilities

+ (ARKiOSUtilities*)instance {
	//Static objects
	static dispatch_once_t Token		= 0;
	static ARKiOSUtilities* Instance	= nil;
	
	//Only create object once
	dispatch_once(&Token, ^{
		Instance = [[self alloc] init];
	});
	
	//Return
	return Instance;
}

//Accessors
- (int)getRow		{ return [[ARKDevice instance] getRow];											}
- (int)getColumn	{ return [[ARKDevice instance] getColumn];										}
- (float)getScale	{ return [[ARKDevice instance] getScale];										}
- (float)getWidth	{ return [[ARKDevice instance] getWidth] / [[ARKDevice instance] getScale];		}
- (float)getHeight	{ return [[ARKDevice instance] getHeight] / [[ARKDevice instance] getScale];	}

- (int)getARandomNumberBetween:(int)from to:(int)to {
	if (from + 1 >= to) return from;
	else				return arc4random_uniform(to - from) + from;
}

- (NSString*)writeFloat:(float)number withDecimalDigit:(int)decimal {
	//Get decimal
	float Number = number;
	for (int i = 0; i < decimal; i++) Number *= 10;
	
	//Get string
	int Integer				= (int)Number;
	NSMutableString* Text	= [NSMutableString stringWithFormat:@"%d", Integer];
	while ([Text length] < decimal + 1) Text = [NSMutableString stringWithFormat:@"0%@", Text];
	
	//For each digit
	NSMutableString* Builder = [NSMutableString string];
	for (int i = 0; i < [Text length]; i++) {
		//Add
		[Builder appendFormat:@"%c", [Text characterAtIndex:i]];
		if (i == [Text length] - decimal - 1) [Builder appendString:@"."];
	}
	
	//Return
	return [NSString stringWithString:Builder];
}

- (NSString*)writeVersion:(NSArray*)version {
	return [self writeVersion:version limitedIn:[NSArray arrayWithObjects:[NSNumber numberWithInt:1], [NSNumber numberWithInt:2], nil]];
}

- (NSString*)writeVersion:(NSArray*)version limitedIn:(NSArray*)digits {
	//Create builder
	NSMutableString* Builder = [NSMutableString string];
	
	//Ensure version exist
	if (version) {
		//For each version
		for (int i = 0; i < [version count]; i++) {
			//Get number of digit
			int Digit = 1;
			if (digits && [digits count] > i) Digit = [[digits objectAtIndex:i] intValue];
			if (Digit < 1) Digit = 1;
			
			//Get numbers
			NSArray* Numbers = [self getDigitsFromNumber:[[version objectAtIndex:i] intValue] limitedTo:Digit];
			for (int j = 0; j < [Numbers count]; j++) [Builder appendFormat:@"%d", [Numbers[j] intValue]];
			
			//Add dot if not last
			if (i != [version count] - 1) [Builder appendString:@"."];
		}
	}
	
	//Return
	return [NSString stringWithString:Builder];
}

- (NSArray*)splitFilePath:(NSString *)path {
	//Initialize
	NSString* File		= @"";
	NSString* Folder	= @"";
	NSString* Extension = @"";
	
	//If there's a path
	if (path) {
		//Find characters position
		NSRange DotRange	= [path rangeOfString:@"." options:NSBackwardsSearch];
		NSRange SlashRange	= [path rangeOfString:@"/" options:NSBackwardsSearch];
		
		//If exist
		if (DotRange.location != NSNotFound)	Extension = [path substringFromIndex:DotRange.location + 1];
		if (SlashRange.location != NSNotFound)	Folder = [path substringToIndex:SlashRange.location];
		
		//Get file name
		int Start	= SlashRange.location == NSNotFound ? 0 : SlashRange.location + 1;
		int End		= DotRange.location == NSNotFound ? [path length] : DotRange.location;
		File		= [path substringWithRange:NSMakeRange(Start, End - Start)];
	}
	
	//Return as array
	return [NSArray arrayWithObjects:Folder, File, Extension, nil];
}

- (NSString*)getResourcePath:(NSString *)path {
	//Initialize
	NSString* Result = nil;
	
	//If path exist
	if (path) {
		//Split
		NSArray* Sections = [self splitFilePath:path];
		if ([[Sections objectAtIndex:0] length] > 0) {
			Result = [[NSBundle mainBundle] pathForResource:[Sections objectAtIndex:1] ofType:[Sections objectAtIndex:2] inDirectory:[Sections objectAtIndex:0]];
		} else {
			Result = [[NSBundle mainBundle] pathForResource:[Sections objectAtIndex:1] ofType:[Sections objectAtIndex:2]];
		}
	}
	
	//Return
	return Result;
}

@end
