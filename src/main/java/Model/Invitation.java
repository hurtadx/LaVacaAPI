package Model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.sql.Timestamp;

@Entity
public class Invitation {

    @Id
    private String id;

    private String vaca_id;

    private String sender_id;

    private String status;

    private Timestamp created_at;

}
