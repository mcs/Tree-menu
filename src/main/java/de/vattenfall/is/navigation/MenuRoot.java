package de.vattenfall.is.navigation;

public class MenuRoot extends MenuItem {

  public MenuRoot(String id) {
    super(id, "");
  }

  @Override
  public String getId() {
    return id == null ? getLabel() : id;
  }
}
