package net.ark.framework.system.images;

import org.json.JSONException;
import org.json.JSONObject;

import net.ark.framework.system.Utilities;
import net.ark.framework.system.images.android.AndroidBitmapFont;
import net.ark.framework.system.images.android.BitmapChar;

public abstract class BitmapFont {
	protected BitmapFont() {
		//Initialize
		m_Size		= 0;
		m_Height	= 0;
		m_Texture	= null;
	}
	
	public static BitmapFont create(JSONObject json) {
		return new AndroidBitmapFont(json);
	}
	
	protected BitmapFont(JSONObject json) {
		//Initialize
		this();
		
		try {
			//Read json
			JSONObject JSONFont	= json.getJSONObject(KEY_FONT);
			
			//Read common layout
			JSONObject JSONData = JSONFont.getJSONObject(KEY_LAYOUT);
			JSONObject JSONInfo	= JSONFont.getJSONObject(KEY_INFO);
			m_Height			= JSONData.getInt(KEY_LINE);
			m_Size				= JSONInfo.getInt(KEY_SIZE);
			
			//Get texture
			JSONObject JSONPages 	= JSONFont.getJSONObject(KEY_PAGES);
			JSONObject JSONPage		= JSONPages.getJSONObject(KEY_PAGE);
			m_Texture				= Utilities.FONT_TEXTURES + JSONPage.getString(KEY_FILE);	
		} catch (JSONException e) {}
	}
	
	//Accessors
	public float getHeight()	{	return m_Height;	}
	public String getTexture()	{	return m_Texture;	}
	public abstract BitmapChar getChar(char character);
	
	//Character constants
	protected final static String KEY_CHAR			= "char";
	protected final static String KEY_CHARS			= "chars";
	protected final static String KEY_CHAR_INDEX	= "id";
	
	//Kerning constants
	protected final static String KEY_KERNING_FIRST		= "first";
	protected final static String KEY_KERNING_SECOND	= "second";
	protected final static String KEY_KERNING_OFFSET	= "amount";
	protected final static String KEY_KERNINGS			= "kernings";
	protected final static String KEY_KERNING			= "kerning";

	//Pages constants
	protected final static String KEY_FILE	= "file";
	protected final static String KEY_PAGE	= "page";
	protected final static String KEY_PAGES	= "pages";
	
	//Others
	protected final static String KEY_FONT		= "font";
	protected final static String KEY_INFO		= "info";
	protected final static String KEY_SIZE		= "size";
	protected final static String KEY_WIDTH		= "scalew";
	protected final static String KEY_HEIGHT	= "scaleh";
	protected final static String KEY_LAYOUT	= "common";
	protected final static String KEY_LINE		= "lineheight";
	
	//Data
	protected float		m_Size;
	protected float		m_Height;
	protected String	m_Texture;
}
