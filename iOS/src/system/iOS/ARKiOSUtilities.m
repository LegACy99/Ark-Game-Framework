//
//  ARKiOSUtilities.m
//  Ark Framework
//
//  Created by LegACy on 4/12/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import <stdlib.h>
#import "ARKiOSUtilities.h"

@implementation ARKiOSUtilities

+ (ARKUtilities*)instance {
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
- (int)getRow		{ return [super getRow];	}
- (int)getColumn	{ return [super getColumn];	}
- (float)getScale	{ return 1;					}
- (float)getWidth	{ return 320;				}
- (float)getHeight	{ return 480;				}

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
			if (digits && [digits count] > i) Digit = [digits[i] intValue];
			if (Digit < 1) Digit = 1;
			
			//Get numbers
			NSArray* Numbers = [self getDigitsFromNumber:[version[i] intValue] limitedTo:Digit];
			for (int j = 0; j < [Numbers count]; j++) [Builder appendFormat:@"%d", [Numbers[j] intValue]];
			
			//Add dot if not last
			if (i != [version count] - 1) [Builder appendString:@"."];;
		}
	}
	
	//Return
	return [NSString stringWithString:Builder];
}

@end
