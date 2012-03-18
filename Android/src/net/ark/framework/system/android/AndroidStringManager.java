package net.ark.framework.system.android;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.ark.framework.system.StringManager;
import net.ark.framework.system.Utilities;
import net.ark.framework.system.resource.ResourceManager;

public class AndroidStringManager extends StringManager {
	//Inaccessible constructor
	protected AndroidStringManager() {
		//Super
		super();
		
		//Initialize
		m_StringTable	= new HashMap<String, String>();
	}

	
	public static StringManager instance() {
		//Create if null
		if (s_Instance == null) s_Instance = new AndroidStringManager();
		
		//Return the only instances
		return s_Instance;
	}
	
	@Override
	public String getString(String key) {		
		//Find string in table
		return (String) m_StringTable.get(key);		
	}
	
	@Override
	public void loadString(Language lang) {
		//Skip if no lang
		if (lang == null) return;
		
		//Reset table
		m_StringTable = new HashMap<String, String>();
		
		//Create JSON object
		String Filename = Utilities.STRING_FOLDER + lang.getName().toLowerCase() + ".json";
		JSONObject JSON	= ResourceManager.instance().readJSON(Filename);
		
		try {			
			//Get data
			m_Tag 				= JSON.getString(KEY_TAG);
			JSONArray Strings	= JSON.getJSONArray(KEY_STRINGS);
			
			//For each element
			for (int i = 0; i < Strings.length(); i++) {
				//Get data
				JSONObject StringObject = Strings.getJSONObject(i);
				m_StringTable.put(StringObject.getString(KEY_IDENTIFIER), StringObject.getString(KEY_STRING));
			}
		} catch (JSONException e) {}
		
		//Save language
		m_Language = lang;
	}
	
	//Constants
	protected final String KEY_TAG			= "Tag";
	protected final String KEY_STRING		= "String";
	protected final String KEY_STRINGS		= "Strings";
	protected final String KEY_IDENTIFIER	= "Key";
	
	//Instance
	protected static StringManager s_Instance = null;
	
	//String table
	protected HashMap<String, String> m_StringTable;
}
