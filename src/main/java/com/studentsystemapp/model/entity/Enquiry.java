package com.studentsystemapp.model.entity;

import com.studentsystemapp.model.enums.UserRolesEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table
@NoArgsConstructor
@Getter
@Setter
public class Enquiry extends BaseEntity {

    @ManyToMany(fetch = FetchType.EAGER)
    private List<BaseUser> participants = new LinkedList();

    @Column(nullable = false)
    private String question;

    @Column
    private String response;

    @Column(nullable = false)
    private Boolean isOpen = true;

    public BaseUser getStudent() {
        return participants.stream().filter(u -> u.getRole().equals(UserRolesEnum.STUDENT))
                .limit(1).collect(Collectors.toList()).get(0);
    }

    public BaseUser getTeacher() {
        return participants.stream().filter(u -> u.getRole().equals(UserRolesEnum.TEACHER))
                .limit(1).collect(Collectors.toList()).get(0);
    }

}
