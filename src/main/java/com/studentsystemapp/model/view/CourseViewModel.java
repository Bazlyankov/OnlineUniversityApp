package com.studentsystemapp.model.view;
import com.studentsystemapp.model.entity.Course;

import com.studentsystemapp.model.entity.BaseUser;
import com.studentsystemapp.model.entity.Student;
import com.studentsystemapp.model.entity.Teacher;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CourseViewModel {


    private Long id;

    private BaseUser teacher;

    private Set<BaseUser> students;


    private String name;


    private String description;


}
