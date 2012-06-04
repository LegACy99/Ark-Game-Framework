package com.pedongi.kite.states;
import com.pedongi.framework.states.GameState;
import java.util.Vector;


/**
 *
 * @author LegACy
 */
public class StateFactory {
	/**
	 * Protected constructor
	 */
	protected StateFactory() {}

	public static GameState createState(int id, Vector parameters) {
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
