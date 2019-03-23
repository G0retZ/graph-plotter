package com.github.g0retz.chartapp.model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.junit.Test;

public class HistogramGraphTest {

  @Test
  public void testConstructorWithoutNameAndColor() {
    Graph graph = new HistogramGraph(Arrays.asList(0, 3, 2));
    assertEquals("Unnamed HistogramGraph", graph.name);
    assertEquals(0, graph.color);
    assertEquals(Arrays.asList(0, 3, 2), graph.values);
    assertEquals(graph.minValue, 0);
    assertEquals(graph.maxValue, 3);
  }

  @Test
  public void testConstructorWithoutColor() {
    Graph graph = new HistogramGraph("A", Arrays.asList(10, 13, 12));
    assertEquals("A", graph.name);
    assertEquals(0, graph.color);
    assertEquals(Arrays.asList(10, 13, 12), graph.values);
    assertEquals(graph.minValue, 10);
    assertEquals(graph.maxValue, 13);
  }

  @Test
  public void testConstructorWithoutName() {
    Graph graph = new HistogramGraph(5, Arrays.asList(20, 23, 22));
    assertEquals("Unnamed HistogramGraph", graph.name);
    assertEquals(5, graph.color);
    assertEquals(Arrays.asList(20, 23, 22), graph.values);
    assertEquals(graph.minValue, 20);
    assertEquals(graph.maxValue, 23);
  }

