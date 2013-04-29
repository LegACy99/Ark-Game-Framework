//
//  ARKiOSSoundManager.m
//  Ark Framework
//
//  Created by LegACy on 4/18/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKiOSSoundManager.h"
#import "ARKiOSUtilities.h"

//Constant
const int SOUND_INITIAL_SFX = 3;

@interface ARKiOSSoundManager ()

//Private function
- (AVAudioPlayer*)createPlayerForSFX:(NSString*)sfx;

@end

@implementation ARKiOSSoundManager

- (id)init {
	//Super
	self = [super init];
	if (self) {
		//Set constants
		m_InitVolume = 60;
		
		//Initialize
		m_BGM			= @"";
		m_Volume		= [self getInitialVolume];
		m_SFXPlayers	= [NSMutableDictionary dictionary];
		m_SFXPlayings	= [NSMutableDictionary dictionary];
		m_BGMPlayer		= nil;
	}
	
	//Return
	return self;
}

+ (ARKiOSSoundManager*)instance {
	//Static objects
	static dispatch_once_t Token		= 0;
	static ARKiOSSoundManager* Instance	= nil;
	
	//Only create object once
	dispatch_once(&Token, ^{
		Instance = [[self alloc] init];
	});
	
	//Return
	return Instance;
}

- (void)destroy {
	//Stop BGM
	if (m_BGMPlayer) [m_BGMPlayer stop];
	
	//For all keys
	NSArray* Keys = [m_SFXPlayers allKeys];
	for (int i = 0; i < [Keys count]; i++) {
		//Get array
		id Players = [m_SFXPlayers objectForKey:[Keys objectAtIndex:i]];
		if (Players && [Players isKindOfClass:[NSMutableArray class]]) {
			//Stop all
			for (int j = 0; j < [Players count]; j++) [[Players objectAtIndex:j] stop];
		}
	}
	
	//Kill
	[m_SFXPlayers removeAllObjects];
	[m_SFXPlayings removeAllObjects];
	m_BGMPlayer = nil;
}

- (void)resume {
	//Resume
	if (m_BGMPlayer) [m_BGMPlayer play];
	
	//For all keys
	//Do nothing for SFX
	/*NSArray* Keys = [m_SFXPlayers allKeys];
	for (int i = 0; i < [Keys count]; i++) {
		//Get array
		id Players = [m_SFXPlayers objectForKey:[Keys objectAtIndex:i]];
		if (Players && [Players isKindOfClass:[NSMutableArray class]]) {
			//For each player
			id Playings = [m_SFXPlayings objectForKey:[Keys objectAtIndex:i]];
			for (int j = 0; j < [Players count]; j++) {
				//Check status
				BOOL Resume = NO;
				if (Playings && [Playings isKindOfClass:[NSMutableArray class]]) Resume = [[Playings objectAtIndex:j] boolValue];
				
				//Resume
				if (Resume) [(AVAudioPlayer*)[Players objectAtIndex:j] play];
			}
		}
	}*/
}

- (void)pause {
	//Pause
	if (m_BGMPlayer) [m_BGMPlayer pause];
	
	//For all keys
	NSArray* Keys = [m_SFXPlayers allKeys];
	for (int i = 0; i < [Keys count]; i++) {
		//Get array
		id Players = [m_SFXPlayers objectForKey:[Keys objectAtIndex:i]];
		if (Players && [Players isKindOfClass:[NSMutableArray class]]) {
			//Pause all
			for (int j = 0; j < [Players count]; j++) [[Players objectAtIndex:j] pause];
		}
	}
}

- (void)audioPlayerEndInterruption:(AVAudioPlayer *)player withOptions:(NSUInteger)flags {
	//Start player
	if (player) [player play];
}

- (void)loadBGMWithName:(NSString *)bgm {
	//Skip if equal
	if (!bgm)							return;
	if ([m_BGM isEqualToString:bgm])	return;
	
	//Get file
	NSString* Path = [[ARKiOSUtilities instance] getResourcePath:bgm];
	if (Path) {
		//Save
		m_BGM = bgm;
		
		//If exist
		if (m_BGMPlayer) {
			//Destroy
			[m_BGMPlayer stop];
			m_BGMPlayer = nil;
		}
		
		//Create player
		NSURL* FileURL	= [[NSURL alloc] initFileURLWithPath:Path];
		m_BGMPlayer		= [[AVAudioPlayer alloc] initWithContentsOfURL:FileURL error:nil];
		if (m_BGMPlayer) {
			//Configure
			[m_BGMPlayer setVolume:m_Volume / SOUND_MAX_VOLUME];
			[m_BGMPlayer setNumberOfLoops:-1];
			[m_BGMPlayer setDelegate:self];
			[m_BGMPlayer prepareToPlay];
		}
	}
}

