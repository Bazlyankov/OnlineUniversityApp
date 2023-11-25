package com.studentsystemapp.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "resources")
@Getter
@Setter
@NoArgsConstructor
public class CourseResource extends BaseEntity{

    @Column
    @Size(min = 3, max = 40)
    private String title;

    @Column
    @Size(min = 3, max = 40)
    private String description;

    @Column
    @Size(min = 11, max = 11)
    private String videoUrl;

}
