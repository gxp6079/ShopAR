package devkit.blade.vuzix.com.blade_template_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vuzix.hud.actionmenu.ActionMenuActivity;

/**
 * Secondary Template Activity. This activity demonstrates how to create a Bottom Lock Navigation style.
 * This style is the same style used in the Vuzix Blade Settings Application.
 * This application extends the ActionMenuActivity to allow the system to create the navigation
 * options from the Menu Items.
 * For more information on the ActionMenuActivity read the JavaDocs in Android Studio or download the
 * Java docs at:  https://www.vuzix.com/support/Downloads_Drivers
 */
public class around_content_template_activity extends ActionMenuActivity {

    private int statusCount = 1;

    private MenuItem HelloMenuItem;
    private MenuItem VuzixMenuItem;
    private MenuItem BladeMenuItem;
    private TextView mainTitle;
    private TextView mainValue;
    private ImageView mainImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_around_content_template_style);


        mainTitle = findViewById(R.id.main_title);
        mainValue = findViewById(R.id.main_value);
        mainImage = findViewById(R.id.icon);
    }

    /**
     *  Main override to create the ACTION MENU. Notice that this is onCreate-ACTION-MENU. Not to be
     *  confuse with onCreate-Option-Menu which will create the basic Android menu that will not
     *  display properly in the small device screen.
     * @param menu Menu to inflate too.
     * @return Return if menu was setup correctly.
     */
    @Override
    public boolean onCreateActionMenu(Menu menu) {
        super.onCreateActionMenu(menu);
        getMenuInflater().inflate(R.menu.around_content_menu, menu);

        HelloMenuItem = menu.findItem(R.id.action_menu_hello);
        VuzixMenuItem = menu.findItem(R.id.action_menu_vuzix);
        BladeMenuItem = menu.findItem(R.id.action_menu_blade);
        menu.findItem(R.id.action_menu_lunch_center_lock).setIntent(new Intent(this, center_content_template_activity.class));
        menu.findItem(R.id.action_menu_lunch_pop_up).setIntent(new Intent(this, center_content_pop_up_menu_template_activity.class));
        updateMenuItems();

        return true;
    }

    /**
     * Method to tell the ActionMenuActivity what is the gravity(location of items inside other
     * containers). Center will place the ActionMenu Navigation on the center.
     * For more information on Gravity: https://developer.android.com/reference/android/view/Gravity
     * @return
     */
    @Override
    protected int getActionMenuGravity() {
        return Gravity.CENTER;
    }

    /**
     * Override of the ActionMenuActivity. This will tell the BladeOS which item to start at and
     * which one if the default action to start at on activity restarts.
     * @return
     */
    @Override
    protected int getDefaultAction() {
        return 0;
    }

    /**
     * Override of the ActionMenuActivity. TRUE will tell the system to always show the Action Menu in
     * the position that you ask for. If this is false, the action menu will be hidden and will be
     * presented upon the Menu Option Gesture (1 Finger hold for 1 second.)
     * https://www.vuzix.com/Developer/KnowledgeBase/Detail/65
     */
    @Override
    protected boolean alwaysShowActionMenu() {
        return true;
    }

    private void updateMenuItems() {
        if (HelloMenuItem == null) {
            return;
        }
        mainImage.setImageResource(R.drawable.baseline_info_24);

        VuzixMenuItem.setEnabled(false);
        BladeMenuItem.setEnabled(false);
    }

    //Action Menu Click events
    public void showHello(MenuItem item){

        showToast(getString(R.string.Hello));
        VuzixMenuItem.setEnabled(true);
        BladeMenuItem.setEnabled(true);
    }

    public void showVuzix(MenuItem item){
        showToast(getString(R.string.Vuzix));
    }

    public void showBlade(MenuItem item){
        showToast(getString(R.string.Blade1));
        statusCount++;

    }

    private void showToast(final String text){

        final Activity activity = this;

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Event that will be trigger by the BladeOS when the ActionItem gain focus.
     * You can use this in anyway you want. We use it to update the UI for the proper selected
     * element.
     * @param item Menu Item that had the Focus now.
     */
    @Override
    protected void onActionItemFocused(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_menu_hello:

                mainTitle.setText(getString(R.string.Hello));
                mainValue.setText(R.string.showHelloToast2);
                mainImage.setImageResource(R.drawable.baseline_android_24);

                break;
            case R.id.action_menu_vuzix:

                mainTitle.setText(getString(R.string.Vuzix));
                mainValue.setText(R.string.showVuzixToast2);
                mainImage.setImageResource(R.drawable.baseline_info_24);

                break;
            case R.id.action_menu_blade:

                mainTitle.setText(getString(R.string.Blade,statusCount));
                mainValue.setText(getString(R.string.showBladeToast2));
                mainImage.setImageResource(R.drawable.baseline_copyright_24);

                break;
            case R.id.action_menu_lunch_center_lock:

                mainTitle.setText(R.string.showCenterContent);
                mainValue.setText(getString(R.string.centerContent));
                mainImage.setImageResource(R.drawable.baseline_info_24);

                break;
        }
    }


    /**
     *  We use the onKeyUp event to override and get the keycodes we utilize to navigate to create custom
     *  navigation methods and unique way to move from place to place in the application.
     * keyCode = Main Android Keycode
     * event = Main Android Event for the keycode
     * return =If the event was handle properly.
     */
//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_ENTER:
//            case KeyEvent.KEYCODE_DPAD_CENTER:
//                finish();
//                return true;
//
//            case KeyEvent.KEYCODE_DPAD_LEFT:
//                onLeft();
//                return true;
//            case KeyEvent.KEYCODE_DPAD_RIGHT:
//                onRight();
//                return true;
//        }
//        return super.onKeyUp(keyCode, event);
//    }
//
//    private void onLeft() {
//        //Another way to override the Left and Right Swipe events.
//    }
//
//    private void onRight() {
//        //Another way to override the Left and Right Swipe events.
//    }


}
