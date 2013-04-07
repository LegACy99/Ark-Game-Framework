//
//  ViewController.m
//  Ark Framework
//
//  Created by LegACy on 4/6/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

//Header files
#import "ViewController.h"
#import "ARKImage.h"

#define BUFFER_OFFSET(i) ((char *)NULL + (i))

GLfloat gCubeVertexData[216] = {
    -64, -128, 0,		0, 0,	1, 1, 1, 0.5f,
    -64, 128, 0,	0, 2,		1, 1, 1, 0.5f,
    0, -128, 0,		0.5f, 0,	1, 1, 1, 0.5f,
    0, -128, 0,		0.5f, 0,	1, 1, 1, 0.5f,
    -64, 128, 0,	0, 2,		1, 1, 1, 0.5f,
    0, 128, 0,		0.5f, 2,		1, 1, 1, 0.5f,
};

@interface ViewController () {
    GLKTextureInfo*	m_Texture;
    GLKTextureInfo*	m_Texture2;
	ARKImage*		m_Image;
}

@property (strong, nonatomic) EAGLContext	*context;
@property (strong, nonatomic) GLKBaseEffect *effect;

//Methods
- (void)setupGL;
- (void)tearDownGL;

@end

@implementation ViewController

- (void)viewDidLoad {
	//Super
    [super viewDidLoad];
    
	//Create context
    self.context = [[EAGLContext alloc] initWithAPI:kEAGLRenderingAPIOpenGLES2];
    if (self.context) {
		//Configure view
		((GLKView *)self.view).context				= self.context;
		//((GLKView *)self.view).drawableDepthFormat	= GLKViewDrawableDepthFormat24;
		
		//Setup openGL
		[self setupGL];
		m_Image = [ARKImage createFromPath:@"texture"];
    }
}

- (void)dealloc {
	//Destroy OpenGL
    [self tearDownGL];
    
	//Remove context
    if ([EAGLContext currentContext] == self.context) [EAGLContext setCurrentContext:nil];
}

- (void)didReceiveMemoryWarning {
	//Super
    [super didReceiveMemoryWarning];
	
	//If there's a view
    if ([self isViewLoaded] && ([[self view] window] == nil)) {
		//No view
        self.view = nil;
        
		//Destroy openGL
        [self tearDownGL];

		//Remove context
        if ([EAGLContext currentContext] == self.context) [EAGLContext setCurrentContext:nil];
        self.context = nil;
    }
}

- (void)setupGL {
	//Set context
    [EAGLContext setCurrentContext:self.context];
	
	//Initialize openGL
    glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
	
	//Set openGL state
	glEnable(GL_BLEND);
	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    
	//Initialize effect
    self.effect						= [[GLKBaseEffect alloc] init];
	self.effect.texture2d0.envMode	= GLKTextureEnvModeModulate;
	self.effect.texture2d0.target	= GLKTextureTarget2D;
	
	//Load texture
	/*glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
	m_Texture2				= [GLKTextureLoader textureWithContentsOfFile:File options:Options error:nil];
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);*/
}

- (void)tearDownGL {
	//Set context
    [EAGLContext setCurrentContext:self.context];
    
	//Destroy buffers
    //glDeleteBuffers(1, &_vertexBuffer);
    //glDeleteVertexArraysOES(1, &_vertexArray);
    
	//No effect
    self.effect = nil;
}

- (void)update {
    float aspect = fabsf(self.view.bounds.size.width / self.view.bounds.size.height);
    GLKMatrix4 projectionMatrix = GLKMatrix4MakeFrustum(-aspect, aspect, -1, 1, 1.0f, 1000.0f);
    
    self.effect.transform.projectionMatrix = projectionMatrix;
}

- (void)glkView:(GLKView *)view drawInRect:(CGRect)rect {
    glClear(GL_COLOR_BUFFER_BIT);
    [m_Image drawWithEffect:self.effect];
}

@end
