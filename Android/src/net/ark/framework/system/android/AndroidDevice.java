package net.ark.framework.system.android;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import net.ark.framework.system.Device;
import net.ark.framework.system.StateManager;
import net.ark.framework.system.Utilities;
import net.ark.framework.system.android.input.AccelerometerInfo;
import net.ark.framework.system.android.input.TouchInfo;
import net.ark.framework.system.resource.ResourceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.net.Uri;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.os.Process;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;

public class AndroidDevice extends Device implements Renderer, OnTouchListener, OnKeyListener, SensorEventListener, OnGestureListener {
	protected AndroidDevice() {
		//Super
		super();
		
		//Initialize
		m_OpenGL	= null;
		m_Gesture	= new GestureDetector(this);
		if (GameActivity != null) m_Assets = GameActivity.getAssets();
		
		//Initialize input
        m_Menu  		= -1;
		m_Keys			= null;
        m_Back  		= KeyEvent.KEYCODE_BACK;
		m_KeyList		= new ArrayList<Integer>();
		m_Accelerometer = new AccelerometerInfo();
		for (int i = 0; i < m_Touches.length; i++) m_Touches[i] = new TouchInfo();
	}
	
	public synchronized static AndroidDevice instance() {
		//Create state manager if doesn't exist
		if (s_Instance == null) s_Instance = new AndroidDevice();
		return s_Instance;
	}
	
    //Accessors
	public GL10 getGL()				{	return m_OpenGL;	}
	public AssetManager getAssets()	{	return m_Assets;	}
	
	@Override
	public int[] getKeys() {
		//Copy keys
		int[] Keys = new int[m_KeyList.size()];
		for (int i = 0; i < Keys.length; i++) Keys[i] = m_KeyList.get(i).intValue();
		
		//Reset
		m_KeyList.clear();
		
		//Return
		return Keys;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		//Save
		m_OpenGL = gl;
		
		//Initialize OpenGL
		gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);

		//Set OpenGL states
		gl.glEnable(GL10.GL_BLEND);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		
		//Reload resource manager
		ResourceManager.instance().reload();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		//Save
		m_OpenGL = gl;
		
		//Save screen size
		m_Width 	= width;
		m_Height	= height;
		
		//No column/row
		m_Row		= 1;
		m_Column	= 1;
		
		//Calculate scale
		if (Utilities.instance().isSystemBasedOnHeight()) 	m_Scale = (float)m_Height / (float)Utilities.instance().getBaseHeight();
		else 											 	m_Scale = (float)m_Width / (float)Utilities.instance().getBaseWidth();
		
		//Set view port
		gl.glViewport(0, 0, width, height);
		
		//Create view frustum
		float Ratio = m_Width / m_Height;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-Ratio, Ratio, -1, 1, 1.0f, 1000.0f);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		//Save
		m_OpenGL = gl;
		
		//Clear
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
		//Change to model matrix
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		//Set eye position
		GLU.gluLookAt(gl, 0, 0, m_Height / 2, 0, 0, 0, 0, 1, 0);
		
		//Check all touch
		for (int i = 0; i < m_Touches.length; i++) if (!m_Touches[i].isPressed()) m_Touches[i].removed();
		
		//Run state manager
		StateManager.instance().run();
		if (!StateManager.instance().isRunning()) Process.killProcess(Process.myPid());
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		try {
			//Sleep a bit
			Thread.sleep(20);
		} catch (InterruptedException e) {}
		
		//For each pointer
		for (int i = 0; i < event.getPointerCount(); i++) {			
			//Get pointer ID
			int Index 	= (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;
			int ID		= event.getPointerId(Index);
			
			//Check action type
			int Action = event.getAction() & MotionEvent.ACTION_MASK;
			if (Action == MotionEvent.ACTION_DOWN || Action == MotionEvent.ACTION_POINTER_DOWN) 										m_Touches[ID].pressed(event.getX(Index), event.getY(Index));
			else if (Action == MotionEvent.ACTION_UP || Action == MotionEvent.ACTION_POINTER_UP || Action == MotionEvent.ACTION_CANCEL)	m_Touches[ID].released(event.getX(Index), event.getY(Index));
			else if (Action == MotionEvent.ACTION_MOVE)																					m_Touches[event.getPointerId(i)].dragged(event.getX(i), event.getY(i));
		}
		
		//Detect gesture
		m_Gesture.onTouchEvent(event);
		
		//Consume the event
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		//Add to touch
		m_Touches[0].addSwipe(e1.getX(), e1.getY(), e2.getX(), e2.getY());
		
		//Do nothing
		return false;
	}


	@Override public void onShowPress(MotionEvent e) 													{}
	@Override public void onLongPress(MotionEvent e) 													{}
	@Override public boolean onSingleTapUp(MotionEvent e) 												{return false;}
	@Override public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {return false;}
	@Override public boolean onDown(MotionEvent e) 														{return false;}
	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		//Check key
		boolean Ignore = false;
		for (int i = 0; i < IGNORED_KEYS.length; i++) if (event.getKeyCode() == IGNORED_KEYS[i]) Ignore = true;
		
		//Check key if not ignored
		if (!Ignore) if (event.getAction() == KeyEvent.ACTION_UP) m_KeyList.add(new Integer(event.getKeyCode()));
		
		//Consume or no?
		return !Ignore;
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		//Do nothing
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		//Set acceleration value
		m_Accelerometer.setAcceleration(event.values[AXIS_X], event.values[AXIS_Y], event.values[AXIS_Z]);
	}

	@Override
	public void openURL(String url, boolean browser, String title, String loading) {
		//Skip if no activity
		if (GameActivity == null) return;
		
		//Initialize intent
		Intent URLIntent = null;
		if (browser) URLIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		else {
			//Create intent
			URLIntent = new Intent(GameActivity, GameActivity.getClass());
			URLIntent.putExtra(EXTRA_URL, url);
			
			//Add loading and title
			if (title != null) 		URLIntent.putExtra(EXTRA_TITLE, title);
			if (loading != null) 	URLIntent.putExtra(EXTRA_LOADING, loading);
		}
		
		//Start intent if exist
		if (URLIntent != null) GameActivity.startActivity(URLIntent);
	}
	
	//Single instances
	public static Activity 			GameActivity = null;
	private static AndroidDevice 	s_Instance = null;
	
	//Constants
	protected static final int AXIS_X 			= 0;
	protected static final int AXIS_Y 			= 1;
	protected static final int AXIS_Z 			= 2;
	protected static final int[] IGNORED_KEYS 	= { KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_VOLUME_UP, KeyEvent.KEYCODE_VOLUME_MUTE };
    public static final String EXTRA_LOADING 	= "loading";
    public static final String EXTRA_TITLE 		= "title";
	public static final String EXTRA_URL		= "url";
	
	//Android stuff
	protected GL10					m_OpenGL;
	protected AssetManager			m_Assets;
	protected GestureDetector		m_Gesture;
	protected ArrayList<Integer>	m_KeyList;
}
