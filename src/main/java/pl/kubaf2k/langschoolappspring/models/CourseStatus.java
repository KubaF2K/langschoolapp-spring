package pl.kubaf2k.langschoolappspring.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class CourseStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    //TODO check if not already signed up or enrolled in custom validator
    @ManyToOne(optional = false)
    @NotNull
    private User user;
    @ManyToOne(optional = false)
    @NotNull
    private Course course;

    @Column(precision = 8, scale = 2)
    @NotNull
    @Min(0)
    @Max(99999999)
    private BigDecimal cost;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    @NotNull
    private Status status;

    public CourseStatus(){}

    public CourseStatus(Course course, User user, Status status) {
        this.course = course;
        this.user = user;
        this.status = status;
    }

    public CourseStatus(Course course, User user, Status status, BigDecimal cost) {
        this.course = course;
        this.user = user;
        this.status = status;
        this.cost = cost;
    }

    public enum Status {
        NOT_ACCEPTED, ATTENDING, HISTORICAL
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
