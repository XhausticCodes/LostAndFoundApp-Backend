package edu.infosys.lostAndFoundApplication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
// Removed AuthenticationManager and related imports
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import edu.infosys.lostAndFoundApplication.bean.CampusUser;
import edu.infosys.lostAndFoundApplication.service.CampusUserService;

@RestController
@RequestMapping("/lost-found")
@CrossOrigin(origins = "http://localhost:3939")
public class LoginController {

    @Autowired
    private CampusUserService service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public void registerNewUser(@RequestBody CampusUser user ) {
        // Ensure role is set if needed, or handle default role
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("Student"); // Example: Default to Student if not provided
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        service.save(user);
    }

    @GetMapping("/user/details")
    public CampusUser getUserDetails() {
        return service.getUser();
    }

    @GetMapping("/admin/students")
    public List<CampusUser> getAllStudents() {
        return service.getAllStudents();
    }

    @DeleteMapping("/admin/student/{username}")
    public void deleteStudent(@PathVariable String username) {
        service.deleteStudentByUsername(username);
    }
}