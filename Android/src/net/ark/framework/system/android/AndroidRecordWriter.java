package net.ark.framework.system.android;

import org.json.JSONException;
import org.json.JSONObject;

import net.ark.framework.system.RecordWriter;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AndroidRecordWriter extends RecordWriter {
	public AndroidRecordWriter() {
		//Create store
		m_Store = s_Activity.getSharedPreferences(STORE_NAME, Context.MODE_WORLD_READABLE);
	}
	
	public static void setActivity(Activity activity) {
		//Set activity
		s_Activity = activity;
	}

	@Override
	public void save(JSONObject json) {
		//Skip if no store or object
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
	
	//Activity
	protected static Activity s_Activity;
	
	//Record system
	SharedPreferences m_Store;
}
