package com.example.userTasks.controller;

import com.example.userTasks.model.Task;
import com.example.userTasks.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TaskController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping(value = "/task/all")
    public List<Task> getAllTasks() {

        return taskRepository.findAll();
    }
}
