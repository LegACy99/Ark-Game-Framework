package net.ark.framework.system.images;

import net.ark.framework.components.Croppable;
import net.ark.framework.system.Utilities;
import net.ark.framework.system.images.android.AndroidLabel;

public abstract class Label extends Croppable {	
	protected Label(String text, float x, float y) {
		//Init
		super();
		
		//Initialize
		m_Text = text;
		if (m_Text == null) m_Text = "";
		setPosition(x, y);
	}

	//Creation functions
	public static Label create(String text) 													{ return Label.create(text, Utilities.instance().getSystemFont(), 0, 0); 										}
	public static Label createInteger(int number) 												{ return Label.create("" + number);																				}
	public static Label create(String text, String font) 										{ return Label.create(text, font, 0, 0); 																		}
	public static Label createInteger(int number, String font) 									{ return Label.create("" + number, font);																		}
	public static Label create(String text, String font, float x, float y) 						{ return new AndroidLabel(text, font, x, y);																	}
	public static Label createInteger(int number, String font, float x, float y)				{ return Label.create("" + number, font, x, y);																	}
	public static Label createFloat(float number, int decimal, String font) 					{ return Label.create(Utilities.instance().writeFloat(number, decimal), font);									}
	public static Label createFloat(float number, int decimal, String font, float x, float y)	{ return Label.create(Utilities.instance().writeFloat(number, decimal), font, x, y);							}
	public static Label createFloat(float number, int decimal) 									{ return Label.create(Utilities.instance().writeFloat(number, decimal), Utilities.instance().getSystemFont());	}
	
	//Accessors
	public String getText()	{ return m_Text; }
	
	//Abstract functions
	
	//Data
	protected String	m_Text;
}
