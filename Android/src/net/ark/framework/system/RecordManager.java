package net.ark.framework.system;

import org.json.JSONObject;

import net.ark.framework.system.android.AndroidRecordManager;

public abstract class RecordManager {
	protected RecordManager() {
	}
	
	public synchronized static RecordManager instance() {
		//Return the corresponding class
		return AndroidRecordManager.instance();
	}
	
	protected void initialize() {
	}
	
	//Save
	public String createSave() {
		//Initialize
		JSONObject Result = new JSONObject();
		
		//Return
		return Result.toString();
	}
	
	public void readSave(String save) {
		//Skip if null
		if (save == null) return;
	}
	
	//Abstract functions
	public abstract void destroy();
	public abstract void save();
	public abstract void load();
	
	//Key constanst
	protected static final String KEY_NAME		= "Name";
	protected static final String KEY_MONEY		= "Money";
	protected static final String KEY_AVATAR	= "Avatar";
	protected static final String KEY_PARTS		= "Parts";
	protected static final String KEY_SLOTS		= "Slots";
	
	//Data
}
