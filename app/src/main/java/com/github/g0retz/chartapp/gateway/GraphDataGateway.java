package com.github.g0retz.chartapp.gateway;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Handler;
import android.os.Looper;
import com.github.g0retz.chartapp.model.Graph;
import com.github.g0retz.chartapp.model.GraphData;
import com.github.g0retz.chartapp.model.HistogramGraph;
import com.github.g0retz.chartapp.model.LineGraph;
import com.github.g0retz.chartapp.util.Consumer;
import com.github.g0retz.chartapp.util.Disposable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class GraphDataGateway {

  private final Context context;

  public GraphDataGateway(Context context) {
    this.context = context;
  }

  public Disposable getGraphData(
      Consumer<List<GraphData>> successResult,
      Consumer<Throwable> errorResult) {
    Thread thread = new Thread(() -> {
      Handler mainThread = new Handler(Looper.getMainLooper());
      try {
        List<GraphData> graphDatas = new ArrayList<>();
        String json = loadJSONFromAsset();
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
          JSONObject jsonObject = jsonArray.getJSONObject(i);
          JSONObject types = jsonObject.getJSONObject("types");
          JSONObject names = jsonObject.getJSONObject("names");
          JSONObject colors = jsonObject.getJSONObject("colors");
          JSONArray columns = jsonObject.getJSONArray("columns");
          ArrayList<Long> domain = new ArrayList<>();
          ArrayList<Graph> coDomain = new ArrayList<>();
          for (int j = 0; j < columns.length(); j++) {
            JSONArray column = columns.getJSONArray(j);
            String key = column.getString(0);
            if (types.has(key)) {
              List<Integer> list;
              if ("x".equals(types.getString(key))) {
                for (int k = 1; k < column.length(); k++) {
                  domain.add(column.getLong(k));
                }
              } else if ("line".equals(types.getString(key))){
                list = new LinkedList<>();
                for (int k = 1; k < column.length(); k++) {
                  list.add(column.getInt(k));
                }
                coDomain.add(new LineGraph(
                    names.getString(key),
                    Integer.valueOf(colors.getString(key).substring(1), 16),
                    list
                ));
              } else if ("histogram".equals(types.getString(key))){
                list = new LinkedList<>();
                for (int k = 1; k < column.length(); k++) {
                  list.add(column.getInt(k));
                }
                coDomain.add(new HistogramGraph(
                    names.getString(key),
                    colors.getInt(key),
                    list
                ));
              }
            }
          }
          graphDatas.add(new GraphData(domain, coDomain));
        }
        mainThread.post(() -> successResult.accept(graphDatas));
      } catch (InterruptedIOException ie) {
        ie.printStackTrace();
      } catch (Throwable e) {
        mainThread.post(() -> errorResult.accept(e));
      }
    });
    thread.run();
    return new Disposable() {
      @Override
      public void dispose() {
        thread.interrupt();
      }

      @Override
      public boolean isDisposed() {
        return thread.isAlive();
      }
    };
  }

  private String loadJSONFromAsset() throws IOException {
    InputStream inputStream = context.getAssets().open("chart_data.json");
    int size = inputStream.available();
    byte[] buffer = new byte[size];
    //noinspection ResultOfMethodCallIgnored
    inputStream.read(buffer);
    inputStream.close();
    if (VERSION.SDK_INT < VERSION_CODES.KITKAT) {
      //noinspection CharsetObjectCanBeUsed
      return new String(buffer, "UTF-8");
    } else {
      return new String(buffer, StandardCharsets.UTF_8);
    }
  }
}
