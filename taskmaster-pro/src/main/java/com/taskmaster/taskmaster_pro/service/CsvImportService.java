package com.taskmaster.taskmaster_pro.service;

import com.taskmaster.taskmaster_pro.model.Priority;
import com.taskmaster.taskmaster_pro.model.Task;
import com.taskmaster.taskmaster_pro.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvImportService {

    private final TaskRepository repository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public CsvImportService(TaskRepository repository) {
        this.repository = repository;
    }

    public int importCsv(MultipartFile file) throws Exception {
        List<Task> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            // Skip header if present (assume first line is column names)
            String line = reader.readLine(); // header
            if (line == null) return 0;

            String[] headers = line.split(",");
            // Validate headers: title, description, priority, deadline (optional)
            // We'll parse assuming columns: title,description,priority,deadline

            String row;
            while ((row = reader.readLine()) != null) {
                String[] fields = row.split(",", -1); // keep trailing empty
                if (fields.length < 3) continue; // invalid row

                Task task = new Task();
                task.setTitle(fields[0].trim());
                task.setDescription(fields[1].trim());
                task.setPriority(Priority.valueOf(fields[2].trim().toUpperCase()));
                if (fields.length > 3 && fields[3] != null && !fields[3].trim().isEmpty()) {
                    task.setDeadline(LocalDate.parse(fields[3].trim(), DATE_FORMATTER));
                }
                task.setCreatedAt(LocalDateTime.now());
                tasks.add(task);
            }
        }
        return repository.saveAll(tasks).size();
    }
}