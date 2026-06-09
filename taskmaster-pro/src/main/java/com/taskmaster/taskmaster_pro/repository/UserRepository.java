package com.taskmaster.taskmaster_pro.repository;

import com.taskmaster.taskmaster_pro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}