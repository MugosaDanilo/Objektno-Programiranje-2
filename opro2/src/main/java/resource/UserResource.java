package resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.AppUser;
import repository.UserRepository;

import java.util.List;

@Path("/user/")
public class UserResource {

    @Inject
    private UserRepository userRepository;

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

}
