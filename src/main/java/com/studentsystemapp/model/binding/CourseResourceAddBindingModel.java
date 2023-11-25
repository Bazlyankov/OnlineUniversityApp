package com.studentsystemapp.model.binding;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CourseResourceAddBindingModel {
    @Size(min = 3, max = 40, message = "Title length must be between 3 and 40 characters")
    private String title;
    @Size(min = 3, max = 100, message = "Description length must be between 3 and 100 characters")
    private String description;
    @Size(min = 11, max = 11, message = "Video id length must be between exactly 11 characters")
    private String videoUrl;

}
