package com.example.userTasks;

import com.example.userTasks.model.Task;
import com.example.userTasks.model.User;
import com.example.userTasks.repository.TaskRepository;
import com.example.userTasks.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demoData(UserRepository userRepository, TaskRepository taskRepository) {
        return (args) -> {

            // online
            User user1 = new User();
            user1.setUsername("user1");
            user1.setEmail("email1");
            user1.setStatus(User.Status.Online);
            userRepository.save(user1);

            // online
            User user2 = new User();
            user2.setUsername("user2");
            user2.setEmail("email2");
            user2.setStatus(User.Status.Online);
            userRepository.save(user2);

            // online
            User user3 = new User();
            user3.setUsername("user3");
            user3.setEmail("email3");
            user3.setStatus(User.Status.Online);
            userRepository.save(user3);

            // offline
            User user4 = new User();
            user4.setUsername("user4");
            user4.setEmail("email4");
            userRepository.save(user4);

            Task task1 = new Task();
            task1.setName("name task1");
            task1.setUserId(user1.getId());
            taskRepository.save(task1);

            Task task2 = new Task();
            task2.setName("name task2");
            task2.setUserId(user1.getId());
            taskRepository.save(task2);
        };
    }
}


