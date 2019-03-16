package com.github.g0retz.chartapp.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;

public class HistogramGraphTest {

  @Test
  public void testConstructorWithoutNameAndColor() {
    Graph graph = new HistogramGraph(Arrays.asList(0, 3, 2));
    assertEquals("Unnamed Graph", graph.name);
    assertEquals(0, graph.color);
    assertEquals(Arrays.asList(0, 3, 2), graph.values);
  }

  @Test
  public void testConstructorWithoutColor() {
    Graph graph = new HistogramGraph("A", Arrays.asList(10, 13, 12));
    assertEquals("A", graph.name);
    assertEquals(0, graph.color);
    assertEquals(Arrays.asList(10, 13, 12), graph.values);
  }

  @Test
  public void testConstructorWithoutName() {
    Graph graph = new HistogramGraph(5, Arrays.asList(20, 23, 22));
    assertEquals("Unnamed Graph", graph.name);
    assertEquals(5, graph.color);
    assertEquals(Arrays.asList(20, 23, 22), graph.values);
  }

  @Test
  public void testConstructor() {
    Graph graph = new HistogramGraph("A", 1, Arrays.asList(30, 33, 32));
    assertEquals("A", graph.name);
    assertEquals(1, graph.color);
    assertEquals(Arrays.asList(30, 33, 32), graph.values);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorFailOnNull() {
    Graph graph = new HistogramGraph("B", 2, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorFailOnEmpty() {
    Graph graph = new HistogramGraph("C", 3, new ArrayList<Integer>());
  }
}