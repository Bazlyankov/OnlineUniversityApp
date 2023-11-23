package com.studentsystemapp.model.view;

import com.studentsystemapp.model.entity.Grade;
import com.studentsystemapp.model.entity.Task;
import com.studentsystemapp.model.enums.UserRolesEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserViewModel {

    private Long id;

    private String username;

    private String firstName;

    private UserRolesEnum role;


    private String lastName;
    private String email;
    private List<EnrollmentViewModel> enrollments;
    private Set<EnquiryViewModel> enquiries;

    private List<Grade> grades;


    protected List<Task> tasks;

    private String profilePicURL;
}
