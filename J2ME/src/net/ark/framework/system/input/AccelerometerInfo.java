/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ark.framework.system.input;

/**
 *
 * @author LegACy
 */
public class AccelerometerInfo {
	public AccelerometerInfo() {
		//Initialize
		m_AccelX 		= 0;
		m_AccelY 		= 0;
		m_AccelZ 		= 0;
		m_ReferenceX	= 0;
		m_ReferenceY	= 0;
		m_ReferenceZ	= 0;
	}
	
	//Accessors
	public float getX()				{	return m_AccelX - m_ReferenceX;	}
	public float getY()				{	return m_AccelY - m_ReferenceY;	}
	public float getZ()				{	return m_AccelZ - m_ReferenceZ;	}
	public float getReferenceX()	{	return m_ReferenceX;			}
	public float getReferenceY()	{	return m_ReferenceY;			}
	public float getReferenceZ()	{	return m_ReferenceZ;			}
	
	public int getTiltDirection(boolean horizontal) {
		//Initialize
		int Tilt = TILT_UNKNOWN;
		
		//Find standing axis
		if (m_ReferenceX > STAND_LIMIT) {
			//If horizontal
			if (horizontal) {
				//Check Y
				if (getY() < 0) Tilt = TILT_LEFT;
				else			Tilt = TILT_RIGHT;
			} else {
				//Check Z
				if (getZ() < 0) Tilt = TILT_DOWN;
				else			Tilt = TILT_UP;
			}
		} else if (m_ReferenceY > STAND_LIMIT) {
			//If horizontal
			if (horizontal) {
				//Check X
				if (getX() < 0) Tilt = TILT_RIGHT;
				else			Tilt = TILT_LEFT;
			} else {
				//Check Z
				if (getZ() < 0) Tilt = TILT_DOWN;
				else			Tilt = TILT_UP;
			}
		} else if (m_ReferenceX < -STAND_LIMIT) {
			//If horizontal
			if (horizontal) {
				//Check Y
				if (getY() < 0) Tilt = TILT_RIGHT;
				else			Tilt = TILT_LEFT;
			} else {
				//Check Z
				if (getZ() < 0) Tilt = TILT_UP;
				else			Tilt = TILT_DOWN;
			}
		} else if (m_ReferenceY < -STAND_LIMIT) {
			//If horizontal
			if (horizontal) {
				//Check X
				if (getX() < 0) Tilt = TILT_LEFT;
				else			Tilt = TILT_RIGHT;
			} else {
				//Check Z
				if (getZ() < 0) Tilt = TILT_DOWN;
				else			Tilt = TILT_UP;
			}
		}
		
		//Return
		return Tilt;
	}
	
	public float getTilt(boolean horizontal) {
		return getTilt(getTiltDirection(horizontal));
	}
	
	public float getTilt(int direction) {
		//Initialize
		float Tilt = 0;
		
		//If unknown
		if (direction == TILT_UNKNOWN) {
			//Find the bigger one
			Tilt = getY();
			if (Math.abs(getX()) > Math.abs(Tilt)) Tilt = getX();
		} else {
			//Check standing
			if (m_ReferenceX > STAND_LIMIT) {
				//Check direction
				if (direction == TILT_LEFT || direction == TILT_RIGHT) 	Tilt = getY();
				else 													Tilt = -getZ();
			} else if (m_ReferenceY > STAND_LIMIT) {
				//Check direction
				if (direction == TILT_LEFT || direction == TILT_RIGHT) 	Tilt = -getX();
				else 													Tilt = -getZ();
			} else if (m_ReferenceX < -STAND_LIMIT) {
				//Check direction
				if (direction == TILT_LEFT || direction == TILT_RIGHT) 	Tilt = -getY();
				else 													Tilt = getZ();
			} else if (m_ReferenceY < -STAND_LIMIT) {
				//Check direction
				if (direction == TILT_LEFT || direction == TILT_RIGHT) 	Tilt = getX();
				else 													Tilt = getZ();
			}
		}
		
		//Return
		return Tilt;
	}
	
	//Setter
	public void setAcceleration(float x, float y, float z) {
		//Set
		m_AccelX = x;
		m_AccelY = y;
		m_AccelZ = z;
	}
	
	public void setReference() {
		//Save
		m_ReferenceX = m_AccelX;
		m_ReferenceY = m_AccelY;
		m_ReferenceZ = m_AccelZ;
	}	
	
	//Constants
	public final int TILT_UP		= 0;
	public final int TILT_DOWN		= 1;
	public final int TILT_LEFT		= 2;
	public final int TILT_RIGHT		= 3;
	public final int TILT_UNKNOWN	= -1;
	public final float STAND_LIMIT	= 4f;
	
	//Data
	protected float m_AccelX;
	protected float m_AccelY;
	protected float m_AccelZ;
	protected float m_ReferenceX;
	protected float m_ReferenceY;
	protected float m_ReferenceZ;
}
