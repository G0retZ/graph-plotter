package com.github.g0retz.chartapp.model;

import java.util.List;

public class HistogramGraph extends Graph {

  public HistogramGraph(List<Integer> values) {
    this(null, 0, values);
  }

  public HistogramGraph(String name, List<Integer> values) {
    this(name, 0, values);
  }

  public HistogramGraph(int color, List<Integer> values) {
    this(null, color, values);
  }

  public HistogramGraph(String name, int color, List<Integer> values) {
    super(name, color, values);
  }

  @Override
  public void visit(GraphVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  float getValue(float start, float position, float end, float min, float max, Float[] result) {
    int pos = (int) position;
    if (pos <= position - 0.5f) {
        pos++;
    }
    while (pos < 0 || pos > values.size() - 1) {
      pos ++;
    }
    result[0] = (float) pos;
    position = Float.valueOf(values.get(pos));
    if (position > max) {
      result[1] = max;
      result[2] = 1f;
    } else if (position < min) {
      result[1] = min;
      result[2] = 1f;
    } else {
      result[1] = position;
      result[2] = 0f;
    }
    return pos + 1;
  }
}
