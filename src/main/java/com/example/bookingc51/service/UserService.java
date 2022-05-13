package com.example.bookingc51.service;

import com.example.bookingc51.DTO.UserRegistrationDTO;
import com.example.bookingc51.entity.User;
import com.example.bookingc51.enums.UserRole;
import com.example.bookingc51.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User save(User user) {
        log.info("Save user "+user.getUsername());
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        List<UserRole> rolesRoles = new ArrayList<>();
        rolesRoles.add(UserRole.USER);
        user.setRoles(rolesRoles);
        User save = userRepository.save(user);
        log.info("User "+user.getUsername()+" was saved");
        return save;
    }

    public boolean isUsernameExist(String username) {
        log.info("Exist user by username(username = "+username+")");
        return userRepository.existsByUsername(username);
    }

    public Optional<User> findByUsername(String username) {
        log.info("Find user by username(username = "+username+")");
        return userRepository.findByUsername(username);
    }

    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    public Optional<User> findById(long id) {
        log.info("Exist user by id(id = "+id+")");
        return userRepository.findById(id);
    }

    public List<User> getUsersByRole(UserRole role) {
        log.info("Find users by role(role = "+role+")");
        return userRepository.findAllByRolesContains(role);
    }

    public boolean hasRoleById(long id, UserRole role){
        log.info("Checking if user role '"+role+"' exists by id(id = "+id+")");
        User user = userRepository.getById(id);
        List<UserRole> roles = user.getRoles();
        return roles.contains(role);
    }

    public boolean hasRoleByUsername(String username, UserRole role){
        log.info("Checking if user role '"+role+"' exists by username(username = "+username+") with user role = ");
        User user = userRepository.getByUsername(username);
        List<UserRole> roles = user.getRoles();
        return roles.contains(role);
    }

    public boolean addRoleById(long id, UserRole role){
        log.info("Add user role "+role+"  by id(id = "+id+")");
        User user = userRepository.getById(id);
        List<UserRole> roles = user.getRoles();
        boolean add = roles.add(role);
        userRepository.save(user);
        return add;
    }

    public boolean removeRoleById(long id, UserRole role){
        log.info("Remove user role "+role+"  by id(id = "+id+")");
        User user = userRepository.getById(id);
        List<UserRole> roles = user.getRoles();
        boolean remove = roles.remove(role);
        userRepository.save(user);
        return remove;
    }

    public User userRegistrationDTOToEntity(UserRegistrationDTO userRegistrationModel){
        User user = new User();
        user.setUsername(userRegistrationModel.getUsername());
        user.setPassword(userRegistrationModel.getPassword());
        return user;
    }

    public boolean isPasswordEquals(String bCryptOldPassword, String newPassword){
        log.info("Compare passwords");
        boolean matches = passwordEncoder.matches(newPassword, bCryptOldPassword);
        log.info("Result: "+matches);
        return matches;
    }
}
