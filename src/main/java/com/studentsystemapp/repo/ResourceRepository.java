package com.studentsystemapp.repo;

import com.studentsystemapp.model.entity.CourseResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<CourseResource, Long> {



}
