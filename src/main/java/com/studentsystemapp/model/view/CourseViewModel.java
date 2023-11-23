package com.studentsystemapp.model.view;
import com.studentsystemapp.model.entity.BaseUser;
import com.studentsystemapp.model.enums.UserRolesEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CourseViewModel {


    private Long id;

    private Set<EnrollmentViewModel> enrollments;


    private String name;


    private String description;

    public UserViewModel getTeacher() {
        return enrollments.stream().map(e -> e.getUser())
                .filter(u -> u.getRole().equals(UserRolesEnum.TEACHER))
                .limit(1L).toList().get(0);
    }


}
