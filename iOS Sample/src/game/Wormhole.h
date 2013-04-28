//
//  Wormhole.h
//  ARK Framework Example
//
//  Created by LegACy on 4/28/13.
//  Copyright (c) 2013 Raka Mahesa. All rights reserved.
//

#import <ARKFramework/ARKFramework.h>

@interface Wormhole : ARKDrawable {
	//Data
	int			m_Type;
	long		m_Duration;
	ARKImage*	m_Image;
}

//Property
@property (readonly, getter = getType) int type;

//Constructor
- (id)initWithType:(int)type atX:(float)x atY:(float)y;

//Functions
- (BOOL)isAlive;
- (float)calculateXForceAtX:(float)x atY:(float)y;
- (float)calculateYForceAtX:(float)x atY:(float)y;
- (void)updateWithDeltaTime:(long)time;

@end

//Constants
extern const int HOLE_BLACKHOLE;
extern const int HOLE_WHITEHOLE;
