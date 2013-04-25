//
//  ARKButtonContainer.m
//  Ark Framework
//
//  Created by LegACy on 4/25/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKButtonContainer.h"
#import "ARKSoundManager.h"
#import "ARKTouchInfo.h"
#import "ARKButton.h"

//Constants
const int CONTAINER_NO_BUTTON = -1;

@implementation ARKButtonContainer

- (id)init {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		m_Pressed = -1;
		m_Buttons = [NSMutableArray array];
	}
	
	//Return
	return self;
}

- (ARKButton*)getButtonWithID:(int)ID {
	//Find
	ARKButton* Result = nil;
	for (int i = 0; i < [m_Buttons count]; i++) if ([[m_Buttons objectAtIndex:i] getID] == ID) Result = [m_Buttons objectAtIndex:i];
	
	//Return
	return Result;
}

- (ARKButton*)getButtonWithIndex:(int)index {
	//Return
	return [m_Buttons objectAtIndex:index];
}

- (ARKButton*)getPressedButton {
	//Get pressed button
	ARKButton* Result = nil;
	if (m_Pressed >= 0) Result = [m_Buttons objectAtIndex:m_Pressed];
	
	//Return
	return Result;
}

- (ARKButton*)addButtonWithID:(int)ID withResource:(NSString *)resource withText:(NSString *)text {
	return [self addButton:[[ARKButton alloc] initWithID:ID withResource:resource withText:text]];
}

- (ARKButton*)addButtonWithID:(int)ID withResource:(NSString *)resource withText:(NSString *)text withFont:(NSString *)font {
	return [self addButton:[[ARKButton alloc] initWithID:ID withResource:resource withText:text withFont:font]];
}

- (ARKButton*)addButton:(ARKButton *)button {
	//Add a button
	if (button) [m_Buttons addObject:button];
	return button;
}

- (void)removeButtons {
	//Destroy all buttons
	[m_Buttons removeAllObjects];
}

- (int)updateWithKeys:(NSArray *)keys withTouches:(NSArray *)touches {
	//Initialize
	int Result = CONTAINER_NO_BUTTON;
	
	//If there's touch
	if (touches) {
		//Check
		if ([m_Buttons count] <= m_Pressed) m_Pressed = -1;
		
		//If pressed
		id Touch = [touches objectAtIndex:0];
		if (Touch && [Touch isPressed]) {
			//If no button is pressed
			if (m_Pressed < 0) {
				//For each button
				for (int i = 0; i < [m_Buttons count]; i++) {
					//If pressed
					ARKButton* Button = [m_Buttons objectAtIndex:i];
					if (Button && [Button visible] && [Button active] && [Button isInsideX:[Touch getStartX] Y:[Touch getStartY]]) {
						//Set pressed button
						m_Pressed = i;
						[Button setState:BUTTON_STATE_PRESSED];
						
						//SFX
						if ([Button getPressSFX]) [[ARKSoundManager instance] playSFXWithName:[Button getPressSFX]];
					}
				}
			} else {
				//Set state
				ARKButton* Pressed = [m_Buttons objectAtIndex:m_Pressed];
				if (Pressed && [Pressed isInsideX:[Touch getCurrentX] Y:[Touch getCurrentY]]) 	[Pressed setState:BUTTON_STATE_PRESSED];
				else																			[Pressed setState:BUTTON_STATE_NORMAL];
			}
		} else {
			//If there's a pressed button
			ARKButton* Pressed = [self getPressedButton];
			if (Pressed) {
				//If released inside
				if ([Pressed isInsideX:[Touch getCurrentX] Y:[Touch getCurrentY]])	{
					//Set as result
					Result = [Pressed getID];
					
					//SFX
					if ([Pressed getReleaseSFX]) [[ARKSoundManager instance] playSFXWithName:[Pressed getReleaseSFX]];
				}
				
				//Reset button state
				[Pressed setState:BUTTON_STATE_NORMAL];
			}
			
			//Nothing pressed
			m_Pressed = -1;
		}
	}
	
	//Return
	return Result;
}

- (void)drawWithGL:(GLKBaseEffect *)gl {
	//Draw all buttons
	for (int i = 0; i < [m_Buttons count]; i++) [[m_Buttons objectAtIndex:0] drawWithGL:gl];
}

@end
