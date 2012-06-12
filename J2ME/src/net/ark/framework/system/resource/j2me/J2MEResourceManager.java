/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ark.framework.system.resource.j2me;

import net.ark.framework.system.resource.Loadable;
import net.ark.framework.system.resource.ResourceManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Vector;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 *
 * @author LegACy
 */
public class J2MEResourceManager extends ResourceManager {
    protected J2MEResourceManager() {
        //Super
		super();
		
		//Initialize
		m_Loadables	= new Vector();
		m_Resources	= new Hashtable();
		
		//Load
		start();
		update();
    }
	
	public synchronized static ResourceManager instance() {
		//Create state manager if doesn't exist
		if (s_Instance == null) s_Instance = new J2MEResourceManager();
		return s_Instance;
	}

	//Resource
	public void addBGM(String file) 						{	if (!isLoading()) m_Loadables.addElement(Loadable.createBGM(file));              		}
	public void addSFX(String file) 						{	if (!isLoading()) m_Loadables.addElement(Loadable.createSFX(file));      				}
	public void addJSON(String file) 						{	if (!isLoading()) m_Loadables.addElement(Loadable.createJSON(file));                    }
	public void addFont(String file)						{	if (!isLoading()) m_Loadables.addElement(Loadable.createFont(file));					}
	public void addImage(String file) 						{	if (!isLoading()) m_Loadables.addElement(Loadable.createImage(file));					}
	public void addImages(String file, int column, int row)	{	if (!isLoading()) m_Loadables.addElement(J2MELoadableImages.create(file, column, row));	}
	public void addDarkBackground()							{	if (!isLoading()) m_Loadables.addElement(J2MELoadableDark.createDarkBackground());		}
	public void addShadow()									{	if (!isLoading()) m_Loadables.addElement(J2MELoadableDark.createShadow());				}
	public void addString(int lang)							{	if (!isLoading()) m_Loadables.addElement(Loadable.createLanguage(lang));				}
	public void addNumber(int font)							{	if (!isLoading()) m_Loadables.addElement(Loadable.createNumber(font));					}

	//Remove resource
	public void destroyResource(String name) {
		//Remove from table
		m_Resources.remove(name);
	}

	//Resources
	public Object getImage(String file)		{	return m_Resources.get(file);				}
	public Object[] getImages(String file)	{	return (Object[])m_Resources.get(file);		}
	public JSONObject getJSON(String file)	{	return (JSONObject)m_Resources.get(file);	}
	
	public JSONObject readJSON(String file) {
		//Initialize
		JSONObject Result = new JSONObject();

		//Open JSON file
		InputStream	Input	= getClass().getResourceAsStream(file);
		StringBuffer Buffer	= new StringBuffer();
		try {
			//Read a byte
			int Char = Input.read();
			while (Char != -1) {
				//Add to buffer
				Buffer.append((char)Char);
				Char = Input.read();
			}

			//Create JSON
			Result = new JSONObject(Buffer.toString());

			//Close input
			Input.close();
		} catch (JSONException e) {
		} catch (IOException e) {}

		//Return
		return Result;
	}
	
	public void start() {
		//Set added
		m_Added = m_Loadables.size();

		//Super
		super.start();
	}

	public void reload()						{	/*Do nothing*/	}
	public void destroy(JSONObject resources)	{	/*Do nothing*/	}
	
	public void update() {
		//If loading
		if (isLoading()) {
			//Get loadable
			Loadable Current = (Loadable)m_Loadables.elementAt(m_Loaded);
			
			//If doesn't exist
			if (!m_Resources.containsKey(Current.getName())) {
				//Load
				Object Loaded = Current.load();
				if (Loaded != null) m_Resources.put(Current.getName(), Loaded);
			}
			
			//Update
			super.update();
			
			//If finished
			if (m_Finished) {
				//Reset
				m_Added = 0;
				m_Loadables.removeAllElements();	
			}
		}
	}
	
	//The only instance
	private static ResourceManager s_Instance = null;
	
	//Resources
	protected Vector    m_Loadables;
	protected Hashtable m_Resources;
}
