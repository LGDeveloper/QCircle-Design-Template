package com.lge.qcircle.template;

import com.lge.qcircle.template.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * This class provides design templates for LG QuickCircle.
 * <P>
 * There are 5 templates: <BR>
 * <ul>
 * <li>empty content</li>
 * <li>content with a horizontal sidebar</li>
 * <li>content with a vertical sidebar</li>
 * <li>content with 2 topbars</li>
 * <li>content with 2 sidebars</li>
 * </ul>
 * In order to get the layout, see {@link TemplateType}.<BR>
 * In addition, you can add a title or a back button into each layout described above.
 * <P>
 * This class supports APIs for setting layout, changing attributes of Views in the layout, and
 * setting fullscreen intent.
 * 
 * @author jeongeun.jeon
 * @see TemplateType
 */
public class QCircleTemplate {
	// constants
	protected static final String TAG = "QCircleTemplate";
	protected static final int EXTRA_ACCESSORY_COVER_OPENED = 0;
	protected static final int EXTRA_ACCESSORY_COVER_CLOSED = 1;
	protected static final String EXTRA_ACCESSORY_COVER_STATE = "com.lge.intent.extra.ACCESSORY_COVER_STATE";
	protected static final String ACTION_ACCESSORY_COVER_EVENT = "com.lge.android.intent.action.ACCESSORY_COVER_EVENT";
	protected static final int QUICKCOVERSETTINGS_QUICKCIRCLE = 3;
	protected static final int QUICKCOVERSETTINGS_USEQUICKCIRCLE = 1;
	
	protected static final String DEVICE_G3 = "g3";
	protected static final String DEVICE_T6 = "tiger6";

	//
	protected Context mContext = null;
	protected BroadcastReceiver mReceiver = null;
	protected Intent mFullscreenIntent = null;

	// Views
	protected TemplateType mLayoutType = TemplateType.CIRCLE_EMPTY;
	protected FrameLayout mRootLayout = null;
	protected RelativeLayout mCircleLayout = null;
	protected RelativeLayout mContent = null;
	protected QCircleBackButton mBackButton = null;
	protected QCircleTitle mTitle = null;

	// layout values
	private int mFullSize = 0; // circle diameter
	private int mTopOffset = 0; // top offset of circle
	private int mYpos = 0; // y offset of circle
	
	private final float fixedButtonRatio = 0.23f; // Button height ratio
	private final float fixedTitleRatio = 0.23f; // Title height ratio
//	protected boolean useSmartLighting = false; // flag for smartlighting

	/**
	 * creates a template with an empty content.
	 * 
	 * @param context
	 *            Activity which has the template. <BR>
	 *            It should be an Activity because the template will close the Activity when the
	 *            cover is opened.<BR>
	 *            If it is null, other APIs will not work and report errors.
	 */
	public QCircleTemplate(Context context) {
		this(context, TemplateType.CIRCLE_EMPTY);
	}

	/**
	 * creates a template with the given template type.
	 * 
	 * @param context
	 *            Activity which has the template. <BR>
	 *            It should be an Activity because the template will close the Activity when the
	 *            cover is opened.<BR>
	 *            If it is null, other APIs will not work and report errors.
	 * @param type
	 *            type of the design template you want to use.<BR>
	 *            If it is null, an empty content template will be used.
	 * @see TemplateType
	 */
	public QCircleTemplate(Context context, TemplateType type) {
		mContext = context;
		registerIntentReceiver(); // register cover events

		if (type == null)
			type = TemplateType.CIRCLE_EMPTY;

		setTemplateType(type); // set layout style
	}

	/**
	 * creates a template with the given layout.
	 * 
	 * @param context
	 *            Activity which has the template. <BR>
	 *            It should be an Activity because the template will close the Activity when the
	 *            cover is opened.<BR>
	 *            If it is null, other APIs will not work and report errors.
	 * @param templateId
	 *            ID of the layout to use
	 */
	public QCircleTemplate(Context context, int templateId) {
		mContext = context;
		registerIntentReceiver();

		if (templateId <= 0) {
			templateId = R.layout.qcircle_empty;
		}
		loadCustomTemplate(templateId);
	}

	/**
	 * sets the intent running in fullscreen when the cover is opened.
	 * 
	 * @param intent
	 *            intent to run the fullscreen Activity. <BR>
	 *            If it is null, nothing will be happened when the cover is opened.<BR>
	 *            You can set this as null when you clean up the existing intent.
	 */
	public void setFullscreenIntent(Intent intent) {
		if (intent == null) {
			Log.w(TAG, "The given intent is null");
		}
		mFullscreenIntent = intent;
	}

