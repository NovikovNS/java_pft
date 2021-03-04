package ru.stqa.pft.sandbox;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PointTests {

  @Test
  public void testPointEquals1() {
    Point p1 = new Point(8, -1);
    Point p2 = new Point(4, 2);
    Assert.assertEquals(p1.distance(p2), 5);
  }

  @Test
  public void testPointEquals2() {
    Point p1 = new Point(1, 1);
    Point p2 = new Point(1, 1);
    Assert.assertEquals(p1.distance(p2), 0);
  }

  @Test
  public void testPointNotEquals() {
    Point p1 = new Point(10, 10);
    Point p2 = new Point(10, 10);
    Assert.assertNotEquals(p1.distance(p2), 5);
  }
}
