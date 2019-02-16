package devkit.blade.vuzix.com.blade_template_app;

import com.vuzix.hud.resources.DynamicThemeApplication;

/**
 * Main Application reference in the Manifest. This is the main application instance that gets loaded
 * and those properties and instantiation are passed to the rest of the application.
 * The DynamicThemeApplication allow the user to intercept and modify the theme base on the BladeOS
 * Interpretation of Light around the user. This allows for dynamic theme modification for different
 * light environment situations.
 * For more information on Android manifest definitions: https://developer.android.com/guide/topics/manifest/manifest-intro
 * For more information on the DynamicThemeApplication read the JavaDocs in Android Studio or download the
 * Java docs at:  https://www.vuzix.com/support/Downloads_Drivers
 */
public class BladeSampleApplication extends DynamicThemeApplication {

    @Override
    protected int getNormalThemeResId() {
        return R.style.AppTheme;
    }

    @Override
    protected int getLightThemeResId() {
        return R.style.AppTheme_Light;
    }
}
