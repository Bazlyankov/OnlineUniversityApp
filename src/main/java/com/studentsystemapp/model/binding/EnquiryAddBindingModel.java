package com.studentsystemapp.model.binding;

import com.studentsystemapp.model.entity.Student;
import com.studentsystemapp.model.entity.Teacher;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
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

    @Size(min = 3, max = 100, message = "Question length must be between 3 and 100 characters long")
    private String question;
}
