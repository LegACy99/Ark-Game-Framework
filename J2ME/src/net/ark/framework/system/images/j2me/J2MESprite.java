/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ark.framework.system.images.j2me;

import net.ark.framework.system.images.Sprite;
import net.ark.framework.system.resource.ResourceManager;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 *
 * @author LegACy
 */
public class J2MESprite extends Sprite {
    protected J2MESprite(int x, int y, long delay) {
		//Super
		super(x, y, delay);
		
		//Initialize
		m_Images	= null;
		m_Transform	= javax.microedition.lcdui.game.Sprite.TRANS_NONE;
	}

	public J2MESprite(String resource, int x, int y, long delay) {
		//Default
		this(x, y, delay);

		//Set image data
		m_Images			= (Image[])ResourceManager.instance().getImages(resource);
		m_OriginalHeight	= m_Images[0].getHeight();
		m_OriginalWidth		= m_Images[0].getWidth();
		m_Height			= m_Images[0].getHeight();
		m_Width				= m_Images[0].getWidth();
		m_Total				= m_Images.length;
	}
	
	public void setRotation(float angle, float addition) {
		//Super
		super.setRotation(angle, addition);
		
		//Correct angle
		while (m_Rotation <= -45)	m_Rotation += 360;
		while (m_Rotation > 315)	m_Rotation -= 360;
		
		//Set transformation from angle
		if (m_Rotation < 45)		m_Transform = javax.microedition.lcdui.game.Sprite.TRANS_NONE;
		else if (m_Rotation < 135)	m_Transform = javax.microedition.lcdui.game.Sprite.TRANS_ROT90;
		else if (m_Rotation < 225)	m_Transform = javax.microedition.lcdui.game.Sprite.TRANS_ROT180;
		else						m_Transform = javax.microedition.lcdui.game.Sprite.TRANS_ROT270;
	}

	public void draw(Graphics g) {
		//Draw
		g.drawRegion(m_Images[m_Frame], 0, 0, (int)m_Width, (int)m_Height, m_Transform, (int)m_X, (int)m_Y, Graphics.LEFT | Graphics.TOP);
	}

	//Images
	protected int		m_Transform;
	protected Image[]	m_Images;
}
