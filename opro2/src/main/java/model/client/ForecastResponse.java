package model.client;

public class ForecastResponse {
    private String temperature;
    private String wind;
    private String description;

    public ForecastResponse(String temperature, String wind, String description) {
        this.temperature = temperature;
        this.wind = wind;
        this.description = description;
    }

    public ForecastResponse() {
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
}
