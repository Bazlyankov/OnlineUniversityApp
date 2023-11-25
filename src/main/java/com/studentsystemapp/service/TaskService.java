package com.studentsystemapp.service;

import com.studentsystemapp.model.binding.TaskAddBindingModel;
import com.studentsystemapp.model.view.TaskViewModel;
import com.studentsystemapp.model.entity.Task;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Set;

public interface TaskService {

    public void addTask(Long id, TaskAddBindingModel taskAddBindingModel);

    public Task getById(Long id);


    @Transactional
    String uploadFile(MultipartFile multipartFile, Long taskId) throws IOException;

    void setGradeById(Long taskId, BigDecimal gradeValue, Long studentId);

    Set<TaskViewModel> getTasksByUsername(String username);

    void deleteAll();
}
