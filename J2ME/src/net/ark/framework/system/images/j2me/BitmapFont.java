/*
 *  font4mobile version 0.25
 * 
 *  (c) 2007 www.gsmdev.com
 * 
 *  visit www.gsmdev.com for updates
 * 
 *  For MIDP 2.0 compatible devices
 *  with transparency support 
 *  
 *  author: Adam Babol (adam.babol@gmail.com)
 *  
 *  transparency support by Anton Banchev
 */

package net.ark.framework.system.images.j2me;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;


public class BitmapFont {
	//The only instance
	private static Hashtable s_Fonts = new Hashtable();
	
	/**
	 * Crate instance of class
	 */
	private BitmapFont() {}

	public static void create(String font) {		
		try {
			//Create bitmap font
			BitmapFont Font = new BitmapFont();
			Image img = null;
			InputStream is = Font.getClass().getResourceAsStream(font);
			DataInputStream dis = new DataInputStream(is);
			int len = dis.readInt();
			Font.chars = new char[len];
			Font.charImage = new Image[len];
			Font.charsWidth = new byte[len];
			for ( int i = 0; i<len; i++ ) {
				Font.chars[i] = dis.readChar();
				Font.charsWidth[i] = dis.readByte();
			}
			int imgSize = dis.readInt();
			byte[] tmpBuffer = new byte[imgSize];
			dis.read(tmpBuffer, 0, imgSize);

			img = Image.createImage(tmpBuffer, 0, imgSize);
			if ( img != null ) {
				Font.imgHeight = img.getHeight();
				int x = 0;
				for (int i = 0; i<len; i++) {
					Font.charImage[i] = Image.createImage(Font.charsWidth[i], Font.imgHeight);
					Graphics g = Font.charImage[i].getGraphics();
					g.setClip(0, 0, Font.charsWidth[i], Font.imgHeight);
					g.drawImage(img, -x, 0, Graphics.TOP|Graphics.LEFT);
					x += Font.charsWidth[i];
					g=null;
				}
			}
			tmpBuffer=null;
			img=null;

			//If okay
			if (Font != null) {
				//Create numbers
				Font.m_Numbers = new Image[10];
				for (int i = 0; i < Font.m_Numbers.length; i++)
					Font.m_Numbers[i] = Font.renderTransparentText(String.valueOf(i), 0x00000000);
			}
			
			//Save font
			s_Fonts.put(font, Font);
		} catch (IOException ex) {
		}
	}

	public static BitmapFont getFont(String font) {
		
		//Return the font
		return (BitmapFont) s_Fonts.get(font);
	}
	
	public void createNumber() {
		//Create numbers
		m_Numbers = new Image[10];
		for (int i = 0; i < m_Numbers.length; i++) m_Numbers[i] = renderTransparentText(String.valueOf(i), 0x00000000);
	}
	
	public Image getNumber(int number) {
		return m_Numbers[number];
	}
	
	/**
	 * Render given text as image using font
	 * 
	 * @param text text to render
	 * @return rendered text as image
	 */
	public Image renderText(String text) {
		//Null if no text
		if (text == null) {
			lastImg = null;
			return null;
		}

		if(text.equals(lastStr))//Eliminate possible creation of already created image
		return lastImg;
		
		Image result;
		//calculate image size
		int width = 0;
		int txtLength = text.length();
		int[] indxs = new int[txtLength];		
		for ( int i = 0; i < txtLength; i++ ) {											
			int val = charIndex(text.charAt(i));
			indxs[i] = val;
			if ( val != -1 ) {
				width += charsWidth[val];				
			}
		}		
		result = Image.createImage(width, imgHeight);
		int resultWidth = result.getWidth();
		Graphics g = result.getGraphics();		
		int x = 0;
		for ( int i = 0; i < indxs.length; i++ ) {
			int val = indxs[i];			
			if ( val != -1 ) {				
				g.setClip(x, 0, charsWidth[val], imgHeight);			
				g.drawImage(charImage[val], x, 0, 20);
				g.setClip(0, 0, resultWidth, imgHeight);
				x += charsWidth[val];
			}
		}
		lastStr=text;
		lastImg=Image.createImage(result);
		return lastImg;
	}
	
	/**
	 * Render given text as transparent image using font
	 * 
	 * @param text text to render
	 * @param transparentColor color to make transparent
	 * @return transparent image
	 */
	public Image renderTransparentText(String text, int transparentColor) {
		renderText(text);
		if (lastImg != null) lastImg = calcBlend(lastImg, transparentColor);
		return lastImg;
	}
	
	/**
	 * Returns position of char in CHAR_TAB array
	 * 
	 * @param ch character to find
	 * @return position of char in characters array
	 */
	private int charIndex(char ch) {
		int i = 0;
		boolean end = false;
		while ( !end && i < chars.length ) {			
			if ( chars[i] == ch ) {
				end = true;				
			} else {
				i++;
			}
		}		
		if (!end) i = -1;
		return i;
	}
	
	/**
	 * Gets font height
	 * 
	 * @return font height in pixels
	 */
	/*public int getFontHeight() {
		return imgHeight;
	}*/

	/**
	 * Gets the width of specified character
	 * 
	 * @param ch - the character to be measured 
	 * @return width of the given character
	 */
	/*public int charWidth(char ch) {		
		int index = charIndex(ch);
		if ( index != -1 ) {
			return charsWidth[index];
		} else return 0;		
	}	*/
	
	/**
	 * Make selected color in the image transparent
	 * Note: the font should be aliased or else borders with the old color may remain 
	 *
	 * @param tmpImg Image object to process
	 * @return processed Image object
	 */
	private Image calcBlend(Image tmpImg, int maskColor){
		int tmpWidth = tmpImg.getWidth();
		int tmpHeight = tmpImg.getHeight();
		int[] raw = new int[tmpWidth * tmpHeight];
		tmpImg.getRGB(raw, 0, tmpWidth, 0, 0, tmpWidth, tmpHeight);
		for(int i=0; i<raw.length; i++){
			int a = 255;//visible
			int color = (raw[i] & 0x00FFFFFF); // get the color of the pixel.
			if(maskColor==color){
				a = 0;//transparent
			}            
			a = (a<<24);    // left shift the alpha value 24 bits.
			color += a;
			raw[i] = color;
		}
		return Image.createRGBImage(raw, tmpWidth, tmpHeight, true);
	}

	private int		imgHeight;
	private char[]	chars;
	private byte[]	charsWidth;
	private Image[]	charImage;
	private String	lastStr="";
	private Image	lastImg;

	//Numbers
	protected Image[] m_Numbers = null;
}
