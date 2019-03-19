package com.github.g0retz.chartapp;

import android.app.Activity;
import android.app.UiModeManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends Activity {

  private UiModeManager uiManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setTitle(R.string.statistics);
    uiManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
    if (VERSION.SDK_INT < VERSION_CODES.M) {
      uiManager.enableCarMode(0);
    }
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
