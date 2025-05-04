package resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.AppUser;
import model.Holiday;
import model.HolidayType;
import model.client.CountryResponse;
import model.client.HolidayResponse;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import repository.HolidayRepository;
import repository.UserRepository;
import restclient.CountryClient;
import restclient.HolidayClient;

import java.util.List;
import java.util.stream.Collectors;

@Path("/user/")
public class UserResource {

    @Inject
    private UserRepository userRepository;

    @Inject
    private HolidayRepository holidayRepository;

    @RestClient
    private CountryClient countryClient;

    @RestClient
    private HolidayClient holidayClient;

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
