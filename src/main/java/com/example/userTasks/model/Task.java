package com.example.userTasks.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false, updatable = false)
    @Fetch(FetchMode.JOIN)
    private User user;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return String.format("{id: %s,name: %s, user: %s}",
                id, name, user);
    }

    public void setId(int id) {
        this.id = id;
    }
}
