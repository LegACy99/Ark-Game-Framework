//
//  StateTitle.m
//  Ark Framework
//
//  Created by LegACy on 4/25/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "StateTitle.h"
#import "HoleUtilities.h"
#import "StateGame.h"

@implementation StateTitle

- (id)initWithGame:(StateGame *)game {
	//Super
	self = [super initWithID:STATE_TITLE];
	if (self) {
		//Initialize
		m_Game			= game;
		m_Timer			= 1000;
		m_DrawPrevious 	= true;
		float Height	= [[ARKUtilities instance] getHeight];
		float Width		= [[ARKUtilities instance] getWidth];
		
		//Create stuff
		m_Title			= [ARKImage createFromPath:[UTIL_INTERFACE_IMAGES stringByAppendingString:@"title.json"]];
		m_MusicCredit 	= [ARKLabel createWithText:@"Music by Farcloud" withFont:[HoleUtilities FONT_PIXEL_SMALL]];
		m_GameCredit 	= [ARKLabel createWithText:@"Game by Raka Mahesa" withFont:[HoleUtilities FONT_PIXEL_SMALL]];
		m_Instruction 	= [ARKLabel createWithText:@"Tap anywhere to start playing" withFont:[HoleUtilities FONT_PIXEL]];
		[m_Title setPositionAtX:Width / 2 atY:Height / 3 horizontallyAlignedTo:DRAWABLE_ANCHOR_HCENTER verticallyAlignedTo:DRAWABLE_ANCHOR_VCENTER];
		[m_GameCredit setPositionAtX:(Width * 0.25) - 12 atY:Height - 8 horizontallyAlignedTo:DRAWABLE_ANCHOR_HCENTER verticallyAlignedTo:DRAWABLE_ANCHOR_BOTTOM];
		[m_MusicCredit setPositionAtX:(Width * 0.75) + 12 atY:Height - 8 horizontallyAlignedTo:DRAWABLE_ANCHOR_HCENTER verticallyAlignedTo:DRAWABLE_ANCHOR_BOTTOM];
		[m_Instruction setPositionAtX:Width / 2 atY:Height * 0.7 horizontallyAlignedTo:DRAWABLE_ANCHOR_HCENTER verticallyAlignedTo:DRAWABLE_ANCHOR_VCENTER];
		
		//Create buttons
		m_Buttons = [[ARKButtonContainer alloc] init];
		[[m_Buttons addButton:[[ARKButton alloc] initWithID:100 withResource:nil]] setSizeWithX:0 withY:0 withWidth:Width withHeight:Height];
		
		//Play music
		[[ARKSoundManager instance] playBGM];
	}
	
	//Return
	return self;
}

- (void)updateWithDelta:(long)time withKeys:(NSArray *)keys withTouches:(NSArray *)touches withAccelerometer:(ARKAccelerometerInfo *)accel {
	//Super
	[super updateWithDelta:time withKeys:keys withTouches:touches withAccelerometer:accel];
	
	//Manage timer
	m_Timer -= time;
	if (m_Timer < 0) m_Timer += 1000;
	
	//Update button
	int ID = [m_Buttons updateWithKeys:keys withTouches:touches];
	if (ID != CONTAINER_NO_BUTTON) {
		//Deactivate
		m_Active = NO;
		[m_Game start];
	}
}

- (void) drawWithGL:(GLKBaseEffect *)gl {
	//Draw components
	[m_Title drawWithGL:gl];
	[m_GameCredit drawWithGL:gl];
	[m_MusicCredit drawWithGL:gl];
	if (m_Timer >= 500) [m_Instruction drawWithGL:gl];
}

@end
