package com.example.userTasks.repository;

import com.example.userTasks.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findAll();
    List<Task> findByUserId(int userId);
    Task findById(int id);
    Task save(Task task);

}
