package net.ark.framework.system.android;

import java.util.Random;

import android.view.KeyEvent;

import net.ark.framework.system.Device;
import net.ark.framework.system.Utilities;

public class AndroidUtilities extends Utilities {
    protected AndroidUtilities() {
		//System
        m_Back  = KeyEvent.KEYCODE_BACK;
        m_Menu  = -1;
		m_FPS   = 30;
		m_Wait  = 160;
		
		//Create random
		m_Random = new Random();
    }
	
	public static synchronized Utilities instance() {
		//Create if null
		if (s_Instance == null) s_Instance = new AndroidUtilities();
		return s_Instance;
	}

	@Override public int getRow() 		{	return Device.instance().getRow();										}
	@Override public int getColumn() 	{	return Device.instance().getColumn();									}
	@Override public float getScale() 	{	return Device.instance().getScale();									}
	@Override public float getWidth() 	{	return Device.instance().getWidth() / Device.instance().getScale();		}
	@Override public float getHeight() 	{	return Device.instance().getHeight() / Device.instance().getScale();	}

	@Override
	public int getRandom(int from, int to) {
		//Return random
		if (from + 1 >= to) return 0;
		else 				return m_Random.nextInt(to - from) + from;
	}
    
	//The only instance
	private static AndroidUtilities s_Instance = null;
	
	//Random
	protected Random m_Random;
}
