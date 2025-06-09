package resource;

import exception.WeatherException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.*;
import model.client.CountryResponse;
import model.client.HolidayResponse;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import repository.HolidayRepository;
import repository.UserRepository;
import repository.WeatherRepository;
import restclient.CountryClient;
import restclient.HolidayClient;
import restclient.WeatherClient;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Path("/user/")
public class UserResource {

    private static final String UPLOAD_DIR = "/uploads";

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

    @GET
    @Path("/getUserById/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") Long id) {
        AppUser user = userRepository.findById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }

        if (user.getUploadedFilePath() != null) {
            File file = new File(user.getUploadedFilePath());
            if (file.exists()) {
                user.setUploadedFile(file);
                try {
                    //NOTE(danilo): Entitet u kom se nalazi putanja do fajla ce imati i varijablu koja predstavlja taj fajl i u koju ce se ucitavati fajl sa fajlsistema
                    byte[] fileBytes = Files.readAllBytes(file.toPath());
                    String base64File = Base64.getEncoder().encodeToString(fileBytes);
                    user.setFileContentBase64(base64File);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return Response.ok(user).build();
    }


    @POST
    @Path("/uploadFile")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public Response uploadFile(@QueryParam("id") Long userId,
                               @RestForm("file") FileUpload file,
                               @RestForm("fileName") String fileName) {
        if (userId == null || file == null || fileName == null || fileName.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing file or fileName or user ID").build();
        }

        AppUser user = userRepository.findById(userId);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }

        try {
            String extension = getExtensionFromMimeType(file.contentType());
            if (!fileName.contains(".") && extension != null) {
                fileName += extension;
            }

            File dir = new File("uploads");
            if (!dir.exists()) dir.mkdirs();

            File dest = new File(dir, fileName);
            Files.copy(file.uploadedFile(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            user.setUploadedFilePath(dest.getAbsolutePath());

            userRepository.createUser(user);

            return Response.ok("File uploaded and user updated").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity("Upload failed: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/file/{id}")
    public Response getUserFile(@PathParam("id") Long id) {
        AppUser user = userRepository.findById(id);
        if (user == null || user.getUploadedFilePath() == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("File not found").build();
        }

        File file = new File(user.getUploadedFilePath());
        if (!file.exists()) {
            return Response.status(Response.Status.NOT_FOUND).entity("File not found on disk").build();
        }

        try {
            String mimeType = Files.probeContentType(file.toPath());
            if (mimeType == null) {
                mimeType = "application/octet-stream"; // fallback
            }
            return Response.ok(file, mimeType)
                    .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity("Failed to load file").build();
        }
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

    private String getExtensionFromMimeType(String mimeType) {
        if (mimeType == null) return null;
        switch (mimeType) {
            case "image/jpeg":
                return ".jpg";
            case "image/png":
                return ".png";
            case "image/gif":
                return ".gif";
            case "application/pdf":
                return ".pdf";
            default:
                return null;
        }
    }
}
