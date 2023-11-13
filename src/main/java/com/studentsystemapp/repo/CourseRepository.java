package com.studentsystemapp.repo;

import com.studentsystemapp.model.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.studentsystemapp.model.entity.Course;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    public Optional<Course> getCourseByName(String name);

    public Optional<Course> getCourseById(Long id);


}
