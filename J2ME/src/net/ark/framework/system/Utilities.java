package net.ark.framework.system;

import java.util.Vector;

import net.ark.framework.system.j2me.J2MEUtilities;

public abstract class Utilities {
	//Application constants
	public static final String APPLICATION 		= "Kite Knight";
	public static final int REFERENCE_HEIGHT	= 320;

	//Folder constants
	public static final String DATA_FOLDER			= "/data/";
	public static final String FONT_FOLDER			= "/fonts/";
	public static final String AUDIO_FOLDER			= "/audio/";
	public static final String IMAGE_FOLDER			= "/images/";
	public static final String GAME_FOLDER			= IMAGE_FOLDER + "game/";
	public static final String STORY_FOLDER			= IMAGE_FOLDER + "story/";
	public static final String INTERFACE_FOLDER		= IMAGE_FOLDER + "interfaces/";
	public static final String BACKGROUND_FOLDER	= IMAGE_FOLDER + "backgrounds/";
	public static final String BUTTON_FOLDER		= INTERFACE_FOLDER + "buttons/";
	public static final String TUTORIAL_FOLDER		= INTERFACE_FOLDER + "tutorial/";
	public static final String ACHIEVEMENT_FOLDER	= GAME_FOLDER + "achiements/";
	public static final String UPGRADE_FOLDER		= GAME_FOLDER + "upgrades/";
	public static final String PICKUP_FOLDER		= GAME_FOLDER + "pickups/";
	public static final String KITE_FOLDER			= GAME_FOLDER + "kites/";
	public static final String STRING_FOLDER		= DATA_FOLDER + "strings/";
	public static final String BGM_FOLDER			= AUDIO_FOLDER + "bgm/";
	public static final String SFX_FOLDER			= AUDIO_FOLDER + "sfx/";

	//Fonts constants
	public static final String HUGE_FONT		= FONT_FOLDER + "huge.font";
	public static final String TINY_FONT		= FONT_FOLDER + "tiny.font";
	public static final String TITLE_FONT		= FONT_FOLDER + "thick.font";
	public static final String MAIN_FONT		= FONT_FOLDER + "normal.font";
	public static final String SMALL_FONT		= FONT_FOLDER + "smaller.font";
	public static final String SMALL_BOLD_FONT	= FONT_FOLDER + "smaller-bold.font";
	
	public static Utilities instance() {
		//Return J2ME
		return J2MEUtilities.instance();
	}
	
	//Display abstracts
	public abstract float	getScale();
	public abstract float	getWidth();
	public abstract float	getHeight();
	public abstract int		getColumn();
	public abstract int		getRow();
	
	//System constants
	public int getFPS()			{	return m_FPS;   }	
	public int getButtonWait()	{	return m_Wait;  }
	public int getMenuButton()	{	return m_Menu;  }
	public int getBackButton()	{	return m_Back;	}
	
	//Game constants
	public int getRepeatX()		{	return m_RepeatX;		}
	public int getRepeatY()		{	return m_RepeatY;		}
	public int getTileWidth()	{	return m_TileWidth;		}
	public int getTileHeight()	{	return m_TileHeight;	}
	
	//Utility
	public abstract int getRandom(int from, int to);
	
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
	
	public int getExperienceNeeded(int level) {
		//Calculate
		float Experience = INITIAL_EXP;
		for (int i = 1; i < level; i++) Experience *= EXP_MULTIPLIER;
		
		//Return
		return (int)Experience;
	}
	
	public int getExperience(int[] stars, float height) {
		//Initialize
		float Stars = 0;
		for (int i = 0; i < stars.length; i++) Stars += stars[i];
		
		//Return
		return (int)((Stars / STARS_DIVIDER) + (height / HEIGHT_DIVIDER));
	}
	
	public String getFloatAsString(float number, int decimal) {		
		//Get decimal
		float Number = number;
		for (int i = 0; i < decimal; i++) Number *= 10;
		
		//Get string
		int Integer = (int)Number;
		String Text = String.valueOf(Integer);
		while (Text.length() < decimal + 1) Text = "0" + Text;
		
		//For each digit
		StringBuffer Buffer = new StringBuffer();
		for (int i = 0; i < Text.length(); i++) {
			//Add
			Buffer.append(Text.charAt(i));
			if (i == Text.length() - decimal - 1) Buffer.append('.');
		}
		
		//Return
		return Buffer.toString();
	}
	
	//Constants
	protected static final int INITIAL_EXP		= 15;
	protected static final float EXP_MULTIPLIER = 1.25f;
	protected static final float HEIGHT_DIVIDER = 20f;
	protected static final float STARS_DIVIDER	= 5f;
	
	//System
	protected int m_FPS;
	protected int m_Wait;
    protected int m_Menu;
    protected int m_Back;
	
	//Gameplay
	protected int m_RepeatX;
	protected int m_RepeatY;
	protected int m_TileWidth;
	protected int m_TileHeight;
}
