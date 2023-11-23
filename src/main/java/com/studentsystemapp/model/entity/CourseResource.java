package com.studentsystemapp.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "resources")
@Getter
@Setter
@NoArgsConstructor
public class CourseResource extends BaseEntity{


    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String videoUrl;



}
