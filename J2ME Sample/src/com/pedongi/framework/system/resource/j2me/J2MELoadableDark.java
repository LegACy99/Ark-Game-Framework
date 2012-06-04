package com.pedongi.framework.system.resource.j2me;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.pedongi.framework.system.Utilities;
import com.pedongi.framework.system.resource.Loadable;
import com.pedongi.framework.system.resource.ResourceManager;

public class J2MELoadableDark extends Loadable {
    protected J2MELoadableDark(String type) {
        super(type, null);
    }
    
    public static Loadable createShadow()           {   return new J2MELoadableDark(KEY_SHADOW);   }
    public static Loadable createDarkBackground()   {   return new J2MELoadableDark(KEY_BACKGROUND);   }

	public Object load() {
		//Initialize
		Image Loaded = null;
		
		//Calculate width and height
		int Width	= (int)Utilities.instance().getWidth() / Utilities.instance().getRepeatX();
		int Height	= (int)Utilities.instance().getHeight() / Utilities.instance().getRepeatY();
		
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
			Loaded.getGraphics().drawImage((Image)ResourceManager.instance().getImage(Utilities.BACKGROUND_FOLDER + "title.png"), 0, 0, Graphics.LEFT | Graphics.TOP);
			
			//Draw shadow
			for (int x = 0; x < Utilities.instance().getRepeatX(); x++)
				for (int y = 0; y < Utilities.instance().getRepeatY(); y++)
					Loaded.getGraphics().drawImage(Shadow, x * Shadow.getWidth(), y * Shadow.getHeight(), Graphics.TOP | Graphics.LEFT);
		}	

		//Return
		return Loaded;
	}
	
	//Constants
	public static final String KEY_SHADOW		= "Shadow";
	public static final String KEY_BACKGROUND	= "DarkBG";
	public static final int COLOR				= 0x70000000;
}
