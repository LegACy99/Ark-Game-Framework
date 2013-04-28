//
//  ARKiOSTexture.h
//  Ark Framework
//
//  Created by LegACy on 4/10/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKTexture.h"

//Forward declaration
@class GLKTextureInfo;

@interface ARKiOSTexture : ARKTexture {
	//Variable
	GLKTextureInfo* m_Texture;
}

//Functions
- (id)initFromFile:(NSString*)file withAntiAlias:(BOOL)antiAlias;

@end
