package nl.ikrefact.authentication.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;


@Entity
@Table(name = "user_account")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @JoinColumn(name = "role")
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();

    public User() {}

    public User(UUID uuid, String username, String password, Collection<Role> roles) {
        this.id = uuid;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public User(String username, String password, Collection<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public UUID getUuid() {
        return id;
    }

    public void setUuid(UUID uuid) {
        this.id = uuid;
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

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRole(Collection<Role> role) {
        this.roles = role;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }
}
