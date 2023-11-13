package com.studentsystemapp.model.binding;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CourseResourceAddBindingModel {

    private String title;

    private String description;

    private String videoUrl;

}
