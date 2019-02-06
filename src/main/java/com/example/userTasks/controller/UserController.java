package com.example.userTasks.controller;

import com.example.userTasks.model.User;
import com.example.userTasks.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final String ONLINE = "online";
    private final String OFFLINE = "offline";

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/user/all")
    public List<User> getAllTasks() {

        return userRepository.findAll();
    }

    @GetMapping(value = "/user/online")
    public List<User> getUsersOnline() {

        return userRepository.findByStatus(User.Status.Online);
    }

    @GetMapping(value = "/user/offline")
    public List<User> getUsersOffline() {

        return userRepository.findByStatus(User.Status.Offline);
    }

    @GetMapping(value = "/user/{id}")
    public User getUserDetails(@PathVariable int id) {

        return userRepository.findById(id);
    }

    @PostMapping(value = "/user")
    public User addLink(final User user) {

        log.info("get new user data. email: {}, username: {}", user.getEmail(), user.getUsername());

        return userRepository.save(user);
    }

    @PutMapping(value = "/user/{id}")
    public User updateUserStatus(@PathVariable int id, final String status) {

        log.info("try update status {} for userId {}", status, id);
        User user = userRepository.findById(id);
        if (user != null) {
            log.info("found user for id: {}", id);

            User.Status userStatus = convertStatus(status);

            if (status != null) {
                user.setStatus(userStatus);
                userRepository.save(user);
            } else {
                log.info("status: {} not valid", status);
            }
        } else {
            log.info("not found user for id: {}", id);
        }

        return user;
    }

    private User.Status convertStatus(String status) {

        status = status.toLowerCase();

        if (status.equals(ONLINE)) return User.Status.Online;
        if (status.equals(OFFLINE)) return User.Status.Offline;

        return null;
    }
}
