package com.github.g0retz.chartapp.model;

import java.util.List;

public class LineGraph extends Graph {

  public LineGraph(List<Integer> values) {
    super(values);
  }

  public LineGraph(String name, List<Integer> values) {
    super(name, values);
  }

  public LineGraph(int color, List<Integer> values) {
    super(color, values);
  }

  public LineGraph(String name, int color, List<Integer> values) {
    super(name, color, values);
  }

  @Override
  public void visit(GraphVisitor visitor) {
    visitor.visit(this);
  }
}
