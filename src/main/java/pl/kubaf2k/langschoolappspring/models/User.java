package pl.kubaf2k.langschoolappspring.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    @NotNull
    @Size(max = 255)
    private String firstName;
    private String lastName;

    //TODO unique check in custom validator
    @Column(nullable = false, unique = true)
    @NotNull
    @Size(max = 255)
    private String name;

    //TODO unique check in custom validator
    @Column(nullable = false, unique = true)
    @NotNull
    @Email
    @Size(max = 255)
    private String email;

    @Column(nullable = false)
    @NotNull
    @Size(max = 255)
    private String password;

    @ManyToOne
    private Language language;

    @ManyToMany
    private List<Role> roles;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user")
    private List<CourseSignup> courseSignups;

    @ManyToMany
    private List<Course> attendedCourses;

    @OneToMany(mappedBy = "teacher")
    private List<Course> taughtCourses;

    public boolean hasRole(Role role) {
        for (Role iRole : roles)
            if (Objects.equals(role.getName(), iRole.getName()))
                return true;
        return false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<CourseSignup> getCourseSignups() {
        return courseSignups;
    }

    public void setCourseSignups(List<CourseSignup> courseSignups) {
        this.courseSignups = courseSignups;
    }

    public List<Course> getAttendedCourses() {
        return attendedCourses;
    }

    public void setAttendedCourses(List<Course> attendedCourses) {
        this.attendedCourses = attendedCourses;
    }

    public List<Course> getTaughtCourses() {
        return taughtCourses;
    }

    public void setTaughtCourses(List<Course> taughtCourses) {
        this.taughtCourses = taughtCourses;
    }

    public User() {}

    public User(String firstName, String lastName, String name, String email, String password, List<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public User(String firstName, String lastName, String name, String email, String password, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = Collections.singletonList(role);
    }

    public User(String firstName, String lastName, String name, String email, String password, Language language, List<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.name = name;
        this.email = email;
        this.password = password;
        this.language = language;
        this.roles = roles;
    }

    public User(String firstName, String lastName, String name, String email, String password, Language language, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.name = name;
        this.email = email;
        this.password = password;
        this.language = language;
        this.roles = Collections.singletonList(role);
    }
}
