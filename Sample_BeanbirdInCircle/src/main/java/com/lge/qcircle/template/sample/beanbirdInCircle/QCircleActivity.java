package com.lge.qcircle.template.sample.beanbirdInCircle;



import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
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
        //template.setTitle("Bean Bird In Circle");

		//add back button
        mBackButton = new QCircleBackButton(this);
        mBackButton.isDark(true);
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
		template.setBackgroundDrawable(mypic, true);
		
		//get main content area 
		RelativeLayout main = template.getLayoutById(TemplateTag.CONTENT_MAIN);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		params.topMargin = QCircleFeature.getRelativePixelValue(300);
		
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
        template.unregisterReceiver();

	}
}
