//
//  ARKiOSSoundManager.m
//  Ark Framework
//
//  Created by LegACy on 4/18/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKiOSSoundManager.h"

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
		m_BGMPlayer		= nil;
	}
	
	//Return
	return self;
}

+ (ARKSoundManager*)instance {
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
	//Stop all sound
	if (m_BGMPlayer) [m_BGMPlayer stop];
	
	//Kill
	m_BGMPlayer = nil;
}

- (void)resume {
	//Resume
	if (m_BGMPlayer) [m_BGMPlayer play];
}

- (void)pause {
	//Pause
	if (m_BGMPlayer) [m_BGMPlayer pause];
}

- (void)audioPlayerEndInterruption:(AVAudioPlayer *)player withOptions:(NSUInteger)flags {
	//Start player
	NSLog(@"Interruption ended");
	if (player) [player play];
}

- (void)loadBGMWithName:(NSString *)bgm {
	//Skip if equal
	if (!bgm)							return;
	if ([m_BGM isEqualToString:bgm])	return;
	
	//Get file
	NSString* Path	= [[NSBundle mainBundle] pathForResource:bgm ofType:@"mp3" inDirectory:@"audio/bgm"];
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

- (void)loadSFXWithName:(NSString *)sfx {
	//Skip if no sfx or already exist
	if (!sfx)								return;
	if ([m_SFXPlayers objectForKey:sfx])	return;
}

@end
