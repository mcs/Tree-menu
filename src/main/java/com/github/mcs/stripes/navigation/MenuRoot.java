package com.github.mcs.stripes.navigation;

public class MenuRoot extends MenuItem {

  public MenuRoot(String id) {
    super(id);
  }

  @Override
  public String getId() {
    return id;
  }
}
