package net.ark.framework.system;

import java.util.Vector;

import net.ark.framework.system.j2me.J2MEUtilities;

public abstract class Utilities {
	//Constants
	public static final int FLOAT_SIZE		= 4;
	public static final int SHORT_SIZE		= 2;
	public static final long SCROLL_WAIT	= 160;

	//Folder constants
	public static final String DATA_FOLDER			= "/data/";
	public static final String AUDIO_FOLDER			= "/audio/";
	public static final String IMAGE_FOLDER			= "/images/";
	public static final String TEXTURE_FOLDER		= "/textures/";
	public static final String FONT_TEXTURES		= TEXTURE_FOLDER + "fonts/";
	public static final String STRING_FOLDER		= DATA_FOLDER + "strings/";
	public static final String FONT_FOLDER			= DATA_FOLDER + "fonts/";
	public static final String BGM_FOLDER			= AUDIO_FOLDER + "bgm/";
	public static final String SFX_FOLDER			= AUDIO_FOLDER + "sfx/";

	//Fonts constants
	/*public static final String HUGE_FONT		= FONT_FOLDER + "huge.font";
	public static final String TINY_FONT		= FONT_FOLDER + "tiny.font";
	public static final String TITLE_FONT		= FONT_FOLDER + "thick.font";
	public static final String MAIN_FONT		= FONT_FOLDER + "normal.font";
	public static final String SMALL_FONT		= FONT_FOLDER + "smaller.font";
	public static final String SMALL_BOLD_FONT	= FONT_FOLDER + "smaller-bold.font";*/
		
	protected Utilities() {
		//Initialize
		m_FPS			= 1;
		m_BaseWidth		= 1;
		m_BaseHeight	= 1;
		m_FontSmooth	= true;
		m_HeightAsBase	= false;
		m_FontTexture	= null;
		m_ReleaseSFX	= null;
		m_PressSFX		= null;
		m_Font			= null;
		m_Name			= "";
	}
	
	public static Utilities instance() {
		//Return J2ME
		return J2MEUtilities.instance();
	}
	
	public void setSystem(System system) {
		//If exist
		if (system != null) {
			//Save data
			m_FPS			= system.getFPS();
			m_Font			= system.getFont();
			m_Name			= system.getApplicationName();
			m_FontTexture	= system.getFontTexture();
			m_ReleaseSFX	= system.getReleaseSFX();
			m_FontSmooth	= system.isFontSmooth();
			m_PressSFX		= system.getPressSFX();
			
			//Get base size
			m_BaseWidth		= system.getBaseWidth();
			m_BaseHeight	= system.getBaseHeight();
			if (m_BaseWidth < 0) 			m_BaseWidth *= -1;
			if (m_BaseHeight < 0) 			m_BaseHeight *= -1;
			if (system.getBaseHeight() > 0) m_HeightAsBase = true;
		}
	}
	
	//Display abstracts
	public abstract int getRow();
	public abstract int getColumn();
	public abstract float getScale();
	public abstract float getWidth();
	public abstract float getHeight();
	
	//Game data
	public String getSystemFont()			{ return m_Font;			}
	public String getApplicationName()		{ return m_Name;			}
	public String getSystemPressSFX()		{ return m_PressSFX;		}
	public String getSystemReleaseSFX()		{ return m_ReleaseSFX;		}
	public String getSystemFontTexture()	{ return m_FontTexture;		}
	public boolean isSystemBasedOnHeight()	{ return m_HeightAsBase;	}
	public boolean isSystemFontSmooth()		{ return m_FontSmooth;		}
	public int getBaseHeight()				{ return m_BaseHeight;		}
	public int getBaseWidth()				{ return m_BaseWidth;		}
	public int getSystemFPS()				{ return m_FPS;				}
	
	//Utility
	public abstract int		getRandom(int from, int to);
	public abstract String 	writeVersion(int[] version);
	public abstract String	writeVersion(int[] version, int[] digits);
	public abstract String 	writeFloat(float number, int decimal);
	
	public int getEuclidean(int x1, int y1, int x2, int y2) {
		return Math.abs(x2 - x1) + Math.abs(y2 - y1);
	}
	
	public int[] getDigits(int number) {
		//If number is 0, return 0
		if (number <= 0) return new int[] { 0 };
		
		//Default
		Vector Digits = new Vector();
		
		//While not all
		int Number = number;
		while (Number > 0) {
			//Get current
			int Current = Number % 10;
			Digits.addElement(new Integer(Current));
			
			//Change number
			Number = (Number - Current) / 10;
		}
		
		//Return
		int[] Result = new int[Digits.size()];
		for (int i = 0; i < Digits.size(); i++) Result[i] = ((Integer)Digits.elementAt(i)).intValue();
		return Result;
	}
	
	public int[] getDigits(int number, int length) {
		//Default
		int[] Result = new int[length];
		
		//For each digit
		int Number = number;
		for (int i = length - 1; i >= 0; i--) {
			//Calculate
			Result[i] 	= Number % 10;
			Number		= (Number - Result[i]) / 10;
		}
		
		//Return
		return Result;
	}
	
	//System
    protected String	m_Name;
    protected String	m_Font;
    protected String	m_PressSFX;
    protected String	m_ReleaseSFX;
    protected boolean	m_FontSmooth;
    protected String	m_FontTexture;
    protected boolean	m_HeightAsBase;
    protected int		m_BaseHeight;
    protected int		m_BaseWidth;
	protected int		m_FPS;
}
