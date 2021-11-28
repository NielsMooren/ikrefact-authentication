package nl.ikrefact.authentication.dao;

import nl.ikrefact.authentication.model.Role;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RoleDAO {

    private final RoleRepo roleRepo;

    public RoleDAO(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    public void saveRole(Role role) {
        this.roleRepo.save(role);
    }

    public Role getRole(String role) {
        return this.roleRepo.findByName(role);
    }

    public List<Role> getRoles() {
        return this.roleRepo.findAll();
    }

    public Optional<Role> getRoleByID(UUID id) {
        return this.roleRepo.findById(id);
    }

}
