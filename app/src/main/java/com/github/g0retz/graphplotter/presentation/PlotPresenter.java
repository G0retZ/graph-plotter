package com.github.g0retz.graphplotter.presentation;

import com.github.g0retz.graphplotter.gateway.GraphDataGateway;
import com.github.g0retz.graphplotter.model.GraphData;
import com.github.g0retz.graphplotter.util.Consumer;
import com.github.g0retz.graphplotter.util.Disposable;
import com.github.g0retz.graphplotter.view.PlotAdapter;
import java.util.LinkedList;
import java.util.List;

public class PlotPresenter {

  private final GraphDataGateway graphDataGateway;
  private List<PlotAdapter> plotAdapters;

  public PlotPresenter(GraphDataGateway graphDataGateway) {
    this.graphDataGateway = graphDataGateway;
  }

  public Disposable getAdapters(
      Consumer<List<PlotAdapter>> successResult,
      Consumer<Throwable> errorResult) {
    if (plotAdapters != null) {
      successResult.accept(plotAdapters);
      return new Disposable() {
        @Override
        public void dispose() {
        }

        @Override
        public boolean isDisposed() {
          return true;
        }
      };
    }
    return graphDataGateway.getGraphData(result -> {
      plotAdapters = new LinkedList<>();
      for (GraphData graphData : result) {
        plotAdapters.add(new GraphPlotAdapter(graphData));
      }
      successResult.accept(plotAdapters);
    }, errorResult);
  }

}
