//
//  ARKiOSSPrite.h
//  Ark Framework
//
//  Created by LegACy on 4/14/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKSprite.h"

@interface ARKiOSSprite : ARKSprite {
	NSArray* m_Images;
}

- (id)initWithPath:(NSString*)path AtX:(float)x atY:(float)y withDelay:(long)delay;

@end
