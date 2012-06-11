/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ark.framework.system.j2me;

import net.ark.framework.system.RecordManager;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 *
 * @author LegACy
 */
public class J2MERecordManager extends RecordManager {
    protected J2MERecordManager() {
		//Super
		super();
		
        //Initialize
		m_Store	= null;
		
		try {			
			//Open recordstore
			m_Store = RecordStore.openRecordStore(RecordManager.STORE_NAME, true);
			//m_Store.setMode(RecordStore.AUTHMODE_ANY, false);
			
			//If no record yet, save it
			if (m_Store.getNumRecords() <= 0) {
				//Add and close
				m_Store.addRecord(null, 0, 0);
				m_Store.closeRecordStore();
				m_Store = null;
				
				//Save data
				save();
			} else {
				//If there's a record, close it again
				m_Store.closeRecordStore();
				m_Store = null;
			}
		} catch (RecordStoreNotFoundException e) {
		} catch (RecordStoreFullException e) {
		} catch (RecordStoreException e) {}
    }

	public synchronized static RecordManager instance() {
		//Create state manager if doesn't exist
		if (s_Instance == null) s_Instance = new J2MERecordManager();
		return s_Instance;
	}

	public void destroy() {
		//Skip if no record store
		if (m_Store == null) return;
		
		try {
			//Close record store
			m_Store.closeRecordStore();
		} catch (RecordStoreNotOpenException e) {
		} catch (RecordStoreException e) {}
	}
	
	public void save() {
		try {
			//Get store
			m_Store = RecordStore.openRecordStore(RecordManager.STORE_NAME, false);
			
			//Create achievement array
			JSONArray AchievementArray = new JSONArray();
			for (int i = 0; i < m_Achievements.length; i++) AchievementArray.put(m_Achievements[i]); 
			
			//Create upgrades array
			JSONArray UpgradeArray = new JSONArray();
			for (int i = 0; i < m_Upgrades.length; i++) UpgradeArray.put(m_Upgrades[i]); 
			
			//Create json
			JSONObject Record = new JSONObject();
			Record.put(KEY_ACHIEVEMENTS, AchievementArray);
			Record.put(KEY_UPGRADES, UpgradeArray);
			Record.put(KEY_HIGHSCORE, m_Highscore);
			Record.put(KEY_INITIALIZED, m_Played);
			Record.put(KEY_EXPERIENCE, m_Exp);
			Record.put(KEY_STATS, m_Stats);
			Record.put(KEY_LEVEL, m_Level);
			Record.put(KEY_MUTE, m_Mute);
			
			//Get json string
			String JSONString = Record.toString();
			if (JSONString == null) JSONString = new JSONObject().toString();
			
			//Store in store
			byte[] Data = JSONString.getBytes();
			m_Store.setRecord(RECORD_ID, Data, 0, Data.length);
		} catch (RecordStoreNotOpenException e) {
		} catch (RecordStoreFullException e) {
		} catch (RecordStoreException e) {
		} catch (JSONException e) {
		} finally {
			try {
				//Close
				m_Store.closeRecordStore();
				m_Store = null;
			} catch (RecordStoreNotOpenException e) {
			} catch (RecordStoreException e) {}
		}
	}
	
	public void load() {
		try {
			//Get store
			m_Store = RecordStore.openRecordStore(RecordManager.STORE_NAME, false);

			//Read Data;
			byte[] Data 		= m_Store.getRecord(RECORD_ID);
			StringBuffer Buffer	= new StringBuffer();
			for (int i = 0; i < Data.length; i++) Buffer.append((char)Data[i]);
			String JSONText = Buffer.toString();
			
			//Create JSON
			JSONObject JSON = new JSONObject(JSONText);
			
			//If not null
			if (JSON != null) {
				try {
					//Read json
					m_Level		= JSON.getInt(KEY_LEVEL);
					m_Stats		= JSON.getInt(KEY_STATS);
					m_Exp		= JSON.getInt(KEY_EXPERIENCE); 
					m_Highscore	= (float) JSON.getDouble(KEY_HIGHSCORE);
					m_Played	= JSON.getBoolean(KEY_INITIALIZED);
					m_Mute		= JSON.getBoolean(KEY_MUTE);
					
					//Get upgrades
					JSONArray UpgradeArray	= JSON.getJSONArray(KEY_UPGRADES);
					m_Upgrades				= new int[UpgradeArray.length()];
					for (int i = 0; i < m_Upgrades.length; i++) m_Upgrades[i] = UpgradeArray.getInt(i);
					
					//Get achievements
					JSONArray AchivementArray	= JSON.getJSONArray(KEY_ACHIEVEMENTS);
					m_Achievements				= new int[AchivementArray.length()];
					for (int i = 0; i < m_Achievements.length; i++) m_Achievements[i] = AchivementArray.getInt(i);
				} catch (JSONException e) {}
			}	
		} catch (RecordStoreNotOpenException e1) {
		} catch (InvalidRecordIDException e1) {
		} catch (RecordStoreException e1) {
		} catch (JSONException e) {
		} finally {
			try {
				//Close
				m_Store.closeRecordStore();
				m_Store = null;
			} catch (RecordStoreNotOpenException e) {
			} catch (RecordStoreException e) {}
		}
	}
	
	//Constants
	protected static final int RECORD_ID			= 1;
	protected static final String KEY_MUTE			= "Mute";
	protected static final String KEY_INITIALIZED	= "Init";
	protected static final String KEY_ACHIEVEMENTS	= "Achievements";
	protected static final String KEY_EXPERIENCE	= "Experience";
	protected static final String KEY_HIGHSCORE		= "Highscore";
	protected static final String KEY_UPGRADES		= "Upgrades";
	protected static final String KEY_LEVEL			= "Level";
	protected static final String KEY_STATS			= "Stats";
	
	//The only instance
	private static RecordManager s_Instance = null;
	
	//Record system
	protected RecordStore m_Store;
}
