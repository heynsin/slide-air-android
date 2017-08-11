package company.fortytwo.airquality;

import android.content.Context;
import android.content.SharedPreferences;

import io.slde.sdk.android.core.io.GsonSerializer;

public class SharedPreferencesManager {

    private final static String KEY_AIR_QUALITY = "saved_data";

    private final SharedPreferences sharedPreferences;
    private final GsonSerializer<AirQuality> serializer;

    public SharedPreferencesManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(context.getPackageName() + ".airQuality", Context.MODE_PRIVATE);
        this.serializer = new GsonSerializer<>(AirQuality.class);
    }

    public boolean save(AirQuality data) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_AIR_QUALITY, serializer.serialize(data));
        return editor.commit();
    }

    public AirQuality get() {
        final String data = sharedPreferences.getString(KEY_AIR_QUALITY, null);
        return serializer.deserialize(data);
    }
}
