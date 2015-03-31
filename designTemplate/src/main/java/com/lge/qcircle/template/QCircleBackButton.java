package com.lge.qcircle.template;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Color;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.lge.qcircle.utils.QCircleFeature;

/**
 * The {@code QCircleBackButton} class represents back buttons of QuickCircle.
 *
 * @author jeongeun.jeon
 */
public final class QCircleBackButton extends QCircleTemplateElement{

    private final String TAG = "QCircleBackButton";
	private OnClickListener mListener;
	private ImageView mBtnContent = null;
	private int		mButtonHeight = 0;
	private boolean isDark = false;



	private static float PADDING_RATIO = 0.35f;
    //sujin.cho
    private final float fixedButtonRatio = 0.23f;
    private RelativeLayout.LayoutParams mParams = null;

    // layout values
    private static int mFullSize = 0; // circle diameter


    /**
     * creates a back button.
     *
     * @param context {@code Activity} which has a circle view.<br>
     * <b>If it is null, you might get errors when you use method of this class.</b>
     */
    public QCircleBackButton(Context context) {
        mContext = context;
        getTemplateDiameter(context);
        mButtonHeight = (int)(fixedButtonRatio * mFullSize);
        if (!setButton())
            Log.d(TAG, "Cannot create a button. Context is null.");
    }

	/**
	 * creates a back button.
	 *
	 * @param context {@code Activity} which has a circle view.<br>
	 * <b>If it is null, you might get errors when you use method of this class.</b>
	 */
    /*
	public QCircleBackButton(Context context, float heightRatio) {

        mContext = context;
        getTemplateDiameter(context);
        mButtonHeight = (int)(heightRatio * mFullSize);
        if (!setButton())
            Log.d(TAG, "Cannot create a button. Context is null.");
        //this(context, height, null);
	}
*/

	/**
	 * creates a back button.
	 *
	 * @param context {@code Activity} which has a circle view.<br>
	 * <b>If it is null, you might get errors when you use method of this class.</b>
	 * @param listener Listener on click
	 */
	public QCircleBackButton(Context context, int height, OnClickListener listener) {
		mContext = context;
		mListener = listener;
		mButtonHeight = height;
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
			mBtnContent.setPadding(0,(int)(mButtonHeight*PADDING_RATIO), 0, (int)(mButtonHeight*PADDING_RATIO));
			// set attributes
			mBtnContent.setId(R.id.backButton);
			setTheme();
			mBtnContent.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mListener != null) mListener.onClick(v);
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
     * sets theme of the back button.
     * @author Yoav Sternberg
     */
	private void setTheme() {
		mBtnContent.setImageResource(isDark ? R.drawable.backover_dark : R.drawable.backover);
		mBtnContent.setBackgroundResource(isDark ? R.drawable.back_button_background_dark : R.drawable.back_button_background);
	}

    /**
     * Uses dark theme for the back button.<P>
     *
     * @param isDark  flag which indicates whether dark theme is used or not.
     * @author Yoav Sternberg
     */
	public void isDark(boolean isDark) {
		this.isDark = isDark;
		setTheme();
	}

	/**
	 * Gets the view of the button.
	 *
	 * @return button view
	 */
	public View getView() {
		return mBtnContent;
	}

	/**
	 * Gets the ID of the button view.
	 *
	 * @return ID of the button view
	 */
	public int getId() {
		return R.id.backButton;
	}

	/**
	 * Sets the background of the button transparent.
	 */
	public void setBackgroundTransparent() {
		if (mBtnContent != null)
			mBtnContent.setBackgroundResource(R.drawable.back_button_background_semi_transparent);
	}

    /**
     * Adds this to the template.
     * @param parent
     * @author sujin.cho
     */
    @Override
    protected void addTo(RelativeLayout parent, RelativeLayout content) {
        if ((mBtnContent != null) && (parent != null)) {
            setLayoutParams();
            parent.addView(mBtnContent);
            adjustLayout(content);
        }
    }

    private void adjustLayout(RelativeLayout content)
    {
        RelativeLayout.LayoutParams contentParams = (RelativeLayout.LayoutParams) content.getLayoutParams();
        contentParams.addRule(RelativeLayout.ABOVE, mBtnContent.getId());
        content.setLayoutParams(contentParams);
    }

    /**
     * sets layout parameters of the back button.
     *
     */
    private void setLayoutParams()
    {
        int buttonAreaHeight = (int)(mFullSize * fixedButtonRatio);
        // add a button into the bottom of the circle layout
        mParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,buttonAreaHeight);
        mParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
        mBtnContent.setLayoutParams(mParams);
    }

    /**
     * locates the circle on the correct position. The correct position depends on phone model.
     * <p>
     * @author sujin.cho
     */
    private void getTemplateDiameter(Context context)
    {
        if(context != null) {
            if (!QCircleFeature.isQuickCircleAvailable(context)) {
                Log.i(TAG, "Quick Circle case is not available");
                return;
            }
            // circle size
            int id = context.getResources().getIdentifier(
                    "config_circle_diameter", "dimen", "com.lge.internal");
            mFullSize = context.getResources().getDimensionPixelSize(id);
        }
        else
        {

        }
    }
}
