package net.ark.framework.system.android;

import java.io.IOException;
import java.util.HashMap;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import net.ark.framework.system.Device;
import net.ark.framework.system.SoundManager;

public class AndroidSoundManager extends SoundManager {
	protected AndroidSoundManager() {
		//Set constants
		m_InitVolume = 60;
		
		//Initialize data
		m_BGM		= "";
		m_Mute		= INITIAL_MUTE;
		m_Volume	= getInitialVolume();
		m_SFXMap	= new HashMap<String, Integer>();
		m_SFXPlayer	= new SoundPool(MAX_SFX_CHANNEL, AudioManager.STREAM_MUSIC, 0);
		m_BGMPlayer	= null;
	}

	public synchronized static SoundManager instance() {
		//Create sound manager if doesn't exist
		if (s_Instance == null) s_Instance = new AndroidSoundManager();
		return s_Instance;
	}

	@Override
	public void destroy() {
		//Destroy all musics
		m_BGMPlayer.release();
		m_SFXPlayer.release();
	}

	@Override
	public void resume() {
		//Resume
		if (m_BGMPlayer != null) m_BGMPlayer.start();
	}

	@Override
	public void pause() {
		//Pause
		if (m_BGMPlayer != null) m_BGMPlayer.pause();
	}

	@Override
	public void loadBGM(String bgm) {
		//Skip if equal
		if (m_BGM.equals(bgm)) return;
		
		//Save
		m_BGM = bgm;
		
		//If exist
		if (m_BGMPlayer != null) {
			//Destroy
			m_BGMPlayer.stop();
			m_BGMPlayer.release();
			m_BGMPlayer = null;
		}
		
		//Create player
		m_BGMPlayer = new MediaPlayer();
		m_BGMPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				
		try {
			//Load
			AssetFileDescriptor File = ((AndroidDevice)Device.instance()).getAssets().openFd(m_BGM);
			m_BGMPlayer.setDataSource(File.getFileDescriptor(), File.getStartOffset(), File.getLength());
			
			//Initialize
			m_BGMPlayer.prepare();
			m_BGMPlayer.setLooping(true);
			m_BGMPlayer.setVolume(m_Volume / MAX_VOLUME, m_Volume / MAX_VOLUME);
		} catch (IOException e) {}
	}

	@Override
	public void playBGM() {
		//Play!
		if (m_BGM != null && m_BGMPlayer != null && !m_BGMPlayer.isPlaying()) m_BGMPlayer.start();
	}

	@Override
	public void loadSFX(String sfx) {
		try {
			//Save sound
			int ID = m_SFXPlayer.load(((AndroidDevice)Device.instance()).getAssets().openFd(sfx), 1);
			m_SFXMap.put(sfx, new Integer(ID));
		} catch (IOException e) {}
	}

	@Override
	public void playSFX(String sfx) {
		//Skip if doesn't exist
		if (!m_SFXMap.containsKey(sfx)) return;
		
		//Play
		m_SFXPlayer.play(m_SFXMap.get(sfx).intValue(), m_Volume / MAX_VOLUME, m_Volume / MAX_VOLUME, 0, 0, 1);
	}
	
	//The only instance
	private static AndroidSoundManager s_Instance = null;
	
	//Constants
	protected int MAX_SFX_CHANNEL = 10;
	
	//Musics
	protected String					m_BGM;
	protected MediaPlayer				m_BGMPlayer;
	protected SoundPool 				m_SFXPlayer;
	protected HashMap<String, Integer>	m_SFXMap;
}
