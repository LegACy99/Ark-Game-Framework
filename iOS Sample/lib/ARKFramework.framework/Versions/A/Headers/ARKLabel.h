//
//  ARKLabel.h
//  Ark Framework
//
//  Created by LegACy on 4/17/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKCroppable.h"

@interface ARKLabel : ARKCroppable {
	//Data
	NSString* m_Text;
}

//Property
@property (readonly, getter = getText) NSString* text;

//Constructor
- (id)initWithText:(NSString*)text atX:(float)x atY:(float)y;

//Static factory methods
+ (ARKLabel*)createWithInteger:(int)number;
+ (ARKLabel*)createWithText:(NSString*)text;
+ (ARKLabel*)createWithInteger:(int)number withFont:(NSString*)font;
+ (ARKLabel*)createWithText:(NSString*)text withFont:(NSString*)font;
+ (ARKLabel*)createWithInteger:(int)number withFont:(NSString*)font atX:(float)x atY:(float)y;
+ (ARKLabel*)createWithText:(NSString*)text withFont:(NSString*)font atX:(float)x atY:(float)y;
+ (ARKLabel*)createWithFloat:(float)number withDecimalDigit:(int)decimal;
+ (ARKLabel*)createWithFloat:(float)number withDecimalDigit:(int)decimal withFont:(NSString*)font;
+ (ARKLabel*)createWithFloat:(float)number withDecimalDigit:(int)decimal withFont:(NSString*)font atX:(float)x atY:(float)y;

@end
