package net.ark.framework.system.input;

public class TouchInfo {
	public TouchInfo() {
		//Reset
		reset();
	}

	public void reset() {
		//Reset data
		m_X			= -1;
		m_Y			= -1;
		m_LastX		= -1;
		m_LastY		= -1;
		m_StartX	= -1;
		m_StartY	= -1;
		m_Swipe	= 0;

		//Reset state
		m_Pressed		= false;
		m_WasPressed	= false;
	}

	public float getStartX()	{	return m_StartX;		}
	public float getStartY()	{	return m_StartY;		}
	public float getCurrentX()	{	return m_X;				}
	public float getCurrentY()	{	return m_Y;				}
	public boolean isPressed()	{	return m_Pressed;		}
	public boolean wasPressed()	{	return m_WasPressed;	}

	public float getOffsetX() {
		//Get offset
		float Offset 	= m_X - m_LastX;
		m_LastX			= m_X;

		//Return
		return Offset;
	}

	public float getOffsetY() {
		//Get offset
		float Offset 	= m_Y - m_LastY;
		m_LastY			= m_Y;

		//Return
		return Offset;
	}

	public int getSwipe() {
		//Extract swipe
		int Swipe = m_Swipe;
		m_Swipe = 0;

		//Return
		return Swipe;
	}

	public void pressed(float x, float y) {
		//Skip if already pressed
		if (m_Pressed) return;

		//Set data
		m_X	= x;
		m_Y	= y;

		//Set other data
		m_LastX		= m_X;
		m_LastY		= m_Y;
		m_StartX 	= m_X;
		m_StartY 	= m_Y;

		//Pressed
		m_Pressed	= true;
	}

	public void dragged(float x, float y) {
		//Skip if not pressed
		if (!m_Pressed) return;

		//Was pressed
		m_WasPressed = true;

		//Set current position
		m_X	= x;
		m_Y	= y;
	}

	public void released(float x, float y) {
		//Skip if not pressed
		if (!m_Pressed) return;

		//Set current position
		m_X	= x;
		m_Y	= y;

		//Released
		m_Pressed = false;
	}

	public void removed() {
		if (m_Pressed) m_Pressed = false;
		m_WasPressed = false;
	}

	public void addSwipe(float x1, float y1, float x2, float y2) {
		//Validate swipe
		float DistanceX = (x2 - x1);
		float DistanceY = (y2 - y1);
		if ((DistanceX * DistanceX) + (DistanceY * DistanceY) < (MIN_DISTANCE * MIN_DISTANCE)) return;

		//Initialize
		int Swipe = NO_SWIPE;

		//Get Ttan
		double TanXY = Math.abs(DistanceX) / Math.abs(DistanceY);
		double TanYX = Math.abs(DistanceY) / Math.abs(DistanceX);

		//If up
		if (DistanceY <= 0) {
			//Is north?
			if (TanXY <= TAN_22) Swipe = SWIPE_NORTH;
			else if (TanYX > TAN_22) {
				//Check diagonal
				if (DistanceX >= 0) Swipe = SWIPE_NORTHEAST;
				else 				Swipe = SWIPE_NORTHWEST;
			} else {
				//Check horizontal
				if (DistanceX >= 0) Swipe = SWIPE_EAST;
				else				Swipe = SWIPE_WEST;
			}
		} else {
			//Is south?
			if (TanXY <= TAN_22) Swipe = SWIPE_SOUTH;
			else if (TanYX > TAN_22) {
				//Check diagonal
				if (DistanceX >= 0) Swipe = SWIPE_SOUTHEAST;
				else 				Swipe = SWIPE_SOUTHWEST;
			} else {
				//Check horizontal
				if (DistanceX >= 0) Swipe = SWIPE_EAST;
				else				Swipe = SWIPE_WEST;
			}
		}

		//If direction got
		if (Swipe != NO_SWIPE) m_Swipe = Swipe;
	}

	//Constants
	public static final int NO_SWIPE	 		= 0;
	public static final int SWIPE_NORTH 		= 1;
	public static final int SWIPE_NORTHEAST 	= 2;
	public static final int SWIPE_EAST 			= 3;
	public static final int SWIPE_SOUTHEAST 	= 4;
	public static final int SWIPE_SOUTH 		= 5;
	public static final int SWIPE_SOUTHWEST 	= 6;
	public static final int SWIPE_WEST 			= 7;
	public static final int SWIPE_NORTHWEST 	= 8;
	protected static final float MIN_DISTANCE	= 200;
	protected static final double TAN_22		= Math.tan(22.5);

	//properties
	protected float	m_X;
	protected float m_Y;
	protected float m_LastX;
	protected float m_LastY;
	protected float	m_StartX;
	protected float	m_StartY;

	//State
	protected boolean m_Pressed;
	protected boolean m_WasPressed;

	//Gesture
	protected int m_Swipe;
}
