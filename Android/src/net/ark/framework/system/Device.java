package net.ark.framework.system;

import net.ark.framework.system.android.AndroidDevice;
import net.ark.framework.system.android.input.AccelerometerInfo;
import net.ark.framework.system.android.input.TouchInfo;

public abstract class Device {
	protected Device() {
		//Initialize;
		m_Row		= 0;
		m_Scale		= 1;
		m_Width		= 0;
		m_Height	= 0;
		m_Column	= 0;
		m_System	= null;
		
		//Intialize input
		m_Accelerometer	= null;
		m_Touches 		= new TouchInfo[10];
		m_Keys			= new int[] {	0	};
		for (int i = 0; i < m_Touches.length; i++) m_Touches[i] = null;
	}

	public synchronized static AndroidDevice instance() {
		//Return the correct instance
		return AndroidDevice.instance();
	}
	
	public void setSystem(System system) {
		//Set
		m_System = system;
	}
	
    //Accessors
	public int getRow() 						{	return m_Row;			}
	public int getColumn()						{	return m_Column;		}
	public int[] getKeys()						{	return m_Keys;			}
	public float getScale()						{	return m_Scale;			}
	public float getWidth() 					{	return m_Width;			}
	public float getHeight()					{	return m_Height;		}
	public TouchInfo[] getTouches()				{	return m_Touches;		}
	public AccelerometerInfo getAccelerometer()	{	return m_Accelerometer;	}
	
	//Display
	protected float m_Scale;
	protected float m_Width;
	protected float m_Height;
	protected int 	m_Column;
	protected int 	m_Row;
	
	//Input
	protected int[]				m_Keys;
	protected TouchInfo[]		m_Touches;
	protected AccelerometerInfo	m_Accelerometer;
	protected System			m_System;
}
