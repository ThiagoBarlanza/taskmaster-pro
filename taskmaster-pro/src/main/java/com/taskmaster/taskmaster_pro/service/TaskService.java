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
        // 1. Verificar cache
        Task cached = cacheService.get(id);
        if (cached != null) {
            System.out.println("Cache HIT for id " + id);
            return cached;
        }

        // 2. Cache miss – buscar no repositório
        System.out.println("Cache MISS for id " + id + " – fetching from DB");
        Task task = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id " + id));

        // 3. Armazenar no cache para próximas consultas
        cacheService.put(id, task);
        return task;
    }

    public Task save(Task task) {
        Task saved = repository.save(task);
        // Opcional: invalidar cache ou atualizar
        cacheService.put(saved.getId(), saved);
        return saved;
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
        cacheService.evict(id);
    }

    // Outros métodos (listAll, etc.) podem ser delegados diretamente ao repositório
    public Iterable<Task> findAll() {
        return repository.findAll();
    }
}