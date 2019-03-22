package com.github.g0retz.chartapp.model;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class Graph {

  public final String name;
  public final int color;
  public final List<Integer> values;
  public final int minValue;
  public final int maxValue;

  Graph(String name, int color, List<Integer> values) {
    if (name == null) {
      name = "Unnamed " + this.getClass().getSimpleName();
    }
    if (values == null || values.isEmpty()) {
      throw new IllegalArgumentException(String.format("No values provided: %s", values));
    }
    this.name = name;
    this.color = color;
    this.values = Collections.unmodifiableList(values);
    int min = Integer.MAX_VALUE;
    int max = Integer.MIN_VALUE;
    for (int value : values) {
      if (value < min) {
        min = value;
      }
      if (value > max) {
        max = value;
      }
    }
    minValue = min;
    maxValue = max;
  }

  abstract public void visit(GraphVisitor visitor);

  public Float getPoint(float position) {
    int pos = Math.round(position);
    if (pos >= 0 && pos < values.size()) {
      return (float) values.get(pos);
    }
    return null;
  }

  /**
   * Extract visible path of points represented by array of Float[3], where indexes are: 0 - X value
   * of next point (position), 1 - Y value of next point (from values), 2 - 1 if point is visible, 0
   * if not.
   *
   * WARNING! For memory saving the resulting array changes while iterating! So copy the values from
   * array you get ASAP if you need them.
   *
   * @param start - start position
   * @param max - max value
   * @param end - end position
   * @param min - min value
   * @return iterable of next points
   */
  public Iterable<Float[]> getPathForArea(final float start, final float end,
      final float min, final float max) {
    final int last = values.size() - 1;
    if (max <= min || start == end
        || !checkRangesClipping(start, end, 0, last)) {
      return Collections.emptySet();
    }
    return () -> new Iterator<Float[]>() {
      Float[] result = new Float[3];
      float currentPosition = start;

      @Override
      public boolean hasNext() {
        if (start < end && currentPosition > end) {
          return false;
        }
        if (start > end && currentPosition < end) {
          return false;
        }
        return checkRangesClipping(currentPosition, end, 0, last);
      }

      @Override
      public Float[] next() {
        // Check
        currentPosition = getValue(start, currentPosition, end, min, max, result);
        return result;
      }
    };
  }

  boolean checkPointClipping(float point, float start, float end) {
    return point >= start && point <= end;
  }

  boolean checkRangesClipping(float from, float to, float start, float end) {
    return Math.min(from, to) <= end && Math.max(from, to) >= start;
  }

  /**
   * Should return next position and fill the [result] with position, value and gap flag It is
   * guaranteed, that the incrementing position will meet the actual values positions.
   *
   * @param position current position
   * @param end last position
   * @param min value bound
   * @param max value bound
   * @param result position, value and flag
   * @return next position
   */
  abstract float getValue(float start, float position, float end, float min, float max, Float[] result);

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Graph graph = (Graph) o;

    if (color != graph.color) {
      return false;
    }
    if (name != null ? !name.equals(graph.name) : graph.name != null) {
      return false;
    }
    return values != null ? values.equals(graph.values) : graph.values == null;
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + color;
    result = 31 * result + (values != null ? values.hashCode() : 0);
    return result;
  }

  @SuppressWarnings("NullableProblems")
  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "{" +
        "name='" + name + '\'' +
        ", color=" + color +
        ", values=" + values +
        '}';
  }
}
