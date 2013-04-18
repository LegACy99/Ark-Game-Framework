//
//  ARKSoundManager.h
//  Ark Framework
//
//  Created by LegACy on 4/18/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ARKSoundManager : NSObject {
	//Data
	BOOL 	m_Mute;
	float	m_Volume;
	float	m_InitVolume;
}

//Properties
@property (readonly, getter = isMute)			BOOL mute;
@property (readonly, getter = getVolume)		float volume;
@property (readonly, getter = getInitialVolume) float initialVolume;

//Singleton function
+ (ARKSoundManager*)instance;

//Setters
- (void)setMute:(BOOL)mute;
- (void)setVolume:(float)volume;

//Abstract manager functions
- (void)pause;
- (void)resume;
- (void)destroy;

//Abstract audio functions
- (void)playBGM;
- (void)loadBGMWithName:(NSString*)bgm;
- (void)playSFXWithName:(NSString*)sfx;
- (void)playSFXWithName:(NSString*)sfx withLooping:(BOOL)looping;
- (void)stopSFXWithName:(NSString*)sfx;
- (void)loadSFXWithName:(NSString*)sfx;

@end

//Constants
extern const float SOUND_MAX_VOLUME;
