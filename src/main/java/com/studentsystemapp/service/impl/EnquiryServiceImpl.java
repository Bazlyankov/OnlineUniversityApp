package com.studentsystemapp.service.impl;

import com.studentsystemapp.model.binding.EnquiryAddBindingModel;
import com.studentsystemapp.model.entity.BaseUser;
import com.studentsystemapp.model.entity.Enquiry;
import com.studentsystemapp.model.view.EnquiryViewModel;
import com.studentsystemapp.repo.EnquiryRepository;
import com.studentsystemapp.repo.UserRepository;
import com.studentsystemapp.service.EnquiryService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnquiryServiceImpl implements EnquiryService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final EnquiryRepository enquiryRepository;

    public EnquiryServiceImpl(UserRepository userRepository, ModelMapper modelMapper, EnquiryRepository enquiryRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.enquiryRepository = enquiryRepository;
    }

    @Override
    @Transactional
    public void add(EnquiryAddBindingModel enquiryAddBindingModel) {

        Optional<BaseUser> optionalTeacher = userRepository.findById(enquiryAddBindingModel.getTeacherId());
        Optional<BaseUser> optionalStudent = userRepository.findById(enquiryAddBindingModel.getStudentId());

        if (optionalTeacher.isEmpty() || optionalStudent.isEmpty()) {
            throw new NoSuchElementException();
        }

        Enquiry enquiry = modelMapper.map(enquiryAddBindingModel, Enquiry.class);

        BaseUser student = optionalStudent.get();
        BaseUser teacher = optionalTeacher.get();

        student.getEnquiries().add(enquiry);
        teacher.getEnquiries().add(enquiry);
        enquiry.getParticipants().add(teacher);
        enquiry.getParticipants().add(student);


        enquiryRepository.save(enquiry);
        userRepository.saveAllAndFlush(List.of(student, teacher));

    }

    @Override
    public Set<EnquiryViewModel> getEnquiriesForUser(String username) {

        Optional<BaseUser> optionalBaseUser = userRepository.getByUsername(username);

        if (optionalBaseUser.isEmpty()) {
            throw new NoSuchElementException();
        }

        return optionalBaseUser.get().getEnquiries().stream().map(e ->  modelMapper.map(e, EnquiryViewModel.class)
                ).collect(Collectors.toSet());

    }

    @Override
    @Transactional
    public void addResponse(String response, Long enquiryId) {

        Optional<Enquiry> optionalEnquiry = enquiryRepository.findById(enquiryId);

        if (optionalEnquiry.isEmpty()) {
            throw new NoSuchElementException();
        }

        Enquiry enquiry = optionalEnquiry.get();
        enquiry.setIsOpen(false);
        enquiry.setResponse(response);
        enquiryRepository.saveAndFlush(enquiry);

    }

    @Override
    public EnquiryViewModel getById(Long id) {

        Optional<Enquiry> optionalEnquiry = enquiryRepository.findById(id);

        if (optionalEnquiry.isEmpty()) {
            throw new NoSuchElementException();
        }

        return modelMapper.map(optionalEnquiry.get(), EnquiryViewModel.class);
    }


}
