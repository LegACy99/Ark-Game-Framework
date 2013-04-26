//
//  ARKLabel.m
//  Ark Framework
//
//  Created by LegACy on 4/17/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKLabel.h"
#import "ARKiOSLabel.h"
#import "ARKUtilities.h"

@implementation ARKLabel

//Synthesize
@synthesize text = m_Text;

- (id)initWithText:(NSString*)text atX:(float)x atY:(float)y {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		m_Text = text;
		if (!m_Text) m_Text = @"";
		[self setPositionAtX:x atY:y];
	}
	
	//Return
	return self;
}

+ (ARKLabel*)createWithInteger:(int)number	{ return [ARKLabel createWithInteger:number withFont:[[ARKUtilities instance] getSystemFont]];	}
+ (ARKLabel*)createWithText:(NSString*)text	{ return [ARKLabel createWithText:text withFont:[[ARKUtilities instance] getSystemFont]];		}
+ (ARKLabel*)createWithInteger:(int)number withFont:(NSString*)font		{ return [ARKLabel createWithInteger:number withFont:font atX:0 atY:0];	}
+ (ARKLabel*)createWithText:(NSString*)text withFont:(NSString*)font	{ return [ARKLabel createWithText:text withFont:font atX:0 atY:0];		}
+ (ARKLabel*)createWithInteger:(int)number withFont:(NSString*)font atX:(float)x atY:(float)y {
	//Return label
	return [ARKLabel createWithText:[NSString stringWithFormat:@"%d", number] withFont:font atX:x atY:y];
}

+ (ARKLabel*)createWithText:(NSString*)text withFont:(NSString*)font atX:(float)x atY:(float)y {
	//Return label
	return [[ARKiOSLabel alloc] initWithText:text withFont:font atX:x atY:y];
}

+ (ARKLabel*)createWithFloat:(float)number withDecimalDigit:(int)decimal {
	//Return label
	return [ARKLabel createWithFloat:number withDecimalDigit:decimal withFont:[[ARKUtilities instance] getSystemFont]];
}

+ (ARKLabel*)createWithFloat:(float)number withDecimalDigit:(int)decimal withFont:(NSString*)font {
	//Return label
	return [ARKLabel createWithFloat:number withDecimalDigit:decimal withFont:font atX:0 atY:0];
}

+ (ARKLabel*)createWithFloat:(float)number withDecimalDigit:(int)decimal withFont:(NSString*)font atX:(float)x atY:(float)y {
	//Return label
	return [ARKLabel createWithText:[[ARKUtilities instance] writeFloat:number withDecimalDigit:decimal] withFont:font atX:x atY:y];
}

@end
