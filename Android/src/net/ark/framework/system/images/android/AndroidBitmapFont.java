package net.ark.framework.system.images.android;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.ark.framework.system.images.BitmapFont;

public class AndroidBitmapFont extends BitmapFont {	
	public AndroidBitmapFont(JSONObject json) {
		//Super
		super(json);
		m_Characters = new HashMap<Integer, BitmapChar>();
		
		try {
			//Read json
			JSONObject JSONFont	= json.getJSONObject(KEY_FONT);
			
			//Get texture size
			JSONObject JSONData = JSONFont.getJSONObject(KEY_LAYOUT);
			float Height		= JSONData.getInt(KEY_HEIGHT);
			float Width			= JSONData.getInt(KEY_WIDTH);
			
			//Create kerning map
			HashMap<Integer, HashMap<Integer, Long>> Kernings = new HashMap<Integer, HashMap<Integer,Long>>();
			
			//Get kerning data
			JSONObject JSONKernings = JSONFont.getJSONObject(KEY_KERNINGS);
			JSONArray KerningsArray	= JSONKernings.getJSONArray(KEY_KERNING);
			
			//For each kerning
			for (int i = 0; i < KerningsArray.length(); i++) {
				//Get data
				JSONObject JSONKerning 	= KerningsArray.getJSONObject(i);
				Integer SecondChar		= new Integer(JSONKerning.getInt(KEY_KERNING_FIRST));
				Integer FirstChar		= new Integer(JSONKerning.getInt(KEY_KERNING_SECOND));
				Long Offset				= new Long(JSONKerning.getLong(KEY_KERNING_OFFSET));			
				
				//Get map
				HashMap<Integer, Long> Kerning = Kernings.get(FirstChar);
				if (Kerning == null) Kerning = new HashMap<Integer, Long>();
				
				//Add to map
				Kerning.put(SecondChar, Offset);
			}
			
			//Get font data
			JSONObject JSONCharacters 	= JSONFont.getJSONObject(KEY_CHARS);
			JSONArray CharactersArray	= JSONCharacters.getJSONArray(KEY_CHAR);
			
			//For each character
			for (int i = 0; i < CharactersArray.length(); i++) {
				//Get index
				JSONObject JSONChar = CharactersArray.getJSONObject(i);
				Integer Index 		= new Integer(JSONChar.getInt(KEY_CHAR_INDEX)); 
				
				//Create character
				BitmapChar Char = new BitmapChar(JSONChar, Kernings.get(Index), Width, Height);
				m_Characters.put(Index, Char);
			}
		} catch (JSONException e) {}
	}
	
	//Accessor
	@Override
	public BitmapChar getChar(char character)	{	return m_Characters.get(new Integer(character));	}
	
	//Data
	protected HashMap<Integer, BitmapChar>	m_Characters;
}
