package company.fortytwo.airquality;

import android.support.annotation.NonNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AqicnApiClient {

    private final AqicnService service;
    private final Retrofit retrofit;

    public AqicnApiClient() {
        this.retrofit = buildRetrofit();
        this.service = retrofit.create(AqicnService.class);
    }

    @NonNull
    private Retrofit buildRetrofit() {
        Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

        return new Retrofit.Builder()
            .client(new OkHttpClient.Builder().build())
            .baseUrl("https://api.waqi.info")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    }

    public AqicnService getAqicnService() {
        return service;
    }
}
