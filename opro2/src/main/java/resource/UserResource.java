package resource;

import exception.WeatherException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.*;
import model.client.CountryResponse;
import model.client.ForecastResponse;
import model.client.HolidayResponse;
import model.client.WeatherResponse;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import repository.HolidayRepository;
import repository.UserRepository;
import repository.WeatherRepository;
import restclient.CountryClient;
import restclient.HolidayClient;
import restclient.WeatherClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/user/")
public class UserResource {

    @Inject
    WeatherRepository weatherRepository;
    @Inject
    private UserRepository userRepository;

    @Inject
    private HolidayRepository holidayRepository;

    @RestClient
    private CountryClient countryClient;

    @RestClient
    private HolidayClient holidayClient;

    @RestClient
    private WeatherClient weatherClient;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/addUser")
    public Response createUser(AppUser user) {
        AppUser au = userRepository.createUser(user);
        return Response.ok().entity(au).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getAllUsers")
    public Response getAllUsers() {
        List<AppUser> users = userRepository.getAllUsers();

        return Response.ok().entity(users).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getAvailableCountries")
    public Response getAvailableCountries() {
        List<CountryResponse> countryResponse = countryClient.getAvailableCountries();
        return Response.ok().entity(countryResponse).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/NextPublicHolidays/{countryCode}")
    public Response getNextPublicHolidays(@PathParam("countryCode") String countryCode) {
        List<HolidayResponse> holidayResponses = holidayClient.getNextPublicHolidays(countryCode);

        for (HolidayResponse hr : holidayResponses) {
            Holiday holiday = convertToHolidayEntity(hr);
            if (!holidayRepository.existsByDateAndCountryCode(holiday.getDate(), holiday.getCountryCode())) {
                holidayRepository.createHoliday(holiday);
            }
        }

        return Response.ok().entity(holidayResponses).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getForecast/")
    public Response getrecast(@QueryParam("city") String city) {

        WeatherResponse weatherResponse = weatherClient.getForecast(city);
        Weather w = convertToWeatherEntity(weatherResponse);
        w.setCityName(city);

        if(!weatherRepository.existsByCity(w.getCityName())) {
            weatherRepository.createWeather(w);
        } else {
           System.out.println("err");
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

    private Forecast convertToForecastEntity(ForecastResponse fr) {
        Forecast forecast = new Forecast();
        forecast.setDescription(fr.getDescription());
        forecast.setWind(fr.getWind());
        forecast.setTemperature(fr.getTemperature());

        return forecast;
    }

    private Holiday convertToHolidayEntity(HolidayResponse hr) {
        Holiday holiday = new Holiday();
        holiday.setDate(hr.getDate());
        holiday.setLocalName(hr.getLocalName());
        holiday.setName(hr.getName());
        holiday.setCountryCode(hr.getCountryCode());
        holiday.setFixed(hr.isFixed());
        holiday.setGlobal(hr.isGlobal());
        holiday.setLaunchYear(hr.getLaunchYear());

        List<HolidayType> types = hr.getTypes().stream()
                .map(type -> {
                    HolidayType holidayType = new HolidayType();
                    holidayType.setType(type);
                    holidayType.setHoliday(holiday);
                    return holidayType;
                })
                .collect(Collectors.toList());

        holiday.setTypes(types);
        return holiday;
    }
}
