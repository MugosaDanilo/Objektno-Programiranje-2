package repository;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import model.Post;

import java.util.List;

@Dependent
public class PostRepository {
    @Inject
    private EntityManager em;

    @Transactional
    public Post createPost(Post post) { return em.merge(post); }

    @Transactional
    public List<Post> getAllPosts() {
        return em.createNamedQuery(Post.GET_ALL_POSTS, Post.class).getResultList();
    }

}
