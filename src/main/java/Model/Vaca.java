package Model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.security.Timestamp;

@Entity
public class Vaca {
    @Id
    private String id;

    private String name;
    private String description;
    private BigDecimal goal;
    private BigDecimal current;
    private String color;
    private Timestamp createdAt;
    private Timestamp deadline;
    private Boolean isActive;
    private String status;
}


