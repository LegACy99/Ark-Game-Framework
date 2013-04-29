//
//  StateResult.h
//  ARK Framework Example
//
//  Created by LegACy on 4/29/13.
//  Copyright (c) 2013 Raka Mahesa. All rights reserved.
//

#import "HoleState.h"

//Forward declaration
@class StateGame;

@interface StateResult : HoleState {
	//Components
	ARKImage*			m_Panel;
	ARKLabel*			m_Total;
	ARKLabel*			m_Score;
	ARKLabel*			m_Passed;
	ARKLabel*			m_Evaded;
	ARKLabel*			m_Result;
	ARKLabel*			m_NearMiss;
	ARKLabel*			m_Highscore;
	ARKButtonContainer* m_Buttons;
	StateGame*			m_Game;
}

//Constructor
- (id)initWithGame:(StateGame*)game withScore:(int)score withEvaded:(int)evaded withDestroyed:(int)destroyed withNearMiss:(int)nearmiss withDuration:(int)seconds;

@end
