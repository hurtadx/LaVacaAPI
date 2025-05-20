package Model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.sql.Timestamp;

@Entity
public class Event {

    @Id
    private String id;

    @Column(nullable = false)
    private String vaca_id;

    private String title;

    private  String description;

    private Timestamp created_at;

    private String user_id;



}
