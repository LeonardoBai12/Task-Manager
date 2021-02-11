package com.example.bancodedadossqlite;

import java.util.List;

public interface ITaskDAO {
    public boolean newTask(Task task);
    public boolean update(Task task);
    public boolean delete(Task task);
    public List<Task> taskList();
}
