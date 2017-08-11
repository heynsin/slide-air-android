package company.fortytwo.airquality;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import company.fortytwo.slide_example.R;
import io.slde.sdk.android.Slide;

/**
 * Class contains menu information.
 */

public class ExampleMenuAdapter extends BaseAdapter {
    final static public int MENU_LOGIN = 6;
    final static public int MENU_LOCK_SCREEN = 7;

    final String[] menuArray;

    public ExampleMenuAdapter(Context context) {
        this.menuArray = context.getResources().getStringArray(R.array.menu);
    }

    @Override
    public int getCount() {
        return menuArray.length;
    }

    @Override
    public Object getItem(int position) {
        return menuArray[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        TextView textView = (TextView) convertView;
        if (textView == null) {
            final LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            textView = (TextView) inflater.inflate(R.layout.menu_item_view, null);
        }

        if (position == MENU_LOGIN) {
            final String text = context.getString(Slide.isLoggedIn()
                ? R.string.logged_in : R.string.login);

            textView.setText(text);
            textView.setEnabled(true);
        } else if (position == MENU_LOCK_SCREEN) {
            final String text = context.getString(Slide.isActive()
                ? R.string.lock_creen_on : R.string.lock_creen_off);

            textView.setText(text);
            textView.setEnabled(Slide.isLoggedIn());
        } else {
            final String text = (String) getItem(position);

            textView.setText(text);
            textView.setEnabled(false);
        }

        return textView;
    }
}
