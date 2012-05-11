package net.ark.framework.system.resource;

import net.ark.framework.system.SoundManager;
import net.ark.framework.system.StringManager;
import net.ark.framework.system.images.BitmapFont;
import net.ark.framework.system.images.Texture;

public class Loadable {
	protected static class Resource {
		//Enum
		public static final Resource BGM		= new Resource();
		public static final Resource SFX		= new Resource();
		public static final Resource JSON		= new Resource();
		public static final Resource FONT		= new Resource();
		public static final Resource NUMBER		= new Resource();
		public static final Resource STRING		= new Resource();
		public static final Resource TEXTURE	= new Resource();

		//Protected constructor
		protected Resource() {}
	}
	
	protected Loadable(Resource resource) {
		this("", resource);
	}

	protected Loadable(String name, Resource resource) {
		//Save
		m_Name		= name;
		m_Resource	= resource;
	}

	public static Loadable createBGM(String file)							{	return new Loadable(file, Resource.BGM);					}
	public static Loadable createSFX(String file)							{	return new Loadable(file, Resource.SFX);					}
	public static Loadable createJSON(String file)							{	return new Loadable(file, Resource.JSON);					}
	public static Loadable createFont(String file)							{	return new Loadable(file, Resource.FONT);					}
	public static Loadable createNumber(int font)							{	return new Loadable(String.valueOf(font), Resource.NUMBER);	}
	public static Loadable createLanguage(int lang) 						{	return new Loadable(String.valueOf(lang), Resource.STRING);	}
	public static Loadable createTexture(String file, boolean antialias)	{	return new LoadableTexture(file, antialias);				}
	public static Loadable createTexture(String file)						{	return new LoadableTexture(file);							}
    
    public String getName() {
        return m_Name;
    }

	public Object load() {
		//Initialize
		Object Result = null;

		//Check resource type
		if (m_Resource == Resource.TEXTURE) {
			//Create texture
			Result = Texture.create(m_Name);
		} else if (m_Resource == Resource.JSON) {
			//Store json object
			Result = ResourceManager.instance().readJSON(m_Name);
		} else if (m_Resource == Resource.FONT) {
			//Load font
			Result = BitmapFont.create(ResourceManager.instance().readJSON(m_Name));
		} else if (m_Resource == Resource.SFX) {
			//Load SFX
			SoundManager.instance().loadSFX(m_Name);
		} else if (m_Resource == Resource.BGM) {
			//Load BGM
			SoundManager.instance().loadBGM(m_Name);
		} /*else if (m_Resource == Resource.NUMBER) {
			BitmapFont.getFont(Integer.parseInt(m_Name)).createNumber();
		}*/ else if (m_Resource == Resource.STRING) {
			//Get and load language
			StringManager.Language Lang = StringManager.Language.getLanguage(Integer.parseInt(m_Name));
			StringManager.instance().loadString(Lang);
		}

		//Return result
		return Result;
	}
	
	//Data
	protected String	m_Name;
	protected Resource	m_Resource;
}
