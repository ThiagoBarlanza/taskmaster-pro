package com.taskmaster.taskmaster_pro.repository;

import com.taskmaster.taskmaster_pro.model.Task;
import com.taskmaster.taskmaster_pro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // INNER JOIN: returns only tasks that have an associated user
    @Query("SELECT t FROM Task t INNER JOIN t.user u")
    List<Task> findTasksWithUser();

    // LEFT JOIN: returns all tasks, including those without a user (user will be null)
    @Query("SELECT t FROM Task t LEFT JOIN t.user u")
    List<Task> findAllTasksWithUserLeftJoin();

    // (Optional) Retrieve users along with their tasks (LEFT JOIN from the User side)
    @Query("SELECT u FROM User u LEFT JOIN u.tasks")
    List<User> findAllUsersWithTasksLeftJoin();
}