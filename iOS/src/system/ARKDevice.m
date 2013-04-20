//
//  ARKDevice.m
//  Ark Framework
//
//  Created by LegACy on 4/20/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKDevice.h"
#import "ARKiOSDevice.h"

@implementation ARKDevice

//Synthesize
@synthesize up				= m_Up;
@synthesize row				= m_Row;
@synthesize keys			= m_Keys;
@synthesize back			= m_Back;
@synthesize menu			= m_Menu;
@synthesize fire			= m_Fire;
@synthesize down			= m_Down;
@synthesize left			= m_Left;
@synthesize right			= m_Right;
@synthesize scale			= m_Scale;
@synthesize width			= m_Width;
@synthesize column			= m_Column;
@synthesize height			= m_Height;
@synthesize touches			= m_Touches;
@synthesize accelerometer	= m_Accelerometer;

- (id)init {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		m_Up		= -1;
		m_Down		= -1;
		m_Left		= -1;
		m_Fire		= -1;
		m_Menu		= -1;
		m_Back		= -1;
		m_Menu		= -1;
		m_Right		= -1;
		m_Scale		= 1;
		m_Width		= 0;
		m_Height	= 0;
		m_Column	= 0;
		m_Row		= 0;
		
		//INitialize input
		m_Keys			= [NSMutableArray array];
		m_Touches		= [NSMutableArray arrayWithCapacity:10];
		m_Accelerometer	= nil;
	}
	
	//Return
	return self;
}

+ (ARKDevice*)instance {
	//Return the correct instance
	return [ARKiOSDevice instance];
}

- (void)openURL:(NSString *)url inBrowser:(BOOL)browser withTitle:(NSString *)title withLoading:(NSString *)loading { [self doesNotRecognizeSelector:_cmd]; }
- (void)openAppPage:(NSString *)app																					{ [self doesNotRecognizeSelector:_cmd]; }

@end
