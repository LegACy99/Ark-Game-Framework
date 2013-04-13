//
//  ARKGameState.h
//  Ark Framework
//
//  Created by LegACy on 4/13/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import <Foundation/Foundation.h>

//Forward declaration
@class ARKDrawable, GLKBaseEffect;

@interface ARKGameState : NSObject {
	//Members
	int				m_ID;
	BOOL			m_Active;
	BOOL			m_Running;
	BOOL			m_UpdatePrevious;
	BOOL			m_DrawPrevious;
	ARKDrawable*	m_Background;
}

//Properties
@property (readonly, getter = getID)			int ID;
@property (readonly, getter = isActive)			BOOL active;
@property (readonly, getter = runsInBackground) BOOL running;
@property (readonly)							BOOL drawPrevious;
@property (readonly)							BOOL updatePrevious;

//Constructors
- (id)initWithID:(int)ID;
- (id)initWithID:(int)ID withBackground:(ARKDrawable*)background;
- (id)initWithID:(int)ID withBackgroundFromFile:(NSString*)file;

//State event handler
- (void)initialize;
- (void)onEnter;
- (void)onRemove;
- (void)onExit;
- (void)onResume;

//Update and draw
- (void)updateWithDelta:(long)time withKeys:(NSArray*)keys withTouches:(NSArray*)touches withAccelerometer:(id)accel;
- (void)drawWithGL:(GLKBaseEffect*)gl;

@end
