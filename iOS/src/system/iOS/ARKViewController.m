//
//  ARKViewController.m
//  Ark Framework
//
//  Created by LegACy on 4/20/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

#import "ARKViewController.h"
#import "ARKAccelerometerInfo.h"
#import "ARKTouchInfo.h"

@interface ARKViewController ()

//Touch stuff
- (ARKTouchInfo*)findInfoFor:(UITouch*)touch;
- (ARKTouchInfo*)findInfoFor:(UITouch*)touch thenSave:(BOOL)save;

- (void)swipeEast;
- (void)swipeWest;
- (void)swipeNorth;
- (void)swipeSouth;

@end

@implementation ARKViewController

//Synthesize
@synthesize touches			= m_Touches;
@synthesize accelerometer	= m_Accelerometer;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
	//Super
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
		//Initialize
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
	EAGLContext* Context = [[EAGLContext alloc] initWithAPI:kEAGLRenderingAPIOpenGLES2];
	if (Context) {
		//Set context
		((GLKView*)self.view).context = Context;
	}
}

- (void)dealloc {
	
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)glkViewController:(GLKViewController *)controller willPause:(BOOL)pause {
	
}

- (void)glkViewControllerUpdate:(GLKViewController *)controller {
}

- (void)glkView:(GLKView *)view drawInRect:(CGRect)rect {
	
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
