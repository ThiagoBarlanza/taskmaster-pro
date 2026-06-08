package com.taskmaster.taskmaster_pro.service;

import com.taskmaster.taskmaster_pro.model.Priority;
import com.taskmaster.taskmaster_pro.model.Task;
import com.taskmaster.taskmaster_pro.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @Mock
    private TaskCacheService cacheService;

    @InjectMocks
    private TaskService taskService;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task("Test", "Desc", Priority.HIGH, LocalDate.now());
        task.setId(1L);
    }

    // Cache miss: deve ir ao repositório e depois guardar no cache
    @Test
    void findById_CacheMiss_ReturnsTaskAndCachesIt() {
        when(cacheService.get(1L)).thenReturn(null);
        when(repository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(cacheService).put(1L, task);
    }

    // Cache hit: retorna do cache sem tocar no repositório
    @Test
    void findById_CacheHit_ReturnsCachedTask() {
        when(cacheService.get(1L)).thenReturn(task);

        Task result = taskService.findById(1L);

        assertEquals(task, result);
        verify(repository, never()).findById(anyLong());
    }

    @Test
    void findById_NotFound_ThrowsRuntimeException() {
        when(cacheService.get(1L)).thenReturn(null);
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.findById(1L));
    }

    @Test
    void save_CreatesTaskAndUpdatesCache() {
        when(repository.save(task)).thenReturn(task);

        Task saved = taskService.save(task);

        verify(cacheService).put(task.getId(), task);
        assertEquals(task, saved);
    }

    @Test
    void deleteById_RemovesFromRepositoryAndEvictsCache() {
        doNothing().when(repository).deleteById(1L);
        doNothing().when(cacheService).evict(1L);

        taskService.deleteById(1L);

        verify(repository).deleteById(1L);
        verify(cacheService).evict(1L);
    }
}