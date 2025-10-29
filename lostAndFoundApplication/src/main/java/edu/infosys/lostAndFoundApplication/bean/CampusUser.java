package edu.infosys.lostAndFoundApplication.bean;

// REMOVE ALL 'org.springframework.security' IMPORTS
// import java.util.ArrayList;
// import java.util.Collection;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.userdetails.User;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CampusUser { // <-- REMOVED 'extends User'
    @Id
    private String username;
    private String password;
    private String personName;
    private String email;
    private String role;

    // REMOVED both constructors (the empty one with 'super' and the complex one)

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPersonName() {
        return personName;
    }
    public void setPersonName(String personName) {
        this.personName = personName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    @Override
    public String toString() {
        return "CampusUser [username=" + username + ", password=" + password + ", personName=" + personName + ", email="
                + email + ", role=" + role + "]";
    }

    // REMOVED the 'getAuthorities()' method
}