  @Test
  public void testConstructor() {
    Graph graph = new HistogramGraph("A", 1, Arrays.asList(30, 33, 32));
    assertEquals("A", graph.name);
    assertEquals(1, graph.color);
    assertEquals(Arrays.asList(30, 33, 32), graph.values);
    assertEquals(graph.minValue, 30);
    assertEquals(graph.maxValue, 33);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorFailOnNull() {
    new HistogramGraph("B", 2, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorFailOnEmpty() {
    new HistogramGraph("C", 3, new ArrayList<>());
  }

  @Test
  public void testHasValuesInTheAreaRange() {
    Graph graph = new HistogramGraph("A", 1, Arrays
        .asList(-630, 288, 809, 1551, 1809, 107, 1737, -1663, -1471, -990, 964, 1580, -1983, 1349,
            -834, 477, 5, -683, -273, 229));
    assertEquals(graph.minValue, -1983);
    assertEquals(graph.maxValue, 1809);
    // Area is lefter
    assertFalse(graph.getPathForArea(-10.1f, -0.01f, -3000, 3000).iterator().hasNext());
    assertFalse(graph.getPathForArea(-0.01f, -10.1f, -3000, 3000).iterator().hasNext());
    // Area is righter
    assertFalse(graph.getPathForArea(19.01f, 30.3f, -3000, 3000).iterator().hasNext());
    assertFalse(graph.getPathForArea(30.3f, 19.01f, -3000, 3000).iterator().hasNext());
    // Area is exact
    assertTrue(graph.getPathForArea(0f, 19f, -3000, 3000).iterator().hasNext());
    assertFalse(graph.getPathForArea(19f, 0f, -3000, 3000).iterator().hasNext());
    // Area starts inside and ends exact
    assertTrue(graph.getPathForArea(1.3f, 19f, -3000, 3000).iterator().hasNext());
    assertFalse(graph.getPathForArea(17.7f, 0f, -3000, 3000).iterator().hasNext());
    // Area starts exact and ends inside
    assertTrue(graph.getPathForArea(0f, 17.7f, -3000, 3000).iterator().hasNext());
    assertFalse(graph.getPathForArea(19f, 1.3f, -3000, 3000).iterator().hasNext());
    // Area starts outside and ends exact
    assertTrue(graph.getPathForArea(-1.3f, 19f, -3000, 3000).iterator().hasNext());
    assertFalse(graph.getPathForArea(20.3f, 0f, -3000, 3000).iterator().hasNext());
    // Area starts exact and ends outside
    assertTrue(graph.getPathForArea(0f, 20.3f, -3000, 3000).iterator().hasNext());
    assertFalse(graph.getPathForArea(19f, -1.3f, -3000, 3000).iterator().hasNext());
    // Area starts outside and ends inside
    assertTrue(graph.getPathForArea(-1.3f, 18f, -3000, 3000).iterator().hasNext());
    assertFalse(graph.getPathForArea(20.3f, 1f, -3000, 3000).iterator().hasNext());
    // Area starts inside and ends outside
    assertTrue(graph.getPathForArea(1f, 20.3f, -3000, 3000).iterator().hasNext());
    assertFalse(graph.getPathForArea(18f, -1.3f, -3000, 3000).iterator().hasNext());
    // Area starts and ends inside
    assertTrue(graph.getPathForArea(1.3f, 17.7f, -3000, 3000).iterator().hasNext());
    assertFalse(graph.getPathForArea(17.7f, 1.3f, -3000, 3000).iterator().hasNext());
    // Area starts and ends outside
    assertTrue(graph.getPathForArea(-1.3f, 20.3f, -3000, 3000).iterator().hasNext());
    assertFalse(graph.getPathForArea(20.3f, -1.3f, -3000, 3000).iterator().hasNext());
  }

  @Test
  public void testValuesInTheExactAreaRange() {
    Graph graph = new HistogramGraph("A", 1, Arrays
        .asList(-630, 288, 809, 1551, 1809, 107, 1737, -1663, -1471, -990, 964, 1580, -1983, 1349,
            -834, 477, 5, -683, -273, 229));
    Iterator<Float[]> forwards = graph.getPathForArea(0f, 19f, -1500, 1500).iterator();
    assertArrayEquals(new Float[] {0f, -630f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {1f, 288f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {2f, 809f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {3f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {4f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {5f, 107f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {6f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {7f, -1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {8f, -1471f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {9f, -990f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {10f, 964f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {11f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {12f, -1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {13f, 1349f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {14f, -834f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {15f, 477f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {16f, 5f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {17f, -683f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {18f, -273f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {19f, 229f, 0f}, forwards.next());
    assertFalse(forwards.hasNext());
  }

  @Test
  public void testValuesInTheInsideToExactAreaRange() {
    Graph graph = new HistogramGraph("A", 1, Arrays
        .asList(-630, 288, 809, 1551, 1809, 107, 1737, -1663, -1471, -990, 964, 1580, -1983, 1349,
            -834, 477, 5, -683, -273, 229));
    Iterator<Float[]> forwards = graph.getPathForArea(1.3f, 19f, -1500, 1500).iterator();
    assertArrayEquals(new Float[] {1f, 288f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {2f, 809f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {3f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {4f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {5f, 107f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {6f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {7f, -1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {8f, -1471f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {9f, -990f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {10f, 964f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {11f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {12f, -1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {13f, 1349f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {14f, -834f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {15f, 477f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {16f, 5f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {17f, -683f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {18f, -273f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {19f, 229f, 0f}, forwards.next());
    assertFalse(forwards.hasNext());
  }

  @Test
  public void testValuesInTheExactToInsideAreaRange() {
    Graph graph = new HistogramGraph("A", 1, Arrays
        .asList(-630, 288, 809, 1551, 1809, 107, 1737, -1663, -1471, -990, 964, 1580, -1983, 1349,
            -834, 477, 5, -683, -273, 229));
    Iterator<Float[]> forwards = graph.getPathForArea(0f, 17.3f, -1500, 1500).iterator();
    assertArrayEquals(new Float[] {0f, -630f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {1f, 288f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {2f, 809f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {3f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {4f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {5f, 107f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {6f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {7f, -1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {8f, -1471f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {9f, -990f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {10f, 964f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {11f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {12f, -1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {13f, 1349f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {14f, -834f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {15f, 477f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {16f, 5f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {17f, -683f, 0f}, forwards.next());
    assertFalse(forwards.hasNext());
  }

  @Test
  public void testValuesInOutsideToExactAreaRange() {
    Graph graph = new HistogramGraph("A", 1, Arrays
        .asList(-630, 288, 809, 1551, 1809, 107, 1737, -1663, -1471, -990, 964, 1580, -1983, 1349,
            -834, 477, 5, -683, -273, 229));
    Iterator<Float[]> forwards = graph.getPathForArea(-1.3f, 19f, -1500, 1500).iterator();
    assertArrayEquals(new Float[] {0f, -630f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {1f, 288f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {2f, 809f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {3f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {4f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {5f, 107f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {6f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {7f, -1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {8f, -1471f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {9f, -990f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {10f, 964f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {11f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {12f, -1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {13f, 1349f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {14f, -834f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {15f, 477f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {16f, 5f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {17f, -683f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {18f, -273f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {19f, 229f, 0f}, forwards.next());
    assertFalse(forwards.hasNext());
  }

  @Test
  public void testValuesInTheExactToOutsideAreaRange() {
    Graph graph = new HistogramGraph("A", 1, Arrays
        .asList(-630, 288, 809, 1551, 1809, 107, 1737, -1663, -1471, -990, 964, 1580, -1983, 1349,
            -834, 477, 5, -683, -273, 229));
    Iterator<Float[]> forwards = graph.getPathForArea(0f, 20.3f, -1500, 1500).iterator();
    assertArrayEquals(new Float[] {0f, -630f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {1f, 288f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {2f, 809f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {3f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {4f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {5f, 107f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {6f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {7f, -1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {8f, -1471f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {9f, -990f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {10f, 964f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {11f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {12f, -1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {13f, 1349f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {14f, -834f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {15f, 477f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {16f, 5f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {17f, -683f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {18f, -273f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {19f, 229f, 0f}, forwards.next());
    assertFalse(forwards.hasNext());
  }

  @Test
  public void testValuesInTheInsideToOutsideAreaRange() {
    Graph graph = new HistogramGraph("A", 1, Arrays
        .asList(-630, 288, 809, 1551, 1809, 107, 1737, -1663, -1471, -990, 964, 1580, -1983, 1349,
            -834, 477, 5, -683, -273, 229));
    Iterator<Float[]> forwards = graph.getPathForArea(1.3f, 20.3f, -1500, 1500).iterator();
    assertArrayEquals(new Float[] {1f, 288f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {2f, 809f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {3f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {4f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {5f, 107f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {6f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {7f, -1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {8f, -1471f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {9f, -990f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {10f, 964f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {11f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {12f, -1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {13f, 1349f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {14f, -834f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {15f, 477f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {16f, 5f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {17f, -683f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {18f, -273f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {19f, 229f, 0f}, forwards.next());
    assertFalse(forwards.hasNext());
  }

  @Test
  public void testValuesInTheOutsideToInsideAreaRange() {
    Graph graph = new HistogramGraph("A", 1, Arrays
        .asList(-630, 288, 809, 1551, 1809, 107, 1737, -1663, -1471, -990, 964, 1580, -1983, 1349,
            -834, 477, 5, -683, -273, 229));
    Iterator<Float[]> forwards = graph.getPathForArea(-1.3f, 17.3f, -1500, 1500).iterator();
    assertArrayEquals(new Float[] {0f, -630f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {1f, 288f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {2f, 809f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {3f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {4f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {5f, 107f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {6f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {7f, -1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {8f, -1471f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {9f, -990f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {10f, 964f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {11f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {12f, -1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {13f, 1349f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {14f, -834f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {15f, 477f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {16f, 5f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {17f, -683f, 0f}, forwards.next());
    assertFalse(forwards.hasNext());
  }

  @Test
  public void testValuesInTheOutsideToOutsideAreaRange() {
    Graph graph = new HistogramGraph("A", 1, Arrays
        .asList(-630, 288, 809, 1551, 1809, 107, 1737, -1663, -1471, -990, 964, 1580, -1983, 1349,
            -834, 477, 5, -683, -273, 229));
    Iterator<Float[]> forwards = graph.getPathForArea(-1.3f, 20.3f, -1500, 1500).iterator();
    assertArrayEquals(new Float[] {0f, -630f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {1f, 288f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {2f, 809f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {3f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {4f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {5f, 107f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {6f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {7f, -1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {8f, -1471f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {9f, -990f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {10f, 964f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {11f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {12f, -1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {13f, 1349f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {14f, -834f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {15f, 477f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {16f, 5f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {17f, -683f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {18f, -273f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {19f, 229f, 0f}, forwards.next());
    assertFalse(forwards.hasNext());
  }

  @Test
  public void testValuesInTheInsideToInsideAreaRange() {
    Graph graph = new HistogramGraph("A", 1, Arrays
        .asList(-630, 288, 809, 1551, 1809, 107, 1737, -1663, -1471, -990, 964, 1580, -1983, 1349,
            -834, 477, 5, -683, -273, 229));
    Iterator<Float[]> forwards = graph.getPathForArea(1.3f, 17.3f, -1500, 1500).iterator();
    assertArrayEquals(new Float[] {1f, 288f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {2f, 809f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {3f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {4f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {5f, 107f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {6f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {7f, -1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {8f, -1471f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {9f, -990f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {10f, 964f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {11f, 1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {12f, -1500f, 1f}, forwards.next());
    assertArrayEquals(new Float[] {13f, 1349f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {14f, -834f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {15f, 477f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {16f, 5f, 0f}, forwards.next());
    assertArrayEquals(new Float[] {17f, -683f, 0f}, forwards.next());
    assertFalse(forwards.hasNext());
  }
}