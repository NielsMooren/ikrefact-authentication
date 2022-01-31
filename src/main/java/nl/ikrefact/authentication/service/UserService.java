package nl.ikrefact.authentication.service;

import nl.ikrefact.authentication.dao.UserDAO;
import nl.ikrefact.authentication.model.User;
import nl.ikrefact.authentication.model.UserPasswordlessDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * UserService implements the required logic for mutations on users.
 * @author Niels Mooren
 */
@Service
public class UserService implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserDAO userDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    public UUID saveUser(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return this.userDAO.saveUser(user);
    }

    public Optional<User> getUserByID(UUID id) {
        return this.userDAO.getUserByID(id);
    }

    public List<User> getUsers() {
        return this.userDAO.getUsers();
    }

    /**
     * HidePasswords creates a new list with UserPasswordlessDTO objects.
     * @param users List&lt;User&gt;
     * @return List&lt;UserPasswordlessDTO&gt;
     * @author Niels Mooren
     */
    public List<UserPasswordlessDTO> hidePasswords(List<User> users) {
        List<UserPasswordlessDTO> passwordlessUsers = new ArrayList<>();
        for (User user : users) {
            UserPasswordlessDTO userPasswordlessDTO;
            if (user.getRoles() != null) {
                userPasswordlessDTO = new UserPasswordlessDTO(user.getUuid(), user.getUsername(), user.getRoles());
            } else {
                userPasswordlessDTO = new UserPasswordlessDTO(user.getUuid(), user.getUsername(), new ArrayList<>());
            }
            passwordlessUsers.add(userPasswordlessDTO);
        }
        return passwordlessUsers;
    }

    /**
     * HidePassword creates a new UserPasswordlessDTO object.
     * @param user Optional&lt;User&gt;
     * @return UserPasswordlessDTO
     * @author Niels Mooren
     */
    public UserPasswordlessDTO hidePassword(Optional<User> user) {
        UserPasswordlessDTO userPasswordlessDTO = null;
        if (user.isPresent()) {
            if (user.get().getRoles() != null) {
                userPasswordlessDTO = new UserPasswordlessDTO(user.get().getUuid(), user.get().getUsername(), user.get().getRoles());
            } else {
                userPasswordlessDTO = new UserPasswordlessDTO(user.get().getUuid(), user.get().getUsername(), new ArrayList<>());
            }
        }
        return userPasswordlessDTO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.getUser(username);
        if (user == null) {
            logger.info("User with username: {} not found.", username);
            throw new UsernameNotFoundException("User not found.");
        } else {
            logger.info("User found.");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    public void addRoleToUser(String username, String rolename) {
        userDAO.addRoleToUser(username, rolename);
    }

    public User getUser(String username) {
        return this.userDAO.getUser(username);
    }

    public void deleteUser(User user) {
        this.userDAO.deleteUser(user);
    }
}
