//
//  ARKItemList.h
//  ARK Framework
//
//  Created by LegACy on 10/16/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKCroppable.h"

@interface ARKItemList : ARKCroppable {
	//Data
	float	m_Speed;
	float	m_Slowing;
	BOOL	m_Pressed;
	BOOL	m_Scrolling;
	BOOL	m_Grow;
	
	//Items
	float		m_Gap;
	float		m_Total;
	float		m_Window;
	float		m_Scroll;
	int			m_DrawnFirst;
	int			m_DrawnSize;
	NSArray*	m_Items;
}

//Property
@property (readonly, getter = isPressed) BOOL pressed;

//Getters
- (int)getItemSize;
- (ARKCroppable*)getItemAtPosition:(int)position;

//Constructors
- (id)initWithWindow:(float)window;
- (id)initWithWindow:(float)window withGap:(float)gap;
- (id)initWithWindow:(float)window atX:(float)x atY:(float)y;
- (id)initWithWindow:(float)window atX:(float)x atY:(float)y withGap:(float)gap;

//Functions
- (void)adjustItems;
- (void)updateItems;
- (void)calculateSize;
- (void)calculateSizeWithChangeAt:(int)changed;
- (void)updateWithKeys:(NSArray *)keys withTouches:(NSArray *)touches withDelta:(long)time;
- (ARKCroppable*)addItem:(ARKCroppable*)item;

@end
