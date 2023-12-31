package com.studentsystemapp.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tasks")
public class Task extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Course course;

    @Column
    @Size(min = 3, max = 100)
    private String description;

    @OneToOne
    private Grade grade;

    @Column(name = "task_solution")
    private String taskSolution;

    @Column(name = "is_completed")
    private Boolean isCompleted =false;

    @Column(name = "is_graded")
    private Boolean isGraded = false;

    public Task(String description, Course course ) {

        super();
        this.description = description;
        this.course = course;
    }
}
