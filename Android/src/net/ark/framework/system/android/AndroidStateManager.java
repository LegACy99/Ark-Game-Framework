package net.ark.framework.system.android;

import java.util.ArrayList;

import net.ark.framework.states.GameState;
import net.ark.framework.system.Device;
import net.ark.framework.system.StateManager;

public class AndroidStateManager extends StateManager {
    protected AndroidStateManager() {
    	//Super
    	super();
    	
		//Initialize data
    	m_Initialized	= false;
		m_Paused		= false;
		m_Running		= true;
		m_RemovalDepth	= 0;
		m_CurrentTime	= System.currentTimeMillis();
		
		//Create state stack
		m_StateList = new ArrayList<GameState>();
    }	
    
	public synchronized static StateManager instance() {
		//Create state manager if doesn't exist
		if (s_Instance == null) s_Instance = new AndroidStateManager();
		return s_Instance;
	}

	@Override
	protected void initialize() {
		//Skip if initialized
		if (m_Initialized) return;
        m_Initialized = true;
        
		//Go to the first state
        if (m_Factory != null) goTo(m_Factory.getFirstState(), null, false);
	}

	@Override
	public void quit() {
		//Kill all states
		int StateNumber = m_StateList.size();
		for (int i = 0; i < StateNumber; i++) removeState();
		
		//No longer running
		m_Running = false;
	}

	@Override
	public void goTo(int id, Object[] parameters, boolean swap) {
		//Ensure states exist
		if (m_StateList != null) {
			//Prepare variables
			int i			= 0;
			boolean Exist	= false;

			//Find state
			while (!Exist && i < m_StateList.size()) {
				//Is it exist?
				if (m_StateList.get(i) != null)
					if (m_StateList.get(i).getID() == id) Exist = true;

				//Next
				i++;
			}
			
			//If exist, return, otherwise, add a new one
			if (Exist) returnTo(id, parameters);
			else {
				//Create new state
				GameState NewState = null;
				if (m_Factory != null) 	NewState = m_Factory.createState(id, parameters);
				if (NewState == null) 	return;

				//If not swapped, add state
				if (!swap) addState(NewState);
				else {
					//Remove top
					m_RemovalDepth++;

					//Add the state before current state
					addState(NewState);
					
					//If not empty
					if (m_StateList.size() > 1) {
						m_StateList.remove(m_StateList.size() - 1);
						m_StateList.add(m_StateList.size() - 1, NewState);
					}
				}
			}
		}
	}

	@Override
	protected void addState(GameState state) {
		//Skip if null
		if (state == null) return;
		
		//If current state exist, exit it
		if (!m_StateList.isEmpty()) if (m_StateList.get(m_StateList.size() - 1) != null) m_StateList.get(m_StateList.size() - 1).onExit();
		
		//Add state
		m_StateList.add(state);

		//Initialize
		state.initialize();
		state.onEnter();
	}

	@Override
	protected void removeState() {
		if (m_StateList == null)								return;
		if (m_StateList.isEmpty())								return;
		if (m_StateList.get(m_StateList.size() - 1) == null)	return;

		//Remove state
		m_StateList.get(m_StateList.size() - 1).onExit();
		m_StateList.get(m_StateList.size() - 1).onRemove();
		m_StateList.remove(m_StateList.size() - 1);

		//Enter state below
		if (!m_StateList.isEmpty())
			if (m_StateList.get(m_StateList.size() - 1) != null) m_StateList.get(m_StateList.size() - 1).onEnter();
	}

	@Override
	protected void returnTo(int id, Object[] parameters) {
		//Initialize variable
		m_RemovalDepth	= 0;
		boolean Found	= false;
		int i			= m_StateList.size() - 1;

		//While exist
		while (i >= 0 & !Found) {
			//Remove null, if not
			if (m_StateList.get(i) != null) {
				//If the same id
				if (m_StateList.get(i).getID() == id) {
					//Found
					Found = true;
					m_StateList.get(i).onEnter();
				} else m_RemovalDepth++;
			} else m_RemovalDepth++;

			//Next
			i--;
		}
	}
	
	public void run() {
		//Initialize if not
		if (!m_Initialized) initialize();
		
		//Sleep if difference less than frame time
		long Difference = System.currentTimeMillis() - m_CurrentTime;
		if (Difference < (1000 / m_System.getFPS())) {
			try {
				Thread.sleep((1000 / m_System.getFPS()) - Difference);
			} catch (InterruptedException ex) {}
		}
		
		//Save current time
		Difference 		= System.currentTimeMillis() - m_CurrentTime;
		m_CurrentTime 	= System.currentTimeMillis();

		//Trim states
		for (int i = 0; i < m_RemovalDepth; i++) removeState();
		m_RemovalDepth = 0;

		//Top state exist?
		boolean TopExist	= false;
		boolean ActiveFound = false;
		if (!m_StateList.isEmpty()) TopExist = m_StateList.get(m_StateList.size() - 1) != null;

		//While not empty
		while (!m_StateList.isEmpty() && TopExist && !ActiveFound) {
			//If top state is not active, remove
			if (!m_StateList.get(m_StateList.size() - 1).isActive()) removeState();
			//Otherwise, active state is found
			else ActiveFound = true;

			//Check top state
			TopExist = m_StateList.isEmpty() ? false : m_StateList.get(m_StateList.size() - 1) != null;
		}

		//Quit if empty or last state doesn't exist
		if (m_StateList.isEmpty() || !TopExist) m_Running = false;
		else {			
			//Get current state
			GameState CurrentState = m_StateList.get(m_StateList.size() - 1);
			
			//If paused
			if (m_Paused) {					
				//Update if state should run in background
				//(CurrentState.runsInBackground()) CurrentState.update(Difference, 0, null);
			} else {
				//Update and draw state
				CurrentState.update(Difference, Device.instance().getKeys(), Device.instance().getTouches(), Device.instance().getAccelerometer());
				CurrentState.draw(((AndroidDevice)Device.instance()).getGL());
			}
		}
	}
	
	//The only instance
	private static AndroidStateManager s_Instance = null;
	
	//State data
	protected boolean				m_Paused;
	protected boolean				m_Initialized;
	protected ArrayList<GameState>	m_StateList;
	protected long					m_CurrentTime;
	protected int					m_RemovalDepth;
}
