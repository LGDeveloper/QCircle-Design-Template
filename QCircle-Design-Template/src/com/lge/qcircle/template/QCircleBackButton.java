package com.lge.qcircle.template;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * This class represents back buttons of QuickCircle.
 *
 * @author jeongeun.jeon
 */
public final class QCircleBackButton {
	private final String TAG = "QCircleBackButton";

	private ImageView mBtnContent = null;
	private Context mContext = null;

	/**
	 * creates a back button.
	 *
	 * @param context {@code Activity} which has a circle view.<br>
	 * <b>If it is null, you might get errors when you use method of this class.</b>
	 */
	public QCircleBackButton(Context context) {
		mContext = context;
		if (!setButton())
			Log.d(TAG, "Cannot create a button. Context is null.");
	}

	/**
	 * sets the layout of the button.
	 *
	 * @return true if a button is configured successfully or
	 * false otherwise.
	 */
	private boolean setButton() {
		boolean result = false;
		if (mContext != null) {
			mBtnContent = new ImageView(mContext);
			// set attributes
			mBtnContent.setId(R.id.backButton);
			mBtnContent.setImageResource(R.drawable.backover);
			mBtnContent.setBackgroundResource(R.drawable.back_button_background);
			mBtnContent.setScaleType(ScaleType.CENTER);
			mBtnContent.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mContext != null) {
						try {
							((Activity) mContext).finish();
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

	/**
	 * gets the view of the button.
	 *
	 * @return button view
	 */
	public View getView() {
		return mBtnContent;
	}

	/**
	 * gets the ID of the button view.
	 *
	 * @return ID of the button view
	 */
	public int getId() {
		return R.id.backButton;
	}

	/**
	 * sets the background of the button transparent.
	 */
	public void setBackgroundTransparent() {
		if (mBtnContent != null)
			mBtnContent.setBackgroundColor(Color.TRANSPARENT);
	}
}
