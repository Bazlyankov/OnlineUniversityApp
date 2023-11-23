package com.studentsystemapp.model.view;

import com.studentsystemapp.model.view.CourseViewModel;
import com.studentsystemapp.model.view.StudentViewModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EnrollmentViewModel {

    private UserViewModel user;
    private CourseViewModel course;

}
