package net.ark.framework.system.android;

import net.ark.framework.system.RecordWriter;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AndroidRecordWriter extends RecordWriter {
	public AndroidRecordWriter() {
		//Initialize
		m_Store = null;
		initialize();
	}
	
	protected void initialize() {
		if (AndroidDevice.MainActivity != null) m_Store = AndroidDevice.MainActivity.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE);
	}

	@Override
	public void save(JSONObject json) {
		//Skip if no store or object
		if (m_Store == null) initialize();
		if (m_Store == null || json == null) return;
		
		//Save
		Editor PreferenceEditor = m_Store.edit();
		PreferenceEditor.putString(STORE_NAME, json.toString());
		PreferenceEditor.commit();
	}

	@Override
	public JSONObject load() {
		//Initialize
		JSONObject Result = null;
		
		try {
			//If store exist
			if (m_Store == null) initialize();
			if (m_Store != null && m_Store.contains(STORE_NAME)) {
				//Get string
				JSONObject Loaded 	= new JSONObject(m_Store.getString(STORE_NAME, null));
				Result 				= Loaded;
			}
		} catch (JSONException e) {}
		
		//Return
		return Result;
	}
	
	//Constants
	protected final String STORE_NAME = "Records";
	
	//Record system
	SharedPreferences m_Store;
}
