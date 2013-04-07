//
//  Drawable.h
//  Ark Framework
//
//  Created by LegACy on 4/6/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import <Foundation/Foundation.h>

//Forward declaration
@class GLKBaseEffect;

//Class declaration
@interface ARKDrawable : NSObject {
	//Variables
	float m_X;
	float m_Y;
	float m_Width;
	float m_Height;
	float m_OriginalWidth;
	float m_OriginalHeight;
}

//Properties
@property (readonly, getter = getX)					float x;
@property (readonly, getter = getY)					float y;
@property (readonly, getter = getWidth)				float width;
@property (readonly, getter = getHeight)			float height;
@property (readonly, getter = getOriginalWidth)		float originalWidth;
@property (readonly, getter = getOriginalHeight)	float originalHeight;

//Public functions
- (void) setPositionAtX:(float)x atY:(float)y;
- (void) setPositionAtX:(float)x atY:(float)y horizontallyAlignedTo:(int)horizontal;
- (void) setPositionAtX:(float)x atY:(float)y horizontallyAlignedTo:(int)horizontal verticallyAlignedTo:(int)vertical;
- (void) drawWithEffect:(GLKBaseEffect*)effect;

@end

//Constants
extern const int DRAWABLE_ANCHOR_TOP;
extern const int DRAWABLE_ANCHOR_LEFT;
extern const int DRAWABLE_ANCHOR_RIGHT;
extern const int DRAWABLE_ANCHOR_CENTER;
extern const int DRAWABLE_ANCHOR_VCENTER;
extern const int DRAWABLE_ANCHOR_BOTTOM;
extern const int DRAWABLE_MIRROR_NONE;
extern const int DRAWABLE_MIRROR_HORIZONTAL;
extern const int DRAWABLE_MIRROR_VERTICAL;
extern const int DRAWABLE_MIRROR_BOTH;