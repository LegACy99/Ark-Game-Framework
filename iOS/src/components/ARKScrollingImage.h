//
//  ARKScrollingImage.h
//  Ark Framework
//
//  Created by LegACy on 4/26/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKDrawable.h"

//Forward declaration
@class ARKImage;

@interface ARKScrollingImage : ARKDrawable {
	//Data
	long 		m_Time;
	float		m_ScrollX;
	float		m_ScrollY;
	float		m_OffsetX;
	float		m_OffsetY;
	ARKImage* 	m_Images[4];
}

//Constructors
- (id)initWithJSON:(NSDictionary*)json;
- (id)initWithResource:(NSString*)resource;
- (id)initWithJSON:(NSDictionary*)json scrolledByX:(float)scrollX byY:(float)scrollY;
- (id)initWithResource:(NSString*)resource scrolledByX:(float)scrollX byY:(float)scrollY;
- (id)initWithJSON:(NSDictionary*)json atX:(float)x atY:(float)y scrolledByX:(float)scrollX byY:(float)scrollY;
- (id)initWithResource:(NSString*)resource atX:(float)x atY:(float)y scrolledByX:(float)scrollX byY:(float)scrollY;

//Functions
- (void)scrollByX:(float)x byY:(float)y;
- (void)updateWithDeltaTime:(long)time;

@end
