//
//  ARKSoundManager.m
//  Ark Framework
//
//  Created by LegACy on 4/18/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKSoundManager.h"
#import "ARKiOSSoundManager.h"

//Constants
const BOOL SOUND_INITIAL_MUTE	= NO;
const float SOUND_MAX_VOLUME	= 100;

@implementation ARKSoundManager

//Synthesize
@synthesize mute			= m_Mute;
@synthesize volume			= m_Volume;
@synthesize initialVolume	= m_InitVolume;

- (id)init {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		m_Volume		= 0;
		m_InitVolume	= 0;
		m_Mute			= SOUND_INITIAL_MUTE;
	}
	
	//Return
	return self;
}

+ (ARKSoundManager*)instance {
	//Return the correct manager
	return [ARKiOSSoundManager instance];
}

- (void)setMute:(BOOL)mute {
	//Set mute
	m_Mute = mute;
}

- (void)setVolume:(float)volume {
	//Save volume
	m_Volume = volume;
	if (m_Volume < 0)					m_Volume = 0;
	if (m_Volume > SOUND_MAX_VOLUME)	m_Volume = SOUND_MAX_VOLUME;
}

//Abstract functions
- (void)pause														{ [self doesNotRecognizeSelector:_cmd];			}
- (void)resume														{ [self doesNotRecognizeSelector:_cmd];			}
- (void)destroy														{ [self doesNotRecognizeSelector:_cmd];			}
- (void)playBGM														{ [self doesNotRecognizeSelector:_cmd];			}
- (void)loadBGMWithName:(NSString*)bgm								{ [self doesNotRecognizeSelector:_cmd];			}
- (void)playSFXWithName:(NSString*)sfx								{ [self playSFXWithName:sfx withLooping:NO];	}
- (void)playSFXWithName:(NSString*)sfx withLooping:(BOOL)looping	{ [self doesNotRecognizeSelector:_cmd];			}
- (void)stopSFXWithName:(NSString*)sfx;								{ [self doesNotRecognizeSelector:_cmd];			}
- (void)loadSFXWithName:(NSString*)sfx;								{ [self doesNotRecognizeSelector:_cmd];			}

@end
