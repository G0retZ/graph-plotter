package com.github.g0retz.chartapp;

import android.app.Application;
import com.github.g0retz.chartapp.gateway.GraphDataGateway;
import com.github.g0retz.chartapp.presentation.PlotPresenter;

public class MainApplication extends Application {

  private PlotPresenter plotPresenter;

  public PlotPresenter getPlotPresenter() {
    if (plotPresenter == null) {
      plotPresenter = new PlotPresenter(new GraphDataGateway(this));
    }
    return plotPresenter;
  }

  @Override
  public void onCreate() {
    super.onCreate();
  }
}
