package com.github.g0retz.graphplotter.model;

public interface GraphVisitor {

  void visit(LineGraph lineGraph);

  void visit(HistogramGraph histogramGraph);
}
