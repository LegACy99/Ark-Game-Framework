package net.ark.framework.system;

public interface System {
	//Game data
	public int 		getFPS();		
	public int 		getBaseWidth();
	public int 		getBaseHeight();
	public String 	getApplicationName();
	
	//Game default resources
	public String 	getPressSFX();
	public String 	getReleaseSFX();
	public String 	getFontTexture();
	public boolean 	isFontSmooth();
	public String 	getFont();
}
