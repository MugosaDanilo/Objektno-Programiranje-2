package repository;

import exception.TagException;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import model.Post;
import model.Tag;
import model.TagPost;

import java.util.List;

@Dependent
public class TagRepository {
    @Inject
    private EntityManager em;

    @Transactional
    public Tag createTag(Tag tag) { return em.merge(tag); }

    @Transactional
    public TagPost createTagPost(TagPost tagPost) throws TagException {
        Tag tag = em.find(Tag.class, tagPost.getTag().getId());
        Post post = em.find(Post.class, tagPost.getPost().getId());

        if(tag == null || post == null) {
            throw new TagException("Tag or post does not exist");
        }

        TagPost tp = new TagPost(tag, post);
        return em.merge(tp);
    }

    @Transactional
    public List<Tag> getAllTags() {
        return em.createNamedQuery(Tag.GET_ALL_TAGS, Tag.class).getResultList();
    }
}
