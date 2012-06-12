package net.ark.example;

import net.ark.framework.system.StateManager;
import net.ark.example.states.ExampleStateFactory;
import net.ark.framework.system.j2me.midlets.GameMidlet;

public class Main extends GameMidlet  {
    protected void initialize() {
		//Super
		super.initialize();
		
		//Set factory
		StateManager.instance().setup(new ExampleStateFactory());
    }
}
