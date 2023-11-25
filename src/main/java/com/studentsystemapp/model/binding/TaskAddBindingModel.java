package com.studentsystemapp.model.binding;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskAddBindingModel {


    private Long courseId;

    private Long studentId;
    @Size(min = 3, max = 100, message = "Task description must be between 3 and 100 characters long.")
    private String description;


}
