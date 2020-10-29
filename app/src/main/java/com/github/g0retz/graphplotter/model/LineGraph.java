package com.github.g0retz.graphplotter.model;

import java.util.List;

public class LineGraph extends Graph {

  public LineGraph(List<Integer> values) {
    this(null, 0, values);
  }

  public LineGraph(String name, List<Integer> values) {
    this(name, 0, values);
  }

  public LineGraph(int color, List<Integer> values) {
    this(null, color, values);
  }

  public LineGraph(String name, int color, List<Integer> values) {
    super(name, color, values);
  }

  @Override
  public void visit(GraphVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  float getValue(float start, float position, float end, float min, float max, Float[] result) {
    result[0] = null;
    result[1] = null;
    result[2] = null;
    return getValueForwards(start, position, end, min, max, result);
  }

  private float getValueForwards(float start, float position, float end, float min, float max,
      Float[] result) {
    if (position < 0) {
      position = 0;
    } else if (position > end) {
      position = end;
    }
    result[0] = position;
    result[1] = getValue(position);
    int pos = result[0].intValue();
    if (pos < position) {
      pos++;
    }
    if (result[1] == Float.MIN_VALUE || !checkPointClipping(result[1], min, max)) {
      pos--;
      int last = values.size() - 1;
      while (pos <= end && pos < last) {
        int y1 = values.get(pos);
        int y2 = values.get(pos + 1);
        result[1] = (float) y2;
        result[2] = checkPointClipping(y1, min, max) ? 0f : 1f;
        if (y2 == min || y2 == max) {
          result[0] = (float) pos;
          return pos + 2;
        }
        if (checkRangesClipping(Math.min(y1, y2), Math.max(y1, y2), min, max)) {
          if (!checkPointClipping(y1, min, max) && !checkPointClipping(y2, min, max)) {
            if (y1 > y2) {
              result[0] = pos + (max - y1) / (y2 - y1);
              result[1] = max;
              return pos + (min - y1) / (y2 - y1);
            } else {
              result[0] = pos + (min - y1) / (y2 - y1);
              result[1] = min;
              return pos + (max - y1) / (y2 - y1);
            }
          } else if (y2 > max || y1 > max) {
            result[0] = pos + (max - y1) / (y2 - y1);
            result[1] = max;
            return pos + (y2 > max ? 2 : 1);
          } else {
            result[0] = pos + (min - y1) / (y2 - y1);
            result[1] = min;
            return pos + (y2 < min ? 2 : 1);
          }
        }
        pos++;
      }
    } else if (position == 0 || position <= start) {
      result[2] = 1f;
    } else {
      result[2] = 0f;
    }
    if (position != pos) {
      if (position == start || checkPointClipping(values.get(pos), min, max)) {
        return pos;
      } else {
        return pos + Math.ulp(pos);
      }
    } else {
      return pos + 1;
    }
  }

  private float getValue(float position) {
    if (!checkPointClipping(position, 0, values.size() - 1)) {
      return Float.MIN_VALUE;
    }
    int x11 = (int) position;
    int y1 = values.get(x11);
    if (position == x11) {
      return y1;
    }
    int y2 = values.get(x11 + 1);
    if (position == x11 + 1) {
      return y2;
    }
    return (y2 - y1) * (position - x11) + y1;
  }
}
