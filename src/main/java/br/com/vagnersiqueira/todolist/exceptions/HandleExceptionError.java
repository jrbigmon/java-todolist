package br.com.vagnersiqueira.todolist.exceptions;

public class HandleExceptionError {
  private boolean valid;
  private String message;

  public HandleExceptionError(boolean valid, String message) {
    this.valid = valid;
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public boolean getValid() {
    return valid;
  }

  public void triggerError() throws Exception {
    if (!this.valid) {
      throw new Exception(message);
    }
  }
}
