package net.ark.framework.system;

import net.ark.framework.system.j2me.J2MERecordManager;

public abstract class RecordManager {
	protected RecordManager() {
		//Initialize
		m_Mute			= SoundManager.INITIAL_MUTE;
		m_Played		= false;
		
		m_Upgrades		= new int[] { 1, 1, 1, 1, 1 };
		m_Achievements	= new int[0];
		m_Highscore		= 300f;
		m_Level			= 1;
		m_Stats			= 0;
		m_Exp			= 0;
	}
	
	public synchronized static RecordManager instance() {
		//Return the corresponding class
		return J2MERecordManager.instance();
	}
	
	public abstract void destroy();
	public abstract void save();
	public abstract void load();
	
	//Accessors
	public boolean isMute()			{	return m_Mute;			}
	public boolean isInitialized()	{	return m_Played;		}
	public float getHighscore()		{	return m_Highscore;		}
	public int getStatsPoint()		{	return m_Stats;			}
	public int getExperience()		{	return m_Exp;			}
	public int getLevel()			{	return m_Level;			}
	
	public boolean hasAchievement(int id) {
		//Check
		boolean Have = false;
		for (int i = 0; i < m_Achievements.length; i++) if (m_Achievements[i] == id) Have = true;
		
		//Return
		return Have;
	}
	
	public int getKite() {
		//Based on level
		if (m_Level < 5)		return 1;
		else if (m_Level < 15)	return 2;
		else					return 3;
	}
	
	public int getUpgrade(int id) {
		//Validify
		int Level = 0;
		if (id >= 0 || id < m_Upgrades.length) Level = m_Upgrades[id];
		
		//Return
		return Level;	
	}
	
	public void increaseUpgrade(int id) {
		//If valid
		if (id >= 0 || id < m_Upgrades.length) {
			//Add level with stats
			m_Upgrades[id]++;
			m_Stats--;
		}
	}
	
	//Setters
	public void initialized() {
		//Set initialized
		m_Played = true;
	}
	
	public void setHighscore(float highscore) {
		m_Highscore = highscore;
	}
	
	public void addExperience(int experience) {
		//Add
		m_Exp += experience;
		
		//While more than needed
		int Needed = Utilities.instance().getExperienceNeeded(m_Level);
		while (m_Exp >= Needed) {
			//Add level
			m_Level++;
			m_Stats++;
			
			//Reduce experience
			m_Exp -= Needed;
			Needed = Utilities.instance().getExperienceNeeded(m_Level);
		}
	}
	
	public void addAchievement(int id) {
		//If exist, skip
		if (hasAchievement(id)) return;
		
		//Create new
		int[] Achievements = new int[m_Achievements.length + 1];
		System.arraycopy(m_Achievements, 0, Achievements, 0, m_Achievements.length);
		Achievements[m_Achievements.length] = id;
		
		//Set as achievement
		m_Achievements = Achievements;
	}
	
	public void mute(boolean mute) {
		//Set mute
		m_Mute = mute;
	}
	
	//Constants
	public static final String STORE_NAME 	= "Records";
	
	//Data
	protected boolean	m_Mute;
	protected boolean	m_Played;
	protected float		m_Highscore;
	protected int[]		m_Achievements;
	protected int[]		m_Upgrades;
	protected int		m_Level;
	protected int		m_Stats;
	protected int		m_Exp;
}
