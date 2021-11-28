package nl.ikrefact.authentication.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import nl.ikrefact.authentication.dao.RoleDAO;
import nl.ikrefact.authentication.helper.ResponseHandler;
import nl.ikrefact.authentication.model.*;
import nl.ikrefact.authentication.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${JWT_SECRET}")
    private String jwtSecret;
    private final RoleDAO roleDAO;
    private final UserService userService;
    public static final String AUTHORIZED = "authorized";

    public UserController(RoleDAO roleDAO, UserService userService) {
        this.roleDAO = roleDAO;
        this.userService = userService;
    }


    @PostMapping
    @ResponseBody
    public ResponseEntity<Object> postUser(@RequestBody UserDTO userDTO) {
        User user;
        user = new User(userDTO.getUsername(), userDTO.getPassword(), new ArrayList<>());
        if (userDTO.getRoles() != null) {
            Collection<Role> newRoles = new ArrayList<>();
            for (String rolename : userDTO.getRoles()) {
                newRoles.add(roleDAO.getRole(rolename));
            }
            user = new User(userDTO.getUsername(), userDTO.getPassword(), newRoles);
        }
        try {
            UUID uuidSavedUser = this.userService.saveUser(user);
            return ResponseHandler.generateResponse("User has been added.", HttpStatus.CREATED, uuidSavedUser);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            return ResponseHandler.generateResponse("User has not been added.", HttpStatus.BAD_REQUEST, null);
        }
    }


    @GetMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<Object> getUserByID(@PathVariable UUID id) {
        Optional<User> user = this.userService.getUserByID(id);
        if (user.isPresent()) {
            UserPasswordlessDTO userPasswordlessDTO = this.userService.hidePassword(user);
            logger.info("User {}: found.", userPasswordlessDTO.getId());
            return ResponseHandler.generateResponse("Successfully retrieved user.", HttpStatus.OK, userPasswordlessDTO);
        } else {
            logger.info("User {}: not found.", id);
            return ResponseHandler.generateResponse("An error has occurred.", HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<Object> getUsers() {
        try {
            List<User> users = this.userService.getUsers();
            return ResponseHandler.generateResponse("Successfully retrieved users.", HttpStatus.OK, this.userService.hidePasswords(users));
        } catch (Exception e) {
            logger.info(e.getMessage());
            return ResponseHandler.generateResponse("An error has occurred.", HttpStatus.BAD_REQUEST, null);
        }
    }

    @PutMapping(path = "/changepassword")
    @ResponseBody
    public ResponseEntity<Object> changePassword(HttpServletRequest request, HttpServletResponse response, @RequestBody PasswordChangeDTO passwordChangeDTO) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC512(jwtSecret);
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                User user = userService.getUser(username);
                user.setPassword(passwordChangeDTO.getPassword());
                userService.saveUser(user);

                response.setContentType(APPLICATION_JSON_VALUE);
                return ResponseHandler.generateResponse("Password change successfull.", HttpStatus.NO_CONTENT, "Password change successfull.");

            } catch (Exception e) {
                response.setHeader("Error", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                return ResponseHandler.generateResponse(e.getMessage(), FORBIDDEN, error);
            }
        } else {
            return ResponseHandler.generateResponse("Access token is missing.", HttpStatus.BAD_REQUEST, "Access token is missing.");
        }
    }

    @DeleteMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<Object> deleteUser(@PathVariable UUID id) {
        Optional<User> user = this.userService.getUserByID(id);
        if (user.isPresent()) {
            userService.deleteUser(user.get());
            logger.info("Deleted user {}.", user.get().getUsername());
            return ResponseHandler.generateResponse("Successfully deleted user.", HttpStatus.OK, id);
        } else {
            logger.info("User {}: not found.", id);
            return ResponseHandler.generateResponse("An error has occurred.", HttpStatus.BAD_REQUEST, null);
        }
    }
}
