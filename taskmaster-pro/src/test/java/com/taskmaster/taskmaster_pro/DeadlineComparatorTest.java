package com.taskmaster.taskmaster_pro;

import com.taskmaster.taskmaster_pro.model.Priority;
import com.taskmaster.taskmaster_pro.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DeadlineComparatorTest {

    private DeadlineComparator comparator;
    private Task taskWithDeadline2025;
    private Task taskWithDeadline2026;
    private Task taskWithoutDeadline;

    @BeforeEach
    void setUp() {
        comparator = new DeadlineComparator();
        taskWithDeadline2025 = new Task("A", "", Priority.MID, LocalDate.of(2025, 6, 1));
        taskWithDeadline2026 = new Task("B", "", Priority.HIGH, LocalDate.of(2026, 12, 31));
        taskWithoutDeadline = new Task("C", "", Priority.LOW, null);
    }

    @Test
    void compare_EarlierDeadlineShouldComeFirst() {
        assertTrue(comparator.compare(taskWithDeadline2025, taskWithDeadline2026) < 0);
        assertTrue(comparator.compare(taskWithDeadline2026, taskWithDeadline2025) > 0);
    }

    @Test
    void compare_NullDeadlineShouldBeLast() {
        assertTrue(comparator.compare(taskWithDeadline2025, taskWithoutDeadline) < 0);
        assertTrue(comparator.compare(taskWithoutDeadline, taskWithDeadline2025) > 0);
    }

    @Test
    void compare_BothNullReturnsZero() {
        Task anotherNull = new Task("D", "", Priority.HIGH, null);
        assertEquals(0, comparator.compare(taskWithoutDeadline, anotherNull));
    }

    @Test
    void compare_SameDateReturnsZero() {
        Task sameDate = new Task("E", "", Priority.MID, LocalDate.of(2025, 6, 1));
        assertEquals(0, comparator.compare(taskWithDeadline2025, sameDate));
    }
}