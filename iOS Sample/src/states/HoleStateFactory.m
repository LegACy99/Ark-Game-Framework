//
//  HoleStateFactory.m
//  Ark Framework
//
//  Created by LegACy on 4/25/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "HoleStateFactory.h"
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
	}
	
	//Return
	return NewState;
}

@end
