package Model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
public class Rule {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "vaca_id", nullable = false, length = 36)
    private String vaca_id;

    @Column(name = "rule_type", nullable = false, length = 50)
    private String rule_type;

    @Column(name = "value", length = 255)
    private String value;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "created_at", nullable = false)
    private Timestamp created_at;

    public Rule() {
    }

    public Rule(UUID id, String vaca_id, String rule_type, String value, String description, Timestamp created_at) {
        this.id = id;
        this.vaca_id = vaca_id;
        this.rule_type = rule_type;
        this.value = value;
        this.description = description;
        this.created_at = created_at;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getVaca_id() {
        return vaca_id;
    }

    public void setVaca_id(String vaca_id) {
        this.vaca_id = vaca_id;
    }

    public String getRule_type() {
        return rule_type;
    }

    public void setRule_type(String rule_type) {
        this.rule_type = rule_type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}
