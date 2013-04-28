//
//  StateGame.m
//  Ark Framework
//
//  Created by LegACy on 4/25/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "StateGame.h"
#import "RecordManager.h"
#import "HoleUtilities.h"

//Constants
const int MAX_HEALTH 		= 80;
const int BUTTON_PAUSE		= 1;
const int BUTTON_ATTRACT 	= 2;
const int BUTTON_RETRACT 	= 3;
const long TINT_DURATION 	= 250;
const long INTRO_DURATION 	= 19000;
const float SHIP_SPEED		= 80;

@interface StateGame ()

//Private functions
- (void)updateMovingWithDeltaTime:(long)time;
- (void)updateHolesWithDeltaTime:(long)time withTouch:(ARKTouchInfo*)touch;
- (void)updateBulletsWithDeltaTime:(long)time;
- (void)updateLabelsWithDeltaTime:(long)time;
- (void)changeHealth:(int)change;
- (void)changeScore:(int)score;

@end

@implementation StateGame

- (id)init {
	//Super
	self = [super initWithID:STATE_GAME];
	if (self) {
		//Load stuff
		[[RecordManager instance] load];
		[[ARKResourceManager instance] addImageFromFile:[UTIL_GAME_IMAGES stringByAppendingString:@"sky.json"]];
		[[ARKResourceManager instance] addImageFromFile:[UTIL_GAME_IMAGES stringByAppendingString:@"ship.json"]];
		[[ARKResourceManager instance] addImageFromFile:[UTIL_INTERFACE_IMAGES stringByAppendingString:@"title.json"]];
		[[ARKResourceManager instance] addTextureFromFile:[UTILITIES_TEXTURE_FOLDER stringByAppendingString:@"texture.png"] withAntiAlias:NO];
		[[ARKResourceManager instance] addTextureFromFile:[HoleUtilities FONT_PIXEL_SMALL_TEXTURE] withAntiAlias:NO];
		[[ARKResourceManager instance] addBGMFromFile:[UTILITIES_BGM_FOLDER stringByAppendingString:@"intro.mp3"]];
		[[ARKResourceManager instance] addFontFromFile:[HoleUtilities FONT_PIXEL_SMALL]];
		
		//Load
		[[ARKResourceManager instance] start];
		while (![[ARKResourceManager instance] isFinished]) [[ARKResourceManager instance] update];
		
		//Initialize
		m_Holes			= [NSMutableArray array];
		m_Labels		= [NSMutableArray array];
		m_Bullets		= [NSMutableArray array];
		m_HasBeenDrawn	= NO;
		m_GameStarted	= NO;
		m_StartTime		= 0;
		
		//Create components
		m_Ship	= [ARKImage createFromPath:[UTIL_GAME_IMAGES stringByAppendingString:@"ship.json"]];
		m_Sky	= [ARKImage createFromPath:[UTIL_GAME_IMAGES stringByAppendingString:@"sky.json"]];
		 
		//Positions
		[m_Ship setPositionAtX:[[ARKUtilities instance] getWidth] / 2
						   atY:[[ARKUtilities instance] getHeight] + 10
		 horizontallyAlignedTo:DRAWABLE_ANCHOR_HCENTER
		   verticallyAlignedTo:DRAWABLE_ANCHOR_VCENTER];
				
		//Start
		[self setup];
	}
	
	//Return
	return self;
}

- (void)initialize {
	//Super
	[super initialize];
	
	//Create title
	[[ARKStateManager instance] goToStateID:STATE_TITLE withParameters:[NSArray arrayWithObject:self] swappedWithCurrent:NO];
}

- (void)setup {
	m_ShakeTime = 100;
}

- (void)start {
	//Start
	m_StartTime = 1000;
	
	//Play music
	[[ARKSoundManager instance] loadBGMWithName:[UTILITIES_BGM_FOLDER stringByAppendingString:@"bgm.mp3"]];
	[[ARKSoundManager instance] playBGM];
}

