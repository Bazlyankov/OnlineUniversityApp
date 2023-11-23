package com.studentsystemapp.model.entity;


import com.studentsystemapp.model.enums.UserRolesEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course extends BaseEntity {

    @Column
    private String name;

    @Column
    private String description;



    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "course")
    private Set<Enrollment> enrollments = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<CourseResource> courseResources = new HashSet<>();

    public BaseUser getTeacher() {
        return enrollments.stream().map(e -> e.getUser())
                .filter(u -> u.getRole().equals(UserRolesEnum.TEACHER))
                .limit(1L).toList().get(0);
    }

}
