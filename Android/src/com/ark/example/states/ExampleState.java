package com.ark.example.states;

import net.ark.framework.components.Drawable;
import net.ark.framework.states.GameState;

public abstract class ExampleState extends GameState {
	//State ID
	public static final int SPLASH 	= 1;
	public static final int TITLE 	= 2;
	
	public ExampleState(int id) 						{ super(id);				}
	public ExampleState(int id, String background) 		{ super(id, background);	}
	public ExampleState(int id, Drawable background) 	{ super(id, background);	}
}
