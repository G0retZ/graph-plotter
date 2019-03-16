package com.github.g0retz.chartapp.model;

import java.util.List;

public class HistogramGraph extends Graph {

  public HistogramGraph(List<Integer> values) {
    super(values);
  }

  public HistogramGraph(String name, List<Integer> values) {
    super(name, values);
  }

  public HistogramGraph(int color, List<Integer> values) {
    super(color, values);
  }

  public HistogramGraph(String name, int color, List<Integer> values) {
    super(name, color, values);
  }

  @Override
  public void visit(GraphVisitor visitor) {
    visitor.visit(this);
  }
}