	/**
	 * gets the root view of the template layout.
	 * <P>
	 * 
	 * @return root view of the layout.
	 */
	public View getView() {
		return mRootLayout;
	}

	/**
	 * sets a title.
	 * <P>
	 * It creates a title bar on the top of the layout.
	 * <P>
	 * The font size of the title is default. You can change the font size by using
	 * {@link #setTitleSize}.
	 * <P>
	 * Note that this method does not create a title bar when the title bar already exists. It
	 * changes the text of the existing title bar.
	 * 
	 * @param title
	 *            text for the title. <BR>
	 *            If it is null, no title text will be shown but the title bar will occupy some
	 *            space.
	 */
	public void setTitle(String title) {
		// if (mTitle == null) { // create a Title
		// if (mContext != null)
		// mTitle = new QCircleTitle(title, mContext);
		// else {
		// Log.w(TAG, "Cannot create the title: context is null");
		// return;
		// }
		// addTitleView(mTitle);
		// } else { // change the string only
		// mTitle.setTitle(title);
		// }
		setTitle(title, fixedTitleRatio);
	}

	/**
	 * sets a title with the given height ratio.
	 * <P>
	 * It creates a title bar on the top of the layout. The height of the title bar depends on
	 * {@code heightRatio}.
	 * <P>
	 * The font size of the title is default. You can change the font size by using
	 * {@link #setTitleSize}.
	 * <P>
	 * Note that this method does not create a title bar when the title bar already exists. It
	 * changes the text and the height of the existing title bar.
	 * 
	 * @param title
	 *            text for the title. <BR>
	 *            If it is null, no title text will be shown but the title bar will occupy some
	 *            space.
	 * @param heightRatio
	 *            ratio of the title bar. <BR>
	 *            If it is less or equal to 0, the height ratio of the title bar will be 0.2 of
	 *            QuickCircle diameter.
	 */
	public void setTitle(String title, float heightRatio) {
		if (mTitle == null) { // create a Title
			if (mContext != null)
				mTitle = new QCircleTitle(mContext, title);
			else {
				Log.w(TAG, "Cannot create the title: context is null");
				return;
			}
			addTitleView(mTitle, heightRatio);
		} else { // change the string only
			mTitle.setTitle(title);
			changeTitleViewHeight(heightRatio);
		}
	}

	/**
	 * sets a title as the given view with the given height ratio.
	 * <P>
	 * It creates a title bar on the top of the layout. The content of the title bar is
	 * {@code titleView} and the height of the title bar depends on {@code heightRatio}.
	 * <P>
	 * The font size of the title is default. You can change the font size by using
	 * {@link #setTitleSize}.
	 * <P>
	 * Note that this method does not create a title bar when the title bar already exists. It
	 * changes the content and the view of the existing title bar.
	 * 
	 * @param titleView
	 *            View to be a title
	 * @param heightRatio
	 *            ratio of the title bar. <BR>
	 *            If it is less or equal to 0, the height ratio of the title bar will be 0.2 of
	 *            QuickCircle diameter.
	 */
	public void setTitle(View titleView, float heightRatio) {
		if (mTitle == null) { // create a Title
			if (mContext != null)
				mTitle = new QCircleTitle(mContext, titleView);
			else {
				Log.w(TAG, "Cannot create the title: context is null");
				return;
			}
			addTitleView(mTitle, heightRatio);
		} else {
			Log.i(TAG, "Title view is updated by user.");

			if (!mTitle.setView(titleView)) {
				Log.w(TAG, "Cannot set the view as a title.");
			} else {
				changeTitleViewHeight(heightRatio);
			}
		}
	}

	/**
	 * sets a back button.
	 * <P>
	 * It creates a back button on the bottom of the layout.
	 * <P>
	 * You do not need to implement an {@code onClickListener} for the button, because it already
	 * has one.<BR>
	 * Note that this method does not create a button when the button already exists.
	 */
	public void setBackbutton() {
		if (mBackButton == null) {
			if (mContext != null)
				mBackButton = new QCircleBackButton(mContext);
			else {
				Log.e(TAG, "Cannot create the back button: context is null");
				return;
			}

			addBackButtonView(mBackButton);
		}
	}

