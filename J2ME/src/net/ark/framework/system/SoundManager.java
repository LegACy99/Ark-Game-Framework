package net.ark.framework.system;

import net.ark.framework.system.j2me.J2MESoundManager;

public abstract class SoundManager {
	public synchronized static SoundManager instance() {
		//Return the correct manager
		return J2MESoundManager.instance();
	}
	
	//Accessors
	public boolean isMute() 		{	return m_Mute;			}
	public int getVolume() 			{	return m_Volume;		}
	public int getInitialVolume()	{	return m_InitVolume;	}
	
	public void setMute(boolean mute) {
		//Set mute
		m_Mute = mute;
	}
	
	public void setVolume(int volume) {
		//Save volume
		m_Volume = volume;
		if (m_Volume < 0) 	m_Volume = 0;
		if (m_Volume > 100) m_Volume = 100;
	}

	//Abstract functions
	public abstract void destroy();
	public abstract void resume();
	public abstract void pause();
	
	public abstract void playBGM();	
	public abstract void playSFX(String sfx);
	public abstract void loadBGM(String bgm);
	public abstract void loadSFX(String sfx);	
	
	//Constants
	public static final boolean INITIAL_MUTE	= false;
	protected static final float MAX_VOLUME 	= 100.0f;
	protected static final String TYPE_MIDI		= "audio/midi";
	protected static final String TYPE_WAV	 	= "audio/x-wav";
	
	//Properties
	protected boolean 	m_Mute;
	protected int		m_Volume;
	protected int 		m_InitVolume;
}
