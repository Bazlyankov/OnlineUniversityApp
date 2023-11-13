package com.studentsystemapp.config;

import com.studentsystemapp.model.binding.UserRegisterBindingModel;
import com.studentsystemapp.model.entity.BaseUser;
import com.studentsystemapp.model.entity.Task;
import com.studentsystemapp.model.view.TaskViewModel;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();


        modelMapper.typeMap( Task.class, TaskViewModel.class )
                .addMapping( src -> src.getGrade().getValue(), TaskViewModel::setGradeValue);

        modelMapper.typeMap(UserRegisterBindingModel.class, BaseUser.class);
        modelMapper.getConfiguration().setAmbiguityIgnored(true);

        return modelMapper;
    }



}
