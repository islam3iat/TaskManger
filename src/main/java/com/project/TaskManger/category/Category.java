package com.project.TaskManger.category;

import com.project.TaskManger.category.label.Label;
import com.project.TaskManger.task.Task;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "category",
        uniqueConstraints = {@UniqueConstraint(name = "category_unique_title",columnNames = "title")})

public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "title",columnDefinition = "VARCHAR(50)")
    private String title;
    @Column(name = "description",columnDefinition = "TEXT")
    private String description;
    @OneToOne(mappedBy = "category")
    private Task task;

    @OneToMany(mappedBy = "category",
            cascade = {CascadeType.PERSIST,
            CascadeType.REMOVE},
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private List<Label>  labels;

    public Category(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Category() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
    public void addLabel(Label label){
        if(!labels.contains(label)){
            this.labels.add(label);
            label.setCategory(this);
        }
    }
    public void removeLabel(Label label){
        if(labels.contains(label))
            this.labels.remove(label);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id && Objects.equals(title, category.title) && Objects.equals(description, category.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
