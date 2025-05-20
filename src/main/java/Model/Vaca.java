package Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
public class Vaca {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private UUID id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "goal", nullable = false)
    private BigDecimal goal;

    @Column(name = "current", nullable = false)
    private BigDecimal current;

    @Column(name = "color", length = 20)
    private String color;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "deadline")
    private Timestamp deadline;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "status", length = 50)
    private String status;

    public Vaca() {
    }

    public Vaca(String id, String name, String description, BigDecimal goal, BigDecimal current, String color, Timestamp createdAt, Timestamp deadline, Boolean isActive, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.goal = goal;
        this.current = current;
        this.color = color;
        this.createdAt = createdAt;
        this.deadline = deadline;
        this.isActive = isActive;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getGoal() {
        return goal;
    }

    public void setGoal(BigDecimal goal) {
        this.goal = goal;
    }

    public BigDecimal getCurrent() {
        return current;
    }

    public void setCurrent(BigDecimal current) {
        this.current = current;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}