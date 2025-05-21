package Model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;

@Entity
@Table(name = "participants")
public class Participant {

    @Id
    @Column(length = 36, nullable = false, unique = true)
    private String id;

    @Column(nullable = false, length = 36)
    private String vaca_id;

    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private String email;

    @Column(nullable = false)
    private Timestamp created_at;

    @Column(length = 36)
    private String user_id;

    @Column(nullable = false)
    private String status = "pendiente";  // Valores: activo, pendiente, inactivo


    public Participant() {
    }

    public Participant(String id, String vaca_id, String name, String email, Timestamp created_at, String user_id) {
        this.id = id;
        this.vaca_id = vaca_id;
        this.name = name;
        this.email = email;
        this.created_at = created_at;
        this.user_id = user_id;
    }

    public Participant(String id, String vaca_id, String name, String email, Timestamp created_at, String user_id, String status) {
        this.id = id;
        this.vaca_id = vaca_id;
        this.name = name;
        this.email = email;
        this.created_at = created_at;
        this.user_id = user_id;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
