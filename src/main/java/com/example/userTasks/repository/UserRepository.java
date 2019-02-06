package com.example.userTasks.repository;

import com.example.userTasks.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);
    List<User> findByStatus (User.Status status);
    User save(User user);
    User findById(int id);
}
