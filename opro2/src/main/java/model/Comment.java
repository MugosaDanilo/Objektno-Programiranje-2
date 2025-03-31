package model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

@Entity
@NamedQuery(name = Comment.GET_COMMENTS_FOR_APP_USER, query = "Select c from Comment c where c.appUser.id = :id")
public class Comment {

    public static final String GET_COMMENTS_FOR_APP_USER = "Comment.getCommentsForUser";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_seq")
    private Long id;
    private String text;
    private Date commentDate;


    @ManyToOne
    private AppUser appUser;

    public Comment() {
    }

    public Comment(Long id, String text, Date commentDate) {
        this.id = id;
        this.text = text;
        this.commentDate = commentDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", commentDate=" + commentDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(getId(), comment.getId()) && Objects.equals(getText(), comment.getText()) && Objects.equals(getCommentDate(), comment.getCommentDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getText(), getCommentDate());
    }
}
