//
//  FloatingLabel.h
//  ARK Framework Example
//
//  Created by LegACy on 4/28/13.
//  Copyright (c) 2013 Raka Mahesa. All rights reserved.
//

#import <ARKFramework/ARKFramework.h>

@interface FloatingLabel : ARKDrawable {
	//Data
	ARKLabel*	m_Label1;
	ARKLabel*	m_Label2;
	float		m_InitialY;
	long		m_Duration;
	long		m_Timer;
}

//Functions
- (id)initWithText1:(NSString*)text1 withText2:(NSString*)text2 withDuration:(long)duration atX:(float)x atY:(float)y;
- (void)updateWithDeltaTime:(long)time;
- (BOOL)isDead;

@end
