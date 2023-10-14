package br.com.vagnersiqueira.todolist.tasks;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import br.com.vagnersiqueira.todolist.exceptions.HandleExceptionError;
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
  public void startAtIsValid() throws Exception {
    boolean isValid = true;
    String message = "Start at cannot be less then current date";

    if (startAt != null) {
      isValid = LocalDateTime.now().isBefore(startAt);
    }

    var error = new HandleExceptionError(isValid, message);

    error.triggerError();
  }

  @Transient
  public void endAtIsValid() throws Exception {
    boolean isValid = true;
    String message = "End at cannot be less then start at";

    if (endAt != null) {
      isValid = endAt.isAfter(startAt);
    }

    var error = new HandleExceptionError(isValid, message);

    error.triggerError();
  }

  @Transient
  public void titleIsValid() throws Exception {
    boolean isValid = true;
    String message = "Title cannot be greater then 50 chars";

    if (title != null) {
      isValid = title.length() <= 50;
    }

    var error = new HandleExceptionError(isValid, message);

    error.triggerError();
  }

  @Transient
  public void validateTask() throws Exception {
    startAtIsValid();
    endAtIsValid();
    titleIsValid();
  }
}
