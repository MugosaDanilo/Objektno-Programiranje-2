package model;

import jakarta.persistence.*;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Entity
@NamedQuery(name = AppUser.GET_ALL_APP_USERS, query = "Select u from AppUser u")
public class AppUser {
    public static final String GET_ALL_APP_USERS = "AppUser.getAllAppUsers";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_seq")
    private Long id;
    private String username;
    private String email;
    private Date registrationDate;
    private String uploadedFilePath;

    @Transient
    private File uploadedFile;

    @Transient
    private String fileContentBase64;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "appuser_id")
    private List<Post> postList;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "appuser_id")
    private List<Comment> commentList;

    public AppUser() {
    }

    public AppUser(Long id, String username, String email, Date registrationDate) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.registrationDate = registrationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public String getUploadedFilePath() {
        return uploadedFilePath;
    }

    public void setUploadedFilePath(String uploadedFilePath) {
        this.uploadedFilePath = uploadedFilePath;
    }

    public String getFileContentBase64() {
        return fileContentBase64;
    }

    public void setFileContentBase64(String fileContentBase64) {
        this.fileContentBase64 = fileContentBase64;
    }

    public File getUploadedFile() {
        if (uploadedFile == null && uploadedFilePath != null) {
            uploadedFile = new File(uploadedFilePath);
        }
        return uploadedFile;
    }

    public void setUploadedFile(File uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AppUser appUser = (AppUser) o;
        return Objects.equals(getId(), appUser.getId()) && Objects.equals(getUsername(), appUser.getUsername()) && Objects.equals(getEmail(), appUser.getEmail()) && Objects.equals(getRegistrationDate(), appUser.getRegistrationDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getEmail(), getRegistrationDate());
    }
}
