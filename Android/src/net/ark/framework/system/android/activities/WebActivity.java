package net.ark.framework.system.android.activities;

import net.ark.framework.system.Utilities;
import net.ark.framework.system.android.AndroidDevice;
import android.app.Activity;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	//Super
    	super.onCreate(savedInstanceState);
    	
    	//Show progress bar
    	getWindow().requestFeature(Window.FEATURE_PROGRESS);
    	getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
    	
    	//Get Url
    	String Url = null;
    	if (getIntent() != null && getIntent().hasExtra(AndroidDevice.EXTRA_URL)) Url = getIntent().getStringExtra(AndroidDevice.EXTRA_URL);
    	
    	//Skip if null
    	if (Url == null) finish();
    	
    	//Create webview
    	WebView Webview = new WebView(this);
    	Webview.setWebViewClient(new WebClient());
    	setContentView(Webview);
    	
    	//Set webview properties
    	Webview.getSettings().setJavaScriptEnabled(true);
    	Webview.getSettings().setBuiltInZoomControls(true);
    	Webview.getSettings().setSupportZoom(true);
    	
    	//Load URL
    	Webview.loadUrl(Url);
    	
    	//Set chrome client
    	Webview.setWebChromeClient(new WebChromeClient() {
    		@Override
    		public void onProgressChanged(WebView view, int progress) {
    			//Get loading text
				String Loading = LOADING_TITLE;
				if (getIntent().hasExtra(AndroidDevice.EXTRA_LOADING)) Loading = getIntent().getStringExtra(AndroidDevice.EXTRA_LOADING);
				
				//Set loading
				m_Activity.setTitle(Loading);
				m_Activity.setProgress(progress * 100);
    			
    			//If done
    			if (progress == PROGRESS_FULL) {
    				//Get title
    				String Title = Utilities.instance().getApplicationName();
    				if (getIntent().hasExtra(AndroidDevice.EXTRA_TITLE)) Title = getIntent().getStringExtra(AndroidDevice.EXTRA_TITLE);
    				
    				//Set title
    				m_Activity.setTitle(Title);
    			}
    		}
    	});
    }
  
    protected class WebClient extends WebViewClient {
    	@Override
    	public boolean shouldOverrideUrlLoading(WebView view, String url) {
    		//Load URL in webview (not browser)    		
            view.loadUrl(url);
            return true;
        }

    	@Override
    	public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
    		//Ignore HTTPS error
    	    handler.proceed();
    	}
    }
    
    //Constants
    protected static final int 		PROGRESS_FULL = 100;
    protected static final String 	LOADING_TITLE = "Loading...";
    
    //Save activity
    final Activity m_Activity = this;
}
