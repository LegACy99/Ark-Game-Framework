//
//  ARKItemList.m
//  ARK Framework
//
//  Created by LegACy on 10/16/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKItemList.h"
#import "ARKUtilities.h"
#import "ARKTouchInfo.h"
#import <math.h>

//Constants
const float LIST_SCROLL_OFFSET = 20;

@interface ARKItemList ()

//Private functions
- (BOOL)isInsideAtX:(float)x atY:(float)y;

@end

@implementation ARKItemList

//Synthesize
@synthesize pressed = m_Pressed;

//Constructors
- (id)init { return nil; }
- (id)initWithWindow:(float)window								{ return [self initWithWindow:window withGap:0];				}
- (id)initWithWindow:(float)window withGap:(float)gap			{ return [self initWithWindow:window atX:0 atY:0 withGap:gap];	}
- (id)initWithWindow:(float)window atX:(float)x atY:(float)y	{ return [self initWithWindow:window atX:x atY:y withGap:0]; 	}
- (id)initWithWindow:(float)window atX:(float)x atY:(float)y withGap:(float)gap {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		m_Speed			= 0;
		m_Total			= 0;
		m_Scroll		= 0;
		m_Slowing		= 0;
		m_DrawnSize		= 0;
		m_DrawnFirst	= 0;
		m_Scrolling		= NO;
		m_Pressed		= NO;
		m_Grow			= NO;
		m_Items			= [NSArray array];
		
		//Save data
		m_Gap		= gap;
		m_Window 	= window;
		if (m_Window <= 0) {
			//Growing
			m_Grow	= YES;
			m_Window = 0;
		}
		
		//Get height
		m_Height			= m_Window * [[ARKUtilities instance] getScale];
		m_OriginalHeight	= m_Window;
		
		//Set position
		[self setPositionAtX:x atY:y];
		[self setRegionFromX:0 fromY:0 withWidth:m_OriginalWidth withHeight:m_OriginalHeight];
	}
	
	//Return
	return self;
}

- (ARKCroppable*)addItem:(ARKCroppable*)item {
	//Skip if null
	if (!item) return nil;
	
	//Add
	NSMutableArray* Items = [NSMutableArray arrayWithArray:m_Items];
	[Items addObject:item];
	m_Items	= Items;
	
	//Set position
	[item setPositionAtX:m_X / [[ARKUtilities instance] getScale] atY:(m_Y / [[ARKUtilities instance] getScale]) + m_Total + ([m_Items count] > 1 ? m_Gap : 0)];
	
	//Calculate vertical size
	float Old	 = m_Total;
	m_Total 	+= [item getOriginalHeight] + ([m_Items count] > 1 ? m_Gap : 0);
	
	//If taller than window
	if (m_Total > m_Window) {
		//If don't grow, crop
		if (!m_Grow) [item setRegionFromX:0 fromY:0 withWidth:[item getOriginalWidth] withHeight:m_Window - Old - ([m_Items count] > 1 ? m_Gap : 0)];
		else {
			//Calculate new size
			m_Window 			= m_Total;
			m_OriginalHeight	= m_Window;
			m_Height			= m_Window * [[ARKUtilities instance] getScale];
			
			//Set region
			[self setRegionFromX:m_OriginalRegionX fromY:m_OriginalRegionY withWidth:m_OriginalRegionWidth withHeight:m_OriginalHeight];
		}
	}
	
	//if width is more than current
	if ([item getOriginalWidth] > m_OriginalWidth) {
		//Save width
		m_Width			= [item getWidth];
		m_OriginalWidth = [item getOriginalWidth];
		
		//Set region
		[self setRegionFromX:m_OriginalRegionX fromY:m_OriginalRegionY withWidth:m_OriginalWidth withHeight:m_OriginalRegionHeight];
	}
	
	//Update items
	[self adjustItems];
	[self updateItems];
	
	//Return the item
	return item;
}

//Accessors
- (int)getItemSize { return [m_Items count]; }
- (ARKCroppable*)getItemAtPosition:(int)position {
	//Get item
	ARKCroppable* Result = nil;
	if (position >= 0 && position < [m_Items count]) Result = [m_Items objectAtIndex:position];
	
	//Return
	return Result;
}

- (BOOL)isInsideAtX:(float)x atY:(float)y {
	//Check for false
	if (x < m_X)			return NO;
	if (y < m_Y)			return NO;
	if (x > m_X + m_Width)	return NO;
	if (y > m_Y + m_Height)	return NO;
	
	//If passed, inside
	return YES;
}

- (void)setPositionAtX:(float)x atY:(float)y horizontallyAlignedTo:(int)horizontal verticallyAlignedTo:(int)vertical {
	//Super
	[super setPositionAtX:x atY:y horizontallyAlignedTo:horizontal verticallyAlignedTo:vertical];
	
	//Refresh
	[self updateItems];
}

- (void)setRegionFromX:(float)x fromY:(float)y withWidth:(float)width withHeight:(float)height {
	//Super
	[super setRegionFromX:x fromY:y withWidth:width withHeight:height];
	
	//Refresh
	[self adjustItems];
	[self updateItems];
}

