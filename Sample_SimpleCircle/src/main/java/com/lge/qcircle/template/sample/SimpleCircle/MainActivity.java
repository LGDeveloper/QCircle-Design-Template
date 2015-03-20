package com.lge.qcircle.template.sample.SimpleCircle;

import com.lge.qcircle.template.QCircleBackButton;
import com.lge.qcircle.template.QCircleDialog;
import com.lge.qcircle.template.QCircleTemplate;
import com.lge.qcircle.template.QCircleTitle;
import com.lge.qcircle.template.TemplateTag;
import com.lge.qcircle.template.TemplateType;
import com.lge.qcircle.utils.QCircleFeature;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import android.view.Gravity;
import android.view.View;
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

	private final boolean DEBUG = true;
	private final String TAG = "[Template APIs Sample]";
	private Window win = null;
	private int mQuickCoverState = 0;

    //sujin.cho 20150311
    private QCircleTitle mTitle;
    private QCircleBackButton mBackButton;
    private Button mButton = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
        mTitle = new QCircleTitle(this,"My Title");
        mTitle.setTextSize(20);
        template.addElement(mTitle);

		/*
		//If you want to add an image or sub layout to a title bar
		//create a new view and set it as a title*/
		//View newView = (View) getLayoutInflater().inflate(R.layout.activity_main, null);
        //mTitle.setView(newView);
		//**[END]


		//**[START] Add a back button
        mBackButton = new QCircleBackButton(this);
        template.addElement(mBackButton);


		//**[END]
						
		//**[START] Customize the Layout
		template.setSidebarRatio(0.5f);		
		//**[END]

        //**[START] Create Intent for full screen
        Intent intent = new Intent(this, FullActivity.class);
        template.setFullscreenIntent(intent);
        //**[END]

        //**[START] Register a broadcast receiver
        template.registerIntentReceiver();


        //If you want to use your own broadcast receiver provided by QCircleTemplate,
        //uncomment the following line.
        //IntentFilter filter = new IntentFilter();
        //filter.addAction(ACTION_ACCESSORY_COVER_EVENT);
        //registerReceiver(mIntentReceiver, filter);
        //**[END]

		/* Customize main content area*/
        RelativeLayout main = template.getLayoutById(TemplateTag.CONTENT_MAIN);
		TextView textview = new TextView(this);
        textview.setGravity(Gravity.TOP);
        textview.setText("main : test test test test test test");
        textview.setTextSize(20);
		main.addView(textview);

        /* Add a dialog */
        final QCircleDialog dialog = new QCircleDialog.Builder()
                .setTitle("Dialog")
                .setText("Hello")
                .setMode(QCircleDialog.DialogMode.Ok)
                .create();

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show(MainActivity.this, template);
            }
        });


        //sujin.cho
        mButton = new Button(this);
        mButton.setText("My Button");
        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mTitle.setBackgroundColor(Color.GREEN);
                mTitle.setText("Hello");

            }
        });

		/* Customize the first side bar*/
		//If the app has more than one side bar, CONTENT_SIDE_2 can be used for the second side bar.
		RelativeLayout side1 = template.getLayoutById(TemplateTag.CONTENT_SIDE_1);
		if( side1 != null ) {
			/* Add image to side bar*/
			side1.setBackgroundResource(R.drawable.ic_launcher);
			LayoutParams params = side1.getLayoutParams();
			params.width = QCircleFeature.getRelativePixelValue(400);
			side1.setLayoutParams(params);

            //sujin.cho
            side1.addView(mButton);
		}
		//**[END]

		//[START] Show created layout
		setContentView(template.getView());
		//[END]
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
        //**[START] Unregister a broadcast receiver
        //If you are using your own broadcast receiver provided by QCircleTemplate,
        //uncomment the following line.
        //unregisterReceiver(mIntentReceiver);
        template.unregisterReceiver();
        //**[END]
	}


    // use only in case, you want to implement your own br receiver
    // your own broadcast receiver
    /*
	private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action == null) {
				return;
			}

			//Receives a LG QCirle intent for the cover event
			if (ACTION_ACCESSORY_COVER_EVENT.equals(action)) {

                //Check the availability of the case
                if(!QCircleTemplate.isQuickCircleAvailable(getApplicationContext())){
                    Log.i(TAG, "Quick Circle case is not available");
                }


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
						Intent callFullscreen = new Intent(MainActivity.this, FullActivity.class);
						startActivity(callFullscreen);
						
						//Finish QCircleActivity
						MainActivity.this.finish();
				}
			}
		}
	};
	*/
}
