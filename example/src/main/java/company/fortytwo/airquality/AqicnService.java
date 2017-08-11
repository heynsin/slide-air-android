package company.fortytwo.airquality;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AqicnService {
    @GET("/feed/geo:{lat};{lng}/")
    Call<AirQuality> airQuality(@Path("lat") double latitude, @Path("lng") double longitude, @Query("token") String token);

    @GET("/feed/{city}/")
    Call<AirQuality> airQuality(@Path("city") String cityName, @Query("token") String token);

    @GET("/feed/here/")
    Call<AirQuality> airQuality(@Query("token") String token);
}
