package com.hithub.mcs.stripes.navigation;

import com.github.mcs.stripes.navigation.MenuRoot;
import com.github.mcs.stripes.navigation.MenuItem;
import static org.junit.Assert.*;

import org.junit.Test;

public class MenuRootTest {

  @Test
  public void getExistingId() {
    MenuItem node = new MenuRoot("myId");
    assertEquals("myId", node.getId());
  }

  @Test
  public void getMissingId() {
    MenuItem node = new MenuRoot("myLabel");
    assertEquals("myLabel", node.getId());
  }
}
