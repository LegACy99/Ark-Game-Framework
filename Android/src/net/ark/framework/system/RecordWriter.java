package net.ark.framework.system;

import net.ark.framework.system.android.AndroidRecordWriter;

import org.json.JSONObject;

public abstract class RecordWriter {
	public static RecordWriter create() {
		//Create
		return new AndroidRecordWriter();
	}
	
	//Abstract functions
	public abstract void save(JSONObject json);
	public abstract JSONObject load();
}
