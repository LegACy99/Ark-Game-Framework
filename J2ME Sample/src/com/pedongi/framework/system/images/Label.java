package com.pedongi.framework.system.images;

import com.pedongi.framework.components.Drawable;
import com.pedongi.framework.system.images.j2me.J2MELabel;
import com.pedongi.framework.system.images.j2me.Number;

public abstract class Label extends Drawable {	
	protected Label(String text, float x, float y) {
		//Init
		super();
		
		//initialize
		m_Text = text;
		setPosition(x, y);
	}
	
	public static Label create(String text, int font)										{ return Label.create(text, font, 0, 0);			}
	public static Label create(String text, int font, float x, float y)						{ return new J2MELabel(text, font, x, y);			}
	public static Label createNumber(int number, int font, int alignment)					{ return new Number(number, font, alignment);		}
	public static Label createNumber(int number, int font, float x, float y, int alignment)	{ return new Number(number, font, x, y, alignment);	}
	public static Label createNumber(int number, int font, float x, float y)				{ return new Number(number, font, x, y);			}
	public static Label createNumber(int number, int font)									{ return new Number(number, font);					}
	
	public String getText()	{	return m_Text;	}
	
	//Data
	protected String m_Text;
}
