package com.hithub.mcs.stripes.navigation;

import com.github.mcs.stripes.navigation.MenuRoot;
import com.github.mcs.stripes.navigation.MenuItem;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MenuItemTest {

  private static final String ROOT_LABEL = "Root-Node";
  private static final String CHILD_LABEL = "Child-Node";
  private MenuItem root;
  private MenuItem child;

  @Before
  public void init() {
    root = new MenuRoot(ROOT_LABEL);
    child = new MenuItem("child", CHILD_LABEL, root);
  }

  @Test
  public void getLabel() {
    assertEquals(CHILD_LABEL, child.getLabel());
    assertEquals("Root-Node::child", child.getId());
  }

  @Test
  public void getParent() {
    MenuItem node = new MenuRoot("myLabel");
    assertNull(node.getParent());
  }

  @Test
  public void getExistingId() {
    MenuItem node = new MenuItem("myId", "myLabel", root);
    assertEquals(ROOT_LABEL + "::myId", node.getId());
  }

  @Test
  public void getMissingId() {
    MenuItem node = new MenuItem("myLabel", root);
    assertEquals(ROOT_LABEL + "::myLabel", node.getId());
  }

  @Test
  public void createChild() {
    assertEquals(0, child.getChildren().size());

    MenuItem child2 = new MenuItem("2nd_child", child);
    assertEquals(1, child.getChildren().size());
    assertEquals(child, child2.getParent());
  }
}
