package Model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.sql.Timestamp;

@Entity
public class Participant {

    @Id
    private String id;

    private String vaca_id;

    private String name;

    private String email;

    private Timestamp created_at;

    private  String user_id;

}
