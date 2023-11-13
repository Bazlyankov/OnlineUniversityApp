package com.studentsystemapp.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Set;

public class CustomUserDetails extends User {

    private String firstName;
    private Set<Course> courses;
    private Set<Task> tasks;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,
                             String firstName, Set<Course> courses, Set<Task> tasks) {
        super(username, password, authorities);
        this.firstName = firstName;
        this.courses = courses;
        this.tasks = tasks;
    }

    public String getFirstName() {
        return firstName;
    }

    public CustomUserDetails setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public CustomUserDetails setCourses(Set<Course> courses) {
        this.courses = courses;
        return this;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public CustomUserDetails setTasks(Set<Task> tasks) {
        this.tasks = tasks;
        return this;
    }
}
