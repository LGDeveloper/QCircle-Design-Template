package com.lge.qcircle.template.sample.beanbirdInCircle;



import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;






import android.widget.RelativeLayout.LayoutParams;

import com.lge.qcircle.template.QCircleTemplate;
import com.lge.qcircle.template.TemplateTag;
import com.lge.qcircle.template.TemplateType;

public class QCircleActivity extends Activity {
	
	

	// [START]declared in LGIntent.java of LG Framework
	public static final int EXTRA_ACCESSORY_COVER_OPENED = 0;
	public static final int EXTRA_ACCESSORY_COVER_CLOSED = 1;
	public static final String EXTRA_ACCESSORY_COVER_STATE = "com.lge.intent.extra.ACCESSORY_COVER_STATE";
	public static final String ACTION_ACCESSORY_COVER_EVENT = "com.lge.android.intent.action.ACCESSORY_COVER_EVENT";
	// [END]declared in LGIntent.java of LG Framework

	// [START] QuickCover Settings DB
	public static final String SMARTCOVER_ENABLE = "quick_view_enable";
	// Quick Cover Type
	public static final int TYPE_QUICKCIRCLE = 3;
	// [END] QuickCover Settings DB
	
	// [START] QuickCircle info.
	static boolean quickCircleEnabled = false;
	static int quickCaseType = 0;
	static boolean quickCircleClosed = true;	
	// [END] QuickCircle info.

	QCircleTemplate template = null;
	
	// -------------------------------------------------------------------------------
	private final boolean DEBUG = true;
	private final String TAG = "[QCircleSamepleCode]";
	private int mQuickCoverState = 0;
	private Context mContext;
	private Window win = null;
	private ContentResolver contentResolver = null;
	
	//For background images
	private Drawable mypic = null;
	private Drawable mypic2 = null;
	private Boolean mSwitched = false;
	
	//Query the Device model
	protected String device = android.os.Build.DEVICE;
	protected Boolean isG3 = false;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		

		super.onCreate(savedInstanceState);
		
		mContext = getApplicationContext();
		
		//Get content resolver
		contentResolver = getContentResolver();

		//[START] Creating a template and Customizing
		//create a template and set a layout
		template = new QCircleTemplate(this, TemplateType.CIRCLE_EMPTY);
		//set title
		template.setTitle("Bean Bird In Circle");
		//add back button
		template.setBackButton();
        template.setBackButtonTheme(true);

        //Register your own broadcast receiver
        registerReceiver();             // and then register your own broadcast receiver

        //create drawables for background
		mypic = getResources().getDrawable(R.drawable.background1);
		mypic2 = getResources().getDrawable(R.drawable.background2);
		
		//set background
		template.setBackgroundDrawable(mypic, true);
		
		//get main content area 
		RelativeLayout main = template.getLayoutById(TemplateTag.CONTENT_MAIN);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		params.topMargin = 300;
		
		//create button for switchin background
		Button btn = new Button(mContext);
		btn.setBackgroundResource(R.drawable.button);
		btn.setLayoutParams(params);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mSwitched == false)
				{
					template.setBackgroundDrawable(mypic2, true);
					mSwitched = true;
				}
				else
				{
					template.setBackgroundDrawable(mypic, true);
					mSwitched = false;
				}
			}
		});

		//add button to the main content area
		main.addView(btn);
		//[END]
				
		setContentView(template.getView());
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//unregister the broadcast receiver
		mContext.unregisterReceiver(mIntentReceiver);
	}


	/**
	 * creates an intent filter to filter the intent related Quick Circle only.
	 * registers a broadcast receiver to the system
	 */
	private void registerReceiver() {

		IntentFilter filter = new IntentFilter();
		// Add QCircle intent to the intent filter
		filter.addAction(ACTION_ACCESSORY_COVER_EVENT);
		// Register a broadcast receiver with the system
		mContext.registerReceiver(mIntentReceiver, filter);

	}
	
	/**
	 * sets window manager layout parameters.This method add a FLAG_SHOW_WHEN_LOCKED to the window when the cover is closed.
	 * Also, it sets other parameters for the application.
	 */
	private void setQuickCircleWindowParam() {
		win = getWindow();
		if (win != null) {
			// Show the sample application view on top
			win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
					| WindowManager.LayoutParams.FLAG_FULLSCREEN
					| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
	}
	
   	
	/**
	 * creates a broadcast receiver. The broadcast receiver receives the intent related to Quick Circle.
	 * Once the receiver receives the Quick Circle intent, it gets the current state of the cover.
	 * When the cover is closed, the app shows the circle layout and the cover is open, it shows LG Developer Site
	 * on full screen.  
	 */
	private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			
			
			String action = intent.getAction();
			if (action == null) {
				return;
			}
			

			//Receives a LG QCirle intent for the cover event
			if (ACTION_ACCESSORY_COVER_EVENT.equals(action)) {
				if (DEBUG) {
					Log.i(TAG, "ACTION_ACCESSORY_COVER_EVENT");
				}
				//Check the availability of the case 
				quickCircleEnabled = Settings.Global.getInt(contentResolver,
						SMARTCOVER_ENABLE, 1) == 1 ? true : false;
				
				if (DEBUG) {
					Log.d(TAG, "quickCircleEnabled:" + quickCircleEnabled);
				}
				if(!quickCircleEnabled) return;
				
				//Get a case type
				quickCaseType = Settings.Global.getInt(contentResolver, "cover_type", 0/*default value*/);
				if(quickCaseType != TYPE_QUICKCIRCLE) return;
				
				//Gets the current state of the cover
				mQuickCoverState = intent.getIntExtra(EXTRA_ACCESSORY_COVER_STATE,
						EXTRA_ACCESSORY_COVER_OPENED);
				
				if (DEBUG) {
					Log.i(TAG, "mQuickCoverState:" + mQuickCoverState);
				}

				if (mQuickCoverState == EXTRA_ACCESSORY_COVER_CLOSED) { // closed
					//Set window flags
						setQuickCircleWindowParam();
				} 
				else if (mQuickCoverState == EXTRA_ACCESSORY_COVER_OPENED) { // opened
						
						String url = "http://developer.lge.com/main/Intro.dev";
						Intent full = new Intent(Intent.ACTION_VIEW);
						full.setData(Uri.parse(url));
						startActivity(full);
						QCircleActivity.this.finish();
				}
			}
		}
	};

}
