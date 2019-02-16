package devkit.blade.vuzix.com.blade_template_app;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * BroadcastReceiver to get THe UI_DISPLAY_MODE Change the BladeOS will transmit to let the Applications
 * know that there is a new Enviroment change.
 * This BroadcastReceiver will send a Update App Widget Broadcast that will then be caught by our
 * own widget.
 */
public class Template_Widget_Update_Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //If your receiver goes get more that 1 event you will need to check the receiver type.
        //If you only register your receiver to get one event you don't have to worry.

            Intent updateIntent = new Intent();
            updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            Log.d("Widget_RECEIVER", "Sending Intent");
            context.sendBroadcast(updateIntent);
            Log.d("Widget_RECEIVER", "Intent Send");

    }

}
