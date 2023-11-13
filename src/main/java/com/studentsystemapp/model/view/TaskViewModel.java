package com.studentsystemapp.model.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class TaskViewModel {

    private Long courseId;
    private String courseName;

    private Long studentId;
    private Long id;

    private String description;


    private Boolean isCompleted;


    private Boolean isGraded;

    private BigDecimal gradeValue;

    private String taskSolution;

}
