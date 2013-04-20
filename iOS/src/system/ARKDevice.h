//
//  ARKDevice.h
//  Ark Framework
//
//  Created by LegACy on 4/20/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import <Foundation/Foundation.h>

@class ARKAccelerometerInfo;

@interface ARKDevice : NSObject {
	//Display data
	float	m_Scale;
	float	m_Width;
	float	m_Height;
	int		m_Column;
	int		m_Row;
	
	//Constants
	int m_Back;
	int m_Menu;
	int m_Fire;
	int m_Right;
	int m_left;
	int m_Down;
	int m_Up;
	
	//Input data
	NSMutableArray*			m_Keys;
	NSMutableArray*			m_Touches;
	ARKAccelerometerInfo*	m_Accelerometer;
}

//Properties
@property (readonly, getter = getRow)			int row;
@property (readonly, getter = getColumn)		int column;
@property (readonly, getter = getHeight)		float height;
@property (readonly, getter = getWidth)			float width;
@property (readonly, getter = getScale)			float scale;
@property (readonly, getter = getBackButton)	int back;
@property (readonly, getter = getMenuButton)	int menu;
@property (readonly, getter = getFireButton)	int fire;
@property (readonly, getter = getRightButton)	int right;
@property (readonly, getter = getLeftButton)	int left;
@property (readonly, getter = getDownButton)	int down;
@property (readonly, getter = getUpButton)		int up;
@property (readonly, getter = getKeys)			NSArray* keys;
@property (readonly, getter = getTouches)		NSArray* touches;
@property (readonly, getter = getAccelerometer)	ARKAccelerometerInfo* accelerometer;

//Factory method
+ (ARKDevice*)instance;

//abstract functions
- (void)openURL:(NSString *)url inBrowser:(BOOL)browser withTitle:(NSString *)title withLoading:(NSString*)loading;
- (void)openAppPage:(NSString*)app;

@end
