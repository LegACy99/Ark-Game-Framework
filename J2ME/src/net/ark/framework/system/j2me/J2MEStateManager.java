/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ark.framework.system.j2me;

import net.ark.framework.states.GameState;
import net.ark.framework.system.RecordManager;
import net.ark.framework.system.SoundManager;
import net.ark.framework.system.StateManager;
import net.ark.framework.system.input.TouchInfo;
import net.ark.framework.system.Utilities;
import net.ark.framework.system.input.AccelerometerInfo;
import java.util.Vector;

/**
 *
 * @author LegACy
 */
public class J2MEStateManager extends StateManager {
    protected J2MEStateManager() {
		//Super
		super();
		
		//Initialize data
		m_Paused		= false;
		m_Initialized	= false;
		m_RemovalDepth	= 0;
		
		//Create state stack
		m_StateList = new Vector();
    }	
	public synchronized static StateManager instance() {
		//Create state manager if doesn't exist
		if (s_Instance == null) s_Instance = new J2MEStateManager();
		return s_Instance;
	}

	protected void initialize() {
		//Skip if initialized
		if (m_Initialized) return;
        m_Initialized = true;

		//Go to splash screen
        goTo(m_Factory.getFirstState(), null, false);
	}

    public void quit() {
		//Kill all states
		int StateNumber = m_StateList.size();
		for (int i = 0; i < StateNumber; i++) removeState();
		
		//No longer running
		m_Running = false;
    }

    public void goTo(int id, Object[] parameters, boolean swap) {
		//Ensure states exist
		if (m_StateList != null) {
			//Prepare variables
			int i			= 0;
			boolean Exist	= false;

			//Find state
			while (!Exist && i < m_StateList.size()) {
				//Is it exist?
				if (m_StateList.elementAt(i) != null)
					if (((GameState)m_StateList.elementAt(i)).getID() == id) Exist = true;

				//Next
				i++;
			}
			
			//If exist, return, otherwise, add a new one
			if (Exist) returnTo(id, parameters);
			else {
				//Create new state
				GameState NewState = m_Factory.createState(id, parameters);
				if (NewState == null) return;

				//If not swapped, add state
				if (!swap) addState(NewState);
				else {
					//Remove top
					m_RemovalDepth++;

					//Add the state before current state
					addState(NewState);
					
					//If not empty
					if (m_StateList.size() > 1) {
						m_StateList.removeElement(m_StateList.lastElement());
						m_StateList.insertElementAt(NewState, m_StateList.size() - 1);
					}
				}
			}
		}
    }

    protected void addState(GameState state) {
		//Skip if null
		if (state == null) return;
		
		//If current state exist, exit it
		if (!m_StateList.isEmpty()) if (m_StateList.lastElement() != null) ((GameState)m_StateList.lastElement()).onExit();
		
		//Add state
		m_StateList.addElement(state);

		//Initialize
		state.initialize();
		state.onEnter();
    }

    protected void removeState() {
		if (m_StateList == null)				return;
		if (m_StateList.isEmpty())				return;
		if (m_StateList.lastElement() == null)	return;

		//Remove state
		((GameState)m_StateList.lastElement()).onExit();
		((GameState)m_StateList.lastElement()).onRemove();
		m_StateList.removeElement(m_StateList.lastElement());

		//Enter state below
		if (!m_StateList.isEmpty())
			if (m_StateList.lastElement() != null) ((GameState)m_StateList.lastElement()).onEnter();
    }

    protected void returnTo(int id, Object[] parameters) {
		//Initialize variable
		m_RemovalDepth	= 0;
		boolean Found	= false;
		int i			= m_StateList.size() - 1;

		//While exist
		while (i >= 0 & !Found) {
			//Remove null, if not
			if (m_StateList.elementAt(i) != null) {
				//If the same id
				if (((GameState)m_StateList.elementAt(i)).getID() == id) {
					//Found
					Found = true;
					((GameState)m_StateList.elementAt(i)).onEnter();
				} else m_RemovalDepth++;
			} else m_RemovalDepth++;

			//Next
			i--;
		}
    }

    public void run() {
		//Initialize if not
		if (!m_Initialized) initialize();
		
		//Get current time
		long Current 	= System.currentTimeMillis();
		long Difference	= 0;
		
		//While still running
		while (m_Running) {
			//Sleep if difference less than frame time
			Difference = System.currentTimeMillis() - Current;
			if (Difference < (1000 / Utilities.instance().getFPS())) {
				try {
					Thread.sleep((1000 / Utilities.instance().getFPS()) - Difference);
				} catch (InterruptedException ex) {}
			}
			
			//Save current time
			Difference 	= System.currentTimeMillis() - Current;
			Current 	= System.currentTimeMillis();

			//Trim states
			for (int i = 0; i < m_RemovalDepth; i++) removeState();
			m_RemovalDepth = 0;

			//Top state exist?
			boolean TopExist	= false;
			boolean ActiveFound = false;
			if (!m_StateList.isEmpty()) TopExist = m_StateList.lastElement() != null;

			//While not empty
			while (!m_StateList.isEmpty() && TopExist && !ActiveFound) {
				//If top state is not active, remove
				if (!((GameState)m_StateList.lastElement()).isActive()) removeState();
				//Otherwise, active state is found
				else ActiveFound = true;

				//Check top state
				TopExist = m_StateList.isEmpty() ? false : m_StateList.lastElement() != null;
			}

			//Quit if empty or last state doesn't exist
			if (m_StateList.isEmpty() || !TopExist) m_Running = false;
			else {
				//Get input
				int[] Keys						= J2MEDevice.instance().getKeys();
				AccelerometerInfo Accelerometer	= J2MEDevice.instance().getAccelerometer();
				TouchInfo[] Touches				= J2MEDevice.instance().getTouches();
				
				//Get current state
				GameState CurrentState = (GameState)m_StateList.lastElement();
				
				//If paused
				if (m_Paused) {					
					//Update if state should run in background
					if (CurrentState.runsInBackground()) CurrentState.update(Difference, null, null, null);
					
					//If now canvas is shown
					if (J2MEDevice.instance().isShown()) {
						//Resume
						m_Paused = false;
						SoundManager.instance().resume();
					}
				} else {
					//Update and draw state
					CurrentState.update(Difference, Keys, Touches, Accelerometer);
					CurrentState.draw(((J2MEDevice)J2MEDevice.instance()).getGraphic());
					J2MEDevice.instance().flushGraphics();
					
					//If canvas no longer shown
					if (!J2MEDevice.instance().isShown()) {
						//Pause
						m_Paused = true;
						SoundManager.instance().pause();
					}
				}
			}
		}
		
		//Destroy managers
		SoundManager.instance().destroy();
		
		//Terminate thread
		//Main.Midlet.notifyDestroyed();
    }
	
	//The only instance
	private static J2MEStateManager s_Instance = null;
	
	//Data
	protected boolean	m_Paused;
	protected boolean	m_Initialized;
	protected Vector	m_StateList;
	protected int		m_RemovalDepth;
    
}
