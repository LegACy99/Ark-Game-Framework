//
//  ARKLoadableTexture.h
//  Ark Framework
//
//  Created by LegACy on 4/11/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKLoadable.h"

@interface ARKLoadableTexture : ARKLoadable {
	//Variable
	BOOL m_AntiAlias;
}

//Constructor
- (id)initWithName:(NSString*)name;
- (id)initWithName:(NSString *)name withAntiAlias:(BOOL)antialias;

@end
