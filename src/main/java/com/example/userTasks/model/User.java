package com.example.userTasks.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String username;
    private String email;
    private Status status;

    @OneToMany(targetEntity = Task.class, mappedBy = "id", orphanRemoval = false, fetch = FetchType.LAZY)
    private Set<Task> tasks;

    @Transient
    private final String DEFAULT_ROLE = "ROLE_USER";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        Offline, Online
    }

    @Override
    public String toString() {
        return String.format("{id: %s,username: %s, email: %s, status: %s}",
                id, username, email, status);
    }
}
