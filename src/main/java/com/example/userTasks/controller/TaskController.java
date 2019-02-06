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
    private final String COMPLETED = "completed";
    private final String REJECTED = "rejected";



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

        if (user != null) return taskRepository.findByUserId(userId);
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
    public Task setUserByTask(@PathVariable int taskId, final int userId) {

        log.info("try update userId {} for task {}", userId, taskId);
        Task task = taskRepository.findById(taskId);
        User user = userRepository.findById(userId);

        if (task != null && user != null && user.getStatus() == User.Status.Online) {
            log.info("found task for id: {}", taskId);

            task.setUserId(user.getId());
            log.info("updated task: {}", taskRepository.save(task));

        } else {
            log.info("not found user (online) for id: {} or task for id: {}", userId, taskId);
        }

        return task;
    }

    @PutMapping(value = "/task/{taskId}/status")
    public Task setStatusByTask(@PathVariable int taskId, String status) {

        log.info("try update status {} for task {}", status, taskId);
        Task task = taskRepository.findById(taskId);

        if (task != null && task.getUserId() != 0) {

            Task.Status taskStatus = convertStatus(status);

            log.info("found task for id: {}", taskId);
            if (taskStatus != null) {
                task.setStatus(taskStatus);
                log.info("updated task: {}", taskRepository.save(task));
            } else {
                log.info("status: {} not valid", status);

            }
        } else {
            log.info("not found task for id: {} or assign user to task ", taskId);
        }

        return task;
    }

    private Task.Status convertStatus(String status) {

        status = status.toLowerCase();

        if (status.equals(COMPLETED)) return Task.Status.Completed;
        if (status.equals(REJECTED)) return Task.Status.Rejected;

        return null;
    }
}
