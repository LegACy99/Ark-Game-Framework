//
//  StateGame.h
//  Ark Framework
//
//  Created by LegACy on 4/25/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "HoleState.h"

@interface StateGame : HoleState {
	//Data
	int 	m_Score;
	int 	m_Health;
	float 	m_PressX;
	float 	m_PressY;
	float 	m_Distance;
	BOOL	m_WasPressed;
	BOOL	m_GameStarted;
	BOOL	m_HasBeenDrawn;
	long 	m_BulletDuration;
	long 	m_TutorialTime;
	long 	m_IntroTime;
	long 	m_ShakeTime;
	long 	m_StartTime;
	long 	m_TintTime;
	long 	m_Time;
	long 	m_Cooldown;
	int		m_Destroyed;
	int		m_Nearmiss;
	int		m_Evaded;
	
	//Game
	ARKImage*		m_Sky;
	ARKImage*		m_Ship;
	NSMutableArray*	m_Holes;
	NSMutableArray*	m_Labels;
	NSMutableArray*	m_Bullets;
}

//Functions
- (void)setup;
- (void)start;

@end
