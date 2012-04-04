package com.ark.example.states;

import net.ark.framework.states.GameState;
import net.ark.framework.system.images.Image;

public abstract class ExampleState extends GameState {
	//State ID
	public static final int SPLASH 	= 1;
	public static final int TITLE 	= 2;
	
	public ExampleState(int id) 					{ super(id);				}
	public ExampleState(int id, Image background) 	{ super(id, background);	}
	public ExampleState(int id, String background) 	{ super(id, background);	}
}
