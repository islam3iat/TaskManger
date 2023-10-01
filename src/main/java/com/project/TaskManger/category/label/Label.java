package com.project.TaskManger.category.label;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.TaskManger.category.Category;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "label",uniqueConstraints = @UniqueConstraint(name = "label_unique_name",columnNames = "name"))
public class Label {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name",columnDefinition = "VARCHAR(50)")
    private String name;
    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.REMOVE},fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "category_id_fk")
    )
    private Category category;

    public Label(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public Label() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


    @Override
    public String toString() {
        return "Label{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Label label = (Label) o;
        return id == label.id && Objects.equals(name, label.name) && Objects.equals(category, label.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category);
    }
}
