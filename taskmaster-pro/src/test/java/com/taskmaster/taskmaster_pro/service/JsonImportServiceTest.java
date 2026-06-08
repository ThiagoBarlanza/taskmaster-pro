package com.taskmaster.taskmaster_pro.service;

import com.taskmaster.taskmaster_pro.model.Task;
import com.taskmaster.taskmaster_pro.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JsonImportServiceTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private JsonImportService jsonImportService;

    @Test
    void importJson_ValidArray_ImportsAllTasks() throws Exception {
        String json = """
                [
                  {"title":"Task1","priority":"HIGH","deadline":"2026-12-31"},
                  {"title":"Task2","priority":"LOW"}
                ]
                """;
        MultipartFile file = new MockMultipartFile("file", "tasks.json", "application/json", json.getBytes());

        when(repository.saveAll(anyList())).thenAnswer(inv -> inv.getArgument(0));

        int count = jsonImportService.importJson(file);

        assertEquals(2, count);
        verify(repository, times(1)).saveAll(anyList());
    }

    @Test
    void importJson_EmptyArray_ReturnsZero() throws Exception {
        String json = "[]";
        MultipartFile file = new MockMultipartFile("file", "empty.json", "application/json", json.getBytes());

        int count = jsonImportService.importJson(file);

        assertEquals(0, count);
        verify(repository, never()).saveAll(any());
    }

    @Test
    void importJson_NotAnArray_ThrowsException() {
        String json = "{\"title\":\"not an array\"}";
        MultipartFile file = new MockMultipartFile("file", "invalid.json", "application/json", json.getBytes());

        assertThrows(IllegalArgumentException.class, () -> jsonImportService.importJson(file));
        verify(repository, never()).saveAll(any());
    }

    @Test
    void importJson_InvalidPriority_ThrowsException() {
        String json = """
                [
                  {"title":"Bad","priority":"INVALID"}
                ]
                """;
        MultipartFile file = new MockMultipartFile("file", "bad.json", "application/json", json.getBytes());

        assertThrows(IllegalArgumentException.class, () -> jsonImportService.importJson(file));
        verify(repository, never()).saveAll(any());
    }
}