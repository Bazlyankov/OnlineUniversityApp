package com.studentsystemapp.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne(fetch = FetchType.EAGER)
    private BaseUser teacher;

    @ManyToOne(fetch = FetchType.EAGER)
    private Course course;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.EAGER)
    private Set<BaseUser> students;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<CourseResource> courseResources;

}
