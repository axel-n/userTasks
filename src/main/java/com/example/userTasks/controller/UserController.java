package com.example.userTasks.controller;

import com.example.userTasks.model.User;
import com.example.userTasks.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final Logger log = LoggerFactory.getLogger(getClass());

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
}
