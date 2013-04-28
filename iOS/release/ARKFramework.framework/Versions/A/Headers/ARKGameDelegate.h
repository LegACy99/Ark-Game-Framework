//
//  AppDelegate.h
//  Ark Framework
//
//  Created by LegACy on 4/6/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import <UIKit/UIKit.h>

@class GLKViewController;

//Class declaration
@interface ARKGameDelegate : UIResponder <UIApplicationDelegate>

//Properties
@property (strong, nonatomic) UIWindow			*window;
@property (strong, nonatomic) GLKViewController	*viewController;

//Function
- (void)createViewController;
- (void)initialize;

@end
