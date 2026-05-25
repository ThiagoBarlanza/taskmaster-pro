package com.taskmaster.taskmaster_pro.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    private LocalDate deadline;

    private LocalDateTime createdAt;

    // Construtor vazio (obrigatório para JPA)
    public Task() {}

    // Construtor útil para criação manual
    public Task(String title, String description, Priority priority, LocalDate deadline) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.createdAt = LocalDateTime.now();
    }

    // Getters e Setters (todos)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    // (Se usares Lombok: @Data, @NoArgsConstructor, @AllArgsConstructor)
}

