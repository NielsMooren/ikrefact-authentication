package nl.ikrefact.authentication.dao;

import nl.ikrefact.authentication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {
    User findByUsername(String username);
}