package com.ark.example.states;

import net.ark.framework.states.GameState;

import com.ark.example.states.StateSplash;
import com.ark.example.states.StateTitle;

public class StateFactory {
	// Empty constructor
	protected StateFactory() {
	}

	public static GameState createState(int id, Object[] parameters) {
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
