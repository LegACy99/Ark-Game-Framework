//
//  StatePause.m
//  ARK Framework Example
//
//  Created by LegACy on 4/29/13.
//  Copyright (c) 2013 Raka Mahesa. All rights reserved.
//

#import "StatePause.h"
#import "HoleUtilities.h"
#import "StateGame.h"

//Constants;
const int PAUSE_BUTTON_RETRY	= 1;
const int PAUSE_BUTTON_RESUME	= 2;
const int PAUSE_BUTTON_EXIT		= 3;

@implementation StatePause

- (id)initWithGame:(StateGame *)game {
	//Super
	self = [super initWithID:STATE_PAUSE withBackground:[ARKRectangle createFromX:0 fromY:0 withWidth:[[ARKUtilities instance] getWidth] withHeight:[[ARKUtilities instance] getHeight] withRed:0 withGreen:0 withBlue:0 withAlpha:0.4]];
	if (self) {
		//Initialize
		m_Game			= game;
		m_DrawPrevious 	= YES;
		
		//Create buttons
		m_Buttons			= [[ARKButtonContainer alloc] init];
		float Right			= [[ARKUtilities instance] getWidth] - 8;
		float HalfHeight	= [[ARKUtilities instance] getHeight] / 2;
		//ARKButton* Exit		= [m_Buttons addButtonWithID:PAUSE_BUTTON_EXIT withResource:[UTIL_INTERFACE_IMAGES stringByAppendingString:@"button.json"] withText:@"Exit"];
		ARKButton* Retry 	= [m_Buttons addButtonWithID:PAUSE_BUTTON_RETRY withResource:[UTIL_INTERFACE_IMAGES stringByAppendingString:@"button.json"] withText:@"Retry"];
		ARKButton* Resume 	= [m_Buttons addButtonWithID:PAUSE_BUTTON_RESUME withResource:[UTIL_INTERFACE_IMAGES stringByAppendingString:@"button.json"] withText:@"Resume"];
		[Resume setPositionAtX:Right atY:HalfHeight + 62 horizontallyAlignedTo:DRAWABLE_ANCHOR_RIGHT verticallyAlignedTo:DRAWABLE_ANCHOR_VCENTER];
		//[Exit setPositionAtX:Right atY:HalfHeight - 70 horizontallyAlignedTo:DRAWABLE_ANCHOR_RIGHT verticallyAlignedTo:DRAWABLE_ANCHOR_VCENTER];
		[Retry setPositionAtX:Right atY:HalfHeight horizontallyAlignedTo:DRAWABLE_ANCHOR_RIGHT verticallyAlignedTo:DRAWABLE_ANCHOR_VCENTER];
	}
	
	//Return
	return self;
}

- (void)updateWithDelta:(long)time withKeys:(NSArray *)keys withTouches:(NSArray *)touches withAccelerometer:(ARKAccelerometerInfo *)accel {
	//Super
	[super updateWithDelta:time withKeys:keys withTouches:touches withAccelerometer:accel];
	
	//Check back
	BOOL Back = NO;
	for (int i = 0; i < [keys count]; i++) if ([[keys objectAtIndex:i] intValue] == [[ARKDevice instance] getBackButton]) Back = YES;
	
	//If back
	if (Back) {
		//Done
		[[ARKSoundManager instance] playSFXWithName:[UTILITIES_SFX_FOLDER stringByAppendingString:@"cancel.aiff"]];
		m_Active = NO;
	} else {
		//Update button
		int ID = [m_Buttons updateWithKeys:keys withTouches:touches];
		
		//If pressed
		if (ID != CONTAINER_NO_BUTTON) {
			//Check result
			switch (ID) {
				case PAUSE_BUTTON_RESUME:
					//Kill
					m_Active = NO;
					break;
					
				case PAUSE_BUTTON_RETRY:
					//Reset
					[m_Game setup];
					m_Active = NO;
					break;
					
				case PAUSE_BUTTON_EXIT:
					//Quit
					[[ARKStateManager instance] quit];
					break;
			}
		}
	}
}

- (void)drawWithGL:(GLKBaseEffect *)gl {
	//Super
	[super drawWithGL:gl];
	
	//Draw components
	[m_Buttons drawWithGL:gl];
}

@end
