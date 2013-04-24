//
//  ARKGameState.m
//  Ark Framework
//
//  Created by LegACy on 4/13/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKGameState.h"
#import "ARKAccelerometerInfo.h"
#import "ARKImage.h"

@implementation ARKGameState

//Synthesize properties
@synthesize ID				= m_ID;
@synthesize active			= m_Active;
@synthesize running			= m_Running;
@synthesize drawPrevious	= m_DrawPrevious;
@synthesize updatePrevious	= m_UpdatePrevious;

- (id)initWithID:(int)ID {
	//No background
	return [self initWithID:ID withBackground:nil];
}

- (id)initWithID:(int)ID withBackground:(ARKDrawable*)background {
	//No background
	self = [self initWithID:ID withBackgroundFromFile:nil];
	if (self) m_Background = background;
	
	//Return
	return self;
}

- (id)initWithID:(int)ID withBackgroundFromFile:(NSString*)file {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		m_ID				= ID;
		m_Active			= false;
		m_Running			= false;
		m_DrawPrevious		= false;
		m_UpdatePrevious	= false;
		m_Background		= nil;
		
		//If background exist
		if (file != nil) m_Background = [ARKImage createFromPath:file];
	}
	
	//Return
	return self;
}

- (void)initialize {
	//Set active
	m_Active = true;
}

- (void)onEnter		{}
- (void)onRemove	{}
- (void)onExit		{}
- (void)onResume	{}

- (void)updateWithDelta:(long)time withKeys:(NSArray*)keys withTouches:(NSArray*)touches withAccelerometer:(ARKAccelerometerInfo*)accel {
	//Do nothing here
}

- (void)drawWithGL:(GLKBaseEffect*)gl {
	//Draw background if exist
	if (m_Background) [m_Background drawWithGL:gl];
}

@end
