/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ark.framework.system;

import net.ark.framework.system.input.AccelerometerInfo;
import net.ark.framework.system.input.TouchInfo;
import net.ark.framework.system.j2me.J2MEDevice;
import javax.microedition.lcdui.game.GameCanvas;

/**
 *
 * @author LegACy
 */
public abstract class Device extends GameCanvas {
	protected Device() {
		//Super
		super(false);

		//Initialize;
		m_Row		= 0;
		m_Column	= 0;
		m_Scale		= 1;

		//Intialize input
		m_Keys			= null;
		m_Touches 		= null;
		m_Accelerometer	= null;
	}

	public synchronized static Device instance() {
		//Return the correct instance
		return J2MEDevice.instance();
	}

    //Accessors
	public int getRow() 						{	return m_Row;			}
	public int getColumn()						{	return m_Column;		}
	public float getScale()						{	return m_Scale;			}
	//public float getWidth() 					{	return m_Width;			}
	//public float getHeight()					{	return m_Height;		}
	public TouchInfo[] getTouches()				{	return m_Touches;		}
	public AccelerometerInfo getAccelerometer()	{	return m_Accelerometer;	}

	public int[] getKeys() {
		//Copy keys
		int[] Keys = new int[m_Keys.length];
		System.arraycopy(m_Keys, 0, Keys, 0, m_Keys.length);

		//Reset
		m_Keys = new int[0];
		
		//Return
		return Keys;
	}

	//Display
	protected float m_Scale;
	protected int 	m_Column;
	protected int 	m_Row;

	//Input
	protected int[]				m_Keys;
	protected TouchInfo[]		m_Touches;
	protected AccelerometerInfo	m_Accelerometer;
}
