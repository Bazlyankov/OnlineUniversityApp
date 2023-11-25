package com.studentsystemapp.service.impl;

import com.studentsystemapp.repo.EnrollmentRepository;
import com.studentsystemapp.service.EnrollmentService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override

    @Transactional
    public void deleteAll() {
        enrollmentRepository.deleteAll();
    }
}
