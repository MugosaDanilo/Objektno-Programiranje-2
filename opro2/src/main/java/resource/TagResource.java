package resource;

import exception.TagException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Tag;
import model.TagPost;
import repository.TagRepository;

import java.util.List;

@Path("/tag/")
public class TagResource {
    @Inject
    private TagRepository tagRepository;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/addTag")
    public Response createTag(Tag tag) {
        Tag t = tagRepository.createTag(tag);
        return Response.ok().entity(t).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/addTagPost")
    public Response createTagPost(TagPost tagPost) {
        TagPost tp = null;
        try {
            tp = tagRepository.createTagPost(tagPost);
        } catch (TagException e) {
            return Response.ok().entity(e.getMessage()).build();
        }
        return Response.ok().entity(tp).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAllTags")
    public Response getAllTags() {
        List<Tag> tags = tagRepository.getAllTags();
        return Response.ok().entity(tags).build();
    }
}
