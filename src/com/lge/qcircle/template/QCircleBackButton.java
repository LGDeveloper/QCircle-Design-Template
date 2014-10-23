package com.lge.qcircle.template;

import com.lge.qcircle.template.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * This class represents back buttons of QuickCircle.<P> 
 * @author jeongeun.jeon
 *
 */
public final class QCircleBackButton { 
	private final String TAG = "QCircleBackButton";

	private ImageView mBtnContent = null;
	private Context mContext = null;
	private final int mId = 8855;

	/**
	 * creates a back button.<P>
	 * @param context	{@code Activity} which has a circle view.<BR>
	 * 					If it is null, you might get errors when you use method of this class.
	 */
	public QCircleBackButton(Context context) {
		mContext = context;

		if( !setButton() )
			Log.d(TAG, "Cannot create a button. Context is null.");
	}

//	public void setSideContent() {
//		if (mContext != null && mSideContent == null) {
//			mSideContent = new View(mContext);
//			LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
//			        LayoutParams.WRAP_CONTENT, 1);
//			this.addView(mSideContent, layoutParams);
//		}
//	}

//	public void setSideContent(View sideContent) {
//		if (mContext != null && mSideContent == null) {
//			mSideContent = sideContent;
//			LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
//			        LayoutParams.WRAP_CONTENT, 1);
//			this.addView(mSideContent, layoutParams);
//		}
//	}
	
	/**
	 * sets the layout of the button.<P>
	 * @return 	true if a button is configured successfully or<BR>
	 * 			false otherwise.
	 */
	private boolean setButton() {
		boolean result = false;
		if (mContext != null) {
			mBtnContent = new ImageView(mContext);
		
			// set attributes
			mBtnContent.setId(mId);
			mBtnContent.setImageResource(R.drawable.icon_back);
			mBtnContent.setBackgroundColor(mContext.getResources().getColor(android.R.color.darker_gray));
			mBtnContent.setScaleType(ScaleType.CENTER);
	//		mBtnContent.setPadding(0, padding, 0, padding);
			mBtnContent.setOnClickListener(new OnClickListener() {		// add a handler to close the circle view when user click the button. 
	
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (mContext != null) {
						try {
							((Activity)mContext).finish();
						} catch (ClassCastException e) {
							Log.e(TAG, "Cannot finish the Application: the context is not an Activity.");
						}
					}
				}
			});
			result = true;
		}
		return result;
	}

//	public View getSideContent() {
//		return mSideContent;
//	}

//	public ImageView getButtonContent() {
//		return mBtnContent;
//	}

	/**
	 * gets the view of the button. <P> 
	 * @return	button view
	 */
	public View getView() {
		return mBtnContent;
	}
	
	/**
	 * gets the ID of the button view.<P>
	 * @return	ID of the button view
	 */
	public int getId() {
		return mId;
	}

	/**
	 * sets the background of the button transparent.<P>
	 */
	public void setBackgroundTransparent() {
	    if( mBtnContent != null )
	    	mBtnContent.setBackgroundColor(Color.TRANSPARENT);
    }
}
