package net.ark.framework.system.resource.android;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.ark.framework.system.Device;
import net.ark.framework.system.Utilities;
import net.ark.framework.system.android.AndroidDevice;
import net.ark.framework.system.images.BitmapFont;
import net.ark.framework.system.images.Image;
import net.ark.framework.system.images.Texture;
import net.ark.framework.system.resource.Loadable;
import net.ark.framework.system.resource.ResourceManager;

public class AndroidResourceManager extends ResourceManager {
	protected AndroidResourceManager() {
        //Super
		super();
		
		//Initialize
		m_Destroys	= new ArrayList<String>();
		m_Textures	= new ArrayList<Texture>();
		m_Loadables	= new ArrayList<Loadable>();
		m_Resources	= new HashMap<String, Object>();
		
		//Load system resources
		if (Utilities.instance().getSystemFont() != null) 			addFont(Utilities.instance().getSystemFont());
		if (Utilities.instance().getSystemPressSFX() != null) 		addSFX(Utilities.instance().getSystemPressSFX());
		if (Utilities.instance().getSystemReleaseSFX() != null) 	addSFX(Utilities.instance().getSystemReleaseSFX());
		if (Utilities.instance().getSystemFontTexture() != null)	addTexture(Utilities.instance().getSystemFontTexture(), Utilities.instance().isSystemFontSmooth());
    }
	
	public synchronized static ResourceManager instance() {
		//Create state manager if doesn't exist
		if (s_Instance == null) s_Instance = new AndroidResourceManager();
		return s_Instance;
	}

	//Resource loader
	@Override public void addBGM(String file) 							{	if (!isLoading()) m_Loadables.add(Loadable.createBGM(file));     		  	}
	@Override public void addSFX(String file) 							{	if (!isLoading()) m_Loadables.add(Loadable.createSFX(file));      			}
	@Override public void addJSON(String file) 							{	if (!isLoading()) m_Loadables.add(Loadable.createJSON(file));    			}
	@Override public void addImage(String file) 						{	if (!isLoading()) m_Loadables.add(Loadable.createJSON(file));				}
	@Override public void addFont(String file)							{	if (!isLoading()) m_Loadables.add(Loadable.createFont(file));				}
	@Override public void addNumber(int font)							{	if (!isLoading()) m_Loadables.add(Loadable.createNumber(font));				}
	@Override public void addLanguage(int lang)							{	if (!isLoading()) m_Loadables.add(Loadable.createLanguage(lang));			}
	@Override public void addTexture(String file, boolean antialias) 	{	if (!isLoading()) m_Loadables.add(Loadable.createTexture(file, antialias));	}
	@Override public void addTexture(String file) 						{	if (!isLoading()) m_Loadables.add(Loadable.createTexture(file));			}
	
	
	//Resources
	@Override public Object getImage(String file)		{	return null;								}
	@Override public Object getTexture(String file)		{	return m_Resources.get(file);				}
	@Override public BitmapFont getFont(String file)	{	return (BitmapFont) m_Resources.get(file);	}
	@Override public JSONObject getJSON(String file)	{	return (JSONObject) m_Resources.get(file);	}
	@Override public Object[] getImages(String file)	{	return null;								}

	@Override
	public Object[] getTextures(String file) {
		//Initialize
		JSONObject JSON 	= getJSON(file);
		JSONObject[] Result = null;
		
		//If exist
		if (JSON != null) {			
			try {				
				//Check for image list
				if (!JSON.isNull(KEY_IMAGES)) {
					//Get array
					JSONArray JSONImages = JSON.getJSONArray(KEY_IMAGES);
					
					//Create image array
					Result = new JSONObject[JSONImages.length()];
					for (int i = 0; i < JSONImages.length(); i++) Result[i] = JSONImages.getJSONObject(i);
				} else if (!JSON.isNull(KEY_SHEET)) {
					//Get json
					JSONObject JSONSheet = JSON.getJSONObject(KEY_SHEET);
					
					//Initialize
					int GapX 		= 0;
					int GapY 		= 0;
					int Rows	 	= 0;
					int Columns		= 0;
					int OffsetX		= 0;
					int OffsetY		= 0;
					int CellWidth	= 0;
					int CellHeight	= 0;
					
					//If offset exist
					if (!JSONSheet.isNull(KEY_SHEET_OFFSET)) {
						//Get data
						JSONArray JSONOffset	= JSONSheet.getJSONArray(KEY_SHEET_OFFSET);
						OffsetX					= JSONOffset.getInt(0);
						OffsetY					= JSONOffset.getInt(1);
					}
					
					//If gap exist
					if (!JSON.isNull(KEY_SHEET_GAP)) {
						//Get gap
						JSONArray JSONGaps 	= JSON.getJSONArray(KEY_SHEET_GAP);
						GapX				= JSONGaps.getInt(0);
						GapY				= JSONGaps.getInt(1);
					}
					
					//If grid exist
					if (!JSONSheet.isNull(KEY_SHEET_GRID)) {
						//Get data
						JSONArray JSONGrid 	= JSONSheet.getJSONArray(KEY_SHEET_GRID);
						Columns				= JSONGrid.getInt(0);
						Rows				= JSONGrid.getInt(1);
						
						//If size exist
						if (!JSONSheet.isNull(KEY_SHEET_CELL)) {
							//Get data
							JSONArray JSONCell 	= JSONSheet.getJSONArray(KEY_SHEET_CELL);
							CellWidth			= JSONCell.getInt(0);
							CellHeight			= JSONCell.getInt(1);
						} else if (!JSONSheet.isNull(KEY_SHEET_SIZE)) {
							//Get data
							JSONArray JSONSize 	= JSONSheet.getJSONArray(KEY_SHEET_SIZE);
							CellWidth			= JSONSize.getInt(0) / Columns;
							CellHeight			= JSONSize.getInt(1) / Rows;
						}
					} else if (!JSONSheet.isNull(KEY_SHEET_CELL)) {
						//Get data
						JSONArray JSONCell 	= JSONSheet.getJSONArray(KEY_SHEET_CELL);
						CellWidth			= JSONCell.getInt(0);
						CellHeight			= JSONCell.getInt(1);
						
						//If size exist
						if (!JSONSheet.isNull(KEY_SHEET_SIZE)) {
							//Get data
							JSONArray JSONSize 	= JSONSheet.getJSONArray(KEY_SHEET_SIZE);
							Columns				= JSONSize.getInt(0) / CellWidth;
							Rows				= JSONSize.getInt(1) / CellHeight;
						}
					}
					
					//Create result
					Result = new JSONObject[Rows * Columns];
					for (int y = 0; y < Rows; y++) {
						for (int x = 0; x < Columns; x++) {
							//Create rect
							JSONObject JSONRect = new JSONObject();
							JSONRect.put(Image.KEY_RECT_WIDTH, CellWidth);
							JSONRect.put(Image.KEY_RECT_HEIGHT, CellHeight);
							JSONRect.put(Image.KEY_RECT_LEFT, OffsetX + ((CellWidth + GapX) * x));
							JSONRect.put(Image.KEY_RECT_TOP, OffsetY + ((CellHeight + GapY) * y));
							
							//Create image json
							Result[x + (y * Columns)] = new JSONObject();
							Result[x + (y * Columns)].put(Image.KEY_RECT, JSONRect);
						}
					}
				}
				
				//If default texture
				if (!JSON.isNull(Image.KEY_TEXTURE)) {
					//Get texture
					String TextureName = JSON.getString(Image.KEY_TEXTURE);
					
					//For each result
					for (int i = 0; i < Result.length; i++) {
						//Add texture if doesn't exist
						if (Result[i].isNull(Image.KEY_TEXTURE)) Result[i].put(Image.KEY_TEXTURE, TextureName);
					}
				}
			} catch (JSONException e) {}
		}
		
		//Return
		return Result;
	}
	
