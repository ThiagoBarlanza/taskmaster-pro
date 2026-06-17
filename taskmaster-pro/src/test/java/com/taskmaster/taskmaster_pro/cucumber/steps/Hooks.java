package com.taskmaster.taskmaster_pro.cucumber.steps;

import com.taskmaster.taskmaster_pro.repository.TaskRepository;
import com.taskmaster.taskmaster_pro.service.TaskCacheService;
import io.cucumber.java.After;
import org.springframework.beans.factory.annotation.Autowired;

public class Hooks {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskCacheService taskCacheService;

    @After
    public void cleanDatabase() {
        taskRepository.deleteAll();
        taskCacheService.clear();
    }
}