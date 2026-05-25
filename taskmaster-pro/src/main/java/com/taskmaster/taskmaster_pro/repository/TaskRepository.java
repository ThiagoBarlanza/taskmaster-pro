package com.taskmaster.taskmaster_pro.repository;

import com.taskmaster.taskmaster_pro.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
