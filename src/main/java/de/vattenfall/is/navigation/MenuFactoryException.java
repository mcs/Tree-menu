package de.vattenfall.is.navigation;

public class MenuFactoryException extends RuntimeException {

  public MenuFactoryException() {
  }

  public MenuFactoryException(String message) {
    super(message);
  }

  public MenuFactoryException(Throwable cause) {
    super(cause);
  }

  public MenuFactoryException(String message, Throwable cause) {
    super(message, cause);
  }
  
}
