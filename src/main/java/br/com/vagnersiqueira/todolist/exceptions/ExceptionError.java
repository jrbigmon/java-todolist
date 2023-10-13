package br.com.vagnersiqueira.todolist.exceptions;

public class ExceptionError {
  private Boolean valid;
  private String message;

  public ExceptionError(boolean valid, String message) {
    this.valid = valid;

    if (!valid) {
      this.message = message;
    } else {
      this.message = "";
    }
  }

  public String getMessage() {
    return message;
  }

  public boolean getValid() {
    return valid;
  }

  public void triggerError() {
    throw new Error(message);
  }
}
