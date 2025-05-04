package resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Post;
import repository.PostRepository;

import java.util.List;

@Path("/post/")
public class PostResource {
    @Inject
    private PostRepository postRepository;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/addPost")
    public Response createPost(Post post) {
        Post p = postRepository.createPost(post);
        return Response.ok().entity(p).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAllPosts")
    public Response getAllPosts() {
        List<Post> postList = postRepository.getAllPosts();
        return Response.ok().entity(postList).build();
    }
}
