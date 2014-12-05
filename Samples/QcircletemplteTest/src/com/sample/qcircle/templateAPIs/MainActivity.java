package com.sample.qcircle.templateAPIs;

import com.example.qcircletempltetest.R;



import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import android.view.Window;
import android.view.WindowManager;

import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;


public class MainActivity extends Activity {
	
	
	QCircleTemplate template = null;

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

	private final boolean DEBUG = true;
	private final String TAG = "[Template APIs Sample]";
	private Context mContext;
	private Window win = null;
	private ContentResolver contentResolver = null;
	private int mQuickCoverState = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		setContentView(R.layout.qcircle_empty);
		
		//**[START] Choose a layout 
//		template = new QCircleTemplate(this);
//		template = new QCircleTemplate(this, TemplateType.CIRCLE_HORIZONTAL);
//		template = new QCircleTemplate(this, TemplateType.CIRCLE_COMPLEX);
//		template = new QCircleTemplate(this, TemplateType.CIRCLE_SIDEBAR);
//		template = new QCircleTemplate(this, R.layout.circle_circle);
		template = new QCircleTemplate(this, TemplateType.CIRCLE_VERTICAL);
		//**[END]
		
		//**[START] Add a title bar
		/*Add text*/
		template.setTitle("My Title");
		template.setTitleTextSize(20);
		
		/*
		//If you want to add an image or sub layout to a title bar
		//create a new view and set it as a title
		View newView = (View) getLayoutInflater().inflate(R.layout.activity_main, null);
		template.setTitle(newView, 0.3f);
		*/
		//**[END]
		
		//**[START] Add a back button
		template.setBackbutton();
		//**[END]
						
		//**[START] Customize the Layout
		template.setSidebarRatio(0.5f);		
		//**[END]
		
		
		//LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		//params.gravity = Gravity.CENTER;
		
		//**[START] Customize UI component		
		/* Customize top content area
	 	//set background image
		RelativeLayout top = template.getLayoutById(TemplateTag.CONTENT);
		top.setBackgroundResource(R.drawable.beanbird);
		//set background color
		//top.setBackgroundColor(Color.RED);
		*/
		
		/* Customize main content area*/

		/*
		TextView aaa = new TextView(this); 
		aaa.setGravity(Gravity.TOP);
		aaa.setText("main : test test test test test test");
		aaa.setTextSize(20);
		main.addView(aaa, params);
		*/
		
		/* Customize the first side bar*/
		//If the app has more than one side bar, CONTENT_SIDE_2 can be used for the second side bar.
		RelativeLayout side1 = template.getLayoutById(TemplateTag.CONTENT_SIDE_1);
		if( side1 != null ) {
			//side1.setBackgroundColor(Color.TRANSPARENT);
			//side1.setAlpha(0.5f);
			
			/* Add image to side bar*/
			side1.setBackgroundResource(R.drawable.ic_launcher);
			LayoutParams params = side1.getLayoutParams();
			params.width = 400;
			side1.setLayoutParams(params);
			
			
			/* Add TextView to side bar
			TextView abc = new TextView(this); 
			abc.setText("side1");
			abc.setGravity(Gravity.CENTER);
			abc.setTextSize(20);
			side1.addView(abc, params);
			 */
		}
		//**[END]
		
		
		//**[START] Customize the circle template
		//set background color
		//template.setBackgroundColor(Color.BLACK, true);
		//Drawable mypic = getResources().getDrawable(R.drawable.beanbird);
		//template.setBackgroundDrawable(mypic, true);		
		//**[END}
		
		
			
		//[START] Show created layout
		setContentView(template.getView());
		//[END]
		
		
		
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mContext.unregisterReceiver(mIntentReceiver);
	}
	
	private void registerIntentReceiver() {

		IntentFilter filter = new IntentFilter();
		// Add QCircle intent to the intent filter
		filter.addAction(ACTION_ACCESSORY_COVER_EVENT);
		// Register a broadcast receiver with the system
		mContext.registerReceiver(mIntentReceiver, filter);

	}

	private void setQuickCircleWindowParam() {
		win = getWindow();
		if (win != null) {
			// Show the sample application view on top
			win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
					| WindowManager.LayoutParams.FLAG_FULLSCREEN
					| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
	}
	
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
					Log.d(TAG, "ACTION_ACCESSORY_COVER_EVENT");
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
					Log.d(TAG, "mQuickCoverState:" + mQuickCoverState);
				}

				if (mQuickCoverState == EXTRA_ACCESSORY_COVER_CLOSED) { // closed
					//Set window flags
					setQuickCircleWindowParam();
				} 
				else if (mQuickCoverState == EXTRA_ACCESSORY_COVER_OPENED) { // opened
						
						//Call FullScreenActivity
						Intent callFullscreen = new Intent(mContext, FullActivity.class);
						startActivity(callFullscreen);
						
						//Finish QCircleActivity
						MainActivity.this.finish();
				}
			}
		}
	};
}
