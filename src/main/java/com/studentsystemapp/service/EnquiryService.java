package com.studentsystemapp.service;

import com.studentsystemapp.model.binding.EnquiryAddBindingModel;
import com.studentsystemapp.model.view.EnquiryViewModel;

import java.util.Set;

public interface EnquiryService {


    void add(EnquiryAddBindingModel enquiryAddBindingModel);

    Set<EnquiryViewModel> getEnquiriesForUser(String username);

    void addResponse(String response, Long enquiryId);

    EnquiryViewModel getById(Long id);
}
