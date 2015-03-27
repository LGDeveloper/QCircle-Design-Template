package com.lge.qcircle.utils;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;



/**
 * The {@code QCircleFeature} class provides useful methods for Quick Circle applications.
 * Created by sujin.cho on 2015-03-11.
 */
public class QCircleFeature {

    protected static final String TAG = "QCircleFeature";

    //needs to remove intent
    private static Intent numberBadge = null;
    private static final String ACTION_UPDATE_NOTIFICATION = "com.lge.launcher.intent.action.BADGE_COUNT_UPDATE";
    private static final int G3_DIAMETER = 1046;
    private static int mFullSize = 0; // circle diameter

    /**
     * Activates a number badge with a count.
     * The number badge will show up on the icon.
     * @param count
     */
    public void activateNumberBadge(Context context, int count)
    {
        if(numberBadge == null)
            numberBadge = new Intent(ACTION_UPDATE_NOTIFICATION);
        numberBadge.putExtra("badge_count_package_name", context.getPackageName());
        numberBadge.putExtra("badge_count_class_name",  context.getClass().getName());
        numberBadge.putExtra("badge_count", count);
        context.sendBroadcast(numberBadge);
    }

    /**
     * Changes a count number of a number badge.
     * @param context
     * @param count
     */
    public void setNumberBadge(Context context, int count)
    {
        if(numberBadge == null)
        {
            activateNumberBadge(context,count);
            return;
        }
        numberBadge.putExtra("badge_count", count);
        context.sendBroadcast(numberBadge);
    }

    /**
     * Takes a pixel value implemented for the current model.
     * The argument value has to be a pixel values.
     * Returns a relative pixel value to support other Quick Circle models which have different densities and screen sizes.
     * @param value
     * @return
     */
    public static int getRelativePixelValue(Context context, int value)
    {
        getTemplateDiameter(context);
        return (int)(((double) mFullSize/G3_DIAMETER) * (double)(value));
    }


    /**
     * Checks the availability of  Quick Circle case.
     * <P>
     * Checks whether a smart case is available and if it is, check the case type.
     * @param context
     */
    public static boolean isQuickCircleAvailable(Context context)
    {

        boolean smartcaseEnabled = false;
        int smartcaseType = 0;

        if(context != null){
            ContentResolver contentResolver = context.getContentResolver();

            if(contentResolver == null)
            {
                Log.e(TAG, "Content Resolver is null");
                return false;
            }
            //default is 1. (LG framework setting. When user gets a phone, the case is enable as default)
            smartcaseEnabled = Settings.Global.getInt(contentResolver,"quick_view_enable", 1) == 1 ? true : false;
            if(!smartcaseEnabled) {
                Log.i(TAG, "No smart case available");
                return false;
            }

            smartcaseType = Settings.Global.getInt(contentResolver, "cover_type", 0);
            if(smartcaseType != 3){
                Log.i(TAG, "Case type is not Quick Circle");
                return false;
            }
            return true;
        }
        else{

            Log.e(TAG, "Context is null!!");
            return false;
        }
    }

    /**
     * locates the circle on the correct position. The correct position depends on phone model.
     * <p>
     * @author sujin.cho
     */
    private static void getTemplateDiameter(Context context)
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
