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
#import "FloatingLabel.h"
#import "Wormhole.h"
#import "Bullet.h"
#import <math.h>

//Constants
const int GAME_MAX_HEALTH 		= 80;
const int GAME_BUTTON_PAUSE		= 1;
const int GAME_BUTTON_ATTRACT 	= 2;
const int GAME_BUTTON_RETRACT 	= 3;
const long GAME_TINT_DURATION 	= 250;
const long GAME_INTRO_DURATION 	= 19000;
const float GAME_SHIP_SPEED		= 80;

@interface StateGame ()

//Private functions
- (void)updateMovingWithDeltaTime:(long)time;
- (void)updateHolesWithDeltaTime:(long)time withTouch:(ARKTouchInfo*)touch;
- (void)updateBulletsWithDeltaTime:(long)time;
- (void)updateLabelsWithDeltaTime:(long)time;
- (void)changeHealth:(int)change;
- (void)changeScore:(int)change;

@end

@implementation StateGame

- (id)init {
	//Super
	self = [super initWithID:STATE_GAME];
	if (self) {
		//Load stuff
		[[RecordManager instance] load];
		[[ARKResourceManager instance] addFontFromFile:[HoleUtilities FONT_SCORE]];
		[[ARKResourceManager instance] addFontFromFile:[HoleUtilities FONT_PIXEL]];
		[[ARKResourceManager instance] addFontFromFile:[HoleUtilities FONT_PIXEL_SMALL]];
		[[ARKResourceManager instance] addFontFromFile:[HoleUtilities FONT_CAPS_SMALL]];
		[[ARKResourceManager instance] addTextureFromFile:[HoleUtilities FONT_SCORE_TEXTURE] withAntiAlias:NO];
		[[ARKResourceManager instance] addTextureFromFile:[HoleUtilities FONT_PIXEL_TEXTURE] withAntiAlias:NO];
		[[ARKResourceManager instance] addTextureFromFile:[HoleUtilities FONT_PIXEL_SMALL_TEXTURE] withAntiAlias:NO];
		[[ARKResourceManager instance] addTextureFromFile:[HoleUtilities FONT_CAPS_SMALL_TEXTURE] withAntiAlias:NO];
		[[ARKResourceManager instance] addBGMFromFile:[UTILITIES_BGM_FOLDER stringByAppendingString:@"intro.mp3"]];
		[[ARKResourceManager instance] addSFXFromFile:[UTILITIES_SFX_FOLDER stringByAppendingString:@"warp.aiff"]];
		[[ARKResourceManager instance] addSFXFromFile:[UTILITIES_SFX_FOLDER stringByAppendingString:@"bonus.aiff"]];
		[[ARKResourceManager instance] addSFXFromFile:[UTILITIES_SFX_FOLDER stringByAppendingString:@"cancel.aiff"]];
		[[ARKResourceManager instance] addSFXFromFile:[UTILITIES_SFX_FOLDER stringByAppendingString:@"cursor.aiff"]];
		[[ARKResourceManager instance] addSFXFromFile:[UTILITIES_SFX_FOLDER stringByAppendingString:@"explode.aiff"]];
		[[ARKResourceManager instance] addTextureFromFile:[UTILITIES_TEXTURE_FOLDER stringByAppendingString:@"texture.png"] withAntiAlias:NO];
		[[ARKResourceManager instance] addImageFromFile:[UTIL_INTERFACE_IMAGES stringByAppendingString:@"button-attract.json"]];
		[[ARKResourceManager instance] addImageFromFile:[UTIL_INTERFACE_IMAGES stringByAppendingString:@"button-retract.json"]];
		[[ARKResourceManager instance] addImageFromFile:[UTIL_INTERFACE_IMAGES stringByAppendingString:@"button-pause.json"]];
		[[ARKResourceManager instance] addImageFromFile:[UTIL_INTERFACE_IMAGES stringByAppendingString:@"frame-health.json"]];
		[[ARKResourceManager instance] addImageFromFile:[UTIL_INTERFACE_IMAGES stringByAppendingString:@"button.json"]];
		[[ARKResourceManager instance] addImageFromFile:[UTIL_INTERFACE_IMAGES stringByAppendingString:@"target.json"]];
		[[ARKResourceManager instance] addImageFromFile:[UTIL_INTERFACE_IMAGES stringByAppendingString:@"title.json"]];
		[[ARKResourceManager instance] addImageFromFile:[UTIL_INTERFACE_IMAGES stringByAppendingString:@"panel.json"]];
		[[ARKResourceManager instance] addImageFromFile:[UTIL_GAME_IMAGES stringByAppendingString:@"blackhole.json"]];
		[[ARKResourceManager instance] addImageFromFile:[UTIL_GAME_IMAGES stringByAppendingString:@"whitehole.json"]];
		[[ARKResourceManager instance] addImageFromFile:[UTIL_GAME_IMAGES stringByAppendingString:@"bullet.json"]];
		[[ARKResourceManager instance] addImageFromFile:[UTIL_GAME_IMAGES stringByAppendingString:@"trail.json"]];
		[[ARKResourceManager instance] addImageFromFile:[UTIL_GAME_IMAGES stringByAppendingString:@"ship.json"]];
		[[ARKResourceManager instance] addImageFromFile:[UTIL_GAME_IMAGES stringByAppendingString:@"sky.json"]];
		
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
		m_Ship		= [ARKImage createFromPath:[UTIL_GAME_IMAGES stringByAppendingString:@"ship.json"]];
		m_Frame		= [ARKImage createFromPath:[UTIL_INTERFACE_IMAGES stringByAppendingString:@"frame-health.json"]];
		m_HealthBar	= [ARKRectangle createFromX:0 fromY:0 withWidth:[m_Frame getOriginalWidth] - 8 withHeight:[m_Frame getOriginalHeight] - 8 withRed:0.15 withGreen:0.15 withBlue:0.15 withAlpha:1];
		m_Sky		= [[ARKScrollingImage alloc] initWithResource:[UTIL_GAME_IMAGES stringByAppendingString:@"sky.json"]];
		
		//Create buttons
		m_Buttons			= [[ARKButtonContainer alloc] init];
		ARKButton* Pause	= [m_Buttons addButtonWithID:GAME_BUTTON_PAUSE withResource:[UTIL_INTERFACE_IMAGES stringByAppendingString:@"button-pause.json"] withText:nil];
		 
		//Positions
		float ScreenWidth	= [[ARKUtilities instance] getWidth];
		float ScreenHeight	= [[ARKUtilities instance] getHeight];
		[m_Ship setPositionAtX:ScreenWidth / 2 atY:ScreenHeight + 10 horizontallyAlignedTo:DRAWABLE_ANCHOR_HCENTER verticallyAlignedTo:DRAWABLE_ANCHOR_VCENTER];
		[Pause setPositionAtX:ScreenWidth - 8 atY:ScreenHeight - 8 horizontallyAlignedTo:DRAWABLE_ANCHOR_RIGHT verticallyAlignedTo:DRAWABLE_ANCHOR_BOTTOM];
		[m_HealthBar setPositionAtX:8 + 4 atY:8 + 4];
		[m_Frame setPositionAtX:8 atY:8];
				
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
	//Initialize
	m_Health 			= GAME_MAX_HEALTH;
	m_IntroTime			= GAME_INTRO_DURATION;
	m_WasPressed		= false;
	m_BulletDuration	= [[RecordManager instance] hasShownTutorial] ? 1000 : 2500;
	m_ShakeTime			= 100;
	m_Destroyed			= 0;
	m_Nearmiss			= 0;
	m_TintTime			= 0;
	m_Cooldown			= 0;
	m_Distance			= 0;
	m_Evaded			= 0;
	m_PressX			= 0;
	m_PressY			= 0;
	m_Score				= 0;
	m_Time				= 0;
	
	//If tutorial
	if (![[RecordManager instance] hasShownTutorial]) {
		//Create tutorial
		float ScreenWidth	= [[ARKUtilities instance] getWidth];
		float ScreenHeight	= [[ARKUtilities instance] getHeight];
		m_Target			= [ARKImage createFromPath:[UTIL_INTERFACE_IMAGES stringByAppendingString:@"target.json"]];
		m_Tutorial2			= [ARKLabel createWithText:@"Tap to" withFont:[HoleUtilities FONT_PIXEL_SMALL]];
		m_Tutorial3 		= [ARKLabel createWithText:@"create a black hole" withFont:[HoleUtilities FONT_PIXEL_SMALL]];
		m_Tutorial1 		= [ARKLabel createWithText:@"Deflect bullets to protect your spaceship!" withFont:[HoleUtilities FONT_PIXEL_SMALL]];
		[m_Target setPositionAtX:ScreenWidth / 4 atY:ScreenHeight * 0.75 horizontallyAlignedTo:DRAWABLE_ANCHOR_HCENTER verticallyAlignedTo:DRAWABLE_ANCHOR_VCENTER];
		[m_Tutorial1 setPositionAtX:ScreenWidth / 2 atY:ScreenHeight / 4 horizontallyAlignedTo:DRAWABLE_ANCHOR_HCENTER verticallyAlignedTo:DRAWABLE_ANCHOR_VCENTER];
		[m_Tutorial2 setPositionAtX:ScreenWidth / 4 atY:(ScreenHeight * 0.75) - 12 horizontallyAlignedTo:DRAWABLE_ANCHOR_HCENTER verticallyAlignedTo:DRAWABLE_ANCHOR_BOTTOM];
		[m_Tutorial3 setPositionAtX:ScreenWidth / 4 atY:(ScreenHeight * 0.75) + 12 horizontallyAlignedTo:DRAWABLE_ANCHOR_HCENTER verticallyAlignedTo:DRAWABLE_ANCHOR_TOP];
	}
	
	//Clear arrays
	[m_Holes removeAllObjects];
	[m_Bullets removeAllObjects];
	[m_Labels removeAllObjects];
	
	//Clear tint
	[m_Ship setTintWithRedF:1 withGreenF:1 withBlueF:1];
	
	//Refresh score
	[self changeScore:0];
	[self changeHealth:0];
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
			//Go to pause
			[[ARKStateManager instance] goToStateID:STATE_PAUSE withParameters:[NSArray arrayWithObject:self] swappedWithCurrent:NO];
		} else {
			//Update cooldown
			if (m_Cooldown > 0) m_Cooldown -= time;
			if (m_TintTime > 0) {
				m_TintTime -= time;
				if (m_TintTime <= 0) [m_Ship setTintWithRedF:1 withGreenF:1 withBlueF:1 withAlphaF:1];
			}
			
			//Update
			if (m_Tutorial2 && !m_Tutorial1) {
				//Manage timer
				if (m_TutorialTime > 0) {
					m_TutorialTime -= time;
					[self updateMovingWithDeltaTime:time];
				}
			} else [self updateMovingWithDeltaTime:time];
			if (!m_Tutorial1) [self updateHolesWithDeltaTime:time withTouch:[touches objectAtIndex:0]];
			[self updateLabelsWithDeltaTime:time];
			
			//Check button
			int ID = [m_Buttons updateWithKeys:keys withTouches:touches];
			if (ID == GAME_BUTTON_PAUSE) {
				//Go to pause
				[[ARKStateManager instance] goToStateID:STATE_PAUSE withParameters:[NSArray arrayWithObject:self] swappedWithCurrent:NO];
			}
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
	float Movement = GAME_SHIP_SPEED * (float)time / 1000.0;
	if ([[RecordManager instance] hasShownTutorial]) m_Distance += Movement;
	[m_Sky scrollByX:0 byY:Movement];
	
	//Update
	[self updateBulletsWithDeltaTime:time];
}

- (void)updateHolesWithDeltaTime:(long)time withTouch:(ARKTouchInfo*)touch {
	//If touch exist
	if (touch) {
		//If not pressed but was
		if (![touch isPressed] && m_WasPressed) {
			//If no cooldown
			if (m_Cooldown <= 0) {
				//Get location
				float X = (m_PressX / [[ARKUtilities instance] getScale]);
				float Y = (m_PressY / [[ARKUtilities instance] getScale]);
				
				//If tutorial
				BOOL Okay = YES;
				if (m_Tutorial2) {
					//Check press
					if (X > ([[ARKUtilities instance] getWidth] / 4) + 20) 			Okay = false;
					else if (X < ([[ARKUtilities instance] getWidth] / 4) - 32) 	Okay = false;
					else if (Y > ([[ARKUtilities instance] getHeight] * 0.75) + 32) Okay = false;
					else if (Y < ([[ARKUtilities instance] getHeight] * 0.75) - 20) Okay = false;
					
					//If still okay
					if (Okay) {
						//No tutorial
						m_Tutorial2 = nil;
						m_Tutorial3 = nil;
						m_Target	= nil;
					}
				}
				
				//If okay
				if (Okay) {
					//Create wormhole
					Wormhole* Hole = [[Wormhole alloc] initWithType:HOLE_BLACKHOLE atX:X atY:Y];
					[m_Holes addObject:Hole];
					
					//SFX
					[[ARKSoundManager instance] playSFXWithName:[UTILITIES_SFX_FOLDER stringByAppendingString:@"warp.aiff"]];
					
					//Reset cooldown
					m_Cooldown = 600;
				}
			}
			
			//No longer pressed
			m_WasPressed = NO;
		} else if ([touch isPressed]) {
			//Pressed
			m_WasPressed 	= YES;
			m_PressX		= [touch getCurrentX];
			m_PressY		= [touch getCurrentY];
		}
	}
	
	//Check each holes
	NSMutableArray* Deads = [NSMutableArray array];
	for (int i = 0; i < [m_Holes count]; i++) {
		//Update
		[[m_Holes objectAtIndex:i] updateWithDeltaTime:time];
		if (![[m_Holes objectAtIndex:i] isAlive]) [Deads addObject:[m_Holes objectAtIndex:i]];
	}
	
	//Kill holes
	for (int i = 0; i < [Deads count]; i++) [m_Holes removeObject:[Deads objectAtIndex:i]];
	[Deads removeAllObjects];
}

- (void)updateBulletsWithDeltaTime:(long)time {
	//Check bullet duration
	m_BulletDuration -= time;
	if (m_BulletDuration <= 0) {
		//Calculate region
		float Left 		= [[ARKUtilities instance] getWidth] * 0.3f;
		float Right 	= [[ARKUtilities instance] getWidth] * 0.7f;
		float Bottom	= [[ARKUtilities instance] getHeight] * 0.7f;
		float Top		= [[ARKUtilities instance] getHeight] * 0.3f;
		
		//Get target
		float TargetX = [[ARKUtilities instance] getARandomNumberBetween:(int)Left to:(int)Right + 1];
		float TargetY = [[ARKUtilities instance] getARandomNumberBetween:(int)Top to:(int)Bottom + 1];
		
		//Calculate angle
		float Angle = [[ARKUtilities instance] getARandomNumberBetween:0 to:200];
		if (Angle > 150)		Angle += 160;
		else if (Angle > 40) 	Angle += 80;
		
		//If tutorial
		if (m_Tutorial2) {
			//Set data
			TargetX = [[ARKUtilities instance] getWidth] / 2;
			TargetY = [[ARKUtilities instance] getHeight] / 2;
			Angle 	= 180;
			
			//No text
			m_Tutorial1 	= nil;
			m_TutorialTime	= 300;
		}
		
		//Calculate X
		float OriginX = 0;
		if (Angle >= 135 && Angle <= 225)		OriginX = 0;
		else if (Angle <= 45 || Angle >= 315)	OriginX = [[ARKUtilities instance] getWidth];
		else {
			//Calculate
			float Cos = cosf([[ARKUtilities instance] getRadianFromDegree:Angle]);
			OriginX = ([[ARKUtilities instance] getWidth] / 2) * (1 + Cos);
		}
		
		//Calculate Y
		float OriginY = 0;
		if (Angle >= 45 && Angle <= 135)		OriginY = 0;
		else if (Angle >= 225 && Angle <= 315) 	OriginY = [[ARKUtilities instance] getHeight];
		else {
			//Calculate
			float Sin 	= sinf([[ARKUtilities instance] getRadianFromDegree:Angle]);
			OriginY 	= ([[ARKUtilities instance] getHeight] / 2) * (1 - Sin);
		}
		
		//Calculate speed
		float DistanceX = TargetX - OriginX;
		float DistanceY = TargetY - OriginY;
		float Distance	= hypotf(DistanceX, DistanceY);
		float SpeedX 	= 150 * DistanceX / Distance;
		float SpeedY 	= 150 * DistanceY / Distance;
		
		//Create bullet
		Bullet* NewBullet = [[Bullet alloc] initAtX:OriginX atY:OriginY withXVelocity:SpeedX withYVelocity:SpeedY];
		[m_Bullets addObject:NewBullet];
		
		//Restart
		int Min 			= m_Distance < 4000 ? 5 - ((int)m_Distance / 1000) : 1;
		int Max 			= Min + (m_Distance < 6000 ? 7 - ((int)m_Distance / 1500) : 3);
		m_BulletDuration 	= [[ARKUtilities instance] getARandomNumberBetween:Min to:Max + 1] * 300;
	}
	
	//Get ship size
	float WidthFraction = [m_Ship getWidth] * 0.1f;
	
	//Check each bullets
	NSMutableArray* Deads = [NSMutableArray array];
	for (int i = 0; i < [m_Bullets count]; i++) {
		//Update
		[[m_Bullets objectAtIndex:i] updateForceByWormholes:m_Holes];
		[[m_Bullets objectAtIndex:i] updateWithDeltaTime:time];
		if ([[m_Bullets objectAtIndex:i] isDead]) {
			//Kill
			[Deads addObject:[m_Bullets objectAtIndex:i]];
			[m_Labels addObject:[[FloatingLabel alloc] initWithText1:@"Evaded"
														   withText2:@"+20"
														withDuration:2000
																 atX:[[m_Bullets objectAtIndex:i] getLabelX]
																 atY:[[m_Bullets objectAtIndex:i] getLabelY]
								 ]];
			
			//Add score
			[self changeScore:20];
			m_Evaded++;
			
			//SFX
			[[ARKSoundManager instance] playSFXWithName:[UTILITIES_SFX_FOLDER stringByAppendingString:@"bonus.aiff"]];
		} else {
			//Check if collide
			if ([[m_Bullets objectAtIndex:i] doesCollideWithRectFromX:[m_Ship getX] + WidthFraction fromY:[m_Ship getY] withWidth:WidthFraction * 8 withHeight:[m_Ship getHeight]]) {
				//Damage
				[self changeHealth:-10];
				[Deads addObject:[m_Bullets objectAtIndex:i]];
				
				//SFX
				[[ARKSoundManager instance] playSFXWithName:[UTILITIES_SFX_FOLDER stringByAppendingString:@"explode.aiff"]];
			} else {
				//Check if near miss
				if ([[m_Bullets objectAtIndex:i]isNearMiss]) {
					//Show label
					[m_Labels addObject:[[FloatingLabel alloc] initWithText1:@"Near Miss"
																   withText2:@"+30"
																withDuration:1500
																		 atX:[[m_Bullets objectAtIndex:i] getNearMissX]
																		 atY:[[m_Bullets objectAtIndex:i] getNearMissY]
										 ]];
					
					//Add score
					[self changeScore:30];
					m_Nearmiss++;
					
					//SFX
					[[ARKSoundManager instance] playSFXWithName:[UTILITIES_SFX_FOLDER stringByAppendingString:@"bonus.aiff"]];
				}
			}
		}
	}
	
	//Kill bullets
	for (int i = 0; i < [Deads count]; i++) [m_Bullets removeObject:[Deads objectAtIndex:i]];
	[Deads removeAllObjects];
}

- (void)updateLabelsWithDeltaTime:(long)time {
	//Check each labels
	NSMutableArray* Deads = [NSMutableArray array];
	for (int i = 0; i < [m_Labels count]; i++) {
		//Update
		[[m_Labels objectAtIndex:i] updateWithDeltaTime:time];
		if ([[m_Labels objectAtIndex:i] isDead]) [Deads addObject:[m_Labels objectAtIndex:i]];
	}
	
	//Kill labels
	for (int i = 0; i < [Deads count]; i++) [m_Labels removeObject:[Deads objectAtIndex:i]];
	[Deads removeAllObjects];
}

- (void)changeHealth:(int)change {
	//Change
	m_Health += change;
	if (m_Health < 0)			m_Health = 0;
	if (m_Health > GAME_MAX_HEALTH) 	m_Health = GAME_MAX_HEALTH;
	
	//Recreate health bar
	float Ratio = (float)m_Health / (float)GAME_MAX_HEALTH;
	[m_HealthBar setRegionFromX:0 fromY:0 withWidth:Ratio * [m_HealthBar getOriginalWidth] withHeight:[m_HealthBar getOriginalHeight]];
	
	//If dead
	if (m_Health == 0) {
		//Go to gameover
		NSArray* Parameters = [NSArray arrayWithObjects:self,
							   [NSNumber numberWithInt:[[m_ScoreCounter getText] integerValue]],
							   [NSNumber numberWithInteger:m_Evaded],
							   [NSNumber numberWithInteger:m_Destroyed],
							   [NSNumber numberWithInteger:m_Nearmiss],
							   [NSNumber numberWithInteger:m_Time / 1000],
							   nil];
		[[ARKStateManager instance] goToStateID:STATE_RESULT withParameters:Parameters swappedWithCurrent:NO];
	} else if (change < 0) {
		//Damage
		[m_Ship setTintWithRedF:0.9f withGreenF:0 withBlueF:0];
		m_TintTime = GAME_TINT_DURATION;
	}
}

- (void)changeScore:(int)change {
	//Add score
	m_Score += change;
	
	//Calculate and update score
	m_ScoreCounter = [ARKLabel createWithText:[NSString stringWithFormat:@"%d", m_Score] withFont:[HoleUtilities FONT_SCORE]];
	[m_ScoreCounter setPositionAtX:[[ARKUtilities instance] getWidth] - 8 atY:2 horizontallyAlignedTo:DRAWABLE_ANCHOR_RIGHT];
}

- (void)drawWithGL:(GLKBaseEffect *)gl {
	//Draw game
	[m_Sky drawWithGL:gl];
	[m_Ship drawWithGL:gl];
	
	//Draw wormholes
	for (int i = 0; i < [m_Holes count]; i++) 	[[m_Holes objectAtIndex:i] drawWithGL:gl];
	for (int i = 0; i < [m_Bullets count]; i++) [[m_Bullets objectAtIndex:i] drawWithGL:gl];
	for (int i = 0; i < [m_Labels count]; i++) 	[[m_Labels objectAtIndex:i] drawWithGL:gl];
	
	//If started
	if (m_GameStarted) {
		//Check tutorial
		if (m_Tutorial1) [m_Tutorial1 drawWithGL:gl];
		else if (m_Tutorial2) {
			//Draw
			[m_Tutorial2 drawWithGL:gl];
			[m_Tutorial3 drawWithGL:gl];
			[m_Target drawWithGL:gl];
		}
		
		//Draw interface
		[m_ScoreCounter drawWithGL:gl];
		[m_Frame drawWithGL:gl];
		[m_HealthBar drawWithGL:gl];
		[m_Buttons drawWithGL:gl];
	}
}

@end
