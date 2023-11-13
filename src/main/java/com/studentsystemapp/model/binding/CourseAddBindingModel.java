package com.studentsystemapp.model.binding;

import com.studentsystemapp.model.entity.Student;
import com.studentsystemapp.model.entity.Teacher;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class CourseAddBindingModel {


    private String name;


    private String description;

    private String teacherUsername;



}
