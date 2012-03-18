package net.ark.framework.states;

import com.ark.example.states.StateSplash;
import com.ark.example.states.StateTitle;

public class StateFactory {
	// Empty constructor
	protected StateFactory() {
	}

	public static GameState createState(int id, Object[] parameters) {
		// Default
		GameState NewState = null;

		// Based on id
		switch (id) {
		case GameState.SPLASH:
			NewState = new StateSplash();
			break;

		case GameState.TITLE:
			NewState = new StateTitle();
			break;
		}

		// Return the state
		return NewState;
	}
}
