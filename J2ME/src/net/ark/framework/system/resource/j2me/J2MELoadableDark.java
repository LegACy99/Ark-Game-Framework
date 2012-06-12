package net.ark.framework.system.resource.j2me;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import net.ark.framework.system.Utilities;
import net.ark.framework.system.resource.Loadable;
import net.ark.framework.system.resource.ResourceManager;

public class J2MELoadableDark extends Loadable {
    protected J2MELoadableDark(String type) {
        super(type, null);
    }
    
    public static Loadable createShadow()           {   return new J2MELoadableDark(KEY_SHADOW);		}
    public static Loadable createDarkBackground()   {   return new J2MELoadableDark(KEY_BACKGROUND);	}
	
	public static void setBackground(String background) {
		//Save
		s_Background = background;
	}

	public Object load() {
		//Initialize
		Image Loaded = null;
		
		//Calculate width and height
		int Width	= (int)Utilities.instance().getWidth() / REPEAT_HORIZONTAL;
		int Height	= (int)Utilities.instance().getHeight() / REPEAT_VERTICAL;
		
		//Create color data
		int[] Colors = new int[Width * Height];
		for (int i = 0; i < Colors.length; i++) Colors[i] = COLOR;
		
		//Create shadow
		Image Shadow = Image.createRGBImage(Colors, Width, Height, true);
		
		//Based on key
		if (m_Name.equals(KEY_SHADOW)) {
			//Set as shadow
			Loaded = Shadow;
		} else if (m_Name.equals(KEY_BACKGROUND)) {
			//Draw background
			Loaded = Image.createImage((int)Utilities.instance().getWidth(), (int)Utilities.instance().getHeight());
			if (s_Background != null) Loaded.getGraphics().drawImage((Image)ResourceManager.instance().getImage(s_Background), 0, 0, Graphics.LEFT | Graphics.TOP);
			
			//Draw shadow
			for (int x = 0; x < REPEAT_HORIZONTAL; x++)
				for (int y = 0; y < REPEAT_VERTICAL; y++)
					Loaded.getGraphics().drawImage(Shadow, x * Shadow.getWidth(), y * Shadow.getHeight(), Graphics.TOP | Graphics.LEFT);
		}

		//Return
		return Loaded;
	}
	
	//Static
	protected static String s_Background;
	
	//Constants
	public static final String KEY_SHADOW		= "Shadow";
	public static final String KEY_BACKGROUND	= "DarkBG";
	public static final int REPEAT_HORIZONTAL	= 1;
	public static final int REPEAT_VERTICAL		= 1;
	public static final int COLOR				= 0x70000000;
}
