package com.github.g0retz.chartapp;

import android.app.Activity;
import android.app.UiModeManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.github.g0retz.chartapp.presentation.PlotPresenter;
import com.github.g0retz.chartapp.util.Disposable;
import com.github.g0retz.chartapp.view.PlotView;

public class MainActivity extends Activity {

  private UiModeManager uiManager;
  Disposable disposable;
  private PlotView plotView;
  private PlotView plotControlView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setTitle(R.string.statistics);
    uiManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
    if (VERSION.SDK_INT < VERSION_CODES.M) {
      uiManager.enableCarMode(0);
    }
    plotView = findViewById(R.id.plotView);
    plotControlView = findViewById(R.id.plotControlView);
    PlotPresenter plotPresenter = ((MainApplication) getApplicationContext()).getPlotPresenter();
    disposable = plotPresenter.getAdapters(data -> {
      plotView.setAdapter(data.get(4));
      plotControlView.setAdapter(data.get(4));
    }, throwable -> {
      throwable.printStackTrace();
      Toast.makeText(this,
          String.format("%s: %s", throwable.getClass().getSimpleName(), throwable.getMessage()),
          Toast.LENGTH_LONG).show();
    });
  }

  @Override
  protected void onDestroy() {
    if (disposable != null) {
      disposable.dispose();
    }
    super.onDestroy();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    switch (item.getItemId()) {
      case R.id.night_mode:
        switchNightMode();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void switchNightMode() {
    int nightModeFlags = uiManager.getNightMode();
    switch (nightModeFlags) {
      case UiModeManager.MODE_NIGHT_YES:
        uiManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
        break;
      case UiModeManager.MODE_NIGHT_NO:
        uiManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
        break;
      case UiModeManager.MODE_NIGHT_AUTO:
        uiManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
        break;
    }
  }
}
