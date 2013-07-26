package net.ark.framework.system.images.android;

import net.ark.framework.system.images.BitmapFont;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.SparseArray;

public class AndroidBitmapFont extends BitmapFont {	
	public AndroidBitmapFont(JSONObject json) {
		//Super
		super(json);
		m_Characters = new SparseArray<BitmapChar>();
		
		try {
			//Read json
			JSONObject JSONFont	= json.getJSONObject(KEY_FONT);
			
			//Get texture size
			JSONObject JSONData = JSONFont.getJSONObject(KEY_LAYOUT);
			float Height		= JSONData.getInt(KEY_HEIGHT);
			float Width			= JSONData.getInt(KEY_WIDTH);
			
			//Create kerning map
			SparseArray<SparseArray<Long>> Kernings = new SparseArray<SparseArray<Long>>();
			if (JSONFont.has(KEY_KERNINGS)) {
				//Get kerning data
				JSONObject JSONKernings = JSONFont.getJSONObject(KEY_KERNINGS);
				JSONArray KerningsArray	= JSONKernings.getJSONArray(KEY_KERNING);
				
				//For each kerning
				for (int i = 0; i < KerningsArray.length(); i++) {
					//Get data
					JSONObject JSONKerning 	= KerningsArray.getJSONObject(i);
					int SecondChar			= JSONKerning.getInt(KEY_KERNING_FIRST);
					int FirstChar			= JSONKerning.getInt(KEY_KERNING_SECOND);
					Long Offset				= Long.valueOf(JSONKerning.getLong(KEY_KERNING_OFFSET));			
					
					//Get map if exist
					SparseArray<Long> Kerning = Kernings.get(FirstChar);
					if (Kerning == null) {
						//Create and add
						Kerning = new SparseArray<Long>();
						Kernings.put(FirstChar, Kerning);
					}
					
					//Add to map
					Kerning.put(SecondChar, Offset);
				}				
			}
			
			//Get font data
			JSONObject JSONCharacters 	= JSONFont.getJSONObject(KEY_CHARS);
			JSONArray CharactersArray	= JSONCharacters.getJSONArray(KEY_CHAR);
			
			//For each character
			for (int i = 0; i < CharactersArray.length(); i++) {
				//Get index
				JSONObject JSONChar = CharactersArray.getJSONObject(i);
				Integer Index 		= Integer.valueOf(JSONChar.getInt(KEY_CHAR_INDEX)); 
				
				//Create character
				BitmapChar Char = new BitmapChar(JSONChar, Kernings.get(Index), Width, Height);
				m_Characters.put(Index, Char);
				
				//Check height
				float CharHeight = Char.getHeight() + Char.getOffsetY();
				if (CharHeight > m_Height) m_Height = CharHeight;
			}
		} catch (JSONException e) {}
	}
	
	//Accessor
	@Override
	public BitmapChar getChar(char character) {
		return m_Characters.get(character);
	}
	
	//Data
	protected SparseArray<BitmapChar> m_Characters;
}
