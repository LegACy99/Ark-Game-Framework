package com.pedongi.framework.system.j2me;

import com.pedongi.framework.system.StringManager;
import com.pedongi.framework.system.Utilities;
import com.pedongi.framework.system.resource.ResourceManager;
import java.util.Hashtable;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class J2MEStringManager extends StringManager {
	//Inaccessible constructor
	protected J2MEStringManager() {
		//Super
		super();

		//Initialize
		m_StringTable	= new Hashtable();
	}
	
	public static StringManager instance() {
		//Create if null
		if (s_Instance == null) s_Instance = new J2MEStringManager();
		
		//Return the only instances
		return s_Instance;
	}
	
	public String getString(String key) {		
		//Find string in table
		return (String) m_StringTable.get(key);		
	}
	
	public void loadString(Language lang) {
		//Skip if no lang
		if (lang == null) return;
		
		//Reset table
		m_StringTable = new Hashtable();
		
		//Create JSON object
		String Filename = Utilities.STRING_FOLDER + "/" + lang.getName().toLowerCase() + ".json";
		JSONObject JSON	= ResourceManager.instance().readJSON(Filename);
		
		try {			
			//Get data
			m_Tag 				= JSON.getString(KEY_TAG);
			JSONArray Strings	= JSON.getJSONArray(KEY_STRINGS);
			
			//For each element
			for (int i = 0; i < Strings.length(); i++) {
				//Get data
				JSONObject StringObject = Strings.getJSONObject(i);
				m_StringTable.put(StringObject.get(KEY_IDENTIFIER), StringObject.get(KEY_STRING));
			}
		} catch (JSONException e) {}
		
		//Save language
		m_Language = lang;
	}

	//Constants
	protected static final String KEY_TAG			= "Tag";
	protected static final String KEY_STRING		= "String";
	protected static final String KEY_STRINGS		= "Strings";
	protected static final String KEY_IDENTIFIER	= "Key";
	
	//Instance
	protected static StringManager s_Instance = null;
	
	//Data
	protected Hashtable m_StringTable;
}
