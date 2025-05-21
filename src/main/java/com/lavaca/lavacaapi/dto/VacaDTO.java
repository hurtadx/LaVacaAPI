package com.lavaca.lavacaapi.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class VacaDTO {
    private String name;
    private String description;
    private BigDecimal goal;
    private String color;
    private Timestamp deadline;

    // Getters y setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getGoal() { return goal; }
    public void setGoal(BigDecimal goal) { this.goal = goal; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public Timestamp getDeadline() { return deadline; }
    public void setDeadline(Timestamp deadline) { this.deadline = deadline; }
}
