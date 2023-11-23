package com.studentsystemapp.model.view;

import com.studentsystemapp.model.entity.Student;
import com.studentsystemapp.model.entity.Teacher;
import com.studentsystemapp.model.enums.UserRolesEnum;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EnquiryViewModel {

    private Long id;

    private List<UserViewModel> participants;

    public UserViewModel teacher() {
        return participants.stream().filter(u -> u.getRole().equals(UserRolesEnum.TEACHER))
                .limit(1).collect(Collectors.toList()).get(0);
    }

    public UserViewModel student() {
        return participants.stream().filter(u -> u.getRole().equals(UserRolesEnum.STUDENT))
                .limit(1).collect(Collectors.toList()).get(0);
    }


    private String question;

    private String response;

    private boolean isOpen;

}
