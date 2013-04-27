//
//  ARKiOSResourceManager.h
//  Ark Framework
//
//  Created by LegACy on 4/11/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

//Imports
#import "ARKResourceManager.h"

//Class declaration
@interface ARKiOSResourceManager : ARKResourceManager {
	//Variables
	NSMutableArray*			m_Destroys;
	NSMutableArray*			m_Textures;
	NSMutableArray*			m_Loadables;
	NSMutableDictionary*	m_Resources;
}

+ (ARKiOSResourceManager*)instance;

@end
