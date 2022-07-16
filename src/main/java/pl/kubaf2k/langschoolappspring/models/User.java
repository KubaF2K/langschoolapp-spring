package pl.kubaf2k.langschoolappspring.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import pl.kubaf2k.langschoolappspring.validators.BasicInfo;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    @NotEmpty(groups = BasicInfo.class)
    @Size(max = 255, groups = BasicInfo.class)
    private String firstName;

    @Column(nullable = false)
    @NotEmpty(groups = BasicInfo.class)
    @Size(max = 255, groups = BasicInfo.class)
    private String lastName;

    //TODO unique check in custom validator
    @Column(nullable = false, unique = true)
    @NotEmpty(groups = BasicInfo.class)
    @Size(max = 255, groups = BasicInfo.class)
    private String name;

    //TODO unique check in custom validator
    @Column(nullable = false, unique = true)
    @NotEmpty(groups = BasicInfo.class)
    @Email(groups = BasicInfo.class)
    @Size(max = 255, groups = BasicInfo.class)
    private String email;

    @Column(nullable = false)
    @NotEmpty
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
    private List<CourseStatus> courses;

    @OneToMany(mappedBy = "teacher")
    private List<Course> taughtCourses;

    public boolean hasRole(Role role) {
        if (roles == null)
            return false;
        for (Role iRole : roles)
            if (Objects.equals(role.getName(), iRole.getName()))
                return true;
        return false;
    }

    public boolean hasApplied(Course course) {
        if (courses == null)
            return false;
        for (var status : courses)
            if (status.getCourse().getId() == course.getId() && status.getStatus() != CourseStatus.Status.HISTORICAL)
                return true;
        return false;
    }

    public List<CourseStatus> getAttendedCourses() {
        var list = new LinkedList<CourseStatus>();
        if (courses == null)
            return list;

        for (var status : courses)
            if (status.getStatus() == CourseStatus.Status.ATTENDING)
                list.add(status);

        return list;
    }

    public List<CourseStatus> getCourseSignups() {
        var list = new LinkedList<CourseStatus>();
        if (courses == null)
            return list;

        for (var status : courses)
            if (status.getStatus() == CourseStatus.Status.NOT_ACCEPTED)
                list.add(status);

        return list;
    }

    public List<CourseStatus> getHistoricalCourses() {
        var list = new LinkedList<CourseStatus>();
        if (courses == null)
            return list;

        for (var status : courses)
            if (status.getStatus() == CourseStatus.Status.HISTORICAL)
                list.add(status);

        return list;
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

    public List<CourseStatus> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseStatus> courseSignups) {
        this.courses = courseSignups;
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
