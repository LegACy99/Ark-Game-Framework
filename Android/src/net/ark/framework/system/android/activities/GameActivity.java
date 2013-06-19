package net.ark.framework.system.android.activities;

import java.util.List;

import net.ark.framework.system.Device;
import net.ark.framework.system.SoundManager;
import net.ark.framework.system.StateManager;
import net.ark.framework.system.android.AndroidDevice;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

@TargetApi(11)
public abstract class GameActivity extends Activity {
	 /** Called when the activity is first created. */
    @SuppressLint("InlinedApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	//Super
        super.onCreate(savedInstanceState);
        
        //Setup
        initialize();
        
        //Go full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        //Set audio control
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        //Create relative layout
        m_Layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        
        //Create surface
        m_Canvas = new GLSurfaceView(this);
        m_Layout.addView(m_Canvas, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        
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
        setContentView(m_Layout, Params);
    }
    
    protected void initialize() {        
        //Save activity
        AndroidDevice.MainActivity = this;
    }
    
    public boolean isFocused() {
    	return m_Focus;
    }
    
    @Override
    public void onResume() {
    	//Resume
    	super.onResume();
    	m_Canvas.onResume();
    	StateManager.instance().resume();
		AndroidDevice.instance().allowResize(m_Focus);
    	if (m_Focus) SoundManager.instance().resume();
    }
    
    @Override
    public void onPause() {
    	//Pause
    	super.onPause();
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
		AndroidDevice.instance().allowResize(m_Focus);
    }
    
    @Override
    public void onBackPressed() {
    	//Do nothing
    }
	
	//Members
	protected GLSurfaceView 	m_Canvas;
    protected RelativeLayout	m_Layout;
    protected boolean			m_Focus;
}
