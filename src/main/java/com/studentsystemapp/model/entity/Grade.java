package com.studentsystemapp.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Table(name = "grades")
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Grade extends BaseEntity {



    @ManyToOne(fetch = FetchType.EAGER)
    private BaseUser student;

    @Column
    private BigDecimal value;




    public Grade(BigDecimal value, BaseUser student) {
        super();
        this.value = value;
        this.student = student;

    }
}
