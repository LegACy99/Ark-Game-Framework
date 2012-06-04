package com.pedongi.framework.system;

import com.pedongi.framework.states.GameState;
import com.pedongi.framework.system.j2me.J2MEStateManager;
import java.util.Vector;

public abstract class StateManager implements Runnable {	
	public synchronized static StateManager instance() {
		//Return the corresponding manager
		return J2MEStateManager.instance();
	}

	//Accessors
	public boolean isRunning()	{	return m_Running;	}

	//State navigation
	public abstract void    goTo(int id, Vector parameters, boolean swap);
	protected abstract void addState(GameState state);
	protected abstract void removeState();
	protected abstract void returnTo(int id, Vector parameters);

	//Update
	public abstract void run();
	public abstract void quit();

	//Data
	protected boolean m_Running;
}
