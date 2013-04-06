//
//  Shader.fsh
//  Ark Framework
//
//  Created by LegACy on 4/6/13.
//  Copyright (c) 2013 LegACy. All rights reserved.
//

varying lowp vec4 colorVarying;

void main()
{
    gl_FragColor = colorVarying;
}
