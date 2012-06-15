/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ark.framework.system.j2me;

import net.ark.framework.system.SoundManager;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VolumeControl;

/**
 *
 * @author LegACy
 */
public class J2MESoundManager extends SoundManager {
    protected J2MESoundManager() {
		//Set constants
		m_InitVolume = 40;
		
		//Initialize data
		m_BGM			= "";
		m_BGMPlayer		= null;
		m_PlayerList	= new Vector();
		m_SFXPlayers	= new Hashtable();
		m_Volume		= getInitialVolume();
		m_Mute 			= INITIAL_MUTE;        
    }

	public synchronized static SoundManager instance() {
		//Create state manager if doesn't exist
		if (s_Instance == null) s_Instance = new J2MESoundManager();
		return s_Instance;
	}

	public void destroy() {
		//Stop all players
		if (m_BGMPlayer != null) m_BGMPlayer.close();
		for (Enumeration Enum = m_SFXPlayers.elements(); Enum.hasMoreElements();) ((Player)Enum.nextElement()).close();
		//for (int i = 0; i < m_PlayerList.size(); i++) ((Player)m_PlayerList.elementAt(i)).close();
	}
	
	public void pause() {
		//Mute players
		if (m_BGMPlayer != null) ((VolumeControl)m_BGMPlayer.getControl(CONTROL_VOLUME)).setMute(true);
		if (!m_SFXPlayers.isEmpty())
			for (Enumeration Enum = m_SFXPlayers.elements(); Enum.hasMoreElements();) 
				((VolumeControl)((Player)Enum.nextElement()).getControl(CONTROL_VOLUME)).setMute(true);
	}
	
	public void resume() {
		//Restore players
		if (m_BGMPlayer != null) ((VolumeControl)m_BGMPlayer.getControl(CONTROL_VOLUME)).setMute(m_Mute);
		if (!m_SFXPlayers.isEmpty())
			for (Enumeration Enum = m_SFXPlayers.elements(); Enum.hasMoreElements();)
				((VolumeControl)((Player)Enum.nextElement()).getControl(CONTROL_VOLUME)).setMute(m_Mute);
	}
	
	public void setMute(boolean mute) {
		//Super
		super.setMute(mute);
		
		//Set all mute
		if (m_BGMPlayer != null) ((VolumeControl)m_BGMPlayer.getControl(CONTROL_VOLUME)).setMute(m_Mute);
		for (Enumeration Enum = m_SFXPlayers.elements(); Enum.hasMoreElements();)
			((VolumeControl)((Player)Enum.nextElement()).getControl(CONTROL_VOLUME)).setMute(m_Mute);
	}
	
	public void setVolume(int volume) {
		//Super
		super.setVolume(volume);
		
		//Set all volume
		if (m_BGMPlayer != null) ((VolumeControl)m_BGMPlayer.getControl(CONTROL_VOLUME)).setLevel(m_Volume);
		for (Enumeration Enum = m_SFXPlayers.elements(); Enum.hasMoreElements();)
			((VolumeControl)((Player)Enum.nextElement()).getControl(CONTROL_VOLUME)).setLevel(m_Volume);
	}
	
	public void loadBGM(String bgm) {
		//Skip if the same bgm
		if (m_BGM.equals(bgm)) return;

		//Save
		m_BGM = bgm;
		
		//If playing BGM
		if (m_BGMPlayer != null) {
			//Close
			m_BGMPlayer.close();
			m_BGMPlayer = null;
		}
		
		try {
			//Create player
			m_BGMPlayer = Manager.createPlayer(getClass().getResourceAsStream(m_BGM), TYPE_MIDI);

			//Initialize player
			m_BGMPlayer.realize();
			m_BGMPlayer.setLoopCount(-1);
			((VolumeControl)m_BGMPlayer.getControl(CONTROL_VOLUME)).setMute(m_Mute);
			((VolumeControl)m_BGMPlayer.getControl(CONTROL_VOLUME)).setLevel(m_Volume);
			m_BGMPlayer.prefetch();	
		} catch (IOException e) {
		} catch (MediaException e) {}
	}
	
	public void playBGM() {
		try {
			//If exist
			if (m_BGMPlayer != null) {				
				//Start if not started
				if (m_BGMPlayer.getState() != Player.STARTED) m_BGMPlayer.start();
			}
		} catch (MediaException e) {}
	}
	
	public void loadSFX(String sfx) {
		//Skip if exist
		if (m_SFXPlayers.containsKey(sfx)) return;

		try {
			//Create player
			Player SFXPlayer = Manager.createPlayer(getClass().getResourceAsStream(sfx), TYPE_WAV);
			
			//Initialize player
			SFXPlayer.realize();
			((VolumeControl)SFXPlayer.getControl(CONTROL_VOLUME)).setMute(m_Mute);
			((VolumeControl)SFXPlayer.getControl(CONTROL_VOLUME)).setLevel(m_Volume);
			SFXPlayer.prefetch();
			
			//Add to player list
			m_SFXPlayers.put(sfx, SFXPlayer);
		} catch (IOException e) {
		} catch (MediaException e) {}
	}
	
	public void playSFX(String sfx) {
		//Skip if doesn't exist
		if (!m_SFXPlayers.containsKey(sfx)) return;
		
		try {
			//Get player
			Player SFXPlayer = (Player)m_SFXPlayers.get(sfx);
			
			//Start if not started
			if (SFXPlayer.getState() != Player.STARTED) SFXPlayer.start();	
		} catch (MediaException e) {}
	}
	
	//The only instance
	private static J2MESoundManager s_Instance = null;
	
	//Constants
	protected final String CONTROL_VOLUME = "VolumeControl";
	
	//Musics
	protected String 	m_BGM;
	protected Player	m_BGMPlayer;
	protected Hashtable	m_SFXPlayers;
	protected Vector	m_PlayerList;
}
