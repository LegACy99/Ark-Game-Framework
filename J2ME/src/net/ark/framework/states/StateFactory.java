package net.ark.framework.states;

public interface StateFactory {
	//Return the first state
	public int getFirstState();
	
	//Create the correct state
	public GameState createState(int id, Object[] parameters);
}
