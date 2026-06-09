package com.taskmaster.taskmaster_pro.dto;

import com.taskmaster.taskmaster_pro.model.Priority;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class TaskDtoTest {

    @Test
    void testRecordGettersAndConstructor() {
        TaskDto dto = new TaskDto(1L, "Title", "Desc", Priority.HIGH, LocalDate.now(), 10L, "User");
        assertEquals(1L, dto.id());
        assertEquals("Title", dto.title());
        assertEquals("Desc", dto.description());
        assertEquals(Priority.HIGH, dto.priority());
        assertEquals(10L, dto.userId());
        assertEquals("User", dto.userName());
    }

    @Test
    void testNullFields() {
        TaskDto dto = new TaskDto(2L, "Title2", null, Priority.LOW, null, null, null);
        assertNull(dto.description());
        assertNull(dto.deadline());
        assertNull(dto.userId());
        assertNull(dto.userName());
    }
}