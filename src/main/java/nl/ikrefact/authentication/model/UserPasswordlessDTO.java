package nl.ikrefact.authentication.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class UserPasswordlessDTO {

    private UUID id;
    private String username;
    private Collection<Role> roles = new ArrayList<>();

    public UserPasswordlessDTO(UUID id, String username, Collection<Role> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    public UserPasswordlessDTO(UUID id, String username) {
        this.id = id;
        this.username = username;
    }

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

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}
