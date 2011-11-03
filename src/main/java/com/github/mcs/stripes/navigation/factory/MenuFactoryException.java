package com.github.mcs.stripes.navigation.factory;

/**
 * Wraps possible checked exceptions to provide a common base excepion.
 * @author Marcus
 */
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
