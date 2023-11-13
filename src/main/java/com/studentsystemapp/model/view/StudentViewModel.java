package com.studentsystemapp.model.view;

import com.studentsystemapp.model.entity.BaseUser;
import com.studentsystemapp.model.entity.Course;
import com.studentsystemapp.model.entity.Grade;
import com.studentsystemapp.model.entity.Task;
import com.studentsystemapp.model.enums.UserRolesEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class StudentViewModel {

    private Long id;

    private String username;

    private String firstName;

    private UserRolesEnum role;


    private String lastName;
    private String email;
    private List<Course> courses;


    private List<Grade> grades;


    private List<Task> tasks;

    public StudentViewModel(BaseUser s) {

    }

    public BigDecimal getAverageGrade() {
        if(tasks == null) {
            return BigDecimal.ZERO;
        }

        List<Task> gradedTasks = tasks.stream()
                .filter(task -> task.getGrade() != null)
                .toList();

        return gradedTasks.isEmpty() ? BigDecimal.ZERO : gradedTasks.stream()
                .map(task -> task.getGrade().getValue())
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(gradedTasks.size()),2);
    }

}
