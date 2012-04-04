package com.ark.example.system;

import org.json.JSONObject;

import net.ark.framework.system.RecordWriter;

public class RecordManager {
	protected RecordManager() {
		//Create writer
		m_Writer = RecordWriter.create();
	}

	public synchronized static RecordManager instance() {
		//Create state manager if doesn't exist
		if (s_Instance == null) s_Instance = new RecordManager();
		return s_Instance;
	}
	
	public void save() {
		//Create json
		JSONObject JSON = new JSONObject();
		
		//Save json
		m_Writer.save(JSON);
	}
	
	public void load() {
		//Get record
		JSONObject Result = m_Writer.load();
		if (Result != null) {
			//Read json
		}
	}
	
	//The only instance
	private static RecordManager s_Instance = null;
	
	//Writer
	protected RecordWriter m_Writer;
}
