package com.studentsystemapp.model.binding;

import com.studentsystemapp.model.entity.Student;
import com.studentsystemapp.model.entity.Teacher;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class CourseAddBindingModel {

    @Size(min = 3, max = 40, message = "Course name length must be between 3 and 40 characters")
    private String name;

    @Size(min = 3, max = 100, message = "Description length must be between 3 and 100 characters")
    private String description;

    @Size(min = 3, max = 20, message = "Teacher username length must be between 3 and 20 characters")
    private String teacherUsername;



}
