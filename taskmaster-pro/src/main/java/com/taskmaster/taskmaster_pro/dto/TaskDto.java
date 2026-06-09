package com.taskmaster.taskmaster_pro.dto;

import com.taskmaster.taskmaster_pro.model.Priority;
import java.time.LocalDate;

public record TaskDto(
        Long id,
        String title,
        String description,
        Priority priority,
        LocalDate deadline,
        Long userId,
        String userName
) {}