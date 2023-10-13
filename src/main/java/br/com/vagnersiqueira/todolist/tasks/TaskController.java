package br.com.vagnersiqueira.todolist.tasks;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.vagnersiqueira.todolist.exceptions.ExceptionError;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

  @Autowired
  private ITaskRepository taskRepository;

  @PostMapping("/")
  public ResponseEntity create(@RequestBody TaskModel task, HttpServletRequest request) {
    String taskValid = this.validateCreateTask(task);
    if (taskValid != null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(taskValid);
    }

    UUID userId = (UUID) request.getAttribute("userId");
    task.setUserId(userId);

    TaskModel taskCreated = this.taskRepository.save(task);

    return ResponseEntity.status(HttpStatus.OK).body(taskCreated);
  }

  private String validateCreateTask(TaskModel task) {
    ExceptionError startAtError = task.startAtIsValid();
    ExceptionError endAtError = task.endAtIsValid();
    ExceptionError titleError = task.titleIsValid();

    if (!startAtError.getValid()) {
      return startAtError.getMessage();
    }

    if (!endAtError.getValid()) {
      return endAtError.getMessage();
    }

    if (!titleError.getValid()) {
      return titleError.getMessage();
    }

    return null;
  }

  @GetMapping("/")
  public ResponseEntity list(HttpServletRequest request) {
    UUID userId = (UUID) request.getAttribute("userId");

    List<TaskModel> tasks = taskRepository.findByUserId(userId);

    return ResponseEntity.status(HttpStatus.OK).body(tasks);
  }
}
