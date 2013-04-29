//
//  StateResult.m
//  ARK Framework Example
//
//  Created by LegACy on 4/29/13.
//  Copyright (c) 2013 Raka Mahesa. All rights reserved.
//

#import "StateResult.h"
#import "RecordManager.h"
#import "HoleUtilities.h"
#import "StateGame.h"

//Constants;
const int RESULT_BUTTON_RETRY	= 1;
const int RESULT_BUTTON_EXIT	= 2;

@implementation StateResult

- (id)initWithGame:(StateGame *)game withScore:(int)score withEvaded:(int)evaded withDestroyed:(int)destroyed withNearMiss:(int)nearmiss withDuration:(int)seconds {
	//Super
	self = [super initWithID:STATE_RESULT withBackground:[ARKRectangle createFromX:0 fromY:0 withWidth:[[ARKUtilities instance] getWidth] withHeight:[[ARKUtilities instance] getHeight] withRed:0 withGreen:0 withBlue:0 withAlpha:0.4]];
	if (self) {
		//Initialize
		m_Game			= game;
		m_DrawPrevious 	= YES;
		
		//Check score
		[[RecordManager instance] showTutorial];
		if (score > [[RecordManager instance] getHighscore]) {
			//Set score
			[[RecordManager instance] setHighscore:score];
			[[RecordManager instance] save];
		}
		
		//Create panel
		m_Buttons			= [[ARKButtonContainer alloc] init];
		m_Panel				= [ARKImage createFromPath:[UTIL_INTERFACE_IMAGES stringByAppendingString:@"panel.json"]];
		//ARKButton* Exit		= m_Buttons.addButton(RESULT_BUTTON_EXIT, HoleUtilities.INTERFACE_IMAGES + "button.json", "Exit");
		ARKButton* Retry 	= [m_Buttons addButtonWithID:RESULT_BUTTON_RETRY withResource:[UTIL_INTERFACE_IMAGES stringByAppendingString:@"button.json"] withText:@"Retry"];
		float HOffset		= ([[ARKUtilities instance] getWidth] - [m_Panel getOriginalWidth]) /2;
		float Height		= [m_Panel getOriginalHeight] + [Retry getOriginalHeight] + 32;
		float VOffset		= (([[ARKUtilities instance] getHeight] - Height) / 2) + 14;
		
		//Positions panels
		float ScreenWidth = [[ARKUtilities instance] getWidth];
		[Retry setPositionAtX:ScreenWidth / 2 atY:[[ARKUtilities instance] getHeight] - (VOffset - 22) horizontallyAlignedTo:DRAWABLE_ANCHOR_HCENTER verticallyAlignedTo:DRAWABLE_ANCHOR_BOTTOM];
		//Exit.setPosition(Utilities.instance().getWidth() - HOffset, Utilities.instance().getHeight() - VOffset, Drawable.ANCHOR_RIGHT, Drawable.ANCHOR_BOTTOM);
		[m_Panel setPositionAtX:HOffset atY:VOffset];
		
		//Create labels
		m_Highscore	= [ARKLabel createWithText:[NSString stringWithFormat:@"Highscore: %d", [[RecordManager instance] getHighscore]] withFont:[HoleUtilities FONT_PIXEL_SMALL]];
		m_Total		= [ARKLabel createWithText:[NSString stringWithFormat:@"Total bullets  %d", evaded + destroyed + 8] withFont:[HoleUtilities FONT_PIXEL_SMALL]];
		m_Passed	= [ARKLabel createWithText:[NSString stringWithFormat:@"Flight time %d seconds", seconds] withFont:[HoleUtilities FONT_PIXEL_SMALL]];
		m_Evaded	= [ARKLabel createWithText:[NSString stringWithFormat:@"Bullets evaded  %d", evaded] withFont:[HoleUtilities FONT_PIXEL_SMALL]];
		m_NearMiss	= [ARKLabel createWithText:[NSString stringWithFormat:@"Near miss  %d", nearmiss] withFont:[HoleUtilities FONT_PIXEL_SMALL]];
		m_Score 	= [ARKLabel createWithText:[NSString stringWithFormat:@"Score %d", score] withFont:[HoleUtilities FONT_SCORE]];
		m_Result 	= [ARKLabel createWithText:@"Result" withFont:[HoleUtilities FONT_CAPS_SMALL]];
		
		//Set label positions
		[m_Evaded setPositionAtX:HOffset + 8 atY:VOffset + 10 + [m_Result getOriginalHeight] + 8];
		[m_Total setPositionAtX:HOffset + 8 atY:VOffset + 10 + [m_Result getOriginalHeight]  + 8 + [m_Evaded getOriginalHeight] + 2];
		[m_NearMiss setPositionAtX:HOffset + ([m_Panel getOriginalWidth] / 2) + 12 atY:VOffset + 10 + [m_Result getOriginalHeight] + 8];
		[m_Passed setPositionAtX:HOffset + 8 atY:VOffset + 10 + [m_Result getOriginalHeight] + 8 + [m_Evaded getOriginalHeight] + 2 + [m_NearMiss getOriginalHeight] + 14];
		[m_Score setPositionAtX:ScreenWidth / 2 atY:VOffset + [m_Panel getOriginalHeight] + 5 - [m_Highscore getOriginalHeight] horizontallyAlignedTo:DRAWABLE_ANCHOR_HCENTER verticallyAlignedTo:DRAWABLE_ANCHOR_BOTTOM];
		[m_Highscore setPositionAtX:ScreenWidth / 2 atY:VOffset + [m_Panel getOriginalHeight] - 4 horizontallyAlignedTo:DRAWABLE_ANCHOR_HCENTER verticallyAlignedTo:DRAWABLE_ANCHOR_BOTTOM];
		[m_Result setPositionAtX:ScreenWidth / 2 atY:VOffset + 10 horizontallyAlignedTo:DRAWABLE_ANCHOR_HCENTER];
	}
	
	//Return
	return self;
}

- (void)updateWithDelta:(long)time withKeys:(NSArray *)keys withTouches:(NSArray *)touches withAccelerometer:(ARKAccelerometerInfo *)accel {
	//Super
	[super updateWithDelta:time withKeys:keys withTouches:touches withAccelerometer:accel];
	
	//Update button
	int ID = [m_Buttons updateWithKeys:keys withTouches:touches];
	if (ID != CONTAINER_NO_BUTTON) {
		//Check result
		switch (ID) {
			case RESULT_BUTTON_RETRY:
				//Reset
				[m_Game setup];
				m_Active = false;
				break;
				
			case RESULT_BUTTON_EXIT:
				//Quit
				[[ARKStateManager instance] quit];
				break;
		}
	}
}

- (void)drawWithGL:(GLKBaseEffect *)gl {
	//Super
	[super drawWithGL:gl];
	
	//Draw components
	[m_Buttons drawWithGL:gl];
	[m_Panel drawWithGL:gl];
	[m_Evaded drawWithGL:gl];
	[m_Highscore drawWithGL:gl];
	[m_Total drawWithGL:gl];
	[m_NearMiss drawWithGL:gl];
	[m_Passed drawWithGL:gl];
	[m_Result drawWithGL:gl];
	[m_Score drawWithGL:gl];
}

@end
