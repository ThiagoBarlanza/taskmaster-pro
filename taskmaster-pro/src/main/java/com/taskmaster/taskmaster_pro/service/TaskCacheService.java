package com.taskmaster.taskmaster_pro.service;

import com.taskmaster.taskmaster_pro.model.Task;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class TaskCacheService {

    private final ConcurrentHashMap<Long, Task> cache = new ConcurrentHashMap<>();

    public Task get(Long id) {
        return cache.get(id);
    }

    public void put(Long id, Task task) {
        cache.put(id, task);
    }

    public void evict(Long id) {
        cache.remove(id);
    }

    public void clear() {
        cache.clear();
    }

    public boolean contains(Long id) {
        return cache.containsKey(id);
    }
}