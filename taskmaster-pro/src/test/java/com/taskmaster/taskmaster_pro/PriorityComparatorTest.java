package com.taskmaster.taskmaster_pro;

import com.taskmaster.taskmaster_pro.model.Priority;
import com.taskmaster.taskmaster_pro.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class PriorityComparatorTest {

    private PriorityComparator comparator;
    private Task highTask;
    private Task midTask;
    private Task lowTask;

    @BeforeEach
    void setUp() {
        comparator = new PriorityComparator();
        highTask = new Task("High", "", Priority.HIGH, LocalDate.now());
        midTask = new Task("Mid", "", Priority.MID, LocalDate.now());
        lowTask = new Task("Low", "", Priority.LOW, LocalDate.now());
    }

    @Test
    void compare_HighShouldComeBeforeMid() {
        assertTrue(comparator.compare(highTask, midTask) < 0);
        assertTrue(comparator.compare(midTask, highTask) > 0);
    }

    @Test
    void compare_HighShouldComeBeforeLow() {
        assertTrue(comparator.compare(highTask, lowTask) < 0);
        assertTrue(comparator.compare(lowTask, highTask) > 0);
    }

    @Test
    void compare_MidShouldComeBeforeLow() {
        assertTrue(comparator.compare(midTask, lowTask) < 0);
        assertTrue(comparator.compare(lowTask, midTask) > 0);
    }

    @Test
    void compare_SamePriorityReturnsZero() {
        Task anotherHigh = new Task("Another High", "", Priority.HIGH, LocalDate.now());
        assertEquals(0, comparator.compare(highTask, anotherHigh));
    }
}