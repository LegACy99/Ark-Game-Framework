package net.ark.framework.system.resource;

import net.ark.framework.system.images.Texture;

public class LoadableTexture extends Loadable {
	protected LoadableTexture(String name) {
		this(name, true);
	}
	
	protected LoadableTexture(String name, boolean antialias) {
		//Super
		super(name, Resource.TEXTURE);
		
		//Save antialias
		m_AntiAlias = antialias;
	}

	@Override
	public Object load() {
		//Return texture
		return Texture.create(m_Name, m_AntiAlias);
	}
	
	//Data
	protected boolean m_AntiAlias;
}
