package net.ark.framework.system;

import net.ark.framework.states.GameState;
import net.ark.framework.system.android.AndroidStateManager;

public abstract class StateManager {
	public synchronized static StateManager instance() {
		//Return the corresponding manager
		return AndroidStateManager.instance();
	}

	//Accessors
	public boolean isRunning()	{	return m_Running;	}

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
	protected boolean m_Running;
}
