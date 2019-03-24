package com.github.g0retz.chartapp.presentation;

import com.github.g0retz.chartapp.model.Graph;
import com.github.g0retz.chartapp.model.GraphData;
import com.github.g0retz.chartapp.model.GraphVisitor;
import com.github.g0retz.chartapp.model.HistogramGraph;
import com.github.g0retz.chartapp.model.LineGraph;
import com.github.g0retz.chartapp.view.BasePlotAdapter;
import com.github.g0retz.chartapp.view.PlotView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GraphPlotAdapter extends BasePlotAdapter {

  private final GraphData graphData;
  private int[] types;
  private int maxValue = Integer.MIN_VALUE;
  private int minValue = 0;

  GraphPlotAdapter(GraphData graphData) {
    this.graphData = graphData;
    types = new int[graphData.coDomain.size()];
    List<Graph> coDomain = graphData.coDomain;
    for (int i = 0, coDomainSize = coDomain.size(); i < coDomainSize; i++) {
      Graph graph = coDomain.get(i);
      int finalI = i;
      graph.visit(new GraphVisitor() {
        @Override
        public void visit(LineGraph lineGraph) {
          types[finalI] = PlotView.TYPE_LINE_GRAPH;
        }

        @Override
        public void visit(HistogramGraph histogramGraph) {
          types[finalI] = PlotView.TYPE_HISTOGRAM;
        }
      });
      if (maxValue < graph.maxValue) {
        maxValue = graph.maxValue;
      }
      if (minValue < graph.minValue) {
        minValue = graph.minValue;
      }
    }
  }

  @Override
  public int getCount() {
    return graphData.coDomain.size();
  }

  @Override
  public boolean hasStableIds() {
    return false;
  }

  @Override
  public int getType(int position) {
    return types[position];
  }

  @Override
  public int getColor(int position) {
    return graphData.coDomain.get(position).color;
  }

  @Override
  public String getName(int position) {
    return graphData.coDomain.get(position).name;
  }

  @Override
  public Iterable<Float[]> getGraphPoints(int position) {
    Graph graph = graphData.coDomain.get(position);
    return graph.getPathForArea(0, graphData.domain.size(), graph.minValue, graph.maxValue);
  }

  @Override
  public String getXLabel(float value) {
    long start = graphData.domain.get(0);
    long end = graphData.domain.get(graphData.domain.size() - 1);
    long current = start + Math.round((end - start) * (double) value);
    return new SimpleDateFormat("MMM dd", Locale.getDefault()).format(new Date(current));
  }

  @Override
  public String getYLabel(float value) {
    return String.valueOf(Math.round(maxValue * value));
  }

  @Override
  public int getXValuesCount() {
    return graphData.domain.size();
  }

  @Override
  public int getYValuesCount() {
    return maxValue;
  }
}
