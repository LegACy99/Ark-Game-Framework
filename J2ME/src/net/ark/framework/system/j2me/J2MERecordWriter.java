/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ark.framework.system.j2me;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;
import net.ark.framework.system.RecordWriter;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 *
 * @author LegACy
 */
public class J2MERecordWriter extends RecordWriter {
	public J2MERecordWriter() {
		//Initialize
		m_Store = null;
		initialize();
	}
	
	protected void initialize() {
		try {
			//Get store
			m_Store = RecordStore.openRecordStore(STORE_NAME, false);
		} catch (RecordStoreNotFoundException ex) {
		} catch (RecordStoreFullException ex) {
		} catch (RecordStoreException ex) {}
	}

	public void save(JSONObject json) {
		//Skip if no store or object
		if (m_Store == null) initialize();
		if (m_Store == null || json == null) return;
			
		//Get json string
		String JSONString = json.toString();
		if (JSONString == null) JSONString = new JSONObject().toString();

		try {
			//Store in store
			byte[] Data = JSONString.getBytes();
			m_Store.setRecord(RECORD_ID, Data, 0, Data.length);
		} catch (RecordStoreNotOpenException ex) {
		} catch (InvalidRecordIDException ex) {
		} catch (RecordStoreFullException ex) {
		} catch (RecordStoreException ex) {
		} finally {
			try {
				//Close
				m_Store.closeRecordStore();
				m_Store = null;
			} catch (RecordStoreNotOpenException e) {
			} catch (RecordStoreException e) {}
		}
	}

	public JSONObject load() {
		//Initialize
		JSONObject Result = null;
		
		//If store exist
		if (m_Store == null) initialize();
		if (m_Store != null) {
			try {
				//Read Data
				byte[] Data = m_Store.getRecord(RECORD_ID);
				StringBuffer Buffer	= new StringBuffer();
				for (int i = 0; i < Data.length; i++) Buffer.append((char)Data[i]);

				//Create json
				Result = new JSONObject(Buffer.toString());
			} catch (RecordStoreNotOpenException ex) {
			} catch (InvalidRecordIDException ex) {
			} catch (RecordStoreException ex) {
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
		
		//Return
		return Result;
	}
	
	//Constants
	protected static final int RECORD_ID	= 1;
	protected final String STORE_NAME		= "Records";
	
	//Record system
	RecordStore m_Store;
}
