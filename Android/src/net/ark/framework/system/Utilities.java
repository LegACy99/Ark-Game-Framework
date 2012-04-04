package net.ark.framework.system;

import java.util.ArrayList;

import net.ark.framework.system.android.AndroidUtilities;

public abstract class Utilities {
	//Constants
	public static final int FLOAT_SIZE	= 4;
	public static final int SHORT_SIZE	= 2;
	public static final int MAX_SAVE	= 3;
	
	//Folder constants
	public static final String DATA_FOLDER			= "data/";
	public static final String AUDIO_FOLDER			= "audio/";
	public static final String IMAGE_FOLDER			= "images/";
	public static final String TEXTURE_FOLDER		= "textures/";
	public static final String INTERFACE_FOLDER		= IMAGE_FOLDER + "interface/";
	public static final String BACKGROUND_FOLDER	= IMAGE_FOLDER + "background/";
	public static final String FONT_TEXTURES		= TEXTURE_FOLDER + "fonts/";
	public static final String STRING_FOLDER		= DATA_FOLDER + "strings/";
	public static final String FONT_FOLDER			= DATA_FOLDER + "fonts/";
	public static final String BGM_FOLDER			= AUDIO_FOLDER + "bgm/";
	public static final String SFX_FOLDER			= AUDIO_FOLDER + "sfx/";
	
	public static Utilities instance() {
		//Return blackberry
		return AndroidUtilities.instance();
	}
	
	//Display abstracts
	public abstract int getRow();
	public abstract int getColumn();
	public abstract float getScale();
	public abstract float getWidth();
	public abstract float getHeight();
	
	//System constants
	public int getButtonWait()	{	return m_Wait;  }
	public int getMenuButton()	{	return m_Menu;  }
	public int getBackButton()	{	return m_Back;	}
	
	//Utility
	public abstract int getRandom(int from, int to);
	
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
	protected int m_FPS;
	protected int m_Wait;
    protected int m_Menu;
    protected int m_Back;
}
