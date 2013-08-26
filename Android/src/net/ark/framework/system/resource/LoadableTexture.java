package net.ark.framework.system.resource;

import net.ark.framework.system.images.Texture;

public class LoadableTexture extends Loadable {
	public LoadableTexture(String name, boolean antialias, boolean external) {
		//Super
		super(name, Resource.TEXTURE);
		
		//Save antialias
		m_External	= external;
		m_AntiAlias = antialias;
	}

	@Override
	public Object load() {
		//Return texture
		return Texture.create(m_Name, m_AntiAlias, m_External);
	}
	
	//Data
	protected boolean m_External;
	protected boolean m_AntiAlias;
}
