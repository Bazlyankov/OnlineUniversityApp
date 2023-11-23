package com.studentsystemapp.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "teachers")
@Getter
@Setter
public class Teacher extends BaseUser {

}
