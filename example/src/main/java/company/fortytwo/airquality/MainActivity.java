package company.fortytwo.airquality;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import company.fortytwo.slide_example.R;
import io.slde.sdk.android.Slide;
import io.slde.sdk.android.core.exception.SlideException;
import io.slde.sdk.android.core.model.Session;
import io.slde.sdk.android.core.network.Callback;
import io.slde.sdk.android.core.network.Result;
import io.slde.sdk.android.ui.setting.SlideSettingDialogFragment;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    final static String TOKEN = "9631ff7804f8ff1676bc632325c57e284ee931d3";
    private DrawerLayout drawerLayout;
    private ListView drawerView;
    private ExampleMenuAdapter adapter;
    private LinearLayout airStatusBackground;
    private TextView cityName;
    private TextView airStatusNumber;
    private TextView airStatusText;

    private AqicnApiClient apiClient;
    private LocationManager locationManager;
    private SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.apiClient = new AqicnApiClient();
        this.sharedPreferencesManager = new SharedPreferencesManager(this);

        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.drawerView = (ListView) findViewById(R.id.drawer_view);
        this.adapter = new ExampleMenuAdapter(this);

        this.airStatusBackground = (LinearLayout) findViewById(R.id.air_status_background);
        this.cityName = (TextView) findViewById(R.id.city_name);
        this.airStatusNumber = (TextView) findViewById(R.id.air_status_number);
        this.airStatusText = (TextView) findViewById(R.id.air_status_text);
        cityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AirQuality currentAirQuality = sharedPreferencesManager.get();
                if (currentAirQuality != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentAirQuality.getLink()));
                    startActivity(intent);
                }
            }
        });

        drawerView.setAdapter(adapter);
        drawerView.setOnItemClickListener(new DrawerItemClickListener());

        final Intent intent = getIntent();
        if (intent != null) {
            handleIntent(intent);
        }

        updateViews(sharedPreferencesManager.get());
        initLocation();
    }

    private void updateViews(@Nullable AirQuality airQuality) {
        if (airQuality != null) {
            cityName.setText(airQuality.getCity());
            airStatusNumber.setText(String.valueOf(airQuality.getAirQuality()));
            switch (airQuality.getAirQualityLevel()) {
                case Best:
                    airStatusBackground.setBackgroundResource(R.color.air_quality_level_best);
                    airStatusText.setText(R.string.air_quality_level_best);
                    break;
                case Better:
                    airStatusBackground.setBackgroundResource(R.color.air_quality_level_better);
                    airStatusText.setText(R.string.air_quality_level_better);
                    break;
                case Good:
                    airStatusBackground.setBackgroundResource(R.color.air_quality_level_good);
                    airStatusText.setText(R.string.air_quality_level_good);
                    break;
                case Normal:
                    airStatusBackground.setBackgroundResource(R.color.air_quality_level_normal);
                    airStatusText.setText(R.string.air_quality_level_normal);
                    break;
                case Bad:
                    airStatusBackground.setBackgroundResource(R.color.air_quality_level_bad);
                    airStatusText.setText(R.string.air_quality_level_bad);
                    break;
                case Worse:
                    airStatusBackground.setBackgroundResource(R.color.air_quality_level_worse);
                    airStatusText.setText(R.string.air_quality_level_worse);
                    break;
                case Worst:
                    airStatusBackground.setBackgroundResource(R.color.air_quality_level_worst);
                    airStatusText.setText(R.string.air_quality_level_worst);
                    break;
                case Terrible:
                    airStatusBackground.setBackgroundResource(R.color.air_quality_level_terrible);
                    airStatusText.setText(R.string.air_quality_level_terrible);
                    break;
            }

        }
    }

    private void initLocation() {
        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        } else {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                requestAirQuality(location);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
            }
        }
    }

    private void requestAirQuality(@Nullable Location location) {
        if (location != null) {
            apiClient.getAqicnService()
                .airQuality(location.getLatitude(), location.getLongitude(), TOKEN)
//                .airQuality(28.7500749, 77.1176652, TOKEN)
                .enqueue(airQualityCallback);
        } else {
            apiClient.getAqicnService().airQuality("delhi", TOKEN).enqueue(airQualityCallback);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent != null) {
            handleIntent(intent);
        }
    }

    private void handleIntent(@NonNull Intent intent) {
        final String action = intent.getAction();
        final Uri data = intent.getData();
        String url;

        if (Intent.ACTION_VIEW.equals(action) && data != null
            && (url = data.getQueryParameter("url")) != null) {
            showDialog(url);
        }
    }

    private void showDialog(@NonNull final String url) {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        final View dialogLayout = getLayoutInflater().inflate(R.layout.view_dialog, null);

        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialogLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                final ImageView imageView = (ImageView) dialog.findViewById(R.id.image_view);
                Picasso.with(MainActivity.this).load(url).into(imageView);
            }
        });

        dialog.show();
    }

    private void logIn() {
        final ProgressDialog loadingDialog = new ProgressDialog(this, R.style.Dialog_Loading);
        loadingDialog.setMessage(getString(R.string.loading));
        loadingDialog.setCancelable(false);
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingDialog.show();

        Slide.logIn(new Callback<Session>() {
            @Override
            public void success(Result<Session> result) {
                Slide.activate();
                adapter.notifyDataSetChanged();
                loadingDialog.dismiss();
            }

            @Override
            public void failure(SlideException exception) {
                adapter.notifyDataSetChanged();
                loadingDialog.dismiss();

                final String message = new StringBuilder(getString(R.string.login_failure))
                    .append("\n").append(exception.getMessage()).toString();
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void logOut() {
        Slide.logOut();
        adapter.notifyDataSetChanged();
    }

    private void showSettingDialog() {
        final SlideSettingDialogFragment settingDialog = new SlideSettingDialogFragment();
        settingDialog.show(getSupportFragmentManager(), null);
        settingDialog.setOnSettingChangedListener(new SlideSettingDialogFragment.OnSettingChangedListener() {
            @Override
            public void onSettingChanged(boolean active) {
                adapter.notifyDataSetChanged();
            }
        });
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == ExampleMenuAdapter.MENU_LOGIN) {
                if (Slide.isLoggedIn()) {
                    logOut();
                } else {
                    logIn();
                }
            } else if (position == ExampleMenuAdapter.MENU_LOCK_SCREEN) {
                if (Slide.isLoggedIn()) {
                    showSettingDialog();
                } else {
                    Toast.makeText(parent.getContext(),
                        R.string.request_log_in, Toast.LENGTH_LONG).show();
                }
                drawerLayout.closeDrawer(drawerView);
            }
        }
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location loc) {
            requestAirQuality(loc);
            locationManager.removeUpdates(locationListener);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    private retrofit2.Callback<AirQuality> airQualityCallback = new retrofit2.Callback<AirQuality>() {
        @Override
        public void onResponse(Call<AirQuality> call, Response<AirQuality> response) {
            if (response.isSuccessful()) {
                AirQuality air = response.body();
                sharedPreferencesManager.save(air);
                updateViews(air);
            }
        }

        @Override
        public void onFailure(Call<AirQuality> call, Throwable t) {
            Log.d("test", "onFailure t=" + t.getMessage());
        }
    };
}
