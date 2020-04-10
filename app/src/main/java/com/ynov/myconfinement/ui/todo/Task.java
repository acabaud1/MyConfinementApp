package com.ynov.myconfinement.ui.todo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Tasks")
public class Task {

    @DatabaseField(columnName = "taskId", generatedId = true)
    private int taskId;

    @DatabaseField
    private String title;

    @DatabaseField
    private String deadline;

    @DatabaseField
    private String category;

    public Task() { }

    public Task(String title, String deadline, String category) {
        this.title = title;
        this.deadline = deadline;
        this.category = category;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
