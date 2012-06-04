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

		case GameState.CREDIT:
			//Create title
			NewState = new StateCredit();
			break;

		case GameState.HELP:
			//Create title
			if (parameters != null && parameters.size() >= 1 && parameters.elementAt(0) instanceof Boolean)
				NewState = new StateHelp(((Boolean)parameters.elementAt(0)).booleanValue());
			else NewState = new StateHelp(false);
			break;

		case GameState.UPGRADE:
			//Create upgrade menu
			NewState = new StateUpgrade();
			break;

		case GameState.ACHIEVEMENT:
			//Create title
			NewState = new StateAchievement();
			break;

		case GameState.GAME:
			//Create game
			NewState = new StateGame();
			break;

		case GameState.RESULT:
			//Create result if parameter right
			if (parameters != null && parameters.size() >= 2 && parameters.elementAt(0) instanceof int[] && parameters.elementAt(1) instanceof Float)
				NewState = new StateResult((int[])parameters.elementAt(0), ((Float)parameters.elementAt(1)).floatValue());
			break;

		case GameState.PAUSE:
			//Create title
			NewState = new StatePause();
			break;

		case GameState.STORY:
			//Create title
			NewState = new StateStory();
			break;
		}

		//Return the state
		return NewState;
	}
}
