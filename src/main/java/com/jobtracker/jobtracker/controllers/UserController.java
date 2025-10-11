package com.jobtracker.jobtracker.controllers;

import com.jobtracker.jobtracker.dao.JobApplicationRepository;
import com.jobtracker.jobtracker.dao.UserRepository;
import com.jobtracker.jobtracker.entity.User;
import com.jobtracker.jobtracker.service.EmailService;
import com.jobtracker.jobtracker.service.JwtService;
import com.jobtracker.jobtracker.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor // constructor injection
@CrossOrigin(origins = "https://jobtracker-backend-wdpl.onrender.com", allowCredentials = "true")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final EmailService emailService;
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity
                    .status(400)
                    .body("Email already exists");
        }
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials, HttpServletResponse response) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        String token = userService.login(email, password);

        if (token != null) {
            // Set cookie
            ResponseCookie cookie = ResponseCookie.from("JWT_TOKEN", token)
                    .httpOnly(true)
                    .secure(false) // true in prod
                    .path("/")
                    .sameSite("Lax")
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            // Fetch user for ID
            User user = userRepository.findByEmail(email).orElseThrow();
            // Return response with userId
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Login successful!");
            responseBody.put("userId", user.getId());
            responseBody.put("email", user.getEmail());

            return ResponseEntity.ok(responseBody);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @GetMapping("/check-session")
    public Map<String, Boolean> checkSession(@CookieValue(value = "JWT_TOKEN", required = false) String token) {
        boolean valid = token != null && jwtService.validateToken(token);
        System.out.println("Session validity: " + valid);
        return Collections.singletonMap("authenticated", valid);
    }


    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> user = userService.updateUser(id, updatedUser);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id)
                ? ResponseEntity.ok("User deleted successfully")
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/mail/feedback")
    public ResponseEntity<?> sendFeedback(@RequestBody Map<String, String> feedbackData) {
        try {
            String name = feedbackData.get("name");
            String email = feedbackData.get("email");
            String message = feedbackData.get("message");

            // Construct email text
            String emailText = "You have received new feedback from your website:\n\n"
                    + "Name: " + name + "\n"
                    + "Email: " + email + "\n\n"
                    + "Message:\n" + message;

            // Send feedback to your admin email
            emailService.sendEmail("rajeshbyreddy98@gmail.com", emailText);


            return ResponseEntity.ok("Feedback sent successfully!");
        } catch (Exception e) {

            return ResponseEntity.internalServerError().body("Failed to send feedback");
        }
    }

}
