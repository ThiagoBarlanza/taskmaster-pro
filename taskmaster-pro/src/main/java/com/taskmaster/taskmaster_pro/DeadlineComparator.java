package com.taskmaster.taskmaster_pro;

import com.taskmaster.taskmaster_pro.model.Task;

import java.time.LocalDate;
import java.util.Comparator;

public class DeadlineComparator implements Comparator<Task> {

    @Override
    public int compare(Task t1, Task t2) {
        LocalDate d1 = t1.getDeadline();
        LocalDate d2 = t2.getDeadline();

        // Place tasks without a deadline at the end
        if (d1 == null && d2 == null) return 0;
        if (d1 == null) return 1;
        if (d2 == null) return -1;

        return d1.compareTo(d2); // oldest date first
    }
}