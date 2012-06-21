package net.ark.framework.system.android.activities;

import java.util.List;

import net.ark.framework.system.Device;
import net.ark.framework.system.SoundManager;
import net.ark.framework.system.StateManager;
import net.ark.framework.system.Utilities;
import net.ark.framework.system.android.AndroidDevice;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

public abstract class GameActivity extends Activity {
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	//Super
        super.onCreate(savedInstanceState);
        
        //Setup
        initialize();
        
        //Go full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        //Set audio control
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        //Get wakelock
        m_Lock = ((PowerManager)getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.FULL_WAKE_LOCK, Utilities.instance().getApplicationName());
        
        //Create surface
        m_Canvas = new GLSurfaceView(this);
        
        //Register listeners
        m_Canvas.setRenderer((Renderer) Device.instance());
        m_Canvas.setOnKeyListener((OnKeyListener) Device.instance());
        m_Canvas.setOnTouchListener((OnTouchListener) Device.instance());
        
        //If honeycomb or more, dim bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) m_Canvas.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        
        //Set focus
        m_Canvas.setFocusableInTouchMode(true);
        m_Canvas.requestFocus();
        
        //Find accelerometer
        SensorManager Manager 		= (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> Accelerometers	= Manager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        
        //If exist, register listener
        if (!Accelerometers.isEmpty()) Manager.registerListener(AndroidDevice.instance(), Accelerometers.get(0), SensorManager.SENSOR_DELAY_GAME);

        //Set as activity view
        setContentView(m_Canvas);
    }
    
    protected void initialize() {        
        //Save activity
        AndroidDevice.GameActivity = this;
    }
    
    @Override
    public void onResume() {
    	//Resume
    	super.onResume();
    	m_Lock.acquire();
    	m_Canvas.onResume();
    	StateManager.instance().resume();
    	if (m_Focus) SoundManager.instance().resume();
    }
    
    @Override
    public void onPause() {
    	//Pause
    	super.onPause();
    	m_Lock.release();
    	m_Canvas.onPause();
    	StateManager.instance().pause();
    	SoundManager.instance().pause();
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	//Super
    	super.onWindowFocusChanged(hasFocus);
    	
    	//Set focus
        m_Focus = hasFocus;
        if (m_Focus) SoundManager.instance().resume();
    }
    
    @Override
    public void onBackPressed() {
    	//Do nothing
    }
	
	//Members
	protected WakeLock 		m_Lock;
	protected GLSurfaceView m_Canvas;
    protected boolean		m_Focus;
}
