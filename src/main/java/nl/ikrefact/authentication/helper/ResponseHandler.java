package nl.ikrefact.authentication.helper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {

    private ResponseHandler() {
        throw new IllegalStateException("Utility class");
    }

    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObject) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("status", status.value());
        map.put("data", responseObject);

        return new ResponseEntity<>(map, status);
    }

    public static ResponseEntity<Object> generateAuthResponse(String message, HttpStatus status, Object responseObject, String authorized) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("authorized", authorized);
        map.put("status", status.value());
        map.put("data", responseObject);

        return new ResponseEntity<>(map, status);
    }
}
