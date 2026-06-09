package com.taskmaster.taskmaster_pro.service;

import com.taskmaster.taskmaster_pro.model.Task;
import com.taskmaster.taskmaster_pro.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository repository;
    private final TaskCacheService cacheService;

    public TaskService(TaskRepository repository, TaskCacheService cacheService) {
        this.repository = repository;
        this.cacheService = cacheService;
    }

    public Task findById(Long id) {
        // 1. Check cache
        Task cached = cacheService.get(id);
        if (cached != null) {
            System.out.println("Cache HIT for id " + id);
            return cached;
        }

        // 2. Cache miss – fetch from repository
        System.out.println("Cache MISS for id " + id + " – fetching from DB");
        Task task = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id " + id));

        // 3. Store in cache for future requests
        cacheService.put(id, task);
        return task;
    }

    public Task save(Task task) {
        Task saved = repository.save(task);

        // Optional: invalidate or update cache
        cacheService.put(saved.getId(), saved);

        return saved;
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
        cacheService.evict(id);
    }

    // Other read operations can be delegated directly to the repository
    public Iterable<Task> findAll() {
        return repository.findAll();
    }
}