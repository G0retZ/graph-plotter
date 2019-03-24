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
  private final int domainSize;
  private final int coDomainSize;
  private int[] types;
  private int maxValue = Integer.MIN_VALUE;
  private int minValue = 0;
  private boolean[] enabled;

  GraphPlotAdapter(GraphData graphData) {
    this.graphData = graphData;
    domainSize = graphData.domain.size();
    coDomainSize = graphData.coDomain.size();
    types = new int[coDomainSize];
    enabled = new boolean[types.length];
    List<Graph> coDomain = graphData.coDomain;
    for (int i = 0; i < coDomainSize; i++) {
      Graph graph = coDomain.get(i);
      int finalI = i;
      enabled[i] = true;
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
    return graph.getPathForArea(0, domainSize, graph.minValue, graph.maxValue);
  }

  @Override
  public boolean isEnabled(int position) {
    return enabled[position];
  }

  @Override
  public void setEnabled(int position, boolean enabled) {
    if (this.enabled[position] != enabled) {
      this.enabled[position] = enabled;
      notifyDataSetChanged();
    }
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
    return domainSize;
  }

  @Override
  public int getYValuesCount() {
    return maxValue;
  }
}
