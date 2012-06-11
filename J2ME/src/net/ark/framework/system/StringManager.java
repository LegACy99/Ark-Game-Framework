/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ark.framework.system;

import net.ark.framework.system.j2me.J2MEStringManager;

/**
 *
 * @author LegACy
 */
public abstract class StringManager {
	public static class Language {
		//Enum
		public static final Language ENGLISH	= new Language(0, "english");

		//Protected constructor
		protected Language(int id, String name) {
			//Save
			m_ID	= id;
			m_Name	= name;
		}

		//Accessor
		public int getID()							{	return m_ID;						}
		public String getName()						{	return m_Name;						}
		public static Language[] getLanguages()		{	return new Language[] { ENGLISH };	}
		public static Language getLanguage(int id)	{
			//Get language
			Language Result			= null;
			Language[] Languages	= getLanguages();

			//Find language
			for (int i = 0; i < Languages.length; i++) if (Languages[i].getID() == id) Result = Languages[i];

			//Return result
			return Result;
		}

		//Data
		protected int		m_ID;
		protected String	m_Name;
	}

	//Inaccessible constructor
	protected StringManager() {
		//Initialize
		m_Tag			= "";
		m_Language		= null;
	}


	public static StringManager instance() {
		//Return the corresponding instance
		return J2MEStringManager.instance();
	}

	public abstract String 	getString(String key);
	public abstract void 	loadString(Language lang);

	//Data
	protected String					m_Tag;
	protected Language					m_Language;
}
