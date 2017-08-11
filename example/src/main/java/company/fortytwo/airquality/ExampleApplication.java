package company.fortytwo.airquality;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;

import company.fortytwo.slide_example.BuildConfig;
import company.fortytwo.slide_example.R;
import io.slde.sdk.android.Slide;
import io.slde.sdk.android.core.config.SlideConfig;
import io.slde.sdk.android.ui.lockscreen.LockScreenIntentBuilder;

/**
 * Application class for Slide example.
 */

public class ExampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        final SlideConfig config = new SlideConfig.Builder(getApplicationContext())
            .appIcon(R.drawable.icon_app, intent)
            .appName(getString(R.string.app_name))
            .build();

        Slide.init(getApplicationContext(), BuildConfig.SLIDE_APP_KEY, config);

        final PendingIntent lockScreenIntent = new LockScreenIntentBuilder(getApplicationContext())
            .controlFragmentClass(ExampleControlFragment.class)
            .build();

        Slide.setLockScreenIntent(lockScreenIntent);
    }
}
