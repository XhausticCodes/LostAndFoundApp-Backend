package edu.infosys.lostAndFoundApplication.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.infosys.lostAndFoundApplication.bean.CampusUser;
import edu.infosys.lostAndFoundApplication.dao.CampusUserRepository;

@Service
public class CampusUserService implements UserDetailsService{

    @Autowired
    private CampusUserRepository repository;

    public void save(CampusUser user) {
        repository.save(user);
    }

    private CampusUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }
        String username = authentication.getName();
        return repository.findById(username).orElse(null);
    }

    public String getUserId() {
        CampusUser user = getAuthenticatedUser();
        return (user != null) ? user.getUsername() : null;
    }

    public String getRole() {
        CampusUser user = getAuthenticatedUser();
        return (user != null) ? user.getRole() : null;
    }

    public CampusUser getUser() {
        return getAuthenticatedUser();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //fetching custom user entity:
        CampusUser campusUser = repository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        //Creating list of spring security roles
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + campusUser.getRole().toUpperCase())
        );

        //retturning new user object(spring security)
        return new User(
                campusUser.getUsername(),
                campusUser.getPassword(),
                authorities
        );
    }

    public List<CampusUser>getAllStudents(){
        return repository.getAllStudents();
    }

    public void deleteStudentByUsername(String username) {
        CampusUser user = repository.findById(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if ("Student".equalsIgnoreCase(user.getRole())) {
            repository.deleteById(username);
        } else {
            throw new RuntimeException("Cannot delete non-student users");
        }
    }
}