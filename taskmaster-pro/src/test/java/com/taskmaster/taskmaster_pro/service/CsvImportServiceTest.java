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

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CsvImportServiceTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private CsvImportService csvImportService;

    @Test
    void importCsv_ValidContent_ImportsCorrectNumberOfTasks() throws Exception {
        String csvContent = "title,description,priority,deadline\n" +
                "Task1,Desc1,HIGH,2026-12-31\n" +
                "Task2,Desc2,MID,\n" +
                "Task3,,LOW,2025-01-01";
        MultipartFile file = new MockMultipartFile("file", "tasks.csv", "text/csv", csvContent.getBytes());

        when(repository.saveAll(anyList())).thenAnswer(inv -> inv.getArgument(0));

        int count = csvImportService.importCsv(file);

        assertEquals(3, count);
        verify(repository, times(1)).saveAll(anyList());
    }

    @Test
    void importCsv_EmptyFile_ReturnsZero() throws Exception {
        String csvContent = "title,description,priority,deadline\n";
        MultipartFile file = new MockMultipartFile("file", "empty.csv", "text/csv", csvContent.getBytes());

        int count = csvImportService.importCsv(file);

        assertEquals(0, count);
        verify(repository, never()).saveAll(any());
    }

    @Test
    void importCsv_InvalidPriority_ThrowsException() {
        String csvContent = "title,description,priority,deadline\n" +
                "Bad,InvalidPriority,INVALID,2026-12-31";
        MultipartFile file = new MockMultipartFile("file", "bad.csv", "text/csv", csvContent.getBytes());

        assertThrows(IllegalArgumentException.class, () -> csvImportService.importCsv(file));
    }
}