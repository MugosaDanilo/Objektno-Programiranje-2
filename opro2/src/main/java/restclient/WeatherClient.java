package restclient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import model.client.HolidayResponse;
import model.client.WeatherResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@Path("")
@RegisterRestClient(configKey = "weather-api")
public interface WeatherClient {
    @GET
    @Path("/{city}")
    WeatherResponse getForecast(@PathParam("city") String city);
}