	/**
	 * gets a layout with the given id.
	 * <P>
	 * This method is useful when you want to add or modify Views of the template.<BR>
	 * Every content or sidebar is a {@code RelativeLayout}.
	 * 
	 * @param id
	 *            id of the layout to retrieve. Use {@link TemplateTag}.<BR>
	 * @return a {@code RelativeLayout} whose ID is identical to {@code id}.<BR>
	 *         or null if there is no View with the given ID.
	 */
	public RelativeLayout getLayoutById(int id) {
		RelativeLayout result = null;

		if (mContent != null && id > 0) {
			result = (RelativeLayout)mContent.findViewById(id);
			// if (result == null && mContent.findViewById(R.id.content_top) != null) {
			// result = (RelativeLayout)mContent.findViewById(R.id.content_top).findViewById(id);
			// } else if (result == null && mContentLayout != null) {
			// result = (RelativeLayout)mContentLayout.findViewById(id);
			// }

			// for R.id.content
			// if( result == null && ( mLayoutType == TemplateType.CIRCLE_COMPLEX || mLayoutType ==
			// TemplateType.CIRCLE_EMPTY) )
			// return mContent;
		}

		return result;
	}

	/**
	 * changes the ratio of the first sidebar.
	 * <P>
	 * Some design templates have sidebars which is re-sizable. Note that only the first sidebar can
	 * be re-sized even if the template has more than 1 sidebars.<BR>
	 * This method changes the size of the first sidebar with the ratio comparing to the full
	 * content layout.<BR>
	 * If you add a title bar or a back button after calling this method, the size of the sidebar
	 * will not change. Therefore some UI components might be hidden by the title bar or a back
	 * button. <BR>
	 * Call this method after adding a title bar and a back button to prevent the situation.
	 * 
	 * @param weight
	 *            ratio of the first sidebar. <BR>
	 *            The valid ranage is (0,1). (cannot be 0 or 1).
	 * @return true, if the size is changed successfully or<BR>
	 *         false, otherwise.
	 */
	public boolean setSidebarRatio(float weight) {
		boolean result = false;

		if (mContent != null) { // check validation
			if (weight < 0 || weight > 1) { // check validation
				Log.i(TAG, "content rate should be in range (0,1): current = " + weight);
			} else {
				// get side1
				View firstContent = (View)mContent.findViewById(R.id.side1);

				if (mLayoutType == TemplateType.CIRCLE_COMPLEX) {
					firstContent = (View)firstContent.getParent();
				}

				if (firstContent != null) {
					int parentSize = mFullSize;

					if (mLayoutType == TemplateType.CIRCLE_VERTICAL) { // adjust width
						LayoutParams params = firstContent.getLayoutParams();
						params.width = (int)(parentSize * weight);
						firstContent.setLayoutParams(params);
						
					} else if (mLayoutType == TemplateType.CIRCLE_HORIZONTAL // adjust height
					        || mLayoutType == TemplateType.CIRCLE_COMPLEX) {
						parentSize = (int)(mFullSize * (1 - 0.2 * ((mBackButton != null? 1 : 0) + (mTitle != null? 1
						        : 0))));
						LayoutParams params = firstContent.getLayoutParams();
						params.height = (int)(parentSize * weight);
						firstContent.setLayoutParams(params);
					}
					result = true;
				} else
					Log.w(TAG, "There is no first sidebar in this layout");
			}
		} else
			Log.w(TAG, "No content layout. please set default content layout");
		return result;
	}

	/**
	 * sets the background of the template as the given image.
	 * <P>
	 * The background affects all the layouts including a title bar and a back button.
	 * 
	 * @param image
	 *            background image
	 * @param overwiteButtonArea
	 *            flag for clear background of the back button if it exists.<BR>
	 *            The default background of a back button is light gray. You should clear the
	 *            background color of a back button when you want to use full-layout background.<BR>
	 *            Set this flag in that case.
	 * @see #setBackgroundColor(int, boolean)
	 */
	public void setBackgroundDrawable(Drawable image, boolean overwiteButtonArea) {
		if (mCircleLayout != null)
			mCircleLayout.setBackground(image);

		if (overwiteButtonArea && mBackButton != null)
			mBackButton.setBackgroundTransparent();

		// if (overwiteButtonArea && mTitle != null)
		// mTitle.setBackgroundTransparent();
	}

