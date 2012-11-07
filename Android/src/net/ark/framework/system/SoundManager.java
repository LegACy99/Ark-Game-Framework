package net.ark.framework.system;

import net.ark.framework.system.android.AndroidSoundManager;

public abstract class SoundManager {
	public synchronized static SoundManager instance() {
		//Return the correct manager
		return AndroidSoundManager.instance();
	}
	
	//Accessors
	public boolean isMute() 		{	return m_Mute;			}
	public float getVolume() 		{	return m_Volume;		}
	public float getInitialVolume()	{	return m_InitVolume;	}
	
	public void setMute(boolean mute) {
		//Set mute
		m_Mute = mute;
	}
	
	public void setVolume(int volume) {
		//Save volume
		m_Volume = volume;
		if (m_Volume < 0) 			m_Volume = 0;
		if (m_Volume > MAX_VOLUME) 	m_Volume = MAX_VOLUME;
	}
	
	//Abstract functions
	public abstract void destroy();
	public abstract void resume();
	public abstract void pause();
	
	//Abstract audio functions
	public abstract void playBGM();
	public abstract void stopSFX(String sfx);
	public abstract void playSFX(String sfx, boolean looping);
	public abstract void loadBGM(String bgm);
	public abstract void loadSFX(String sfx);		
	
	public void playSFX(String sfx) {
		playSFX(sfx, false); 
	}
	
	//Constants
	protected final boolean INITIAL_MUTE 	= true;
	protected final float MAX_VOLUME 		= 100.0f;
	
	//Properties
	protected boolean 	m_Mute;
	protected float		m_Volume;
	protected float		m_InitVolume;
}
