package net.ark.framework.system.android;

import java.util.Random;

import net.ark.framework.system.Device;
import net.ark.framework.system.Utilities;

public class AndroidUtilities extends Utilities {
    protected AndroidUtilities() {
    	//Super
    	super();
    	
		//Initialize
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
	
	@Override
	public String writeFloat(float number, int decimal) {		
		//Get decimal
		float Number = number;
		for (int i = 0; i < decimal; i++) Number *= 10;
		
		//Get string
		int Integer = (int)Number;
		String Text = String.valueOf(Integer);
		while (Text.length() < decimal + 1) Text = "0" + Text;
		
		//For each digit
		StringBuilder Builder = new StringBuilder();
		for (int i = 0; i < Text.length(); i++) {
			//Add
			Builder.append(Text.charAt(i));
			if (i == Text.length() - decimal - 1) Builder.append('.');
		}
		
		//Return
		return Builder.toString();
	}
    
	//The only instance
	private static AndroidUtilities s_Instance = null;
	
	//Random
	protected Random m_Random;
}
