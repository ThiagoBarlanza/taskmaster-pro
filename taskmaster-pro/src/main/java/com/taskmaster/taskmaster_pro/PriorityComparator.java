package com.taskmaster.taskmaster_pro;

import com.taskmaster.taskmaster_pro.model.Priority;
import com.taskmaster.taskmaster_pro.model.Task;

import java.util.Comparator;

public class PriorityComparator implements Comparator<Task> {
    @Override
    public int compare(Task t1, Task t2) {
        // Ordena por prioridade: HIGH > MID > LOW
        Priority p1 = t1.getPriority();
        Priority p2 = t2.getPriority();
        // ordinal(): LOW=0, MID=1, HIGH=2. Queremos ordem decrescente (HIGH primeiro)
        return Integer.compare(p2.ordinal(), p1.ordinal());
    }
}
