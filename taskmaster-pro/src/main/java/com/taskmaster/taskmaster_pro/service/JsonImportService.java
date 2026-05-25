package com.taskmaster.taskmaster_pro.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.taskmaster.taskmaster_pro.model.Priority;
import com.taskmaster.taskmaster_pro.model.Task;
import com.taskmaster.taskmaster_pro.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class JsonImportService {

    private final TaskRepository repository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public JsonImportService(TaskRepository repository) {
        this.repository = repository;
    }

    public int importJson(MultipartFile file) throws Exception {
        List<Task> tasks = new ArrayList<>();
        JsonFactory factory = new JsonFactory();
        try (InputStream is = file.getInputStream();
             JsonParser parser = factory.createParser(is)) {

            if (parser.nextToken() != JsonToken.START_ARRAY) {
                throw new IllegalArgumentException("JSON must be an array of tasks");
            }

            while (parser.nextToken() != JsonToken.END_ARRAY) {
                Task task = new Task();
                while (parser.nextToken() != JsonToken.END_OBJECT) {
                    String fieldName = parser.getCurrentName();
                    parser.nextToken();
                    switch (fieldName) {
                        case "title":
                            task.setTitle(parser.getText());
                            break;
                        case "description":
                            task.setDescription(parser.getText());
                            break;
                        case "priority":
                            task.setPriority(Priority.valueOf(parser.getText().toUpperCase()));
                            break;
                        case "deadline":
                            String dateStr = parser.getText();
                            if (dateStr != null && !dateStr.isEmpty()) {
                                task.setDeadline(LocalDate.parse(dateStr, DATE_FORMATTER));
                            }
                            break;
                        default:
                            parser.skipChildren();
                    }
                }
                task.setCreatedAt(LocalDateTime.now());
                tasks.add(task);
            }
        }
        return repository.saveAll(tasks).size();
    }
}