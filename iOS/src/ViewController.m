//
//  ViewController.m
//  Ark Framework
//
//  Created by LegACy on 4/6/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

//Header files
#import "ViewController.h"

#define BUFFER_OFFSET(i) ((char *)NULL + (i))

// Uniform index.
enum {
    UNIFORM_MODELVIEWPROJECTION_MATRIX,
    UNIFORM_NORMAL_MATRIX,
    NUM_UNIFORMS
};

GLint uniforms[NUM_UNIFORMS];

// Attribute index.
enum {
    ATTRIB_VERTEX,
    ATTRIB_NORMAL,
    NUM_ATTRIBUTES
};

GLfloat gCubeVertexData[216] = {
    // Data layout for each line below is:
    // positionX, positionY, positionZ,     normalX, normalY, normalZ,
	
    -100, 0, 0,        0, 0.0f, 1.0f,
    -100, 100, 0,        0, 0.0f, 1.0f,
    0, 0, 0,         0, 0.0f, 1.0f,
    0, 0, 0,         0, 0.0f, 1.0f,
    -100, 100, 0,        0, 0.0f, 1.0f,
    0, 100, 0,         0, 0.0f, 1.0f,
};

@interface ViewController () {
    
    GLuint _vertexArray;
    GLuint _vertexBuffer;
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
		((GLKView *)self.view).drawableDepthFormat	= GLKViewDrawableDepthFormat24;
		
		//Setup openGL
		[self setupGL];
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
    
    self.effect = [[GLKBaseEffect alloc] init];
    self.effect.light0.enabled = GL_TRUE;
    self.effect.light0.diffuseColor = GLKVector4Make(1.0f, 0.4f, 0.4f, 1.0f);
    
    glEnable(GL_DEPTH_TEST);
    
    glGenVertexArraysOES(1, &_vertexArray);
    glBindVertexArrayOES(_vertexArray);
    
    glGenBuffers(1, &_vertexBuffer);
    glBindBuffer(GL_ARRAY_BUFFER, _vertexBuffer);
    glBufferData(GL_ARRAY_BUFFER, sizeof(gCubeVertexData), gCubeVertexData, GL_STATIC_DRAW);
    
    glEnableVertexAttribArray(GLKVertexAttribPosition);
    glVertexAttribPointer(GLKVertexAttribPosition, 3, GL_FLOAT, GL_FALSE, 24, BUFFER_OFFSET(0));
    glEnableVertexAttribArray(GLKVertexAttribNormal);
    glVertexAttribPointer(GLKVertexAttribNormal, 3, GL_FLOAT, GL_FALSE, 24, BUFFER_OFFSET(12));
    
    glBindVertexArrayOES(0);
}

- (void)tearDownGL {
	//Set context
    [EAGLContext setCurrentContext:self.context];
    
	//Destroy buffers
    glDeleteBuffers(1, &_vertexBuffer);
    glDeleteVertexArraysOES(1, &_vertexArray);
    
	//No effect
    self.effect = nil;
}

- (void)update {
    float aspect = fabsf(self.view.bounds.size.width / self.view.bounds.size.height);
    GLKMatrix4 projectionMatrix = GLKMatrix4MakeFrustum(-aspect, aspect, -1, 1, 1.0f, 1000.0f);
    
    self.effect.transform.projectionMatrix = projectionMatrix;
}

- (void)glkView:(GLKView *)view drawInRect:(CGRect)rect {
    glClearColor(0.65f, 0.65f, 0.65f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    
    glBindVertexArrayOES(_vertexArray);
	
	//Create
    GLKMatrix4 baseModelViewMatrix = GLKMatrix4MakeLookAt(0, 0, self.view.bounds.size.height / 2, 0, 0, 0, 0, 1, 0);
    self.effect.transform.modelviewMatrix = baseModelViewMatrix;
    
    // Render the object with GLKit
    [self.effect prepareToDraw];
    glDrawArrays(GL_TRIANGLES, 0, 6);
    
    // Compute the model view matrix for the object rendered with GLKit
    GLKMatrix4 modelViewMatrix				= GLKMatrix4MakeTranslation(100.0f, -100.0f, 0);
    modelViewMatrix							= GLKMatrix4Multiply(baseModelViewMatrix, modelViewMatrix);
    self.effect.transform.modelviewMatrix	= modelViewMatrix;
	
    // Render the object with GLKit
    [self.effect prepareToDraw];
    glDrawArrays(GL_TRIANGLES, 0, 6);
}

@end
