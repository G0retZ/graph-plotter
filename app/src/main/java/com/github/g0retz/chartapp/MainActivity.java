package com.github.g0retz.chartapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setTitle(R.string.statistics);
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
    int nightModeFlags =
        getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
    switch (nightModeFlags) {
      case Configuration.UI_MODE_NIGHT_YES:
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        break;
      case Configuration.UI_MODE_NIGHT_NO:
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        break;
      case Configuration.UI_MODE_NIGHT_UNDEFINED:
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        break;
    }
    recreate();
  }
}
