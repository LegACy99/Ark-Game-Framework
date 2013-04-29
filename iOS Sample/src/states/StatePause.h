//
//  StatePause.h
//  ARK Framework Example
//
//  Created by LegACy on 4/29/13.
//  Copyright (c) 2013 Raka Mahesa. All rights reserved.
//

#import "HoleState.h"

//Forward declaration
@class StateGame;

@interface StatePause : HoleState {
	//Components
	ARKButtonContainer*	m_Buttons;
	StateGame*			m_Game;
}

//Constructor
- (id)initWithGame:(StateGame*)game;

@end
