//
//  ARKTexture.h
//  Ark Framework
//
//  Created by LegACy on 4/10/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import <GLKit/GLKit.h>

@interface ARKTexture : NSObject {
	//Variables
	NSString*	m_Name;
	float		m_Width;
	float		m_Height;
	BOOL		m_AntiAlias;
	GLuint		m_ID;
}

//Properties
@property (readonly, getter = getID)			GLuint ID;
@property (readonly, getter = getWidth)			float width;
@property (readonly, getter = getHeight)		float height;
@property (readonly, getter = isAntiAliased)	BOOL antialias;

//Static functions
+ (ARKTexture*)createFromFile:(NSString*)file;
+ (ARKTexture*)createFromFile:(NSString *)file withAntiAlias:(BOOL)antiAlias;

//Functions
- (void)load;
- (void)destroy;

@end
