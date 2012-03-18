package net.ark.framework.components;

import javax.microedition.khronos.opengles.GL10;

import net.ark.framework.system.Utilities;
import net.ark.framework.system.images.Label;

public class TimeCounter extends Drawable {
	protected TimeCounter() {
		//Initialize
		m_Sign 		= 1;
		m_Time		= 0;
		m_Font		= Utilities.FONT_FOLDER + "font.json";
		
		//Create
		createCounter();
	}
	
	public TimeCounter(String font) {
		this(font, 0, false);
	}
	
	public TimeCounter(String font, long time) {
		this(font, time, true);
	}
	
	public TimeCounter(String font, long time, boolean countdown) {
		//Default
		this();
		
		//Set data
		m_Time = time;
		m_Font = font;
		m_Sign = countdown ? -1 : 1;
		
		//Create
		createCounter();
	}
	
	protected void createCounter() {
		//Correct time
		if (m_Time < 0) m_Time = 0;
		
		//Get seconds and minutes
		long Time 	= m_Time / 1000;
		int Seconds	= (int) (Time % 60);
		int Minutes	= (int) (Time / 60);
		
		//Get digits
		int[] SecondDigits 	= Utilities.instance().getDigits(Seconds, 2);
		int[] MinuteDigits 	= Utilities.instance().getDigits(Minutes, 2);
		String Digits		= MinuteDigits[0] + MinuteDigits[1] + ":" + SecondDigits[0] + SecondDigits[1];
		
		//Create label
		m_Counter 			= Label.create(Digits, m_Font);
		m_OriginalHeight	= m_Counter.getOriginalHeight();
		m_OriginalWidth		= m_Counter.getOriginalWidth();
		m_Height			= m_Counter.getHeight();
		m_Width				= m_Counter.getWidth();
		
		//Set position
		setPosition(m_X / Utilities.instance().getScale(), m_Y / Utilities.instance().getScale());
	}
	
	public long getTime() {
		return m_Time;
	}
	
	@Override
	public void setPosition(float x, float y, int horizontal, int vertical) {
		//Super
		super.setPosition(x, y, horizontal, vertical);
		m_Counter.setPosition(m_X / Utilities.instance().getScale(), m_Y / Utilities.instance().getScale());
	}
	
	public void update(long time) {
		//Set time and recreate
		m_Time += (time * m_Sign);
		createCounter();
	}
	
	@Override
	public void draw(GL10 gl) {
		//Draw counter
		m_Counter.draw(gl);
	}
	
	//Variables
	protected int 		m_Sign;
	protected long		m_Time;
	protected String	m_Font;
	protected Label		m_Counter;
}
