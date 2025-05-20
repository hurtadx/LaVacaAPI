package Model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.sql.Timestamp;

@Entity
public class Profile {

    @Id
    user_id UUID REFERENCES auth.users(id)

    private String username;

    private String email;

    private Timestamp created_at;

    private Timestamp updated_at;

}
