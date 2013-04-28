//
//  ARKStateManager.h
//  Ark Framework
//
//  Created by LegACy on 4/19/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import <Foundation/Foundation.h>

//Forward declarations
@protocol ARKStateFactory;
@class ARKGameState;

@interface ARKStateManager : NSObject {
	//Data
	BOOL				m_Running;
	id<ARKStateFactory> m_Factory;
}

//Property
@property (readonly, getter = isRunning) BOOL running;

//Factory method
+ (ARKStateManager*)instance;

//Setup
- (void)setupWithFactory:(id<ARKStateFactory>)factory;

//State navigation
- (void)initialize;
- (void)removeState;
- (void)addState:(ARKGameState*)state;
- (void)returnToStateID:(int)ID withParameters:(NSArray*)parameters;
- (void)goToStateID:(int)ID withParameters:(NSArray*)parameters swappedWithCurrent:(BOOL)swap;

//Updating
- (void)run;
- (void)quit;
- (void)pause;
- (void)resume;

@end
