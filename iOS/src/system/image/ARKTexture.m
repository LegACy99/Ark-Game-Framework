//
//  ARKTexture.m
//  Ark Framework
//
//  Created by LegACy on 4/10/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKTexture.h"
#import "ARKiOSTexture.h"

@implementation ARKTexture

//Synthesize properties
@synthesize ID		= m_ID;
@synthesize width	= m_Width;
@synthesize height	= m_Height;

- (id)init {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		m_ID		= -1;
		m_Name		= nil;
		m_AntiAlias	= YES;
		m_Height	= 0;
		m_Width		= 0;
	}
	
	//Return
	return self;
}

+ (ARKTexture*)createFromFile:(NSString*)file {
	//Default
	return [ARKTexture createFromFile:file withAntiAlias:YES];
}

+ (ARKTexture*)createFromFile:(NSString *)file withAntiAlias:(BOOL)antiAlias {
	//Create texture
	return [[ARKiOSTexture alloc] initFromFile:file withAntiAlias:antiAlias];
}

- (void)load {
	//Don't use
	[self doesNotRecognizeSelector:_cmd];
}
- (void)destroy {
	//Don't use
	[self doesNotRecognizeSelector:_cmd];
}

@end