	/**
	 * sets the background of the template as the given color.
	 * <P>
	 * The background affects all the layouts including a title bar and a back button.
	 * 
	 * @param color
	 *            background color
	 * @param overwiteButtonArea
	 *            flag for clear background of the back button if it exists.<BR>
	 *            The default background of a back button is light gray. You should clear the
	 *            background color of a back button when you want to use full-layout background.<BR>
	 *            Set this flag in that case.
	 * @see #setBackgroundDrawable(Drawable, boolean)
	 */
	public void setBackgroundColor(int color, boolean overwiteButtonArea) {
		if (mCircleLayout != null)
			mCircleLayout.setBackgroundColor(color);

		if (overwiteButtonArea && mBackButton != null)
			mBackButton.setBackgroundTransparent();
	}

	/**
	 * sets the font size of the title.
	 * <P>
	 * 
	 * @param size
	 *            font size in pixel
	 */
	public void setTitleTextSize(float size) {
		if (mTitle != null) {
			mTitle.setTextSize(size);
		}
	}

	/**
	 * sets whether to use smart lighting on the circle view.
	 * <P>
	 * 
	 * @param flag
	 *            flag for smart lighting. If true, smart lighting is on. If not, smart lighting is
	 *            off.
	 * @see #showSmartLighting()
	 */
//	public void setSmartLighting(boolean flag) {
//		useSmartLighting = flag;
//		// NEED TO IMPLEMENT (some codes for initialize smart lighting
//	}

	/**
	 * shows smart lighting on the circle view.
	 * <P>
	 * It starts an animation for smart lighting and the animation shows the lighting just once.
	 * Call this method whenever you want to show smart lighting.<BR>
	 * Note that you can use this method after {@code setSmartLighting(true)}.
	 * 
	 * @see #setSmartLighting(boolean)
	 */
//	public void showSmartLighting() {
//		if (useSmartLighting) {
//			// NEED TO IMPLEMENT
//		}
//	}

	/**
	 * initializes the layout of the circle window.
	 * <P>
	 * It loads a template layout from the xml file and retrieves the root view (actually a
	 * {@code FrameLayout} and the content view(a {@code RelativeLayout}).
	 * 
	 * @param layoutView
	 *            View that represents a layout. It should be a root view of the layout.<BR>
	 *            If it is null, setting layout will fail.
	 */
	private void initLayout(View layoutView) {
		if (layoutView != null) {
			mRootLayout = (FrameLayout)layoutView.findViewById(R.id.root);
			mCircleLayout = (RelativeLayout)layoutView.findViewById(R.id.circlelayout);
			mContent = (RelativeLayout)layoutView.findViewById(R.id.content);

		} else {
			Log.e(TAG, "Cannot set the layout: root view is null");
		}
	}

	private void changeTitleViewHeight(float heightRatio) {
		if (mTitle != null) {
			if (heightRatio <= 0) // adjust the height
				heightRatio = fixedTitleRatio;

			int titleAreaHeight = (int)(mFullSize * heightRatio);

			// add a title into the top of the circle layout
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
			        LayoutParams.MATCH_PARENT, titleAreaHeight);
			params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1);
			mTitle.getView().setLayoutParams(params);

