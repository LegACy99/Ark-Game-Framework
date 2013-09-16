package net.ark.framework.system.images;

import net.ark.framework.components.Croppable;
import net.ark.framework.system.images.android.AndroidSprite;

public abstract class Sprite extends Croppable {
	protected Sprite() {
		//Super
		super();
		
		//Initialize
		m_Frame		= 0;
		m_Total		= 0;
		m_Delay		= 0;
		m_Timer		= 0;
		m_Mirror	= MIRROR_NONE;
		m_Sequence	= new int[0];
		m_Animating	= false;
	}
	
	protected Sprite(int x, int y, long delay) {
		//Default
		this();
		
		//Set
		m_X	= x;
		m_Y	= y;
		setDelay(delay);
	}

	//Create sprites
	public static Sprite create(String resource, long delay) 				{	return Sprite.create(resource, 0, 0, delay);		}
	public static Sprite create(String resource, int x, int y) 				{	return Sprite.create(resource, x, y, 0);			}
	public static Sprite create(String resource, int x, int y, long delay) 	{	return new AndroidSprite(resource, x, y, delay);	}
	
	//Accessors
	public int getFrame() 				{	return m_Frame;		}
	public int getMaxFrame() 			{	return m_Total;		}
	public int getSequencePosition()	{	return m_Current;	}
	public float getHeight()		 	{	return m_Height;	}
	public float getWidth() 			{	return m_Width;		}
	
	public void setRotation(float angle) 					{ setRotation(angle, getOriginalWidth() / 2f, getOriginalHeight() / 2f); 			}
	public void setRotation(float angle, float addition) 	{ setRotation(angle, addition, getOriginalWidth() / 2f, getOriginalHeight() / 2f); 	}
	public void setRotation(float angle, float x, float y) {
		//Initialize
		int Add 	= 0;
		float Angle	= angle;
		
		//Set angle
		if (Angle == 0) {
			Add 	= -1;
			Angle 	= 1;
		}
		setRotation(Angle, Add, x, y);
	}

	public void setTint(int red, int green, int blue) { setTint(red, green, blue, 255); }
	public void setTint(float red, float green, float blue) { setTint(red, green, blue, 1f); }
	public void setTint(int red, int green, int blue, int alpha) { setTint((float)red / 255f, (float)green / 255f, (float)blue / 255f); }

	public abstract void setTint(float red, float green, float blue, float alpha);
	public abstract void setRotation(float angle, float addition, float x, float y);
	
	public void setMirror(boolean horizontal, boolean vertical) {
		//Set mirror
		if (!horizontal && !vertical) 		m_Mirror = MIRROR_NONE;
		else if (horizontal && !vertical) 	m_Mirror = MIRROR_HORIZONTAL;
		else if (!horizontal && vertical) 	m_Mirror = MIRROR_VERTICAL;
		else 								m_Mirror = MIRROR_BOTH;
	}

	public void setAnimating(boolean animating) {
		//Set animation
		m_Animating = true;
	}

	public void nextFrame() {
		//Skip if no sequence
		if (m_Sequence.length <= 0) return;
		
		//While frame not valid
		boolean Valid = false;
		while (!Valid) {
			//Increase position
			m_Current++;
			if (m_Current >= m_Sequence.length) m_Current = 0;
			
			//Validate
			if (m_Sequence[m_Current] >= 0 && m_Sequence[m_Current] < m_Total) Valid = true;
		}
		
		//Save frame
		m_Frame = m_Sequence[m_Current];
	}
	
	public void setSequencePosition(int position) {
		//Set position
		m_Current = position;

		//Correct frame
		if (m_Current >= m_Sequence.length)	m_Current = m_Sequence.length - 1;
		if (m_Current < 0)					m_Current = 0;
		
		//Reset
		m_Frame = m_Sequence.length > 0 ? m_Sequence[m_Current] : 0;
		m_Timer = 0;
	}
	
	public void setFrame(int frame) {
		//Set frame
		m_Frame = frame;

		//Correct frame
		if (m_Frame >= m_Total)	m_Frame = m_Total - 1;
		if (m_Frame < 0)		m_Frame = 0;
		
		//Reset timer
		m_Timer = 0;
	}
	
	public void setDelay(long delay) {
		//Set
		m_Delay 	= delay;
		m_Animating	= delay > 0;
	}
	
	public void setStraightAnimation() {
		//Create sequence
		int[] Sequence = new int[m_Total];
		for (int i = 0; i < m_Total; i++) Sequence[i] = i;
		
		//Save
		setAnimationSequence(Sequence);
	}
	
	public void setReversedAnimation() {
		//Create sequence
		int[] Sequence = new int[m_Total];
		for (int i = 0; i < m_Total; i++) Sequence[i] = m_Total - 1 - i;
		
		//Save
		setAnimationSequence(Sequence);
	}
	
	public void setPingPongAnimation() {
		//Create sequence
		int[] Sequence = new int[(m_Total * 2) - 1];
		for (int i = 0; i < m_Total; i++) {
			//Set
			Sequence[i] 					= i;
			Sequence[(m_Total * 2) - 2 - i] = i;
		}
		
		//Save
		setAnimationSequence(Sequence);
	}
	
	public void setAnimationSequence(int[] sequence) {
		//Get sequence
		int[] Sequence = sequence;
		if (Sequence == null) Sequence = new int[0];
		
		//Save and reset
		m_Sequence = Sequence;
		setSequencePosition(0);
	}

	public void update(long time) {
		//If animating
		if (m_Animating) {
			//Increase timer
			m_Timer += time;

			//If more than delay
			if (m_Timer >= m_Delay) {
				//Reset
				m_Timer -= m_Delay;

				//Next frame
				nextFrame();
			}
		}
	}

	//Data
	protected int		m_Frame;
	protected int 		m_Total;
	protected int		m_Mirror;
	protected int		m_Current;
	protected boolean	m_Animating;
	protected int[]		m_Sequence;
	protected long		m_Delay;
	protected long		m_Timer;
}
