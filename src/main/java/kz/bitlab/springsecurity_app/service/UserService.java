package kz.bitlab.springsecurity_app.service;

import kz.bitlab.springsecurity_app.model.Role;
import kz.bitlab.springsecurity_app.model.User;
import kz.bitlab.springsecurity_app.repository.RoleRepository;
import kz.bitlab.springsecurity_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("USER NOT FOUND!");
        }
        return user;
    }

    public Boolean addUser(String fullName,
                           String email,
                           String password,
                           String repeatPassword) {

        User user = repository.findByEmail(email);

        if (user == null && password.equals(repeatPassword)) {
            List<Role> roles = new ArrayList<>();

            Role role = roleRepository.findByName("ROLE_USER");

            roles.add(role);

            User newUser = new User();
            newUser.setEmail(email);
            newUser.setPassword(passwordEncoder.encode(password));
            newUser.setFullName(fullName);
            newUser.setRoles(roles);

            repository.save(newUser);
            return true;
        }
        return false;
    }

    public Boolean updatePassword(String oldPassword,
                           String newPassword,
                           String repeatNewPassword) {
        User onlineUser = findOnlineUser();
        if (passwordEncoder.matches(oldPassword, findOnlineUser().getPassword())) {
            if (newPassword.equals(repeatNewPassword)) { //qwerty
                onlineUser.setPassword(passwordEncoder.encode(newPassword));
                repository.save(onlineUser);
                return true;
            }
        }
        return false;
    }

    public User findOnlineUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)){
            return (User) authentication.getPrincipal();
        }
        return null;
    }
}
