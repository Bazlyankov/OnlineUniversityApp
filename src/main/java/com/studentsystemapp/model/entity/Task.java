package com.studentsystemapp.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Task extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    private Course course;

    @Column
    private String description;

    @OneToOne
    private Grade grade;

    @Column(name = "task_solution")
    private String taskSolution;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Column(name = "is_graded")
    private Boolean isGraded;

    public Task(String description, Course course ) {

        super();
        this.description = description;
        this.course = course;
    }
}
