//
//  StateTitle.h
//  Ark Framework
//
//  Created by LegACy on 4/25/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "HoleState.h"

//Forward declaration
@class StateGame;

@interface StateTitle : HoleState {
	//Data
	long 				m_Timer;
	ARKImage*			m_Title;
	ARKLabel*			m_GameCredit;
	ARKLabel*			m_MusicCredit;
	ARKLabel*			m_Instruction;
	ARKButtonContainer*	m_Buttons;
	StateGame*			m_Game;
}

- (id)initWithGame:(StateGame*)game;

@end
