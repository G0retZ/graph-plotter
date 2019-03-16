package com.github.g0retz.chartapp.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;

public class GraphTest {

  @Test
  public void testConstructorWithoutNameAndColor() {
    Graph graph = new Graph(Graph.TYPE_LINE, Arrays.asList(0, 3, 2));
    assertEquals("Unnamed Graph", graph.name);
    assertEquals(0, graph.color);
    assertEquals(0, graph.type);
    assertEquals(Arrays.asList(0, 3, 2), graph.values);
  }

  @Test
  public void testConstructorWithoutColor() {
    Graph graph = new Graph("A", Graph.TYPE_HISTOGRAM, Arrays.asList(10, 13, 12));
    assertEquals("A", graph.name);
    assertEquals(0, graph.color);
    assertEquals(1, graph.type);
    assertEquals(Arrays.asList(10, 13, 12), graph.values);
  }

  @Test
  public void testConstructorWithoutName() {
    Graph graph = new Graph(5, Graph.TYPE_LINE, Arrays.asList(20, 23, 22));
    assertEquals("Unnamed Graph", graph.name);
    assertEquals(5, graph.color);
    assertEquals(0, graph.type);
    assertEquals(Arrays.asList(20, 23, 22), graph.values);
  }

  @Test
  public void testConstructor() {
    Graph graph = new Graph("A", 1, Graph.TYPE_HISTOGRAM, Arrays.asList(30, 33, 32));
    assertEquals("A", graph.name);
    assertEquals(1, graph.color);
    assertEquals(1, graph.type);
    assertEquals(Arrays.asList(30, 33, 32), graph.values);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorFailOnNull() {
    Graph graph = new Graph("B", 2, Graph.TYPE_HISTOGRAM, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorFailOnEmpty() {
    Graph graph = new Graph("C", 3, Graph.TYPE_HISTOGRAM, new ArrayList<Integer>());
  }
}