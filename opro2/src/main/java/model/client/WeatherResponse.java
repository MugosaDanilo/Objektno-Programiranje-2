package model.client;

import java.util.ArrayList;
import java.util.List;

public class WeatherResponse {
    private String temperature;
    private String wind;
    private String description;
    private List<ForecastResponse> forecast;

    public WeatherResponse() {
    }

    public WeatherResponse(String temperature, String wind, String description, List<ForecastResponse> forecast) {
        this.temperature = temperature;
        this.wind = wind;
        this.description = description;
        this.forecast = new ArrayList<>();
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

    public List<ForecastResponse> getForecast() {
        return forecast;
    }

    public void setForecast(List<ForecastResponse> forecast) {
        this.forecast = forecast;
    }
}


/*
{
  "temperature": "+22 °C",
  "wind": "8 km/h",
  "description": "Partly cloudy",
  "forecast": [
    {
      "day": "1",
      "temperature": "+19 °C",
      "wind": "12 km/h"
    },
    {
      "day": "2",
      "temperature": "20 °C",
      "wind": "8 km/h"
    },
    {
      "day": "3",
      "temperature": "19 °C",
      "wind": "7 km/h"
    }
  ]
}


 */
