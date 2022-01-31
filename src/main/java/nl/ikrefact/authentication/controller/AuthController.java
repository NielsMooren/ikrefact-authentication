package nl.ikrefact.authentication.controller;

import nl.ikrefact.authentication.helper.ResponseHandler;
import nl.ikrefact.authentication.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthController {

    public static final String AUTHORIZED = "authorized";
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "admin")
    @ResponseBody
    public ResponseEntity<Object> authAdmin() {
        return ResponseHandler.generateAuthResponse("Authorized.", HttpStatus.OK, null, AUTHORIZED);
    }

    @GetMapping(value = "mod")
    @ResponseBody
    public ResponseEntity<Object> authMod() {
        return ResponseHandler.generateAuthResponse("Authorized.", HttpStatus.OK, null, AUTHORIZED);
    }

    @GetMapping(value = "user")
    @ResponseBody
    public ResponseEntity<Object> authUser() {
        return ResponseHandler.generateAuthResponse("Authorized.", HttpStatus.OK, null, AUTHORIZED);
    }
}
