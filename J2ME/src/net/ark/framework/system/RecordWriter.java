/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ark.framework.system;

import net.ark.framework.system.j2me.J2MERecordWriter;
import org.json.me.JSONObject;

/**
 *
 * @author LegACy
 */
public abstract class RecordWriter {
	public static RecordWriter create() {
		//Create
		return new J2MERecordWriter();
	}
	
	//Abstract functions
	public abstract void save(JSONObject json);
	public abstract JSONObject load();
}