			adjustContentLayout();
		}
	}

	/**
	 * adds a title view to the layout.
	 * <P>
	 * It is called by {@link #setTitle(String)} to adjust the circle layout. The title view is
	 * added on the top of the layout and the content window will be on the below of the title view.
	 * 
	 * @param titleView
	 *            title view to be added. <BR>
	 *            If it is null, the circle layout will not change.
	 * @param heightRatio
	 *            ratio of the title bar. <BR>
	 *            If it is less or equal to 0, the height ratio of the title bar will be 0.2 of
	 *            QuickCircle diameter.
	 */
	private void addTitleView(QCircleTitle titleView, float heightRatio) {
		if (mCircleLayout != null) {
			if (heightRatio <= 0) // adjust the height
				heightRatio = fixedTitleRatio;

			int titleAreaHeight = (int)(mFullSize * heightRatio);

			// add a title into the top of the circle layout
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
			        LayoutParams.MATCH_PARENT, titleAreaHeight);
			params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1);
			mCircleLayout.addView(titleView.getView(), 0, params);

			adjustContentLayout(); // change the size of the content because of adding a title
		}
	}

	/**
	 * adds a button view to the layout.
	 * <P>
	 * It is called by {@link QCircleTemplate#setBackbutton()} to adjust the circle layout. The
	 * button view is added on the bottom of the layout and the content window will be on the top of
	 * the button view.
	 * 
	 * @param buttonView
	 *            button view to be added. <BR>
	 *            If it is null, the circle layout will not change.
	 */
	private void addBackButtonView(QCircleBackButton buttonView) {
		if (mCircleLayout != null) {
			int buttonAreaHeight = (int)(mFullSize * fixedButtonRatio);

			// add a button into the bottom of the circle layout
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mFullSize,
			        buttonAreaHeight);
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
			mCircleLayout.addView(buttonView.getView(), params);

			adjustContentLayout(); // change the size of the content because of adding a button
		}
	}

	/**
	 * adjust the circle layout when a title view or a button view is added.
	 * <P>
	 * It is called by {@link #addTitleView(QCircleTitle)} or
	 * {@link #addBackButtonView(QCircleBackButton)). It changes the relative position of the
	 * content window.
	 */
	protected void adjustContentLayout() {
		// get current size of the content
		RelativeLayout.LayoutParams contentParams = new RelativeLayout.LayoutParams(
		        mContent.getLayoutParams().width, mContent.getLayoutParams().height);

		// set relative layout parameters
		if (mBackButton != null) {
			contentParams.addRule(RelativeLayout.ABOVE, mBackButton.getId());
		}
		if (mTitle != null) {
			contentParams.addRule(RelativeLayout.BELOW, mTitle.getId());
		}

		// update layout parameters
		mContent.setLayoutParams(contentParams);
	}

	/**
	 * locates the circle on the correct position. The correct position depends on phone model.
	 * <P>
	 */
	protected void setCircleLayout() {
		// 1. get circle size and Y offset

		// circle size
		int id = mContext.getResources().getIdentifier("config_circle_diameter", "dimen",
		        "com.lge.internal");
		mFullSize = mContext.getResources().getDimensionPixelSize(id);

		// y position (in G3, y position = y offset)
		id = mContext.getResources().getIdentifier("config_circle_window_y_pos", "dimen",
		        "com.lge.internal");
		mYpos = mContext.getResources().getDimensionPixelSize(id);

		// adjust Y offset for the model
		id = mContext.getResources().getIdentifier("config_circle_window_height", "dimen",
		        "com.lge.internal");
		int height = mContext.getResources().getDimensionPixelSize(id);	
		mTopOffset = mYpos + ((height - mFullSize) / 2);


		
		// 2. adjust the circle layout for the model
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mFullSize, mFullSize);

		// circle image
		View circleView = (View)mRootLayout.findViewById(R.id.circle);
		String device = android.os.Build.DEVICE;
		if (circleView != null) {
			if(device.equalsIgnoreCase(DEVICE_G3)|| device.equalsIgnoreCase(DEVICE_T6)){
				params.topMargin = 0;	
			}
			else{
				params.topMargin = mTopOffset;
			}
			params.gravity = Gravity.CENTER_HORIZONTAL;
			circleView.setLayoutParams(params);

		} else {
			Log.w(TAG, "Cannot found circle image");
		}
		// over-circle layout
		circleView = (View)mRootLayout.findViewById(R.id.circlelayout);
		if (circleView != null) {
			circleView.setLayoutParams(params);
		} else {
			Log.w(TAG, "Cannot found circle layout");
		}
	}

	/**
	 * sets the design template.
	 * <P>
	 * 
	 * @param type
	 *            design template type to be set
	 */
	protected void setTemplateType(TemplateType type) {
		mLayoutType = type;

		if (mContext != null) {
			View layoutView = null;
			switch (mLayoutType) {
				case CIRCLE_EMPTY:
					Log.d("test", "class name = "
					        + mContext.getApplicationContext().getPackageName());
					layoutView = (View)((Activity)mContext).getLayoutInflater().inflate(
					        R.layout.qcircle_empty, null);
					break;
				case CIRCLE_COMPLEX:
					layoutView = (View)((Activity)mContext).getLayoutInflater().inflate(
					        R.layout.qcircle_complex, null);
					break;
				case CIRCLE_HORIZONTAL:
					layoutView = (View)((Activity)mContext).getLayoutInflater().inflate(
					        R.layout.qcircle_horizontal, null);
					break;
				case CIRCLE_VERTICAL:
					layoutView = (View)((Activity)mContext).getLayoutInflater().inflate(
					        R.layout.qcircle_vertical, null);
					break;
				case CIRCLE_SIDEBAR:
					layoutView = (View)((Activity)mContext).getLayoutInflater().inflate(
					        R.layout.qcircle_sidebar, null);
					break;
			}

			initLayout(layoutView); // update root layout with a new template
			setCircleLayout(); // set the circle layout
		} else {
			Log.w(TAG, "Cannot set the layout. Context is null");
		}

	}

	/**
	 * registers cover event broadcasts.
	 * <P>
	 * The a cover-closed event will make the circle shown and a cover-opened event will make the
	 * circle hidden after you invoke this method. The full screen intent will starts if you set a
	 * fullscreen intent by calling {@link #setFullscreenIntent(Intent)}.
	 * 
	 */
	
	protected void registerIntentReceiver() {

		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// Log.d(TAG, "onReceive: " + intent.getAction());
				String action = intent.getAction();
				if (action == null) {
					return;
				}

				int quickCaseType = Settings.Global.getInt(mContext.getContentResolver(),
				        "cover_type", 0);
				int quickCircleEnabled = Settings.Global.getInt(mContext.getContentResolver(),
						"quick_view_enable", 0);

				// Receives a LG QCirle intent for the cover event
				if (ACTION_ACCESSORY_COVER_EVENT.equals(action)
				        && quickCaseType == QUICKCOVERSETTINGS_QUICKCIRCLE
				        && quickCircleEnabled == QUICKCOVERSETTINGS_USEQUICKCIRCLE) {

					// Gets the current state of the cover
					int quickCoverState = intent.getIntExtra(EXTRA_ACCESSORY_COVER_STATE,
					        EXTRA_ACCESSORY_COVER_OPENED);
					
					// Log.d(TAG, "quickCoverState= " + quickCoverState);
					if (quickCoverState == EXTRA_ACCESSORY_COVER_CLOSED) { // closed
//						} else
//							intenta.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						setQuickCircleWindowParam();
					} else if (quickCoverState == EXTRA_ACCESSORY_COVER_OPENED) { // opened
						if (mFullscreenIntent != null && mContext != null) {
							mContext.unregisterReceiver(this);
							mContext.startActivity(mFullscreenIntent);							 
						}
						if (mContext instanceof Activity) {
							((Activity)mContext).finish();
						}
					}
				}
			}
		};

		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_ACCESSORY_COVER_EVENT);

		// Register a broadcast receiver with the system
		mContext.registerReceiver(mReceiver, filter);
	}
	
	/**
	 * makes the circle shown even if the screen is locked.
	 */
	private void setQuickCircleWindowParam() {
		if (mContext != null && mContext instanceof Activity) {
			Window win = ((Activity)mContext).getWindow();
			if (win != null) {
				// Show the sample application view on top
				win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				        | WindowManager.LayoutParams.FLAG_FULLSCREEN
				        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			}
		}
	}

	/*
	 * @deprecated
	 */
	protected boolean setLayoutById(int id, View view) {
		boolean result = false;
		if (mContent != null) {
			View targetView = (View)getLayoutById(id);
			if (targetView != null) {
				result = true;
				LayoutParams params = targetView.getLayoutParams();
				view.setId(targetView.getId());

				ViewParent parent = (ViewParent)targetView.getParent();
				if (parent instanceof LinearLayout) {
					LinearLayout layout = (LinearLayout)parent;
					layout.removeView(targetView);
					layout.addView(view, params);
				} else { // maybe RelativeLayout
					RelativeLayout layout = (RelativeLayout)parent;
					layout.removeView(targetView);
					layout.addView(view, params);
				}

				// mContent.removeView(targetView);
				// mContent.addView(view, params);
			}
		}

		return result;
	}

	/**
	 * loads external layout created by users (or user).
	 * <P>
	 * 
	 * @param templateId
	 *            ID of layout. <BR>
	 *            It should be larger than 0.
	 */
	protected void loadCustomTemplate(int templateId) {
		if (mContext != null && templateId > 0) {
			View layoutView = (View)((Activity)mContext).getLayoutInflater().inflate(templateId,
			        null);

			if (layoutView == null)
				Log.w(TAG, "Cannot set the custom layout: " + templateId);
			else {
				initLayout(layoutView);
				setCircleLayout();
			}
		} else {
			Log.w(TAG, "Cannot set the custom layout. Context is null");
		}
	}
}
