package com.github.g0retz.chartapp.model;

import java.util.List;

public class Graph {

  public static final int TYPE_LINE = 0;
  public static final int TYPE_HISTOGRAM = 1;

  public final String name;
  public final int color;
  public final int type;
  public final List<Integer> values;

  public Graph(int type, List<Integer> values) {
    this(null, 0, type, values);
  }

  public Graph(String name, int type, List<Integer> values) {
    this(name, 0, type, values);
  }

  public Graph(int color, int type, List<Integer> values) {
    this(null, color, type, values);
  }

  public Graph(String name, int color, int type, List<Integer> values) {
    if (name == null) {
      name = "Unnamed Graph";
    }
    if (type != TYPE_LINE && type != TYPE_HISTOGRAM) {
      throw new IllegalArgumentException("Wrong type! Only TYPE_LINE and TYPE_HISTOGRAM allowed");
    }
    if (values == null || values.isEmpty()) {
      throw new IllegalArgumentException(String.format("No values provided: %s", values));
    }
    this.name = name;
    this.color = color;
    this.type = type;
    this.values = values;
  }

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
    if (type != graph.type) {
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
    result = 31 * result + type;
    result = 31 * result + (values != null ? values.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Graph{" +
        "name='" + name + '\'' +
        ", color=" + color +
        ", type=" + type +
        ", values=" + values +
        '}';
  }
}
