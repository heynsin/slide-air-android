package company.fortytwo.airquality;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import company.fortytwo.slide_example.R;
import io.slde.sdk.android.ui.lockscreen.ControlFragment;

/**
 * Lock screen adapter class for example application.
 */

public class ExampleControlFragment extends ControlFragment {
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    protected Drawable getFooterRightIcon() {
        return ContextCompat.getDrawable(getContext(), R.drawable.icon_app);
    }
}
