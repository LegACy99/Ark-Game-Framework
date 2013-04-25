//
//  ARKButtonContainer.h
//  Ark Framework
//
//  Created by LegACy on 4/25/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import <Foundation/Foundation.h>

//Forward declaration
@class ARKButton, GLKBaseEffect;

@interface ARKButtonContainer : NSObject {
	//Data
	int				m_Pressed;
	NSMutableArray*	m_Buttons;
}

//Getters
- (ARKButton*)getButtonWithID:(int)ID;
- (ARKButton*)getButtonWithIndex:(int)index;
- (ARKButton*)getPressedButton;

//Setters
- (ARKButton*) addButtonWithID:(int)ID withResource:(NSString*)resource withText:(NSString*)text;
- (ARKButton*) addButtonWithID:(int)ID withResource:(NSString*)resource withText:(NSString*)text withFont:(NSString*)font;
- (ARKButton*) addButton:(ARKButton*)button;
- (void)removeButtons;

//Function
- (int)updateWithKeys:(NSArray*)keys withTouches:(NSArray*)touches;
- (void)drawWithGL:(GLKBaseEffect*)gl;

@end

//Constants
extern const int CONTAINER_NO_BUTTON;
