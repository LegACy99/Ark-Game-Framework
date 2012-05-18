package net.ark.framework.system;

import java.util.ArrayList;

import net.ark.framework.system.android.AndroidUtilities;

public abstract class Utilities {
	//Constants
	public static final int FLOAT_SIZE		= 4;
	public static final int SHORT_SIZE		= 2;
	public static final long SCROLL_WAIT	= 160;
	
	//Folder constants
	public static final String DATA_FOLDER			= "data/";
	public static final String AUDIO_FOLDER			= "audio/";
	public static final String IMAGE_FOLDER			= "images/";
	public static final String TEXTURE_FOLDER		= "textures/";
	public static final String FONT_TEXTURES		= TEXTURE_FOLDER + "fonts/";
	public static final String STRING_FOLDER		= DATA_FOLDER + "strings/";
	public static final String FONT_FOLDER			= DATA_FOLDER + "fonts/";
	public static final String BGM_FOLDER			= AUDIO_FOLDER + "bgm/";
	public static final String SFX_FOLDER			= AUDIO_FOLDER + "sfx/";
	
	protected Utilities() {
		//Initialize
		m_FPS			= 1;
		m_Font			= null;
		m_FontTexture	= null;
		m_ReleaseSFX	= null;
		m_PressSFX		= null;
	}
	
	public static Utilities instance() {
		//Return blackberry
		return AndroidUtilities.instance();
	}
	
	public void setSystem(System system) {
		//If exist
		if (system != null) {
			//Save
			m_FPS			= system.getFPS();
			m_Font			= system.getFont();
			m_FontSmooth	= system.isFontSmooth();
			m_FontTexture	= system.getFontTexture();
			m_ReleaseSFX	= system.getReleaseSFX();
			m_PressSFX		= system.getPressSFX();
		}
	}
	
	//Display abstracts
	public abstract int getRow();
	public abstract int getColumn();
	public abstract float getScale();
	public abstract float getWidth();
	public abstract float getHeight();
	
	//Game data
	public int getSystemFPS()				{ return m_FPS;			}
	public String getSystemFont()			{ return m_Font;		}
	public boolean isSystemFontSmooth()		{ return m_FontSmooth;	}
	public String getSystemFontTexture()	{ return m_FontTexture;	}
	public String getSystemReleaseSFX()		{ return m_ReleaseSFX;	}
	public String getSystemPressSFX()		{ return m_PressSFX;	}
	
	//Utilities
	public abstract int 	getRandom(int from, int to);
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
		ArrayList<Integer> Digits = new ArrayList<Integer>();
		
		//While not all
		int Number = number;
		while (Number > 0) {
			//Get current
			int Current = Number % 10;
			Digits.add(new Integer(Current));
			
			//Change number
			Number = (Number - Current) / 10;
		}
		
		//Return
		int[] Result = new int[Digits.size()];
		for (int i = 0; i < Digits.size(); i++) Result[i] = ((Integer)Digits.get(i)).intValue();
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
	protected int		m_FPS;
    protected String	m_Font;
    protected boolean	m_FontSmooth;
    protected String	m_FontTexture;
    protected String	m_ReleaseSFX;
    protected String	m_PressSFX;
}
