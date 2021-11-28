package nl.ikrefact.authentication.dao;

import nl.ikrefact.authentication.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepo extends JpaRepository<Role, UUID> {
    Role findByName(String name);
}