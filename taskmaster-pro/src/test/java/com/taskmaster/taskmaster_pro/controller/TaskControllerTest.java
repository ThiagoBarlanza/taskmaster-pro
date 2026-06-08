package com.taskmaster.taskmaster_pro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmaster.taskmaster_pro.model.Priority;
import com.taskmaster.taskmaster_pro.model.Task;
import com.taskmaster.taskmaster_pro.repository.TaskRepository;
import com.taskmaster.taskmaster_pro.service.CsvImportService;
import com.taskmaster.taskmaster_pro.service.JsonImportService;
import com.taskmaster.taskmaster_pro.service.TaskService;
import com.taskmaster.taskmaster_pro.service.TaskSorter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private TaskSorter taskSorter;

    @MockitoBean
    private CsvImportService csvImportService;

    @MockitoBean
    private JsonImportService jsonImportService;

    @MockitoBean
    private TaskRepository taskRepository;   // ← ADD THIS

    @Autowired
    private ObjectMapper objectMapper;

    // ------------------------------------------------------------------
    // Existing tests (GET, POST, GET by ID)
    // ------------------------------------------------------------------

    @Test
    void listAll_ShouldReturnTasks() throws Exception {
        Task task = new Task("Test", "Desc", Priority.HIGH, LocalDate.now());
        task.setId(1L);
        List<Task> tasks = Arrays.asList(task);

        when(taskService.findAll()).thenReturn(tasks);

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Test"));
    }

    @Test
    void create_ShouldReturnCreatedTask() throws Exception {
        Task input = new Task("New", "Desc", Priority.MID, null);
        Task saved = new Task("New", "Desc", Priority.MID, null);
        saved.setId(10L);

        when(taskService.save(any(Task.class))).thenReturn(saved);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.title").value("New"));
    }

    @Test
    void findById_ShouldReturnTask() throws Exception {
        Task task = new Task("Found", "Desc", Priority.HIGH, null);
        task.setId(5L);
        when(taskService.findById(5L)).thenReturn(task);

        mockMvc.perform(get("/tasks/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Found"));
    }

    // ------------------------------------------------------------------
    // Sorted endpoint tests
    // ------------------------------------------------------------------

    @Test
    void sortedTasks_WithPriorityCriteria_ReturnsOk() throws Exception {
        // Mock repository.findAll() because controller uses it directly
        when(taskRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/tasks/sorted").param("criteria", "priority"))
                .andExpect(status().isOk());
    }

    @Test
    void sortedTasks_WithDeadlineCriteria_ReturnsOk() throws Exception {
        when(taskRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/tasks/sorted").param("criteria", "deadline"))
                .andExpect(status().isOk());
    }

    @Test
    void sortedTasks_InvalidCriteria_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/tasks/sorted").param("criteria", "invalid"))
                .andExpect(status().isBadRequest());
    }

    // ------------------------------------------------------------------
    // Generate endpoint tests
    // ------------------------------------------------------------------

    @Test
    void generateRandomTasks_ReturnsSuccessMessage() throws Exception {
        // Mock repository.save() because controller uses it directly
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(post("/tasks/generate").param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().string("10 tasks generated successfully."));
    }

    // ------------------------------------------------------------------
    // CSV import tests
    // ------------------------------------------------------------------

    @Test
    void importCsv_WithMockFile_ReturnsSuccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.csv", "text/csv", "title,priority\nTest,HIGH".getBytes());
        when(csvImportService.importCsv(any())).thenReturn(1);

        mockMvc.perform(multipart("/tasks/import/csv").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Imported 1 tasks from CSV"));
    }

    @Test
    void importCsv_WhenServiceThrowsException_ReturnsInternalServerError() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "bad.csv", "text/csv", "bad".getBytes());
        when(csvImportService.importCsv(any()))
                .thenThrow(new RuntimeException("Parsing error"));

        mockMvc.perform(multipart("/tasks/import/csv").file(file))
                .andExpect(status().isInternalServerError());
    }

    // ------------------------------------------------------------------
    // JSON import tests
    // ------------------------------------------------------------------

    @Test
    void importJson_WithMockFile_ReturnsSuccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.json", "application/json",
                "[{\"title\":\"Test\",\"priority\":\"HIGH\"}]".getBytes());
        when(jsonImportService.importJson(any())).thenReturn(1);

        mockMvc.perform(multipart("/tasks/import/json").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Imported 1 tasks from JSON"));
    }

    @Test
    void importJson_WhenServiceThrowsException_ReturnsInternalServerError() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "bad.json", "application/json", "{}".getBytes());
        when(jsonImportService.importJson(any()))
                .thenThrow(new RuntimeException("JSON parsing error"));

        mockMvc.perform(multipart("/tasks/import/json").file(file))
                .andExpect(status().isInternalServerError());
    }
}