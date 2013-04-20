//
//  ARKiOSStateManager.m
//  Ark Framework
//
//  Created by LegACy on 4/19/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKAccelerometerInfo.h"
#import "ARKiOSStateManager.h"
#import "ARKStateFactory.h"
#import "ARKGameState.h"
#import "ARKDevice.h"

@implementation ARKiOSStateManager

- (id)init {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		m_Paused		= NO;
		m_Resumed		= NO;
		m_Initialized	= NO;
		m_RemovalDepth	= 0;
		m_CurrentTime	= CACurrentMediaTime();
		m_StateList		= [NSMutableArray array];
	}
	
	//Return
	return self;
}

+ (ARKStateManager*)instance {
	//Static objects
	static dispatch_once_t Token		= 0;
	static ARKiOSStateManager* Instance	= nil;
	
	//Only create object once
	dispatch_once(&Token, ^{
		Instance = [[self alloc] init];
	});
	
	//Return
	return Instance;
}

- (void)initialize {
	//Skip if has been initialized
	if (m_Initialized) return;
	m_Initialized = YES;
	
	//Go to first state
	if (m_Factory) [self goToStateID:[m_Factory getFirstState] withParameters:nil swappedWithCurrent:NO];
}

- (void)quit {
	//Kill all states
	while ([m_StateList count] > 0) [self removeState];
	
	//No longer running
	m_Running = NO;
}

- (void)pause {
	//Pause
	m_Paused	= YES;
	m_Resumed	= NO;
}

- (void)resume {
	//Resume
	if (m_Paused) m_Resumed = YES;
	m_Paused = NO;
}

- (void)goToStateID:(int)ID withParameters:(NSArray *)parameters swappedWithCurrent:(BOOL)swap {
	//If state list exist
	if (m_StateList) {
		//Prepare
		int i		= 0;
		BOOL Exist	= NO;
		
		//Find state
		while (!Exist && i < [m_StateList count]) {
			//Is it exist?
			id State = [m_StateList objectAtIndex:i];
			if (State && [State isKindOfClass:[ARKGameState class]])
				if ([State getID] == ID) Exist = true;
				
			//Next
			i++;
		}
		
		//If exist, return, otherwise, add a new one
		if (Exist) [self returnToStateID:ID withParameters:parameters];
		else {
			//Create new state
			ARKGameState* NewState = nil;
			if (m_Factory) NewState = [m_Factory createGameStateWithID:ID withParameters:parameters];
			if (!NewState) return;
			
			//If not swapped, add
			if (!swap) [self addState:NewState];
			else {
				//Remove top
				m_RemovalDepth++;
				
				//Add the state before current state
				[self addState:NewState];
				
				//If not empty
				if ([m_StateList count] > 1) {
					//Swap
					[m_StateList removeObjectAtIndex:[m_StateList count] - 1];
					[m_StateList insertObject:NewState atIndex:[m_StateList count] - 1];
				}
			}
		}
	}
}

- (void)addState:(ARKGameState *)state {
	//Skip if null
	if (!state) return;
	
	//If current exist exit
	if ([m_StateList lastObject] && [[m_StateList lastObject] isKindOfClass:[ARKGameState class]]) [[m_StateList lastObject] onExit];
	
	//Add state
	[m_StateList addObject:state];
	
	//Initialize
	[state initialize];
	[state onEnter];
}

- (void)removeState {
	//Check
	if (!m_StateList)													return;
	if (![m_StateList lastObject])										return;
	if (![[m_StateList lastObject] isKindOfClass:[ARKGameState class]])	return;
	
	//Remove state
	[[m_StateList lastObject] onExit];
	[[m_StateList lastObject] onRemove];
	[m_StateList removeLastObject];
	
	//Enter state below
	if ([m_StateList lastObject] && [[m_StateList lastObject] isKindOfClass:[ARKGameState class]]) [[m_StateList lastObject] onEnter];
}

- (void)returnToStateID:(int)ID withParameters:(NSArray *)parameters {
	//Initialize
	m_RemovalDepth	= 0;
	BOOL Found		= NO;
	int i			= [m_StateList count] - 1;
	
	//While exist
	while (i >= 0 && !Found) {
		//Remove
		if ([m_StateList objectAtIndex:i]) {
			//If same ID
			if ([[m_StateList objectAtIndex:i] getID] == ID) {
				//Found
				Found = YES;
				[[m_StateList objectAtIndex:i] onEnter];
			}
		}
		
		//Next
		i--;
	}
}

- (void)run {
	//Initialize if not
	if (!m_Initialized) [self initialize];
	
	//Save
	long Difference	= (long)((CACurrentMediaTime() - m_CurrentTime) * 1000);
	m_CurrentTime	= CACurrentMediaTime();
	
	//Trim states
	for (int i = 0; i < m_RemovalDepth; i++) [self removeState];
	m_RemovalDepth = 0;
	
	//Top state exist?
	BOOL ActiveFound	= NO;
	BOOL TopExist		= [m_StateList lastObject] != nil;
	
	//While not empty
	while ([m_StateList count] > 0 && TopExist && !ActiveFound) {
		//If top state is not active, remove
		if (![[m_StateList lastObject] isActive]) [self removeState];
		//Otherwise, active state is found
		else ActiveFound = true;
		
		//Check top state
		TopExist = [m_StateList count] <= 0 ? false : [m_StateList lastObject] != nil;
	}
	
	//Quit if empty or last state doesn't exist
	if ([m_StateList count] <= 0 || !TopExist) m_Running = NO;
	else {
		//Create drawn and updated list
		NSMutableArray* Drawn 	= [NSMutableArray arrayWithObject:[m_StateList lastObject]];
		NSMutableArray* Updated = [NSMutableArray arrayWithObject:[m_StateList lastObject]];
		
		//While update previous
		int Index = [m_StateList count] - 1;
		while ([[Updated lastObject] updatePrevious] && Index > 0) {
			//Next
			Index--;
			if (Index >= 0 && [m_StateList objectAtIndex:Index]) [Updated addObject:[m_StateList objectAtIndex:Index]];
		}
		
		//While draw previous
		Index = [m_StateList count] - 1;
		while ([[Drawn lastObject] drawPrevious] && Index > 0) {
			//Next
			Index--;
			if (Index >= 0 && [m_StateList objectAtIndex:Index]) [Drawn addObject:[m_StateList objectAtIndex:Index]];
		}
		
		//If paused
		if (m_Paused) {
			//Update if should run in the back
			//TODO
		} else {
			//If resumed
			if (m_Resumed) {
				//Resume to state
				if ([Updated count] > 0 && [Updated objectAtIndex:0]) [[Updated objectAtIndex:0] onResume];
				
				//No longer resumed
				m_Resumed = NO;
			} else {
				//For each updated
				for (int i = [Updated count] - 1; i >= 0; i--) {
					//Get data
					NSArray* Keys				= i == 0 ? [[ARKDevice instance] getKeys] : [NSArray array];
					NSArray* Touches			= i == 0 ? [[ARKDevice instance] getTouches] : [NSArray array];
					ARKAccelerometerInfo* Accel	= i == 0 ? [[ARKDevice instance] getAccelerometer] : nil;
					
					//Update
					[[Updated objectAtIndex:i] updateWithDelta:Difference withKeys:Keys withTouches:Touches withAccelerometer:Accel];
				}
				
				//Draw all
				for (int i = [Drawn count] - 1; i >= 0; i--) [[Drawn objectAtIndex:i] drawWithGL:nil];
			}
		}
	}
}

@end
