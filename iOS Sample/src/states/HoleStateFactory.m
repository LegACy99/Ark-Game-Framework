//
//  HoleStateFactory.m
//  Ark Framework
//
//  Created by LegACy on 4/25/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "HoleStateFactory.h"
#import "StateResult.h"
#import "StatePause.h"
#import "StateTitle.h"
#import "StateGame.h"

@implementation HoleStateFactory

- (int)getFirstState {
	//Game
	return STATE_GAME;
}

- (ARKGameState*)createGameStateWithID:(int)ID withParameters:(NSArray *)parameters {
	//Default
	HoleState* NewState = nil;
	
	//Based on ID
	if (ID == STATE_GAME) {
		//Create game
		NewState = [[StateGame alloc] init];
	} else if (ID == STATE_TITLE) {
		//Check
		if (parameters && [parameters count] >= 1 && [[parameters objectAtIndex:0] isKindOfClass:[StateGame class]])
			NewState = [[StateTitle alloc] initWithGame:[parameters objectAtIndex:0]];
	} else if (ID == STATE_PAUSE ) {
		//Check
		if (parameters && [parameters count] >= 1 && [[parameters objectAtIndex:0] isKindOfClass:[StateGame class]])
			NewState = [[StatePause alloc] initWithGame:[parameters objectAtIndex:0]];
	} else if (ID == STATE_RESULT) {
		//Check
		if (parameters && [parameters count] >= 6 &&
			[[parameters objectAtIndex:0] isKindOfClass:[StateGame class]] &&
			[[parameters objectAtIndex:1] isKindOfClass:[NSNumber class]] &&
			[[parameters objectAtIndex:2] isKindOfClass:[NSNumber class]] &&
			[[parameters objectAtIndex:3] isKindOfClass:[NSNumber class]] &&
			[[parameters objectAtIndex:4] isKindOfClass:[NSNumber class]] &&
			[[parameters objectAtIndex:5] isKindOfClass:[NSNumber class]])
			NewState = [[StateResult alloc] initWithGame:[parameters objectAtIndex:0]
											   withScore:[[parameters objectAtIndex:1] intValue]
											  withEvaded:[[parameters objectAtIndex:2] intValue]
										   withDestroyed:[[parameters objectAtIndex:3] intValue]
											withNearMiss:[[parameters objectAtIndex:4] intValue]
											withDuration:[[parameters objectAtIndex:5] intValue]
						];
	}
	
	//Return
	return NewState;
}

@end
