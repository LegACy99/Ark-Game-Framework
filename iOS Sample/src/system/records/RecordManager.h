//
//  RecordManager.h
//  ARK Framework Example
//
//  Created by LegACy on 4/28/13.
//  Copyright (c) 2013 Raka Mahesa. All rights reserved.
//

#import <Foundation/Foundation.h>

//Forward declaration
@class ARKRecordWriter;

@interface RecordManager : NSObject {
	//Data
	int		m_Version;
	int		m_Highscore;
	BOOL	m_Tutorial;
	
	//Writer
	ARKRecordWriter* m_Writer;
}

//Properties
@property (readonly, getter = getVersion)		int version;
@property (readonly, getter = getHighscore)		int highscore;
@property (readonly, getter = hasShownTutorial)	BOOL tutorial;

//Singleton
+ (RecordManager*)instance;

//Save load
- (void)save;
- (void)load;

//Setters
- (void)showTutorial;
- (void)setHighscore:(int)highscore;

@end
