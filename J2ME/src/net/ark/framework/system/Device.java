/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ark.framework.system;

import net.ark.framework.system.input.AccelerometerInfo;
import net.ark.framework.system.input.TouchInfo;
import net.ark.framework.system.j2me.J2MEDevice;

/**
 *
 * @author LegACy
 */
public abstract class Device {
	protected Device() {
		//Initialize;
		m_Up		= -1;
		m_Down		= -1;
		m_Left		= -1;
		m_Fire		= -1;
		m_Menu		= -1;
		m_Back		= -1;
		m_Menu		= -1;
		m_Right		= -1;
		m_Scale		= 1;
		m_Width		= 0;
		m_Height	= 0;
		m_Column	= 0;
		m_Row		= 0;
		
		//Intialize input
		m_Accelerometer	= null;
		m_Touches 		= new TouchInfo[10];
		m_Keys			= new int[] {	0	};
		for (int i = 0; i < m_Touches.length; i++) m_Touches[i] = null;
	}

	public synchronized static Device instance() {
		//Return the correct instance
		return J2MEDevice.instance();
	}
	
    //Accessors
	public int getRow() 						{	return m_Row;			}
	public int getColumn()						{	return m_Column;		}
	public int[] getKeys()						{	return m_Keys;			}
	public float getScale()						{	return m_Scale;			}
	public float getWidth() 					{	return m_Width;			}
	public float getHeight()					{	return m_Height;		}
	public int getUpButton()					{	return m_Up;			}
	public int getDownButton()					{	return m_Down;			}
	public int getMenuButton()					{	return m_Menu;  		}
	public int getBackButton()					{	return m_Back;			}
	public int getFireButton()					{	return m_Fire;			}
	public int getLeftButton()					{	return m_Left;			}
	public int getRightButton()					{	return m_Right;			}
	public TouchInfo[] getTouches()				{	return m_Touches;		}
	public AccelerometerInfo getAccelerometer()	{	return m_Accelerometer;	}
	
	//Urls
	public abstract void openURL(String url, boolean browser, String title, String loading);
	
	//Display
	protected float m_Scale;
	protected float m_Width;
	protected float m_Height;
	protected int 	m_Column;
	protected int 	m_Row;
	
	//Constants
	protected int m_Back;
	protected int m_Menu;
	protected int m_Fire;
	protected int m_Right;
	protected int m_Left;
	protected int m_Down;
	protected int m_Up;
	
	//Input
	protected int[]				m_Keys;
	protected TouchInfo[]		m_Touches;
	protected AccelerometerInfo	m_Accelerometer;
}
