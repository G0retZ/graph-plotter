package com.github.g0retz.chartapp.model;

import java.util.Collections;
import java.util.List;

public abstract class Graph {

  public final String name;
  public final int color;
  public final List<Integer> values;

  public Graph(List<Integer> values) {
    this(null, 0, values);
  }

  public Graph(String name, List<Integer> values) {
    this(name, 0, values);
  }

  public Graph(int color, List<Integer> values) {
    this(null, color, values);
  }

  public Graph(String name, int color, List<Integer> values) {
    if (name == null) {
      name = "Unnamed Graph";
    }
    if (values == null || values.isEmpty()) {
      throw new IllegalArgumentException(String.format("No values provided: %s", values));
    }
    this.name = name;
    this.color = color;
    this.values = Collections.unmodifiableList(values);
  }

  abstract public void visit(GraphVisitor visitor);

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Graph graph = (Graph) o;

    if (color != graph.color) {
      return false;
    }
    if (name != null ? !name.equals(graph.name) : graph.name != null) {
      return false;
    }
    return values != null ? values.equals(graph.values) : graph.values == null;
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + color;
    result = 31 * result + (values != null ? values.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Graph{" +
        "name='" + name + '\'' +
        ", color=" + color +
        ", values=" + values +
        '}';
  }
}
