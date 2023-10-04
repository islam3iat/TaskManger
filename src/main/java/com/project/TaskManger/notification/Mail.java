package com.project.TaskManger.notification;

import com.project.TaskManger.security.user.User;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "mail")
public class Mail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "_to",columnDefinition = "VARCHAR(255)")
    private String to;
    @Column(name = "subject",columnDefinition = "TEXT")
    private String subject;
    @Column(name = "text",columnDefinition = "TEXT")
    private String text;
    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    @JoinColumn(name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "user_id_fk"))
    private User user;
    public Mail(String to, String subject, String text) {
        this.to = to;
        this.subject = subject;
        this.text = text;
    }

    public Mail() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mail mail = (Mail) o;
        return Objects.equals(id, mail.id) && Objects.equals(to, mail.to) && Objects.equals(subject, mail.subject) && Objects.equals(text, mail.text) && Objects.equals(user, mail.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, to, subject, text, user);
    }

    @Override
    public String toString() {
        return "Mail{" +
                "id=" + id +
                ", to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                ", user=" + user +
                '}';
    }
}