	@Override
	public JSONObject readJSON(String file) {
		//Initialize
		JSONObject Result = new JSONObject();
		
		try {
			//Open JSON file		
			InputStream	Input	= ((AndroidDevice)Device.instance()).getAssets().open(file);
			StringBuffer Buffer	= new StringBuffer();
			
			//Read a byte
			int Char = Input.read();
			while (Char != -1) {
				//Add to buffer
				Buffer.append((char)Char);
				Char = Input.read();
			}
			
			//Create JSON
			Result = new JSONObject(Buffer.toString());
			
			//Close input
			Input.close();
		} catch (JSONException e) {					
		} catch (IOException e) {}
		
		//Return
		return Result;
	}
	
	@Override
	public void start() {
		//Initialize
		m_Added = m_Loadables.size();
		m_Destroys.clear();

		//Super
		super.start();
	}
	
	@Override
	public void reload() {
		//Reload textures
		for (int i = 0; i < m_Textures.size(); i++) m_Textures.get(i).load();
	}
	
	@Override
	public void destroy() {
		//Destroy all destructibles
		for (int i = 0; i < m_Destroys.size(); i++) destroy(m_Destroys.get(i));
		m_Destroys.clear();
	}

	@Override
	public void destroy(String resource) {
		//if loading
		if (isLoading()) {
			//Check if going to be loaded
			boolean Loaded = false;
			for (int i = 0; i < m_Loadables.size() && !Loaded; i++) if (m_Loadables.get(i).getName().equals(resource)) Loaded = true;
			
			//If isn't going to be loaded, add
			if (!Loaded) m_Destroys.add(resource);
		} else {
			//Remove
			Object Removed = m_Resources.remove(resource);
			
			//If there's something removed and it's a texture
			if (Removed != null && Removed instanceof Texture) {
				//Destroy
				m_Textures.remove(Removed);
				((Texture)Removed).destroy();
			}
		}
	}
	
	public void update() {
		//If loading
		if (isLoading()) {
			//Get object
			Loadable Current	= (Loadable)m_Loadables.get(m_Loaded);
			String Name			= Current.getName();
			
			//If doesn't exist
			if (!m_Resources.containsKey(Name)) {
				//Load
				Object Loaded = Current.load();
				if (Loaded != null) {
					//Add
					m_Resources.put(Current.getName(), Loaded);
					if (Loaded instanceof Texture) m_Textures.add((Texture)Loaded);
				}	
			}
			
			//Update
			super.update();
			
			//If finished
			if (m_Finished) {
				//Reset
				m_Added = 0;
				m_Loadables.clear();
			}
		}
	}
	
	//Constants
	protected final String KEY_SHEET		= "Sheet";
	protected final String KEY_IMAGES		= "Images";
	protected final String KEY_SHEET_OFFSET	= "Offset"; 
	protected final String KEY_SHEET_GRID	= "Grid"; 
	protected final String KEY_SHEET_SIZE	= "Size";
	protected final String KEY_SHEET_CELL	= "Cell";
	protected final String KEY_SHEET_GAP	= "Gap";
	
	//The only instance
	private static ResourceManager s_Instance = null;
	
	//Data
	protected List<String>				m_Destroys;
	protected List<Texture>				m_Textures;
	protected List<Loadable>    		m_Loadables;
	protected HashMap<String, Object> 	m_Resources;

}