- (void)updateWithDelta:(long)time withKeys:(NSArray *)keys withTouches:(NSArray *)touches withAccelerometer:(ARKAccelerometerInfo *)accel {
	//Super
	[super updateWithDelta:time withKeys:keys withTouches:touches withAccelerometer:accel];
	
	//if not started
	if (!m_GameStarted) {
		//Check start time
		if (m_StartTime > 0) {
			//Manage
			m_StartTime -= time;
			if (m_StartTime < 0)  m_StartTime = 0;
			
			//Move ship
			[m_Ship setPositionAtX:[[ARKUtilities instance] getWidth] / 2
							   atY:([[ARKUtilities instance] getHeight] / 2) * (1 + ((float)m_StartTime / 1000))
			 horizontallyAlignedTo:DRAWABLE_ANCHOR_HCENTER
			   verticallyAlignedTo:DRAWABLE_ANCHOR_VCENTER];
			if (m_StartTime == 0) m_GameStarted = YES;
		}
	} else {
		//Update time
		m_Time += time;
		
		//Check back
		BOOL Back = NO;
		for (int i = 0; i < [keys count]; i++) if ([[keys objectAtIndex:i] intValue] == [[ARKDevice instance] getBackButton]) Back = YES;
		
		if (Back) {
			//Pause
			m_Active = false;
		} else {
			//Check button
			
			//Update cooldown
			if (m_Cooldown > 0) m_Cooldown -= time;
			if (m_TintTime > 0) {
				m_TintTime -= time;
				if (m_TintTime <= 0) [m_Ship setTintWithRedF:1 withGreenF:1 withBlueF:1 withAlphaF:1];
			}
			
			//Update
			/*if (m_Tutorial2 && !m_Tutorial1) {
				//Manage timer
				if (m_TutorialTime > 0) {
					m_TutorialTime -= time;
					updateMoving(time);
				}
			} else*/ [self updateMovingWithDeltaTime:time];
			/*if (!m_Tutorial1)*/ [self updateHolesWithDeltaTime:time withTouch:[touches objectAtIndex:0]];
			[self updateLabelsWithDeltaTime:time];
		}
	}
}

- (void)updateMovingWithDeltaTime:(long)time {
	//Check shaking
	if (m_ShakeTime > 0) {
		//Manage time
		m_ShakeTime -= time;
		if (m_ShakeTime <= 0) {
			//Shake
			int ShakeX	= [[ARKUtilities instance] getARandomNumberBetween:0 to:3] + (m_TintTime > 0 ? 3 : 0);
			int ShakeY	= [[ARKUtilities instance] getARandomNumberBetween:0 to:3] + (m_TintTime > 0 ? 3 : 0);
			float X 	= ([[ARKUtilities instance] getWidth] / 2) + (ShakeX * (([[ARKUtilities instance] getARandomNumberBetween:0 to:2] * 2) - 1));
			float Y 	= ([[ARKUtilities instance] getHeight] / 2) + (ShakeY * (([[ARKUtilities instance] getARandomNumberBetween:0 to:2] * 2) - 1));
			[m_Ship setPositionAtX:X atY:Y horizontallyAlignedTo:DRAWABLE_ANCHOR_HCENTER verticallyAlignedTo:DRAWABLE_ANCHOR_VCENTER];
			
			//Reset time
			m_ShakeTime = m_TintTime > 0 ? 60 : 200;
		}
	}
	
	//Move
	//float Movement = SHIP_SPEED * (float)time / 1000;
	//if (RecordManager.instance().hasShownTutorial()) m_Distance += Movement;
	//m_Sky.scroll(0, Movement);
	
	//Update
	[self updateBulletsWithDeltaTime:time];
}

- (void)updateHolesWithDeltaTime:(long)time withTouch:(ARKTouchInfo*)touch {
	
}

- (void)updateBulletsWithDeltaTime:(long)time {
	
}

- (void)updateLabelsWithDeltaTime:(long)time {
	
}

- (void)changeHealth:(int)change {
	
}

- (void)changeScore:(int)score {
	
}

- (void)drawWithGL:(GLKBaseEffect *)gl {
	//Draw game
	[m_Sky drawWithGL:gl];
	[m_Ship drawWithGL:gl];
}

@end
