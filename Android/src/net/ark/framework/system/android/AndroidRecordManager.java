package net.ark.framework.system.android;

import com.ark.example.Main;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import net.ark.framework.system.RecordManager;

public class AndroidRecordManager extends RecordManager {
	protected AndroidRecordManager() {
		//Super
		super();
		
		//Initialize
		m_Store = Main.instance().getSharedPreferences(STORE_NAME, Context.MODE_WORLD_READABLE);
	}

	public synchronized static RecordManager instance() {
		//Create state manager if doesn't exist
		if (s_Instance == null) s_Instance = new AndroidRecordManager();
		return s_Instance;
	}

	@Override
	public void destroy() {
		//Not needed on Android
	}

	@Override
	public void save() {
		//Skip if no store
		if (m_Store == null) return;
		
		//Save
		Editor PreferenceEditor = m_Store.edit();
		PreferenceEditor.putString(STORE_NAME, createSave());
		PreferenceEditor.commit();
	}

	@Override
	public void load() {
		//Skip if no store
		if (m_Store == null) return;
		
		//Read data
		readSave(m_Store.getString(STORE_NAME, null));
	}
	
	//Constants
	protected final String STORE_NAME = "Records"; 
	
	//The only instance
	private static RecordManager s_Instance = null;
	
	//Record system
	SharedPreferences m_Store;
}
