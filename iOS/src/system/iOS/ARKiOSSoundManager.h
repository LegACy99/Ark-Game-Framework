//
//  ARKiOSSoundManager.h
//  Ark Framework
//
//  Created by LegACy on 4/18/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKSoundManager.h"
#import <AVFoundation/AVFoundation.h>

@interface ARKiOSSoundManager : ARKSoundManager <AVAudioPlayerDelegate> {
	//Data
	NSString*				m_BGM;
	AVAudioPlayer*			m_BGMPlayer;
	NSMutableDictionary*	m_SFXPlayers;
	NSMutableDictionary*	m_SFXPlayings;
}

//Singleton
+ (ARKiOSSoundManager*)instance;

@end
