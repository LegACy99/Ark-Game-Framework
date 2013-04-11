//
//  ARKiOSResourceManager.m
//  Ark Framework
//
//  Created by LegACy on 4/11/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKiOSResourceManager.h"
#import "ARKTexture.h"

@implementation ARKiOSResourceManager

- (id)init {
	//Super
	self = [super init];
	if (self) {
		//Initialize
		m_Destroys	= [NSMutableArray array];
		m_Textures	= [NSMutableArray array];
		m_Loadables	= [NSMutableArray array];
		m_Resources	= [NSMutableDictionary dictionary];
	}
	
	//Return
	return self;
}

+ (ARKiOSResourceManager*)instance {
	//Static objects
	static dispatch_once_t Token			= 0;
	static ARKiOSResourceManager* Instance	= nil;
	
	//Only create object once
	dispatch_once(&Token, ^{
		Instance = [[self alloc] init];
	});
	
	//Return
	return Instance;
}

- (NSDictionary*) readJSONFromFile:(NSString *)file {
	//Initialize
	NSDictionary* Result = nil;

	//Open JSON file
	NSString* Path			= [[NSBundle mainBundle] pathForResource:file ofType:@"json"];
	NSInputStream* Stream	= [NSInputStream inputStreamWithFileAtPath:Path];// [[NSString alloc] initWithContentsOfFile:Path encoding:NSUTF8StringEncoding error:nil];
	if (Stream) {
		//Create JSON from stream
		[Stream open];
		id JSON = [NSJSONSerialization JSONObjectWithStream:Stream options:0 error:nil];
		[Stream close];
		
		//If valid, save
		if (JSON && [JSON isKindOfClass:[NSDictionary class]]) Result = JSON;
	}
	
	//Return
	return Result;
}

- (void)start {
	//Initialize
	m_Added = [m_Loadables count];
	[m_Destroys removeAllObjects];
	
	//Super
	[super start];
}

- (void)reload {
	//Reload textures
	for (int i = 0; i < [m_Textures count]; i++) [(ARKTexture*)[m_Textures objectAtIndex:i] load];
}

- (void)destroy {
	//Destroy all destructibles
	for (int i = 0; i < [m_Destroys count]; i++) [self destroyResourceWithName:[m_Destroys objectAtIndex:i]];
	[m_Destroys removeAllObjects];
}

- (void)destroyResourceWithName:(NSString *)name {
	//If loading
	if ([self isLoading]) {
		//Check if going ot be loaded
		BOOL Loaded = NO;
		//for (int i = 0; i < [m_Loadables count] && !Loaded; i++) if (m_Loadables.get(i).getName().equals(resource)) Loaded = true;
		
		//If isn't going to be loaded, add
		if (!Loaded) [m_Destroys addObject:name];
	} else {
		//If exist
		id Removed = [m_Resources objectForKey:name];
		if (Removed) {
			//Remove
			[m_Resources removeObjectForKey:name];
			
			//If texture
			if ([Removed isKindOfClass:[ARKTexture class]]) {
				//Destroy
				[m_Textures removeObject:Removed];
				[(ARKTexture*)Removed destroy];
			}
		}
	}
}

- (void)update {
	//If loading
	if ([self isLoading]) {
		//Get object
		/*id Current		= [m_Loadables objectAtIndex:m_Loaded];
		NSString* Name	= Current;//[Current.getName();
		
		//If doesn't exist
		id Resource = [m_Resources objectForKey:Name];
		if (Resource) {
			//Load
			id Loaded = nil;//[Current.load()];
			if (Loaded) {
				//Add
				[m_Resources setObject:<#(id)#> forKey:<#(id<NSCopying>)#>]
				m_Resources.put(Current.getName(), Loaded);
				if (Loaded instanceof Texture) m_Textures.add((Texture)Loaded);
			}
		}*/
		
		//Update
		[super update];
		
		//If finished
		if (m_Finished) {
			//Reset
			m_Added = 0;
			[m_Loadables removeAllObjects];
		}
	}
}

@end
