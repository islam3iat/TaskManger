package com.project.TaskManger.taskScheduling;

import com.project.TaskManger.task.Task;
import jakarta.persistence.*;
import org.hibernate.Hibernate;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "task_scheduling")
public class TaskScheduling {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "due_date")
    private LocalDateTime dueDate;
    @OneToOne(cascade ={CascadeType.PERSIST,CascadeType.REMOVE},fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "task_id_fk"))
    private Task task;

    public TaskScheduling(LocalDateTime startDate, LocalDateTime dueDate) {
        this.startDate = startDate;
        this.dueDate = dueDate;
    }

    public TaskScheduling() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskScheduling that = (TaskScheduling) o;
        return id == that.id && Objects.equals(startDate, that.startDate) && Objects.equals(dueDate, that.dueDate) && Objects.equals(task, that.task);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startDate, dueDate, task);
    }

    @Override
    public String toString() {
        if(task!=null&&!Hibernate.isInitialized(task)){
            Hibernate.initialize(task);
        }
        return "TaskScheduling{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", dueDate=" + dueDate +
                ", task=" + task +
                '}';
    }
}
