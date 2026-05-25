package com.taskmaster.taskmaster_pro.controller;


import com.taskmaster.taskmaster_pro.DeadlineComparator;
import com.taskmaster.taskmaster_pro.PriorityComparator;
import com.taskmaster.taskmaster_pro.model.Priority;
import com.taskmaster.taskmaster_pro.model.Task;
import com.taskmaster.taskmaster_pro.repository.TaskRepository;
import com.taskmaster.taskmaster_pro.service.CsvImportService;
import com.taskmaster.taskmaster_pro.service.JsonImportService;
import com.taskmaster.taskmaster_pro.service.TaskSorter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository repository;
    private final TaskSorter taskSorter;
    private final CsvImportService csvImportService;
    private final JsonImportService jsonImportService;

    public TaskController(TaskRepository repository, TaskSorter taskSorter, CsvImportService csvImportService, JsonImportService jsonImportService) {
        this.repository = repository;
        this.taskSorter = taskSorter;
        this.csvImportService = csvImportService;
        this.jsonImportService = jsonImportService;
    }

    @GetMapping
    public List<Task> listAll() {
        return repository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@RequestBody Task task) {
        // Garante que o createdAt é preenchido (caso não venha no JSON)
        if (task.getCreatedAt() == null) {
            task.setCreatedAt(java.time.LocalDateTime.now());
        }
        return repository.save(task);
    }

    @GetMapping("/{id}")
    public Task findById(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id " + id));
    }

    @GetMapping("/sorted")
    public List<Task> sortedTasks(@RequestParam String criteria) {
        List<Task> allTasks = repository.findAll();
        Comparator<Task> comparator = switch (criteria.toLowerCase()) {
            case "priority" -> new PriorityComparator();
            case "deadline" -> new DeadlineComparator();
            default -> throw new IllegalArgumentException("Invalid criteria. Use 'priority' or 'deadline'.");
        };
        return taskSorter.sort(allTasks, comparator);
    }

    @PostMapping("/generate")
    public String generateRandomTasks(@RequestParam(defaultValue = "100") int count) {
        Random random = new Random();
        Priority[] priorities = Priority.values();
        for (int i = 1; i <= count; i++) {
            Task task = new Task();
            task.setTitle("Auto Task " + i);
            task.setDescription("Randomly generated for testing");
            task.setPriority(priorities[random.nextInt(priorities.length)]);
            long days = random.nextInt(396) - 30; // -30 to 365 days
            task.setDeadline(LocalDate.now().plusDays(days));
            task.setCreatedAt(LocalDateTime.now());
            repository.save(task);
        }
        return count + " tasks generated successfully.";
    }

    @PostMapping("/import/csv")
    public String importCsv(@RequestParam("file") MultipartFile file) {
        try {
            int count = csvImportService.importCsv(file);
            return String.format("Imported %d tasks from CSV", count);
        } catch (Exception e) {
            throw new RuntimeException("Failed to import CSV: " + e.getMessage());
        }
    }

    @PostMapping("/import/json")
    public String importJson(@RequestParam("file") MultipartFile file) {
        try {
            int count = jsonImportService.importJson(file);
            return String.format("Imported %d tasks from JSON", count);
        } catch (Exception e) {
            throw new RuntimeException("Failed to import JSON: " + e.getMessage());
        }
    }
}