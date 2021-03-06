package com.lge.qcircle.template.sample.SimpleCircle;

import com.lge.qcircle.template.ButtonTheme;
import com.lge.qcircle.template.QCircleBackButton;
import com.lge.qcircle.template.QCircleDialog;
import com.lge.qcircle.template.QCircleTemplate;
import com.lge.qcircle.template.QCircleTitle;
import com.lge.qcircle.template.TemplateTag;
import com.lge.qcircle.template.TemplateType;
import com.lge.qcircle.utils.QCircleFeature;

import android.app.Activity;
import android.content.Intent;


import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;


import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class MainActivity extends Activity {
	QCircleTemplate template = null;

    //Added , 20150320
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
        mTitle = new QCircleTitle(this, "My title");
        mTitle.setTextSize(20);
        mTitle.setBackgroundColor(Color.LTGRAY);
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
        //sujin.cho
        RelativeLayout.LayoutParams btnParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        btnParams.topMargin = QCircleFeature.getRelativePixelValue(this, 300);
        mButton.setLayoutParams(btnParams);
        main.addView(mButton);

		/* Customize the first side bar*/
    	//If the app has more than one side bar, CONTENT_SIDE_2 can be used for the second side bar.
		RelativeLayout side1 = template.getLayoutById(TemplateTag.CONTENT_SIDE_1);
		if( side1 != null ) {
			/* Add image to side bar*/
			side1.setBackgroundResource(R.drawable.ic_launcher);
			LayoutParams params = side1.getLayoutParams();
			//params.width = QCircleFeature.getRelativePixelValue(this,400);
			side1.setLayoutParams(params);

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
        template.unregisterReceiver();
        //**[END]
	}


}
