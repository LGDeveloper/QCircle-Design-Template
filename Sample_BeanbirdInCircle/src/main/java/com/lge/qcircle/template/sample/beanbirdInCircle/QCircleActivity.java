package com.lge.qcircle.template.sample.beanbirdInCircle;



import android.app.Activity;

import android.content.Context;
import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import android.view.Window;

import android.widget.Button;

import android.widget.RelativeLayout;
import android.view.ViewGroup.LayoutParams;
import com.lge.qcircle.template.ButtonTheme;
import com.lge.qcircle.template.QCircleTemplate;
import com.lge.qcircle.template.QCircleTitle;
import com.lge.qcircle.template.TemplateTag;
import com.lge.qcircle.template.TemplateType;
import com.lge.qcircle.template.QCircleBackButton;
import com.lge.qcircle.utils.QCircleFeature;


public class QCircleActivity extends Activity {
	

	QCircleTemplate template = null;
    QCircleTitle mTitle = null;
    QCircleBackButton mBackButton = null;

	private Context mContext;
	private Window win = null;

	
	//For background images
	private Drawable mypic = null;
	private Drawable mypic2 = null;
	private Boolean mSwitched = false;

    private final static String TAG = "QCircleActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		

		super.onCreate(savedInstanceState);
		
		mContext = getApplicationContext();

		//[START] Creating a template and Customizing
		//create a template and set a layout
		template = new QCircleTemplate(this, TemplateType.CIRCLE_EMPTY);

		//set title
        mTitle = new QCircleTitle(this, "Bean Bird In Circle");
        template.addElement(mTitle);

		//add back button
        mBackButton = new QCircleBackButton(this);
        mBackButton.setTheme(ButtonTheme.DARK);
        template.addElement(mBackButton);


        //**[START] Create Intent for full screen
        String url = "http://developer.lge.com/main/Intro.dev";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        template.setFullscreenIntent(intent);
        //**[END]

        //**[START] Register a broadcast receiver
        template.registerIntentReceiver();

        //create drawables for background
		mypic = getResources().getDrawable(R.drawable.background1);
		mypic2 = getResources().getDrawable(R.drawable.background2);
		
		//set background
		template.setBackgroundDrawable(mypic);

		//create button for switchin background
		Button btn = new Button(mContext);
		btn.setBackgroundResource(R.drawable.button);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
       // params.addRule(RelativeLayout.CENTER_IN_PARENT);
        params.topMargin = QCircleFeature.getRelativePixelValue(this,270);
        params.width = QCircleFeature.getRelativePixelValue(this,200);
        params.height = QCircleFeature.getRelativePixelValue(this,200);
        params.leftMargin = QCircleFeature.getRelativePixelValue(this,600);
		btn.setLayoutParams(params);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mSwitched == false)
				{
					template.setBackgroundDrawable(mypic2);
					mSwitched = true;
				}
				else
				{
					template.setBackgroundDrawable(mypic);
					mSwitched = false;
				}
			}
		});

        //create button for switchin background
        Button notiBtn = new Button(mContext);
        notiBtn.setText("Number Badge");
        params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_LEFT, btn.getId());
        params.topMargin = QCircleFeature.getRelativePixelValue(this,270);
        params.leftMargin = QCircleFeature.getRelativePixelValue(this,50);
        notiBtn.setLayoutParams(params);

        Log.d(TAG, this.getPackageName());
        Log.d(TAG, mContext.getClass().getName());
        Log.d(TAG, this.getClass().getName());

        //Numberbadge
        final Activity activity = this;
        final Intent numberIntent  = QCircleFeature.activateNumberBadge(activity, 1);
        mContext.sendBroadcast(numberIntent);

        notiBtn.setOnClickListener(new OnClickListener() {

            int count = 1;
             @Override
             public void onClick(View v) {
                 count++;
                 QCircleFeature.setNumberBadge(activity, numberIntent, count);
                 mContext.sendBroadcast(numberIntent);
             }
         });


		//add button to the main content area
        //get main content area
        RelativeLayout main = template.getLayoutById(TemplateTag.CONTENT_MAIN);
        main.addView(btn);
        main.addView(notiBtn);

		//[END]
				
		setContentView(template.getView());
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//unregister the broadcast receiver
        template.unregisterReceiver();

	}
}
