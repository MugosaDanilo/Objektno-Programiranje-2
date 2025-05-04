package model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class TagPost {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tagpost_seq")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    Tag tag;

    @ManyToOne(cascade = CascadeType.ALL)
    Post post;

    public TagPost() {
        super();
    }

    public TagPost(Tag tag, Post post) {
        super();
        this.tag = tag;
        this.post = post;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TagPost tagPost = (TagPost) o;
        return Objects.equals(getId(), tagPost.getId()) && Objects.equals(getTag(), tagPost.getTag()) && Objects.equals(getPost(), tagPost.getPost());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTag(), getPost());
    }

    @Override
    public String toString() {
        return "TagPost{" +
                "id=" + id +
                ", tag=" + tag +
                ", post=" + post +
                '}';
    }
}
