package net.ark.framework.components.buttons;

import javax.microedition.khronos.opengles.GL10;

import org.json.JSONObject;

import net.ark.framework.components.Drawable;
import net.ark.framework.system.Utilities;
import net.ark.framework.system.android.input.TouchInfo;
import net.ark.framework.system.images.Image;
import net.ark.framework.system.resource.ResourceManager;

public class Slider extends Drawable {
	protected Slider() {
		//Initialize
		m_Value			= 0;
		m_Slider 		= null;
		m_Handle 		= null;
		m_Touch			= null;
		m_Orientation	= ORIENTATION_HORIZONTAL;
	}

	public Slider(String slider, String button) {
		this(slider, button, ORIENTATION_HORIZONTAL);
	}
	
	public Slider(String slider, JSONObject[] button) {
		this(slider, button, ORIENTATION_HORIZONTAL);
	}
	
	public Slider(String slider, String button, int orientation) {
		this(slider, (JSONObject[]) ResourceManager.instance().getTextures(button), orientation);
	}
	
	public Slider(String slider, JSONObject[] button, int orientation) {
		this(slider, button, orientation, 0);
	}
	
	public Slider(String slider, String button, int orientation, float value) {
		this(slider, (JSONObject[]) ResourceManager.instance().getTextures(button), orientation, value);
	}
		
	public Slider(String slider, JSONObject[] button, int orientation, float value) {
		//Default
		this();
		
		//Save
		m_Slider		= Image.create(slider);
		m_Handle 		= new Button(BUTTON_ID, button);
		m_Orientation	= orientation;
		
		//Set value
		setValue(value);
		
		//Set size
		m_Width 			= m_Slider.getWidth();
		m_Height			= m_Slider.getHeight();
		m_OriginalWidth 	= m_Slider.getOriginalWidth();
		m_OriginalHeight	= m_Slider.getOriginalHeight();
		
		//Set position
		setPosition(0, 0);
	}
	
	public float getValue() {
		return m_Value;
	}
	
	@Override
	public void setPosition(float x, float y, int horizontal, int vertical) {
		//Super
		super.setPosition(x, y, horizontal, vertical);
		
		//Calculate
		float OriginalX = m_X / Utilities.instance().getScale();
		float OriginalY = m_Y / Utilities.instance().getScale();
		float ButtonX	= m_Value * m_Slider.getOriginalWidth();
		float ButtonY	= (1f - m_Value) * m_Slider.getOriginalHeight();
		
		//Set position
		m_Slider.setPosition(OriginalX, OriginalY);
		if (m_Orientation == ORIENTATION_VERTICAL) 	m_Handle.setPosition(OriginalX + (m_Slider.getOriginalWidth() / 2), OriginalY + ButtonY, Drawable.ANCHOR_HCENTER, Drawable.ANCHOR_VCENTER);
		else										m_Handle.setPosition(OriginalX + ButtonX, OriginalY + (m_Slider.getOriginalHeight() / 2), Drawable.ANCHOR_HCENTER, Drawable.ANCHOR_VCENTER);
	}
	
	protected void setValue(float value) {
		//Set value
		m_Value = value;
		if (m_Value > 1) m_Value = 1;
		if (m_Value < 0) m_Value = 0;
	}
	
	public void update(TouchInfo[] touches) {
		//If no touch
		if (m_Touch == null) {
			//If touch exist
			if (touches != null) {
				//For each touch
				for (int i = 0; i < touches.length; i++) {
					//If just pressed
					if (touches[i].isPressed() && !touches[i].wasPressed()  && m_Touch == null) {
						//Check button
						if (m_Handle.isInside(touches[i].getCurrentX(), touches[i].getCurrentY())) {
							//Set button and save
							m_Touch = touches[i];
							m_Handle.setState(Button.STATE_PRESSED);
						}
					}
				}
			}
		} else {
			//is pressed?
			if (m_Touch.isPressed()) {
				//Based on orientation
				if (m_Orientation == ORIENTATION_HORIZONTAL) {
					//Set value
					setValue((m_Touch.getCurrentX() - m_X) / m_Slider.getWidth());
					m_Handle.setPosition(
						(m_X / Utilities.instance().getScale()) + (m_Value * m_Slider.getOriginalWidth()),
						(m_Y / Utilities.instance().getScale()) + (m_Slider.getOriginalHeight() / 2), 
						Drawable.ANCHOR_HCENTER, Drawable.ANCHOR_VCENTER
					);
				} else {
					//Set value
					setValue(1f - ((m_Touch.getCurrentY() - m_Y) / m_Slider.getHeight()));
					m_Handle.setPosition(
						(m_X / Utilities.instance().getScale()) + (m_Slider.getOriginalWidth() / 2),
						(m_Y / Utilities.instance().getScale()) + ((1f - m_Value) * m_Slider.getOriginalHeight()),
						Drawable.ANCHOR_HCENTER, Drawable.ANCHOR_VCENTER
					);
				}
			} else {
				//Stop
				m_Touch = null;
				m_Handle.setState(Button.STATE_NORMAL);
			}
		}
	}

	@Override
	public void draw(GL10 gl) {
		//Draw 
		m_Slider.draw(gl);
		m_Handle.draw(gl);
	}
	
	//Constants
	protected static final int BUTTON_ID			= 1;
	protected static final int MAXIMUM_VALUE		= 100;
	public static final int ORIENTATION_VERTICAL 	= 1;
	public static final int ORIENTATION_HORIZONTAL	= 2;
	
	//Components
	protected Image 	m_Slider;
	protected Button	m_Handle;
	
	//Data
	protected TouchInfo	m_Touch;
	protected float 	m_Value;
	protected int 		m_Orientation;
}
