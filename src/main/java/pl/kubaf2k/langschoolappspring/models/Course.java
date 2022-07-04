package pl.kubaf2k.langschoolappspring.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import pl.kubaf2k.langschoolappspring.validators.BasicInfo;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    //TODO unique check in custom validator
    @Column(unique = true, nullable = false)
    @NotEmpty(groups = BasicInfo.class)
    @Size(min = 1, max = 255, groups = BasicInfo.class)
    private String name;

    @NotNull(groups = BasicInfo.class)
    @Min(value = 0, groups = BasicInfo.class)
    @Max(value = Integer.MAX_VALUE, groups = BasicInfo.class)
    private int hours;

    @Column(columnDefinition = "text", nullable = false)
    @NotEmpty(groups = BasicInfo.class)
    @Size(min = 1, max = 32767, groups = BasicInfo.class)
    private String description;

    @Column(precision = 8, scale = 2, nullable = false)
    @NotNull(groups = BasicInfo.class)
    @Min(value = 0, groups = BasicInfo.class)
    @Max(value = 99999999, groups = BasicInfo.class)
    private BigDecimal price;

    @ManyToOne(optional = false)
    @NotNull
    private Language language;

    //TODO check if teaches the language in custom validator
    @ManyToOne(optional = false)
    @NotNull
    private User teacher;

    @ManyToMany(mappedBy = "attendedCourses")
    private List<User> attendants;

    @OneToMany(mappedBy = "course")
    private List<CourseSignup> courseSignups;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Course() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
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

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public List<User> getAttendants() {
        return attendants;
    }

    public void setAttendants(List<User> attendants) {
        this.attendants = attendants;
    }

    public List<CourseSignup> getCourseSignups() {
        return courseSignups;
    }

    public void setCourseSignups(List<CourseSignup> courseSignups) {
        this.courseSignups = courseSignups;
    }

    public Course(String name, int hours, String description, BigDecimal price, Language language, User teacher) {
        this.name = name;
        this.hours = hours;
        this.description = description;
        this.price = price;
        this.language = language;
        this.teacher = teacher;
    }
}
