//
//  Bullet.h
//  ARK Framework Example
//
//  Created by LegACy on 4/28/13.
//  Copyright (c) 2013 Raka Mahesa. All rights reserved.
//

#import <ARKFramework/ARKFramework.h>

@interface Bullet : ARKDrawable {
	//Data
	ARKImage* 		m_Image;
	NSMutableArray* m_Trails;
	float			m_MissX;
	float			m_MissY;
	float			m_LabelX;
	float			m_LabelY;
	float			m_VelocityX;
	float			m_VelocityY;
	long			m_TrailTime;
	long			m_MissTime;
	BOOL			m_NearMiss;
}

//Properties
@property (readonly, getter = getLabelX)	float labelX;
@property (readonly, getter = getLabelY)	float labelY;
@property (readonly, getter = getNearMissX)	float missX;
@property (readonly, getter = getNearMissY)	float missY;

//Constructor
- (id)initAtX:(float)x atY:(float)y withXVelocity:(float)velocityX withYVelocity:(float)velocityY;

//Getters
- (BOOL)isDead;
- (BOOL)isNearMiss;
- (BOOL)doesCollideWithRectFromX:(float)x fromY:(float)y withWidth:(float)width withHeight:(float)height;

//Updating
- (void)updateForceByWormholes:(NSArray*)holes;
- (void)calculateLabelPositionFromOldX:(float)oldX fromOldY:(float)oldY fromX:(float)x fromY:(float)y;
- (void)updateWithDeltaTime:(long)time;

@end
