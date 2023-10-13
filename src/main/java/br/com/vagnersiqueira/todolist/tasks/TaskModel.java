package br.com.vagnersiqueira.todolist.tasks;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import br.com.vagnersiqueira.todolist.exceptions.ExceptionError;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Entity(name = "tb_tasks")
public class TaskModel {
  private UUID userId;

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  private String description;

  @Column(length = 50)
  private String title;

  private LocalDateTime startAt;

  private LocalDateTime endAt;

  private String priority;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @CreationTimestamp
  private LocalDateTime updatedAt;

  @Transient
  public ExceptionError startAtIsValid() {
    boolean isValid = LocalDateTime.now().isBefore(startAt);
    String message = "Start at cannot be less then current date";

    return new ExceptionError(isValid, message);
  }

  @Transient
  public ExceptionError endAtIsValid() {
    boolean isValid = endAt.isAfter(startAt);
    String message = "End at cannot be less then start at";

    return new ExceptionError(isValid, message);
  }

  @Transient
  public ExceptionError titleIsValid() {
    boolean isValid = title.length() <= 50;
    String message = "Title cannot be greater then 50 chars";

    return new ExceptionError(isValid, message);
  }
}
