//
//  ARKViewController.m
//  Ark Framework
//
//  Created by LegACy on 4/20/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKViewController.h"
#import "ARKAccelerometerInfo.h"
#import "ARKStateManager.h"
#import "ARKSoundManager.h"
#import "ARKTouchInfo.h"
#import "ARKUtilities.h"
#import "ARKiOSDevice.h"

@interface ARKViewController ()

//Touch stuff
- (ARKTouchInfo*)findInfoFor:(UITouch*)touch;
- (ARKTouchInfo*)findInfoFor:(UITouch*)touch thenSave:(BOOL)save;

//Swipe stuff
- (void)swipeEast;
- (void)swipeWest;
- (void)swipeNorth;
- (void)swipeSouth;

@end

@implementation ARKViewController

//Synthesize
@synthesize gl				= m_OpenGL;
@synthesize touches			= m_Touches;
@synthesize accelerometer	= m_Accelerometer;
@synthesize time			= m_Time;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
	//Super
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
		//Initialize
		m_Size			= NO;
		m_OpenGL		= nil;
		m_Context		= nil;
		m_UITouches		= [NSMutableDictionary dictionary];
		m_Accelerometer = [[ARKAccelerometerInfo alloc] init];
		
		//10 possible touch
		m_Touches = [NSArray arrayWithObjects:
					 [[ARKTouchInfo alloc] init], [[ARKTouchInfo alloc] init],
					 [[ARKTouchInfo alloc] init], [[ARKTouchInfo alloc] init],
					 [[ARKTouchInfo alloc] init], [[ARKTouchInfo alloc] init],
					 [[ARKTouchInfo alloc] init], [[ARKTouchInfo alloc] init],
					 [[ARKTouchInfo alloc] init], [[ARKTouchInfo alloc] init],
					 nil];
		
		//Set delegate
		self.delegate = self;
    }
	
	//Return
    return self;
}

- (void)viewDidLoad {
	//Super
    [super viewDidLoad];
	
	//Create gesture detectors
	UISwipeGestureRecognizer* RecognizerUp		= [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(swipeNorth)];
	UISwipeGestureRecognizer* RecognizerDown	= [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(swipeSouth)];
	UISwipeGestureRecognizer* RecognizerLeft	= [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(swipeWest)];
	UISwipeGestureRecognizer* RecognizerRight	= [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(swipeEast)];
	[RecognizerRight setDirection:UISwipeGestureRecognizerDirectionRight];
	[RecognizerLeft setDirection:UISwipeGestureRecognizerDirectionLeft];
	[RecognizerDown setDirection:UISwipeGestureRecognizerDirectionDown];
	[RecognizerUp setDirection:UISwipeGestureRecognizerDirectionUp];
	
	//Add gesture detectors
	[self.view addGestureRecognizer:RecognizerUp];
	[self.view addGestureRecognizer:RecognizerDown];
	[self.view addGestureRecognizer:RecognizerLeft];
	[self.view addGestureRecognizer:RecognizerRight];
	
	//Create context
	m_Context = [[EAGLContext alloc] initWithAPI:kEAGLRenderingAPIOpenGLES2];
	if (m_Context) {
		//Set context
		((GLKView*)self.view).context = m_Context;
		self.preferredFramesPerSecond = [[ARKUtilities instance] getSystemFPS];
		
		//Initialize openGL
		glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		
		//Set openGL state
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnableVertexAttribArray(GLKVertexAttribTexCoord0);
		glEnableVertexAttribArray(GLKVertexAttribPosition);
		glEnableVertexAttribArray(GLKVertexAttribColor);
		
		//Initialize OpenGL effect
		m_OpenGL					= [[GLKBaseEffect alloc] init];
		m_OpenGL.texture2d0.envMode	= GLKTextureEnvModeModulate;
		m_OpenGL.texture2d0.target	= GLKTextureTarget2D;
		
		NSLog(@"Setting up OpenGL");
		
		//Save controller
		[[ARKiOSDevice instance] setupViewController:self];
	}
}

- (void)dealloc {
	//Remove context
	if ([EAGLContext currentContext] == m_Context) [EAGLContext setCurrentContext:nil];
	m_Context	= nil;
	m_OpenGL	= nil;
}

- (void)didReceiveMemoryWarning {
	//Super
    [super didReceiveMemoryWarning];
	
	//If view exist
    if ([self isViewLoaded] && ([[self view] window] == nil)) {
		//Destroy
		self.view = nil;
		if ([EAGLContext currentContext] == m_Context) [EAGLContext setCurrentContext:nil];
		m_Context	= nil;
		m_OpenGL	= nil;
    }
}

- (void)glkViewController:(GLKViewController *)controller willPause:(BOOL)pause {
	//If paused
	if (pause) {
		//pause
		[[ARKStateManager instance] pause];
		[[ARKSoundManager instance] pause];
	} else {
		//Resume
		[[ARKStateManager instance] resume];
		[[ARKSoundManager instance] resume];
	}
}