- (void)updateWithKeys:(NSArray *)keys withTouches:(NSArray *)touches withDelta:(long)time {
	//If touched
	if ([[touches firstObject] isPressed]) {
		//If was pressed
		if (m_Pressed) {
			//If scrolling
			if (m_Scrolling) {
				//Get offset
				float Offset 	= [[touches firstObject] getOffsetY] / [[ARKUtilities instance] getScale];
				m_Speed			= time <= 0 ? 0 : Offset / (float)time * 1000.0f;
				m_Slowing		= -m_Speed;
				
				//Scroll
				m_Scroll -= Offset;
			} else {
				//If difference is more than offset
				if (fabsf([[touches firstObject] getCurrentY] - [[touches firstObject] getStartY]) > LIST_SCROLL_OFFSET) {
					//Start scrolling
					m_Scrolling = YES;
					[[touches firstObject] getOffsetY];
				}
			}
		} else {
			//If inside
			if ([self isInsideAtX:[[touches firstObject] getStartX] atY:[[touches firstObject] getStartY]]) {
				//Pressed
				m_Pressed = YES;
			}
		}
	} else {
		//Not pressed anymore
		m_Pressed = NO;
		
		//If scrolling
		if (m_Scrolling) {
			//If there's speed
			if (m_Speed != 0) {
				//Save sign
				BOOL Minus = m_Speed < 0;
				m_Speed += m_Slowing * (float)time / 1000.0f;
				
				//If sign is different, done
				if (m_Speed < 0 != Minus) m_Speed = 0;
			}
			
			//Scroll
			m_Scroll -= m_Speed * (float)time / 1000.0f;
			
			//If died
			if (m_Speed == 0) {
				//No more scroll
				m_Speed		= 0;
				m_Slowing	= 0;
				m_Scrolling = NO;
			}
		}
	}
	
	//If scrolling
	if (m_Scrolling) {
		//Correct scroll
		if (m_Scroll + m_Window > m_Total) 	m_Scroll = m_Total - m_Window;
		if (m_Scroll < 0) 					m_Scroll = 0;
		
		//Update items
		[self adjustItems];
		[self updateItems];
	}
}

- (void)adjustItems {
	//Initialize
	float Y			= 0;
	m_DrawnFirst	= 0;
	m_DrawnSize 	= 0;
	
	//For all item
	for (int i = 0; i < [m_Items count]; i++) {
		//Get height
		float Height = [[m_Items objectAtIndex:i] getOriginalHeight];
		
		//If not started yet
		if (m_DrawnSize <= 0) {
			//Check if inside
			if (Y + Height > m_Scroll + m_OriginalRegionY) {
				//Start
				m_DrawnFirst 	= i;
				m_DrawnSize		= 1;
			}
		} else {
			//Add if inside
			float Bottom = m_Scroll + fminf(m_Window, m_OriginalRegionY + m_OriginalRegionHeight);
			if (Y < Bottom) m_DrawnSize++;
		}
		
		//Next
		Y += Height + m_Gap;
	}
}

- (void)updateItems {
	//Calculate
	float X	= m_X / [[ARKUtilities instance] getScale];
	float Y = (m_Y / [[ARKUtilities instance] getScale]) - m_Scroll;
	
	//For all item
	for (int i = 0; i < [m_Items count]; i++) {
		//Get item
		ARKCroppable* Item = [m_Items objectAtIndex:i];
		
		//If drawn
		if (i >= m_DrawnFirst && i < m_DrawnFirst + m_DrawnSize) {
			//If not first or last
			if (i > m_DrawnFirst && i < m_DrawnFirst + m_DrawnSize - 1) {
				//Ensure that it's drawn fully
				if ([Item getOriginalRegionHeight] < [Item getOriginalHeight] || [Item getOriginalRegionY] > 0) {
					[Item setRegionFromX:m_OriginalRegionX fromY:0 withWidth:m_OriginalRegionWidth withHeight:[Item getOriginalHeight]];
				}
			} else {
				//Calculate
				float RegionY 		= m_Y + m_RegionY - (Y * [[ARKUtilities instance] getScale]);
				float RegionHeight 	= m_Y + m_RegionY + ((fminf(m_Window, m_OriginalRegionHeight) - Y) * [[ARKUtilities instance] getScale]);
				if (RegionY > 0) RegionHeight -= RegionY;
				
				//Crop if needed
				//if (RegionY >= 0 || RegionHeight <= Item.getHeight() || RegionHeight != Item.getOriginalRegionHeight())
				
				//Crop
				[Item setRegionFromX:m_OriginalRegionX fromY:RegionY / [[ARKUtilities instance] getScale] withWidth:m_OriginalRegionWidth withHeight:RegionHeight / [[ARKUtilities instance] getScale]];
			}
			
			//Set position
			[Item setPositionAtX:X atY:Y];
		}
		
		//Next Y
		Y += [Item getOriginalHeight] + m_Gap;
	}
}

- (void)calculateSize { [self calculateSizeWithChangeAt:-1]; }
- (void)calculateSizeWithChangeAt:(int)changed {
	//Save old
	float NewScroll = -1;
	
	//For all items
	m_Total = 0;
	for (int i = 0; i < [m_Items count]; i++) {
		//Check if changed after or before
		if (i == changed && m_Total < m_Scroll) NewScroll = m_Total;
		
		//Add height
		m_Total += [[m_Items objectAtIndex:i] getOriginalHeight];
		if (i < [m_Items count] - 1) m_Total += m_Gap;
	}
	
	//If growing
	if (m_Grow) {
		//Save as window
		m_Window 			= m_Total;
		m_OriginalHeight	= m_Window;
		m_Height			= [[ARKUtilities instance] getScale];
	}
	
	//Correct scrolling
	if (NewScroll >= 0) 				m_Scroll = NewScroll;
	if (m_Scroll + m_Window > m_Total) 	m_Scroll = m_Total - m_Window;
	if (m_Scroll < 0) 					m_Scroll = 0;
	
	//Not scrolling
	m_Scrolling = NO;
}

- (void)drawWithGL:(GLKBaseEffect *)gl {
	//Draw
	for (int i = m_DrawnFirst; i < m_DrawnFirst + m_DrawnSize; i++) [[m_Items objectAtIndex:i] drawWithGL:gl];
}

@end
