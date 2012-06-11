package net.ark.example.states;

import net.ark.framework.states.GameState;
import java.util.Vector;
import net.ark.framework.states.StateFactory;

/**
 *
 * @author LegACy
 */
public class KiteStateFactory implements StateFactory {
	public int getFirstState() {
		//Splash screen
		return GameState.SPLASH;
	}
	public GameState createState(int id, Object[] parameters) {
		//Default value
		GameState NewState = null;

		//Based on id
		switch(id) {
		case GameState.SPLASH:
			//Create front state
			NewState = new StateSplash();
			break;
			
		case GameState.TITLE:
			//Create title
			NewState = new StateTitle();
			break;
		}

		//Return the state
		return NewState;
	}
}
