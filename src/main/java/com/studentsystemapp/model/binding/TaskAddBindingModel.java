package com.studentsystemapp.model.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskAddBindingModel {



    private Long courseId;

    private Long studentId;

    private String description;


}
