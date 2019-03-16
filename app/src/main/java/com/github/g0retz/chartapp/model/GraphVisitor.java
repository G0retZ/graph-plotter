package com.github.g0retz.chartapp.model;

public interface GraphVisitor {

  void visit(LineGraph lineGraph);

  void visit(HistogramGraph histogramGraph);
}
