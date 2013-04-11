//
//  ARKLoadableTexture.m
//  Ark Framework
//
//  Created by LegACy on 4/11/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKLoadableTexture.h"
#import "ARKTexture.h"

@implementation ARKLoadableTexture

- (id)init {
	//DOn't use
	return nil;
}

- (id)initWithName:(NSString*)name { return [self initWithName:name withAntiAlias:YES]; }
- (id)initWithName:(NSString *)name withAntiAlias:(BOOL)antialias {
	//Super
	self = [super initResourceType:LOADABLE_TEXTURE withName:name];
	if (self) {
		//Save antialias
		m_AntiAlias = antialias;
	}
	
	//Return
	return self;
}

- (id)load {
	//Return texture
	return [ARKTexture createFromFile:m_Name withAntiAlias:m_AntiAlias];
}

@end
