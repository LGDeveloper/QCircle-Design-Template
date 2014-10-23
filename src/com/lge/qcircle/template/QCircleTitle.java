package com.lge.qcircle.template;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * This class represents title views of QuickCircle.
 * <P>
 * 
 * @author jeongeun.jeon
 * 
 */
public final class QCircleTitle {
	private final String TAG = "QCircleTitle";
	protected Context mContext = null;
	
	protected LinearLayout mRootView = null;
	protected TextView mTitleView = null;
	protected final int mId = 5588;
	

	/**
	 * creates a title bar with a text.
	 * <P>
	 * It makes a {@link TextView} with the given text.
	 * 
	 * @param title
	 *            title text for the title. <BR>
	 *            If it is null, no title text will be shown but the title bar will occupy some
	 *            space.
	 * @param context
	 *            {@code Activity} which has a circle view.<BR>
	 *            If it is null, you might get errors when you use method of this class.
	 */
	public QCircleTitle(Context context, String title) {
		if (context != null) {
			mContext = context;
			mRootView = createRootView(context);
			mRootView.addView(createTextView(context, title));
		} else {
			Log.e(TAG, "Cannot create a title view. context is null");
		}

	}

	/**
	 * creates a title bar with the given View.
	 * <P>
	 * 
	 * @param context
	 *            {@code Activity} which has a circle view.<BR>
	 *            If it is null, you might get errors when you use method of this class.
	 * @param title
	 *            View for the title bar.<BR>
	 *            If it is null, a empty title bar will be created.
	 */
	public QCircleTitle(Context context, View title) {
		if (context != null) {
			mContext = context;
			mRootView = createRootView(context);
			if (title != null)
				mRootView.addView(title);
		}
	}
	
	/**
	 * gets the ID of the title view.
	 * <P>
	 * 
	 * @return ID of the title view
	 */
	public int getId() {
		return mId;
	}

	/**
	 * gets the view of the title.
	 * <P>
	 * 
	 * @return root of the title view
	 */
	public View getView() {
		return (View)mRootView;
	}

	/**
	 * sets the view of the title.
	 * <P>
	 * 
	 * @param view
	 *            View for title
	 * @return true if the title view is added successfully or<BR>
	 *         false otherwise.
	 */
	public boolean setView(View view) {
		boolean result = false;
		if (view != null && mRootView != null) {
			mRootView.removeAllViews();
			mRootView.addView(view);
			result = true;
		}
		return result;
	}

	/**
	 * sets the text of the title view.
	 * <P>
	 * 
	 * @param title
	 *            title text for the title. <BR>
	 *            If it is null, no title text will be shown but the title bar will occupy some
	 *            space.
	 */
	public void setTitle(String title) {
		if (mRootView != null && mTitleView != null)
			mTitleView.setText(title);
		else if( mRootView != null && mTitleView == null ) {
			mRootView.removeAllViews();
			mRootView.addView(createTextView(mContext, title));
		}
	}

	/**
	 * sets the font size of the title text.
	 * <P>
	 * 
	 * @param size
	 *            font size in pixel. <BR>
	 *            If it is less or equal to 0, the font size will not change.
	 */
	public void setTextSize(float size) {
		if (mTitleView != null && size > 0) {
			mTitleView.setTextSize(size);
		}
	}
	
	/**
	 * sets the background of the title transparent.<P>
	 */
//	public void setBackgroundTransparent() {
//		if( mRootView != null ) {
//			for( int childIdx = 0 ; childIdx < mRootView.getChildCount(); childIdx++ ) {
//				mRootView.getChildAt(childIdx).setBackgroundColor(Color.TRANSPARENT);
//			}
//		}
//    }

	/**
	 * create a root layout.
	 * <P>
	 * The root layout will include all of contents for a title bar.
	 * 
	 * @param context
	 *            Activity which has this title bar
	 * @return root layout with align of horizontal-center
	 */
	private LinearLayout createRootView(Context context) {
		LinearLayout root = new LinearLayout(context);
		root.setId(mId);
		root.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER);
		root.setBackgroundColor(Color.TRANSPARENT);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		params.gravity = Gravity.CENTER_HORIZONTAL;
		
		root.setLayoutParams(params);

		return root;
	}

	/**
	 * create a TextView.
	 * <P>
	 * 
	 * @param context
	 *            Activity which has this title bar
	 * @param title
	 *            title text
	 * @return TextView with the given text
	 */
	private TextView createTextView(Context context, String title) {
		TextView text = new TextView(context);
		text.setText(title);
		text.setBackgroundColor(Color.TRANSPARENT);
		text.setTextColor(context.getResources().getColor(android.R.color.black));
		// text.setTextSize(context.getResources().getDimension(R.dimen.font_size));

		text.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER);
		text.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		return text;
	}
}
