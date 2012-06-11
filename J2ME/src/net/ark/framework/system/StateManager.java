package net.ark.framework.system;

import net.ark.framework.states.GameState;
import net.ark.framework.system.j2me.J2MEStateManager;
import net.ark.framework.states.StateFactory;

public abstract class StateManager implements Runnable {
    protected StateManager() {
		//Initialize data
    	m_Factory	= null;
		m_Running	= true;
    }
	
	public synchronized static StateManager instance() {
		//Return the corresponding manager
		return J2MEStateManager.instance();
	}

	//Accessors
	public boolean isRunning()	{	return m_Running;	}
	
	public void setup(StateFactory factory) {
		//Set components
		m_Factory = factory;
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
	protected boolean		m_Running;
	protected StateFactory	m_Factory;
}
