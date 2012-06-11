package net.ark.framework.system.resource.j2me;

import java.io.IOException;

import javax.microedition.lcdui.Image;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import net.ark.framework.system.resource.Loadable;

public class J2MELoadableImages extends Loadable {
	protected J2MELoadableImages(String file, int column, int row) {
        //Super
        super(file, null);
        
		//Save data
		m_Row		= row;
		m_Column 	= column;
	}
	
	protected J2MELoadableImages(JSONObject json) {
        //Super
        super(null);
        
		try {
			//Set data
			m_Row		= json.getInt(KEY_ROW);
			m_Column	= json.getInt(KEY_COLUMN);
            m_Name      = json.getString(KEY_FILE);
		} catch (JSONException e) {}
	}
    
    public static Loadable create(JSONObject json)                  {   return new J2MELoadableImages(json);                }
    public static Loadable create(String file, int column, int row) {   return new J2MELoadableImages(file, column, row);   }
	
	public static Image[] splitTransparentImage(Image image, int width, int height) {
		//Calcuate column and row
		int column	= image.getWidth() / width;
		int row		= image.getHeight() / height;
		
		//Prepare images
		Image[] Final = new Image[column * row];
		for (int i = 0; i < Final.length; i++) {
			//Get image RGB
			int[] ImageRGB = new int[width * height];
			image.getRGB(ImageRGB, 0, width, width * i, 0, width, height);
			Final[i] = Image.createRGBImage(ImageRGB, width, height, true);
		}
		
		return Final;
	}
	
	public Object load() {		
		//Create image array
		Image[] Images = new Image[m_Row * m_Column];
		
		try {
			//Load image
			Image Source 	= Image.createImage(m_Name);
			int Height		= Source.getHeight() / m_Row;
			int Width		= Source.getWidth() / m_Column;
			
			//For each cell
			for (int i = 0; i < Images.length; i++) {
				//Get image RGB
				int[] ImageRGB = new int[Width * Height];
				Source.getRGB(ImageRGB, 0, Width, Width * (i % m_Column), Height * (i / m_Column), Width, Height);
				Images[i] = Image.createRGBImage(ImageRGB, Width, Height, true);
				/*Images[i] = Image.createImage(Width, Height);
				Images[i].getGraphics().drawRegion(Source, Width * (i % m_Column), Height * (i / m_Column), Width, Height, Sprite.TRANS_NONE, 0, 0, Graphics.LEFT | Graphics.TOP);*/
			}
		} catch (IOException e) {}

		//Return images
		return Images;
	}
	
	//Constants
	protected final String KEY_ROW		= "Row";
	public static final String KEY_FILE = "File";
	protected final String KEY_COLUMN	= "Column";

	//Data
	protected int 		m_Row;
	protected int 		m_Column;
}