- (void)playBGM {
	//Play!
	if (m_BGM && m_BGMPlayer && ![m_BGMPlayer isPlaying]) [m_BGMPlayer play];
}

- (AVAudioPlayer*)createPlayerForSFX:(NSString *)sfx {
	//Initialize
	AVAudioPlayer* Player = nil;
	
	//Get file
	NSString* Path = [[ARKiOSUtilities instance] getResourcePath:sfx];
	if (Path) {
		//Create player
		NSURL* FileURL	= [[NSURL alloc] initFileURLWithPath:Path];
		Player			= [[AVAudioPlayer alloc] initWithContentsOfURL:FileURL error:nil];
		if (Player) {
			//Configure
			[Player setVolume:m_Volume / SOUND_MAX_VOLUME];
			[Player prepareToPlay];
		}
	}
	
	//Return
	return Player;
}

- (void)loadSFXWithName:(NSString *)sfx {
	//Skip if no sfx or already exist
	if (!sfx)								return;
	if ([m_SFXPlayers objectForKey:sfx])	return;
	
	//Create array
	NSMutableArray* Players = [NSMutableArray array];
	for (int i = 0; i < SOUND_INITIAL_SFX; i++) {
		//Create player
		AVAudioPlayer* Player = [self createPlayerForSFX:sfx];
		if (Player) [Players addObject:Player];
	}
	
	//Save array if player is valid
	if ([Players count] > 0) [m_SFXPlayers setObject:Players forKey:sfx];
}

- (void)playSFXWithName:(NSString *)sfx withLooping:(BOOL)looping {
	//Skip if no sfx
	if (!sfx) return;
	
	//Get player
	id Players = [m_SFXPlayers objectForKey:sfx];
	if (Players && [Players isKindOfClass:[NSMutableArray class]]) {
		//Find player
		int Index				= -1;
		AVAudioPlayer* Player	= nil;
		for (int i = 0; i < [Players count] && !Player; i++) {
			//If not playing
			if (![[Players objectAtIndex:i] isPlaying]) {
				//Save
				Index	= i;
				Player	= [Players objectAtIndex:i];
			}
		}
		
		//If last
		if (Index == [Players count] - 1) {
			//Add
			AVAudioPlayer* NewPlayer = [self createPlayerForSFX:sfx];
			if (NewPlayer) [Players addObject:NewPlayer];
		}
		
		//If no player
		if (!Player) {
			//Create
			Player = [self createPlayerForSFX:sfx];
			if (Player) [Players addObject:Player];
		}
		
		//If player exist
		if (Player) {
			//Configure
			[Player setCurrentTime:0];
			if (looping) [Player setNumberOfLoops: -1];
			
			//Play
			[Player play];
		}
	}
}

- (void)stopSFXWithName:(NSString *)sfx {
	//Skip if no sfx
	if (!sfx) return;
	
	//Get player
	id Players = [m_SFXPlayers objectForKey:sfx];
	if (Players && [Players isKindOfClass:[NSMutableArray class]]) {
		//Find the looping one
		NSMutableArray* LoopingPlayers = [NSMutableArray array];
		for (int i = 0; i < [Players count]; i++) {
			//If looping
			if ([[Players objectAtIndex:i] isPlaying] && [[Players objectAtIndex:i] numberOfLoops] < 0) [LoopingPlayers addObject:[Players objectAtIndex:i]];
		}
		
		//If no looping, stop all
		if ([LoopingPlayers count] <= 0) {
			for (int i = 0; i < [Players count]; i++) {
				//If playing
				if ([[Players objectAtIndex:i] isPlaying]) {
					//Pause
					[[Players objectAtIndex:i] pause];
					[[Players objectAtIndex:i] setCurrentTime:0];
				}
			}
		} else {
			//For all of them
			for (int i = 0; i < [LoopingPlayers count]; i++) {
				//Stop
				[[LoopingPlayers objectAtIndex:i] pause];
				[[LoopingPlayers objectAtIndex:i] setCurrentTime:0];
			}
		}
	}
}

@end
