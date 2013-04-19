//
//  ARKStateManager.m
//  Ark Framework
//
//  Created by LegACy on 4/19/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKStateManager.h"
#import "ARKiOSStateManager.h"

@implementation ARKStateManager

//Synthesize
@synthesize running = m_Running;

- (id)init {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		m_Running = YES;
		m_Factory = nil;
	}
	
	//Return
	return  self;
}

+ (ARKStateManager*)instance {
	//Return the correct singleton
	return [ARKiOSStateManager instance];
}

- (void)setupWithFactory:(id<ARKStateFactory>)factory {
	//Save
	m_Factory = factory;
}

//Abstracts
- (void)run																						{ [self doesNotRecognizeSelector:_cmd]; }
- (void)quit																					{ [self doesNotRecognizeSelector:_cmd]; }
- (void)pause																					{ [self doesNotRecognizeSelector:_cmd]; }
- (void)resume																					{ [self doesNotRecognizeSelector:_cmd]; }
- (void)initialize																				{ [self doesNotRecognizeSelector:_cmd]; }
- (void)removeState																				{ [self doesNotRecognizeSelector:_cmd]; }
- (void)addState:(ARKGameState*)state															{ [self doesNotRecognizeSelector:_cmd]; }
- (void)returnToStateID:(int)ID withParameters:(NSArray*)parameters								{ [self doesNotRecognizeSelector:_cmd]; }
- (void)goToStateID:(int)ID withParameters:(NSArray*)parameters swappedWithCurrent:(BOOL)swap	{ [self doesNotRecognizeSelector:_cmd]; }

@end
