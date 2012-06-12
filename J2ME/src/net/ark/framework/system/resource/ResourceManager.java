package net.ark.framework.system.resource;

import org.json.me.JSONObject;

import net.ark.framework.system.resource.j2me.J2MEResourceManager;

public abstract class ResourceManager {	
	protected ResourceManager() {
		//Initialize
		m_Added		= 0;
		m_Loaded	= 0;
		m_Started	= false;
		m_Finished	= false;
	}

	public synchronized static ResourceManager instance() {
		//Return the corresponding manager
		return J2MEResourceManager.instance();
	}
	
	//Load resource
	public abstract void addShadow();
	public abstract void addNumber(int font);
	public abstract void addBGM(String file);
	public abstract void addSFX(String file);
	public abstract void addDarkBackground();
	public abstract void addJSON(String file);
	public abstract void addFont(String file);
	public abstract void addImage(String file);
	public abstract void addImages(String file, int column, int row);
	public abstract void addString(int lang);

	//Removal
	public abstract void destroyResource(String resource);
	
	//Progress
	public boolean isFinished() { 	return m_Finished;					}
	public boolean isLoading() 	{ 	return m_Started && !m_Finished;	}
	
	//Resources
	public abstract Object getImage(String file);
	public abstract Object[] getImages(String file);
	public abstract JSONObject getJSON(String file);
	public abstract JSONObject readJSON(String file);
	
	public float getProgress() {
		//Special case
		if (isFinished())	return 1.0f;
		if (m_Added <= 0) 	return 0;
		
		//Return percentage
		return (float)m_Loaded / (float)m_Added;
	}
	
	public void start() {
		//Skip if loading or no loadable
		if (isLoading())	return;
		if (m_Added <= 0) 	return;
		
		//Start
		m_Loaded	= 0;
		m_Started 	= true;
		m_Finished	= false;
	}	

	public abstract void destroy(JSONObject resources);
	public abstract void reload();
	
	public void update() {
		//If loading
		if (isLoading()) {			
			//Next
			m_Loaded++;
			if (m_Loaded >= m_Added) m_Finished = true;
		}
	}
	
	//Loading progress
	protected int		m_Added;
	protected int 		m_Loaded;
	protected boolean 	m_Started;
	protected boolean 	m_Finished;
}
