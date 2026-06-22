package com.taskmaster.taskmaster_pro.controller;

import com.taskmaster.taskmaster_pro.DeadlineComparator;
import com.taskmaster.taskmaster_pro.PriorityComparator;
import com.taskmaster.taskmaster_pro.dto.TaskDto;
import com.taskmaster.taskmaster_pro.model.Priority;
import com.taskmaster.taskmaster_pro.model.Task;
import com.taskmaster.taskmaster_pro.model.User;
import com.taskmaster.taskmaster_pro.repository.TaskRepository;
import com.taskmaster.taskmaster_pro.repository.UserRepository;
import com.taskmaster.taskmaster_pro.service.CsvImportService;
import com.taskmaster.taskmaster_pro.service.JsonImportService;
import com.taskmaster.taskmaster_pro.service.TaskService;
import com.taskmaster.taskmaster_pro.service.TaskSorter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static final Random RANDOM = new Random();

    private final TaskService taskService;
    private final TaskSorter taskSorter;
    private final CsvImportService csvImportService;
    private final JsonImportService jsonImportService;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public TaskController(TaskService taskService, TaskSorter taskSorter,
                          CsvImportService csvImportService, JsonImportService jsonImportService,
                          UserRepository userRepository, TaskRepository taskRepository) {
        this.taskService = taskService;
        this.taskSorter = taskSorter;
        this.csvImportService = csvImportService;
        this.jsonImportService = jsonImportService;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping("/join-demo")
    public List<TaskDto> demonstrateJoins() {
        List<Task> tasks = taskRepository.findAllTasksWithUserLeftJoin();
        return tasks.stream()
                .map(task -> new TaskDto(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getPriority(),
                        task.getDeadline(),
                        task.getUser() != null ? task.getUser().getId() : null,
                        task.getUser() != null ? task.getUser().getName() : null
                ))
                .collect(Collectors.toList());
    }

    @GetMapping
    public List<Task> listAll() {
        return (List<Task>) taskService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@RequestBody Task task) {
        if (task.getCreatedAt() == null) {
            task.setCreatedAt(LocalDateTime.now());
        }
        return taskService.save(task);
    }

    @GetMapping("/{id}")
    public TaskDto findById(@PathVariable Long id) {
        Task task = taskService.findById(id);
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getDeadline(),
                task.getUser() != null ? task.getUser().getId() : null,
                task.getUser() != null ? task.getUser().getName() : null
        );
    }

    @GetMapping("/sorted")
    public List<Task> sortedTasks(@RequestParam String criteria) {
        List<Task> allTasks = new ArrayList<>((List<Task>) taskService.findAll());
        Comparator<Task> comparator = switch (criteria.toLowerCase()) {
            case "priority" -> new PriorityComparator();
            case "deadline" -> new DeadlineComparator();
            default -> throw new IllegalArgumentException("Invalid criteria. Use 'priority' or 'deadline'.");
        };
        return taskSorter.sort(allTasks, comparator);
    }

    @PostMapping("/generate")
    public String generateRandomTasks(@RequestParam(defaultValue = "100") int count) {
        Priority[] priorities = Priority.values();
        for (int i = 1; i <= count; i++) {
            Task task = new Task();
            task.setTitle("Auto Task " + i);
            task.setDescription("Randomly generated for testing");
            task.setPriority(priorities[RANDOM.nextInt(priorities.length)]);
            long days = (long) RANDOM.nextInt(396) - 30;
            task.setDeadline(LocalDate.now().plusDays(days));
            task.setCreatedAt(LocalDateTime.now());
            taskService.save(task);
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

    @PostMapping("/setup-demo")
    public String setupDemo() {
        // Create users
        User alice = new User("Alice", "alice@example.com");
        User bob = new User("Bob", "bob@example.com");
        userRepository.saveAll(List.of(alice, bob));

        // Create tasks (some with users, others without)
        Task t1 = new Task("Task 1", "Desc1", Priority.HIGH, LocalDate.now());
        t1.setUser(alice);
        taskService.save(t1);

        Task t2 = new Task("Task 2", "Desc2", Priority.MID, LocalDate.now());
        t2.setUser(bob);
        taskService.save(t2);

        Task t3 = new Task("Task 3", "No user", Priority.LOW, null);
        taskService.save(t3);

        return "Demo data created. Users: Alice, Juca. Tasks: 3 (2 with users, 1 without).";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}