package com.github.g0retz.graphplotter.view;

import android.database.DataSetObserver;

public interface PlotAdapter {
  /**
   * Register an observer that is called when changes happen to the data used by this adapter.
   *
   * @param observer the object that gets notified when the data set changes.
   */
  void registerDataSetObserver(DataSetObserver observer);

  /**
   * Unregister an observer that has previously been registered with this
   * adapter via {@link #registerDataSetObserver}.
   *
   * @param observer the object to unregister.
   */
  void unregisterDataSetObserver(DataSetObserver observer);

  /**
   * How many items are in the data set represented by this Adapter.
   *
   * @return Count of items.
   */
  int getCount();

  /**
   * Indicates whether the item ids are stable across changes to the
   * underlying data.
   *
   * @return True if the same id always refers to the same object.
   */
  boolean hasStableIds();

  /**
   * Get the type of graph that will be plotted for the specified item.
   *
   * @param position The position of the item within the adapter's data set whose view type we
   *        want.
   * @return An integer representing the type of graph.
   */
  int getType(int position);

  /**
   * Get the color of graph that will be plotted for the specified item.
   *
   * @param position The position of the item within the adapter's data set whose color we want.
   * @return An integer representing the color of graph.
   */
  int getColor(int position);

  /**
   * Get the name of graph that will be plotted for the specified item.
   *
   * @param position The position of the item within the adapter's data set whose name we want.
   * @return A string representing the name of graph.
   */
  String getName(int position);

  /**
   * Get points of graph that will be plotted for the specified item.
   *
   * @param position The position of the item within the adapter's data set whose points we want.
   * @param start The start of the graph from 0.0 to 1.0
   * @param end The end of the graph from 0.0 to 1.0
   * @return An Iterable representing the points of graph.
   */
  Iterable<Float[]> getGraphPoints(int position, float start, float end);

  /**
   * Returns true if the item at the specified position is enabled.
   *
   * The result is unspecified if position is invalid. An {@link ArrayIndexOutOfBoundsException}
   * should be thrown in that case for fast failure.
   *
   * @param position Index of the item
   *
   * @return True if the item is not a separator
   */
  boolean isEnabled(int position);

  /**
   * Enable or Disable the item at the specified position.
   *
   * The result is unspecified if position is invalid. An {@link ArrayIndexOutOfBoundsException}
   * should be thrown in that case for fast failure.
   *
   * @param position Index of the item
   * @param enabled new state of the item
   */
  void setEnabled(int position, boolean enabled);

  String getXLabel(float value);

  String getYLabel(float value);

  int getXValuesCount();

  int getYValuesCount();
}
