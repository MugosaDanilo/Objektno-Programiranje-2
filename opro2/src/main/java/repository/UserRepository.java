package repository;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import model.AppUser;
import model.Comment;
import model.Post;

import java.util.List;

@Dependent
public class UserRepository {
    @Inject
    private EntityManager em;

    @Transactional
    public AppUser createUser(AppUser user) {
        return em.merge(user);
    }

    @Transactional
    public List<AppUser> getAllUsers() {
        List<AppUser> appUserList = em.createNamedQuery(AppUser.GET_ALL_APP_USERS, AppUser.class).getResultList();

        for(AppUser appUser : appUserList) {
            List<Post> posts = em.createNamedQuery(Post.GET_POSTS_FOR_APP_USER, Post.class).setParameter("id", appUser.getId()).getResultList();
            List<Comment> comments = em.createNamedQuery(Comment.GET_COMMENTS_FOR_APP_USER, Comment.class).setParameter("id", appUser.getId()).getResultList();

            appUser.setPostList(posts);
            appUser.setCommentList(comments);
        }

        return appUserList;
    }

}