- (void)glkViewControllerUpdate:(GLKViewController *)controller {
	//If size not saved yet
	if (!m_Size) {
		//Get view size
		float Width		= self.view.bounds.size.width;
		float Height	= self.view.bounds.size.height;
		
		//Save size
		m_Size = YES;
		[[ARKiOSDevice instance] setSizeWithWidth:Width withHeight:Height];
		
		//Create projection matrix (view frustum)
		float Ratio							= fabsf(Width / Height);
		GLKMatrix4 Projection				= GLKMatrix4MakeFrustum(-Ratio, Ratio, -1, 1, 1.0f, 1000.0f);
		m_OpenGL.transform.projectionMatrix = Projection;
	}
}

- (void)glkView:(GLKView *)view drawInRect:(CGRect)rect {
	//Get time
	m_Time = [self timeSinceLastDraw] * 1000;
	
	//Clear
    glClear(GL_COLOR_BUFFER_BIT);
	//glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
	
	//Check all touch
	for (int i = 0; i < [m_Touches count]; i++) if (![[m_Touches objectAtIndex:i] isPressed]) [[m_Touches objectAtIndex:i] removed];
	
	//Run state manager
	[[ARKStateManager instance] run];
}

- (ARKTouchInfo*)findInfoFor:(UITouch *)touch { return [self findInfoFor:touch thenSave:NO]; }
- (ARKTouchInfo*)findInfoFor:(UITouch *)touch thenSave:(BOOL)save {
	//Initialize
	ARKTouchInfo* Result = nil;
	
	//If exist
	if (touch) {
		//Get touch info
		NSValue* TouchPointer	= [NSValue valueWithPointer:(__bridge const void *)touch];
		Result					= [m_UITouches objectForKey:TouchPointer];
		
		//If no result
		if (!Result) {
			//For all info
			for (int j = 0; j < [m_Touches count] && !Result; j++) {
				//For each recorded info
				BOOL Found		= NO;
				NSArray* Saved	= [m_UITouches allValues];
				for (int k = 0; k < [Saved count] && !Found; k++) {
					//If save, found
					if ([m_Touches objectAtIndex:j] == [Saved objectAtIndex:k]) Found = YES;
				}
				
				//If not found
				if (!Found) {
					//Save
					Result = [m_Touches objectAtIndex:j];
					if (save) [m_UITouches setObject:Result forKey:TouchPointer];
				}
			}
		}
	}
	
	//Return
	return Result;
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
	//If there's touch
	if (touches) {
		//Get array
		NSArray* Touches = [touches allObjects];
		for (int i = 0; i < [Touches count]; i++) {
			//Get touch
			id Touch = [Touches objectAtIndex:i];
			if (Touch && [Touch isKindOfClass:[UITouch class]]) {
				//Get touch info
				ARKTouchInfo* Info = [self findInfoFor:Touch thenSave:YES];
				if (Info) {
					//Press
					CGPoint Location = [Touch locationInView:[self view]];
					[Info pressedAtX:Location.x atY:Location.y];
				}
			}
		}
	}
}

- (void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event {
	//If there's touch
	if (touches) {
		//Get array
		NSArray* Touches = [touches allObjects];
		for (int i = 0; i < [Touches count]; i++) {
			//Get touch
			id Touch = [Touches objectAtIndex:i];
			if (Touch && [Touch isKindOfClass:[UITouch class]]) {
				//Get touch info
				ARKTouchInfo* Info = [self findInfoFor:Touch];
				if (Info) {
					//Move
					CGPoint Location = [Touch locationInView:[self view]];
					[Info draggedToX:Location.x toY:Location.y];
				}
			}
		}
	}
}

- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event {
	//If there's touch
	if (touches) {
		//Get array
		NSArray* Touches = [touches allObjects];
		for (int i = 0; i < [Touches count]; i++) {
			//Get touch
			id Touch = [Touches objectAtIndex:i];
			if (Touch && [Touch isKindOfClass:[UITouch class]]) {
				//Get touch info
				ARKTouchInfo* Info = [self findInfoFor:Touch];
				if (Info) {
					//Move
					CGPoint Location = [Touch locationInView:[self view]];
					[Info releasedAtX:Location.x atY:Location.y];
					
					//Remove touch
					NSValue* TouchPointer = [NSValue valueWithPointer:(__bridge const void *)Touch];
					if (TouchPointer) [m_UITouches removeObjectForKey:TouchPointer];
				}
			}
		}
	}
}

- (void)touchesCancelled:(NSSet *)touches withEvent:(UIEvent *)event {
	//End
	[self touchesEnded:touches withEvent:event];
}

- (void)swipeEast {
	//Add swipe
	[[m_Touches objectAtIndex:0] addSwipeTo:TOUCH_SWIPE_EAST];
}

- (void)swipeWest {
	//Add swipe
	[[m_Touches objectAtIndex:0] addSwipeTo:TOUCH_SWIPE_WEST];
}

- (void)swipeNorth {
	//Add swipe
	[[m_Touches objectAtIndex:0] addSwipeTo:TOUCH_SWIPE_NORTH];
}

- (void)swipeSouth {
	//Add swipe
	[[m_Touches objectAtIndex:0] addSwipeTo:TOUCH_SWIPE_SOUTH];
}

@end
