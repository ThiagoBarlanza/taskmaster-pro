package com.taskmaster.taskmaster_pro.service;

import com.taskmaster.taskmaster_pro.model.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
    public class TaskSorter {

        public List<Task> sort(List<Task> tasks, Comparator<Task> comparator) {
            // Converte para ArrayList para garantir mutabilidade e melhor performance
            List<Task> mutableList = new ArrayList<>(tasks);
            mutableList.sort(comparator);
            return mutableList;
        }
}
