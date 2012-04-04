package net.ark.framework.system;

import net.ark.framework.states.GameState;
import net.ark.framework.system.android.AndroidStateManager;

public abstract class StateManager {
    protected StateManager() {
		//Initialize data
    	m_First		= -1;
		m_Running	= true;
    }	
	public synchronized static StateManager instance() {
		//Return the corresponding manager
		return AndroidStateManager.instance();
	}

	//Accessors
	public boolean isRunning()	{	return m_Running;	}
	
	public void setFirstState(int state) {
		//Set first state
		m_First = state;
	}

	//State navigation
	protected abstract void initialize();
	protected abstract void removeState();
	protected abstract void addState(GameState state);
	protected abstract void returnTo(int id, Object[] parameters);
	public abstract void    goTo(int id, Object[] parameters, boolean swap);
	
	//Update
	public abstract void run();
	public abstract void quit();
	
	//Data
	protected int		m_First;
	protected boolean 	m_Running;
}
