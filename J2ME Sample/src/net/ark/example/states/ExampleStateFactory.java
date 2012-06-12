package net.ark.example.states;

import net.ark.framework.states.GameState;
import net.ark.framework.states.StateFactory;

/**
 *
 * @author LegACy
 */
public class ExampleStateFactory implements StateFactory {
	public int getFirstState() {
		//Splash screen
		return ExampleState.SPLASH;
	}
	
	public GameState createState(int id, Object[] parameters) {
		//Default value
		GameState NewState = null;

		//Based on id
		switch(id) {
		case ExampleState.SPLASH:
			//Create front state
			NewState = new StateSplash();
			break;
			
		case ExampleState.TITLE:
			//Create title
			NewState = new StateTitle();
			break;
		}

		//Return the state
		return NewState;
	}
}
