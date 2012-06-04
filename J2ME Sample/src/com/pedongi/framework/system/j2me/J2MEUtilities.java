/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pedongi.framework.system.j2me;

import com.pedongi.framework.system.Utilities;
import java.util.Random;

/**
 *
 * @author LegACy
 */
public class J2MEUtilities extends Utilities {
    protected J2MEUtilities() {
		//System
        m_Back  = -7;
        m_Menu  = -6;
		m_FPS   = 30;
		m_Wait  = 160;
		
		//Gameplay
		m_RepeatX		= 1;
		m_RepeatY		= 1;
		m_TileWidth		= 32;
		m_TileHeight	= 32;
		
		//Create random
		m_Random = new Random();
    }
	
	public static synchronized Utilities instance() {
		//Create if null
		if (s_Instance == null) s_Instance = new J2MEUtilities();
		return s_Instance;
	}
    
	//Display
	public int getRow()			{	return J2MEDevice.instance().getRow();		}
	public int getColumn()		{	return J2MEDevice.instance().getColumn();	}
	public float getHeight() 	{	return J2MEDevice.instance().getHeight();	}
	public float getWidth() 	{	return J2MEDevice.instance().getWidth();	}
	public float getScale()		{	return J2MEDevice.instance().getScale();	}
	
	public int getRandom(int from, int to) {
		//Return random
		if (from + 1 >= to) return 0;
		else 				return m_Random.nextInt(to - from) + from;
	}
    
	//The only instance
	private static J2MEUtilities s_Instance = null;
	
	//Random
	protected Random m_Random;    
}
