package nl.ikrefact.authentication.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class UserDTO {
    private UUID id;
    private String username;
    private String password;
    private ArrayList<String> roles;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<String> getRoles() {
        return this.roles;
    }

    public void setRole(String role) {
        this.roles.add(role);
    }
}

