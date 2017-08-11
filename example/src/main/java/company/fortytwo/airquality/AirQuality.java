package company.fortytwo.airquality;

import com.google.gson.annotations.SerializedName;

import static company.fortytwo.airquality.AirQuality.AirQualityLevel.Bad;
import static company.fortytwo.airquality.AirQuality.AirQualityLevel.Best;
import static company.fortytwo.airquality.AirQuality.AirQualityLevel.Better;
import static company.fortytwo.airquality.AirQuality.AirQualityLevel.Good;
import static company.fortytwo.airquality.AirQuality.AirQualityLevel.Normal;
import static company.fortytwo.airquality.AirQuality.AirQualityLevel.Terrible;
import static company.fortytwo.airquality.AirQuality.AirQualityLevel.Worse;
import static company.fortytwo.airquality.AirQuality.AirQualityLevel.Worst;

public class AirQuality {

    public enum AirQualityLevel {
        Best, Better, Good, Normal, Bad, Worse, Worst, Terrible
    }

    @SerializedName("data")
    private AirData data;

    public int getAirQuality() {
        try {
            return Integer.parseInt(data.airQuality);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public AirQualityLevel getAirQualityLevel() {
        int airQuality = getAirQuality();
        if (airQuality >= 0 && airQuality <= 15) {
            return Best;
        } else if (airQuality > 15 && airQuality <= 30) {
            return Better;
        } else if (airQuality > 30 && airQuality <= 40) {
            return Good;
        } else if (airQuality > 40 && airQuality <= 50) {
            return Normal;
        } else if (airQuality > 50 && airQuality <= 75) {
            return Bad;
        } else if (airQuality > 75 && airQuality <= 100) {
            return Worse;
        } else if (airQuality > 100 && airQuality <= 150) {
            return Worst;
        } else {
            return Terrible;
        }
    }

    public String getCity() {
        return data.city.name;
    }

    public String getLink() {
        return data.city.link;
    }

    private class AirData {
        @SerializedName("idx")
        private int id;
        @SerializedName("aqi")
        private String airQuality;
        @SerializedName("time")
        private Time time;
        @SerializedName("city")
        private City city;
        @SerializedName("attributions")
        private Object attributions;
        @SerializedName("iaqi")
        private Object details;
    }

    private class Time {
        @SerializedName("s")
        private String localTime;
        @SerializedName("tz")
        private String timezone;
    }

    private class City {
        @SerializedName("name")
        private String name;
        @SerializedName("url")
        private String link;
    }
}
