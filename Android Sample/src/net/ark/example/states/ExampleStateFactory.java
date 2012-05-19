package net.ark.example.states;

import net.ark.framework.states.GameState;
import net.ark.framework.states.StateFactory;

import net.ark.example.states.StateSplash;
import net.ark.example.states.StateTitle;

public class ExampleStateFactory implements StateFactory {
	@Override
	public int getFirstState() {
		//Splash screen
		return ExampleState.SPLASH;
	}
	
	@Override
	public GameState createState(int id, Object[] parameters) {
		// Default
		ExampleState NewState = null;

		// Based on id
		switch (id) {
		case ExampleState.SPLASH:
			NewState = new StateSplash();
			break;

		case ExampleState.TITLE:
			NewState = new StateTitle();
			break;
		}

		// Return the state
		return NewState;
	}
}
