//
//  ARKButton.h
//  Ark Framework
//
//  Created by LegACy on 4/25/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKCroppable.h"

@interface ARKButton : ARKCroppable {
	//Size
	float m_InputX;
	float m_InputY;
	float m_InputWidth;
	float m_InputHeight;
	
	//Data
	int			m_ID;
	int			m_State;
	NSArray*	m_Labels;
	NSArray*	m_Images;
	NSString*	m_FontInactive;
	NSString*	m_ReleasedSFX;
	NSString*	m_PressedSFX;
}

//Properties
@property (readonly, getter = getID)			int	ID;
@property (readonly, getter = getPressSFX)		NSString* pressedSFX;
@property (readonly, getter = getReleaseSFX)	NSString* releasedSFX;
@property										BOOL visible;
@property										BOOL active;

//Constructors
- (id)initWithID:(int)ID withResource:(NSString*)resource;
- (id)initWithID:(int)ID withImages:(NSArray*)images;
- (id)initWithID:(int)ID withResource:(NSString*)resource withText:(NSString*)text;
- (id)initWithID:(int)ID withImages:(NSArray*)images withText:(NSString*)text;
- (id)initWithID:(int)ID withResource:(NSString*)resource withText:(NSString*)text withFont:(NSString*)font;
- (id)initWithID:(int)ID withImages:(NSArray*)images withText:(NSString*)text withFont:(NSString*)font;
- (id)initWithID:(int)ID withResource:(NSString*)resource atX:(float)x atY:(float)y;
- (id)initWithID:(int)ID withImages:(NSArray*)images atX:(float)x atY:(float)y;
- (id)initWithID:(int)ID withResource:(NSString*)resource withText:(NSString*)text withFont:(NSString*)font atX:(float)x atY:(float)y;
- (id)initWithID:(int)ID withImages:(NSArray*)images withText:(NSString*)text withFont:(NSString*)font atX:(float)x atY:(float)y;
- (id)initWithID:(int)ID withResource:(NSString*)resource withText:(NSString*)text withFont:(NSString*)font1 andFont:(NSString*)font2 andFont:(NSString*)font3 atX:(float)x atY:(float)y;
- (id)initWithID:(int)ID withImages:(NSArray*)images withText:(NSString*)text withFont:(NSString*)font1 andFont:(NSString*)font2 andFont:(NSString*)font3 atX:(float)x atY:(float)y;

//Getter
- (BOOL)isInsideX:(float)x Y:(float)y;

//Setters
- (void)setState:(int)state;
- (void)setInactiveImage:(NSDictionary*)json;
- (void)setSFXForPress:(NSString*)pressed forRelease:(NSString*)released;
- (void)setSizeWithX:(float)x withY:(float)y withWidth:(float)width withHeight:(float)height;
- (void)setSizeWithX:(float)x withY:(float)y withWidth:(float)width withHeight:(float)height offsetScaled:(BOOL)scaleOffset sizeScaled:(BOOL)scaleSize;

@end

//Constants
extern const int BUTTON_STATE_NORMAL;
extern const int BUTTON_STATE_PRESSED;
extern const int BUTTON_STATE_INACTIVE;
