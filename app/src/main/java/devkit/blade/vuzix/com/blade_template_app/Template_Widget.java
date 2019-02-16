package devkit.blade.vuzix.com.blade_template_app;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 * Utilizes the Normal AppWidgetProvider interface for the widget instantiation on the BladeOS
 * Launcher.
 */
public class Template_Widget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        Log.d("TEMPLETE_Widget", "Widget Update from UpdateAppWidget");
        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.template_widget_light);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        update(context,isLightMode(context));
    }


    /**
     * Normal Override of the WidgetProvider onUpdate. Notice we don't do all the work in that method.
     * We send the information to other methods to change the UI. This can be done in any way you desire.
     * @param context
     * @param appWidgetManager
     * @param appWidgetIds
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        // Play around with the passed parameters to see how it affects the widget
        Log.d("TEMPLETE_Widget", "Widget onUpdate Received");
        update(context,isLightMode(context));
    }

    //Methods to update the Widget UI. All end in the Main Update function with all parameters.
    public static void update(Context context, boolean islightMode)
    {
        update(context,null,null,islightMode);
    }

    /**
     * Main update Class that will check all the values and perform the proper Widget UI changes as
     * required.
     * @param context Context  Used to get all the needed information form the system.
     * @param appWidgetManager AppWidgetManager used to get the needed information. Can be null.
     * @param appWidgetIds Integer array for all the appWidgetIds that are in the launcher to update.
     *                     Can be null.
     * @param isLightMode Signal from the DynamicThemeApplication on the current state of Enviroment
     *                    Mode. If null, we will get it form the Base Application.
     */
    public static void update(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, boolean isLightMode)
    {

        Log.d("TEMPLETE_Widget", "Widget Update");
        if(appWidgetManager == null && appWidgetIds == null)
        {
            Log.d("TEMPLETE_Widget", "In If with light mode: " + String.valueOf(isLightMode));
            appWidgetManager = (AppWidgetManager)context.getSystemService(Context.APPWIDGET_SERVICE);
            appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, Template_Widget.class));

            RemoteViews views = new RemoteViews(context.getPackageName(),
                    isLightMode ? R.layout.template_widget_light : R.layout.template_widget_dark);

            appWidgetManager.updateAppWidget(appWidgetIds,views);

        }else
        {
            Log.d("TEMPLETE_Widget", "In else");
            for (int appWidgetId : appWidgetIds) {
                //For the BladeOS, you should always ONLY get 1. That is due that the BladeOS Launcher
                //Only has room from 1 Main Launcher page, Called the Rail.
                updateAppWidget(context, appWidgetManager, appWidgetId);
            }
        }

    }

    /**
     * Your widget will automactly be enabled by the BladeOS Launcher if you use the correct
     * AndroidManifest tide-ins.
     * @param context
     */
    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    /**
     * The Widget will NOT be disabled until the Application is uninstall.
     * This method should never be called normally in the BladeOS.
     * @param context
     */
    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    /**
     * This method is used to utilize the DynamicThemeApplication function isLightMode to signal the
     * widget to use a different layout for when the device detects LightMode vs DarkMode environments
     * @param context Used to get the main Application Type and have access to the DynamicThemeApplication
     *                functions
     * @return It will return True if its in LightMode.
     */
    private static boolean isLightMode(Context context)
    {
        return ((BladeSampleApplication)context.getApplicationContext()).isLightMode();
    }
}

