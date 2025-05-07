package model;

import jakarta.persistence.*;
import model.client.ForecastResponse;

import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQuery(name = Weather.GET_WEATHER_FOR_CITY, query = "Select w from Weather w where w.cityName = :name")
public class Weather {

    public static final String GET_WEATHER_FOR_CITY = "Weather.getWeatherForCity";


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "weather_seq")
    private Long id;

    private String cityName;
    private String temperature;
    private String wind;
    private String description;


    @OneToMany
    private List<Forecast> forecast = new ArrayList<>();

    public Weather() {
    }

    public Weather(Long id, String cityName, String temperature, String wind, String description, List<Forecast> forecast) {
        this.id = id;
        this.cityName = cityName;
        this.temperature = temperature;
        this.wind = wind;
        this.description = description;
       // this.forecast = new ArrayList<Forecast>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Forecast> getForecast() {
        return forecast;
    }

    public void setForecast(List<Forecast> forecast) {
        this.forecast = forecast;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
