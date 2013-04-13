//
//  StateFactory.h
//  Ark Framework
//
//  Created by LegACy on 4/13/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import <Foundation/Foundation.h>

//Forward declaration
@class ARKGameState;

@protocol ARKStateFactory <NSObject>

- (int)getFirstState;
- (ARKGameState*)createGameStateWithID:(int)ID withParameters:(NSSArray*)parameters;

@end
