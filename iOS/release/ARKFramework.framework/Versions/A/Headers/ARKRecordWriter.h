//
//  ARKRecordWriter.h
//  Ark Framework
//
//  Created by LegACy on 4/14/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ARKRecordWriter : NSObject

//Creator
+ (ARKRecordWriter*)create;

//Save and load
- (void)save:(NSDictionary*)json;
- (NSDictionary*)load;

@end
