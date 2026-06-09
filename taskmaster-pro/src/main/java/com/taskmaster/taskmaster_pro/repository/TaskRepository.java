package com.taskmaster.taskmaster_pro.repository;

import com.taskmaster.taskmaster_pro.model.Task;
import com.taskmaster.taskmaster_pro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // INNER JOIN: retorna apenas tarefas que têm um user associado
    @Query("SELECT t FROM Task t INNER JOIN t.user u")
    List<Task> findTasksWithUser();

    // LEFT JOIN: retorna todas as tarefas, mesmo as sem user (user será null)
    @Query("SELECT t FROM Task t LEFT JOIN t.user u")
    List<Task> findAllTasksWithUserLeftJoin();

    // (Opcional) Buscar users com as suas tarefas (LEFT JOIN do lado do User)
    @Query("SELECT u FROM User u LEFT JOIN u.tasks")
    List<User> findAllUsersWithTasksLeftJoin();
}