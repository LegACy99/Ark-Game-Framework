//
//  ARKiOSTexture.m
//  Ark Framework
//
//  Created by LegACy on 4/10/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

//Header files
#import <GLKit/GLKit.h>
#import "ARKiOSTexture.h"

@implementation ARKiOSTexture

- (id)initFromFile:(NSString*)file withAntiAlias:(BOOL)antiAlias {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		m_Name		= file;
		m_AntiAlias	= antiAlias;
		
		//Load
		[self load];
	}
	
	//Return
	return self;
}

- (void)load {
	//Load image file
	NSString* Path	= [[NSBundle mainBundle] pathForResource:m_Name ofType:@"png" inDirectory:@"textures"];
	UIImage* Img	= [UIImage imageWithContentsOfFile:Path];
	
	//If texture exist
	if (Img) {
		//Get data
		m_Width		= Img.size.width;
		m_Height	= Img.size.height;
		
		//Create texture
		m_Texture = [GLKTextureLoader textureWithCGImage:Img.CGImage options:nil error:nil];
		if (m_Texture) {
			//Set texture parameter
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, m_AntiAlias ? GL_LINEAR : GL_NEAREST);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			
			//Save data
			m_ID = m_Texture.name;
		}
	}
}

- (void)destroy {
	//IF there's ID
	if (m_ID > 0) {
		//Delete texture
		glDeleteTextures(1, &m_ID);
		m_ID = -1;
	}
}

@end
