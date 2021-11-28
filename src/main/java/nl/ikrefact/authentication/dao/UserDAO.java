package nl.ikrefact.authentication.dao;

import nl.ikrefact.authentication.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserDAO {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    public UserDAO(UserRepo userRepo, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    public UUID saveUser(User user) {
        User newUser = this.userRepo.save(user);
        return newUser.getUuid();
    }

    public void addRoleToUser(String username, String roleName) {
        User user = this.userRepo.findByUsername(username);
        user.addRole(this.roleRepo.findByName(roleName));
        userRepo.save(user);
    }

    public User getUser(String username) {
        return this.userRepo.findByUsername(username);
    }

    public List<User> getUsers() {
        return this.userRepo.findAll();
    }

    public Optional<User> getUserByID(UUID id) {
        return this.userRepo.findById(id);
    }

    public void deleteUser(User user) {
        this.userRepo.delete(user);
    }
}
