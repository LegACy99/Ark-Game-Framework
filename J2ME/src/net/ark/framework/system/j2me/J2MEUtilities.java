/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ark.framework.system.j2me;

import net.ark.framework.system.Utilities;
import net.ark.framework.system.Device;
import java.util.Random;

/**
 *
 * @author LegACy
 */
public class J2MEUtilities extends Utilities {
    protected J2MEUtilities() {
		//Super
		super();
		
		//Create random
		m_Random = new Random();
    }
	
	public static synchronized Utilities instance() {
		//Create if null
		if (s_Instance == null) s_Instance = new J2MEUtilities();
		return s_Instance;
	}
    
	//Display
	public int getRow()			{	return Device.instance().getRow();		}
	public int getColumn()		{	return Device.instance().getColumn();	}
	public float getHeight() 	{	return Device.instance().getHeight();	}
	public float getWidth() 	{	return Device.instance().getWidth();	}
	public float getScale()		{	return Device.instance().getScale();	}
	
	public int getRandom(int from, int to) {
		//Return random
		if (from + 1 >= to) return 0;
		else 				return m_Random.nextInt(to - from) + from;
	}
	public String writeVersion(int[] version) {
		return writeVersion(version, new int[] {1, 2});
	}

	public String writeVersion(int[] version, int[] digits) {
		//Create buffer
		StringBuffer Buffer = new StringBuffer();
		
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
				for (int j = 0; j < Numbers.length; j++) Buffer.append(Numbers[j]);
				
				//Add dot if not last
				if (i != version.length - 1) Buffer.append('.');
			}
		}
		
		//Return
		return Buffer.toString();
	}
	
	public String writeFloat(float number, int decimal) {		
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
    
	//The only instance
	private static J2MEUtilities s_Instance = null;
	
	//Random
	protected Random m_Random;
}
