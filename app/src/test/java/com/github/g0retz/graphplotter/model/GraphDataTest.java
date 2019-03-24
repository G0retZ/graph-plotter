package com.github.g0retz.graphplotter.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class GraphDataTest {

  @Rule
  public MockitoRule rule = MockitoJUnit.rule();
  @Mock
  private Graph graph1;
  @Mock
  private Graph graph2;
  @Mock
  private Graph graph3;

  @Test
  public void testConstructor() {
    GraphData graphData = new GraphData(
        Arrays.asList(0L, 1L, 2L),
        Arrays.asList(graph1, graph2, graph3)
    );
    assertEquals(Arrays.asList(0L, 1L, 2L), graphData.domain);
    assertEquals(Arrays.asList(graph1, graph2, graph3), graphData.coDomain);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithInconsistentDomain() {
    new GraphData(Arrays.asList(0L, 2L, 1L), Arrays.asList(graph1, graph2, graph3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithNullDomain() {
    new GraphData(null, Arrays.asList(graph1, graph2, graph3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithEmptyDomain() {
    new GraphData(new ArrayList<>(), Arrays.asList(graph1, graph2, graph3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithNullCodomain() {
    new GraphData(Arrays.asList(0L, 1L, 2L), null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithEmptyCodomain() {
    new GraphData(Arrays.asList(0L, 1L, 2L), new ArrayList<>());
  }
}