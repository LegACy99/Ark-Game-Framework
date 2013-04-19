//
//  ARKiOSStateManager.h
//  Ark Framework
//
//  Created by LegACy on 4/19/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKStateManager.h"

@interface ARKiOSStateManager : ARKStateManager {
	//Data
	BOOL			m_Paused;
	BOOL			m_Resumed;
	BOOL			m_Initialized;
	NSMutableArray*	m_StateList;
	double			m_CurrentTime;
	int				m_RemovalDepth;
}

//Singleton
+ (ARKStateManager*)instance;

@end
