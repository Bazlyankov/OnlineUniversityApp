package com.studentsystemapp.model.binding;

import com.studentsystemapp.model.entity.Student;
import com.studentsystemapp.model.entity.Teacher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EnquiryAddBindingModel {

    private Long teacherId;

    private Long studentId;

    private String question;
}
