package com.example.userTasks.controller;

import com.example.userTasks.model.Task;
import com.example.userTasks.model.User;
import com.example.userTasks.repository.TaskRepository;
import com.example.userTasks.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/task/all")
    public List<Task> getAllTasks() {

        return taskRepository.findAll();
    }

    @GetMapping(value = "/task/user/{userId}")
    public List<Task> getTasksByUser(@PathVariable int userId) {

        User user = userRepository.findById(userId);

        if (user != null) return user.getTasks();
        else {
            log.info("user not found for user id: {}", userId);
        }

       return null;
    }

    @PostMapping(value = "/task")
    public Task addTask(final Task task) {

        log.info("get new task data. name: {}", task.getName());

        return taskRepository.save(task);
    }

    @GetMapping(value = "/task/{id}")
    public Task getTaskDetails(@PathVariable int id) {

        return taskRepository.findById(id);
    }

    @PutMapping(value = "/task/{taskId}")
    public Task updateUserByTask(@PathVariable int taskId, final int userId) {

        log.info("try update userId {} for task {}", taskId, userId);
        Task task = taskRepository.findById(taskId);
        User user = userRepository.findById(userId);

        if (task != null && user != null && user.getStatus() == User.Status.Online) {
            log.info("found task for id: {}", taskId);

            task.setUser(user);
            taskRepository.save(task);

        } else {
            log.info("not found user (online) for id: {} or task for id: {}", userId, taskId);
        }

        return task;
    }
}
