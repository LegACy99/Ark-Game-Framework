package net.ark.framework.system.android;

import java.util.Random;

import android.view.KeyEvent;

import net.ark.framework.system.Device;
import net.ark.framework.system.Utilities;

public class AndroidUtilities extends Utilities {
    protected AndroidUtilities() {
		//System
        m_Back  = KeyEvent.KEYCODE_BACK;
        m_Menu  = -1;
		m_FPS   = 30;
		m_Wait  = 160;
		
		//Create random
		m_Random = new Random();
    }
	
	public static synchronized Utilities instance() {
		//Create if null
		if (s_Instance == null) s_Instance = new AndroidUtilities();
		return s_Instance;
	}

	@Override public int getRow() 		{	return Device.instance().getRow();										}
	@Override public int getColumn() 	{	return Device.instance().getColumn();									}
	@Override public float getScale() 	{	return Device.instance().getScale();									}
	@Override public float getWidth() 	{	return Device.instance().getWidth() / Device.instance().getScale();		}
	@Override public float getHeight() 	{	return Device.instance().getHeight() / Device.instance().getScale();	}

	@Override
	public int getRandom(int from, int to) {
		//Return random
		if (from + 1 >= to) return 0;
		else 				return m_Random.nextInt(to - from) + from;
	}

	@Override
	public String writeVersion(int[] version) {
		return writeVersion(version, new int[] {1, 2});
	}

	@Override
	public String writeVersion(int[] version, int[] digits) {
		//Create builder
		StringBuilder Builder = new StringBuilder();
		
		//Ensure version exist
		if (version != null) {
			//For each version
			for (int i = 0; i < version.length; i++) {
				//Get number of digit
				int Digit = 1;
				if (digits != null && digits.length > i) Digit = digits[i];
				if (Digit < 1) Digit = 1;
				
				//Get numbers
				int[] Numbers = getDigits(version[i], Digit);
				for (int j = 0; j < Numbers.length; j++) Builder.append(Numbers[j]);
				
				//Add dot if not last
				if (i != version.length - 1) Builder.append('.');
			}
		}
		
		//Return
		return Builder.toString();
	}
    
	//The only instance
	private static AndroidUtilities s_Instance = null;
	
	//Random
	protected Random m_Random;
}
