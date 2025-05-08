package resource;

import exception.WeatherException;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Forecast;
import model.Weather;
import model.client.ForecastResponse;
import model.client.WeatherResponse;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import repository.WeatherRepository;
import restclient.WeatherClient;

@Path("/weather/")
public class WeatherResource {

    @Inject
    WeatherRepository weatherRepository;

    @RestClient
    private WeatherClient weatherClient;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getForecast/")
    public Response getForecast(@QueryParam("city") String city) {
        WeatherResponse weatherResponse = weatherClient.getForecast(city);
        Weather w = convertToWeatherEntity(weatherResponse);
        w.setCityName(city);

        try {
            weatherRepository.createWeatherIfCityNotExists(w);
        } catch (WeatherException e) {
            System.out.println(e.getMessage());
        }

        return Response.ok().entity(weatherResponse).build();
    }

    private Weather convertToWeatherEntity(WeatherResponse wr) {
        Weather weather = new Weather();
        weather.setDescription(wr.getDescription());
        weather.setWind(wr.getWind());
        weather.setTemperature(wr.getTemperature());

        return weather;
    }

}
