package model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

@Entity
@NamedQuery(name = Post.GET_POSTS_FOR_APP_USER, query = "Select p from Post p where p.appUser.id = :id")
public class Post {

    public static final String GET_POSTS_FOR_APP_USER = "Post.getPostsForAppUser";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_seq")
    private Long id;
    private String title;
    private String content;
    private Date publicationDate;

    @ManyToOne
    private AppUser appUser;

    public Post() {
    }

    public Post(Long id, String title, String content, Date publicationDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.publicationDate = publicationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", publicationDate=" + publicationDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(getId(), post.getId()) && Objects.equals(getTitle(), post.getTitle()) && Objects.equals(getContent(), post.getContent()) && Objects.equals(getPublicationDate(), post.getPublicationDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getContent(), getPublicationDate());
    }
}
