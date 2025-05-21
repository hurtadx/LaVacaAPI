package Model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @Column(length = 36, nullable = false, unique = true)
    private String id;

    @Column(nullable = false, length = 36)
    private String vaca_id;

    @Column(length = 255)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Timestamp created_at;

    @Column(length = 36)
    private String user_id;
    public Event() {
    }

    public Event(String id, String vaca_id, String title, String description, Timestamp created_at, String user_id) {
        this.id = id;
        this.vaca_id = vaca_id;
        this.title = title;
        this.description = description;
        this.created_at = created_at;
        this.user_id = user_id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVaca_id() {
        return vaca_id;
    }

    public void setVaca_id(String vaca_id) {
        this.vaca_id = vaca_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
