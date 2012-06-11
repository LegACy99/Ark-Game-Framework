/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ark.framework.system.images.j2me;

import net.ark.framework.system.images.Image;
import net.ark.framework.system.resource.ResourceManager;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author USER5
 */
public class J2MEImage extends Image {
	public J2MEImage(String resource, float x, float y) {
		//Super
		super(resource, x, y);

		//Get image
		m_Image = (javax.microedition.lcdui.Image) ResourceManager.instance().getImage(resource);

		//Set data
		m_Width				= m_Image.getWidth();
		m_Height			= m_Image.getHeight();
		m_RegionHeight		= m_Height;
		m_OriginalHeight	= m_Height;
		m_OriginalWidth		= m_Width;
		m_RegionWidth		= m_Width;
	}

	public void draw(Graphics g) {
		g.drawRegion(
			m_Image,
			(int)m_RegionLeft,
			(int)m_RegionTop,
			(int)m_RegionWidth,
			(int)m_RegionHeight,
			0,
			(int)m_X,
			(int)m_Y,
			Graphics.TOP | Graphics.LEFT
		);
	}

	//Image
	protected javax.microedition.lcdui.Image m_Image;
}
