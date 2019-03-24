package com.github.g0retz.chartapp.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import com.github.g0retz.chartapp.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PlotView extends View {

  public static final int TYPE_LINE_GRAPH = 0;
  public static final int TYPE_HISTOGRAM = 1;
  private final int minWidth;
  private final int minHeight;
  float textSize;
  private PlotAdapter adapter;
  private AdapterDataSetObserver dataSetObserver;
  private boolean legend = true;
  private float strokeWidth;
  private boolean horizontalAxis = true;
  private boolean verticalAxis = false;
  private boolean horizontalGuidelines = true;
  private boolean verticalGuidelines = false;
  private boolean horizontalTickMarks = false;
  private boolean verticalTickMarks = false;
  private int horizontalUnits = 5;
  private int verticalUnits = 5;
  private int axisColor = 0;
  private int labelColor = 0;

  private Paint axisPaint;
  private Paint labelPaint;
  private Paint graphPaint;
  private RectF cBounds = new RectF();
  private Rect bounds = new Rect();
  private RectF boundsF = new RectF();
  private float textHeight;
  private boolean isRtl = false;

  private boolean regenerate;
  private float start = 0;
  private float end = 1;
  private float scaleX = 1;
  private float scaleY = 1;
  private float graphScaleX = 1;
  private float graphScaleY = 1;

  private List<Path> cachedPaths;

  public PlotView(Context context) {
    super(context);
    minWidth = Math.round(48 * context.getResources().getDisplayMetrics().density);
    minHeight = Math.round(48 * context.getResources().getDisplayMetrics().density);
    strokeWidth = 2 * context.getResources().getDisplayMetrics().density;
    cachedPaths = new ArrayList<>();
  }

  public PlotView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public PlotView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    TypedArray typedArray = context.getTheme().obtainStyledAttributes(
        attrs,
        R.styleable.PlotView,
        defStyleAttr,
        0
    );
    minWidth = Math.round(48 * context.getResources().getDisplayMetrics().density);
    minHeight = Math.round(48 * context.getResources().getDisplayMetrics().density);
    init(context, typedArray);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public PlotView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    TypedArray typedArray = context.getTheme().obtainStyledAttributes(
        attrs,
        R.styleable.PlotView,
        defStyleAttr,
        defStyleRes
    );
    minWidth = Math.round(48 * context.getResources().getDisplayMetrics().density);
    minHeight = Math.round(48 * context.getResources().getDisplayMetrics().density);
    init(context, typedArray);
  }

  private void init(Context context, TypedArray typedArray) {

    try {
      textSize = typedArray.getDimension(R.styleable.PlotView_textSize,
          14 * context.getResources().getDisplayMetrics().scaledDensity);
      legend = typedArray.getBoolean(R.styleable.PlotView_legend, true);
      strokeWidth = typedArray.getDimension(R.styleable.PlotView_strokeWidth,
          2f * context.getResources().getDisplayMetrics().density);
      horizontalAxis = typedArray.getBoolean(R.styleable.PlotView_horizontalAxis, true);
      verticalAxis = typedArray.getBoolean(R.styleable.PlotView_verticalAxis, false);
      horizontalGuidelines = typedArray
          .getBoolean(R.styleable.PlotView_horizontalGuidelines, true);
      verticalGuidelines = typedArray
          .getBoolean(R.styleable.PlotView_verticalGuidelines, false);
      horizontalTickMarks = typedArray
          .getBoolean(R.styleable.PlotView_horizontalTickMarks, false);
      verticalTickMarks = typedArray
          .getBoolean(R.styleable.PlotView_verticalTickMarks, false);
      horizontalUnits = typedArray.getInt(R.styleable.PlotView_horizontalUnits, 5);
      verticalUnits = typedArray.getInt(R.styleable.PlotView_verticalUnits, 5);
      axisColor = typedArray.getColor(R.styleable.PlotView_axisColor, 0);
      labelColor = typedArray.getColor(R.styleable.PlotView_labelsColor, 0);
    } finally {
      typedArray.recycle();
    }

    axisPaint = new Paint();
    axisPaint.setDither(false);
    axisPaint.setStyle(Style.STROKE);
    axisPaint.setStrokeJoin(Join.MITER);
    axisPaint.setStrokeCap(Cap.BUTT);
    axisPaint.setAntiAlias(true);
    axisPaint.setStrokeWidth(Math.max(1f, context.getResources().getDisplayMetrics().density));
    axisPaint.setColor(axisColor);

    labelPaint = new Paint();
    labelPaint.setDither(false);
    labelPaint.setAntiAlias(true);
    labelPaint.setTextSize(textSize);
    labelPaint.setColor(labelColor);

    graphPaint = new Paint();
    graphPaint.setDither(false);
    graphPaint.setStyle(Style.STROKE);
    graphPaint.setStrokeJoin(Paint.Join.ROUND);
    graphPaint.setStrokeCap(Paint.Cap.ROUND);
    graphPaint.setAntiAlias(true);
    graphPaint.setStrokeWidth(strokeWidth);

    cachedPaths = new ArrayList<>();
  }

  public void setHorizontalAxis(boolean horizontalAxis) {
    if (horizontalAxis != this.horizontalAxis) {
      this.horizontalAxis = horizontalAxis;
      invalidate();
      requestLayout();
    }
  }

  public void setVerticalAxis(boolean verticalAxis) {
    if (verticalAxis != this.verticalAxis) {
      this.verticalAxis = verticalAxis;
      invalidate();
      requestLayout();
    }
  }

  public void setHorizontalGuidelines(boolean horizontalGuidelines) {
    if (horizontalGuidelines != this.horizontalGuidelines) {
      this.horizontalGuidelines = horizontalGuidelines;
      invalidate();
      requestLayout();
    }
  }

  public void setVerticalGuidelines(boolean verticalGuidelines) {
    if (verticalGuidelines != this.verticalGuidelines) {
      this.verticalGuidelines = verticalGuidelines;
      invalidate();
      requestLayout();
    }
  }

  public void setHorizontalTickMarks(boolean horizontalTickMarks) {
    if (horizontalTickMarks != this.horizontalTickMarks) {
      this.horizontalTickMarks = horizontalTickMarks;
      invalidate();
      requestLayout();
    }
  }

  public void setVerticalTickMarks(boolean verticalTickMarks) {
    if (verticalTickMarks != this.verticalTickMarks) {
      this.verticalTickMarks = verticalTickMarks;
      invalidate();
      requestLayout();
    }
  }

  public void setHorizontalUnits(int horizontalUnits) {
    if (horizontalUnits != this.horizontalUnits) {
      this.horizontalUnits = horizontalUnits;
      invalidate();
      requestLayout();
    }
  }

  public void setVerticalUnits(int verticalUnits) {
    if (verticalUnits != this.verticalUnits) {
      this.verticalUnits = verticalUnits;
      invalidate();
      requestLayout();
    }
  }

  public void setAxisColor(int axisColor) {
    if (axisColor != this.axisColor) {
      this.axisColor = axisColor;
      invalidate();
      requestLayout();
    }
  }

  public void setLabelColor(int labelColor) {
    if (labelColor != this.labelColor) {
      this.labelColor = labelColor;
      invalidate();
      requestLayout();
    }
  }

  public PlotAdapter getAdapter() {
    return adapter;
  }

  public void setAdapter(PlotAdapter adapter) {
    if (this.adapter != null && dataSetObserver != null) {
      this.adapter.unregisterDataSetObserver(dataSetObserver);
    }
    this.adapter = adapter;
    if (this.adapter == null) {
      cachedPaths.clear();
    } else {
      adjustPaths();
      dataSetObserver = new AdapterDataSetObserver();
      adapter.registerDataSetObserver(dataSetObserver);
      regenerate = true;
    }
    invalidate();
    requestLayout();
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldW, int oldH) {
    super.onSizeChanged(w, h, oldW, oldH);
    if (legend) {
      FontMetrics fontMetrics = labelPaint.getFontMetrics();
      textHeight = fontMetrics.descent - fontMetrics.ascent;
      cBounds.set(getPaddingLeft(), getPaddingTop(),
          w - getPaddingRight(), h - getPaddingBottom() - textHeight);
    } else {
      cBounds.set(getPaddingLeft(), getPaddingTop(), w - getPaddingRight(), h - getPaddingBottom());
    }
    if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
      isRtl = getLayoutDirection() == LAYOUT_DIRECTION_RTL;
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    drawAxises(canvas);
    if (adapter != null) {
      for (int i = 0; i < adapter.getCount(); i++) {
        if (adapter.isEnabled(i)) {
          switch (adapter.getType(i)) {
            case TYPE_LINE_GRAPH:
              drawGraph(adapter.getGraphPoints(i, start, end), cachedPaths.get(i), canvas, adapter.getColor(i));
              break;
            case TYPE_HISTOGRAM:
              drawHistogram(adapter.getGraphPoints(i, start, end), cachedPaths.get(i), canvas, adapter.getColor(i));
              break;
          }
        }
      }
      regenerate = false;
    }
  }

  private void drawAxises(Canvas canvas) {
    if (horizontalAxis) {
      canvas.drawLine(cBounds.left, cBounds.bottom, cBounds.right, cBounds.bottom, axisPaint);
    }
    if (verticalAxis) {
      canvas.drawLine(isRtl ? cBounds.right : cBounds.left, cBounds.top,
          isRtl ? cBounds.right : cBounds.left, cBounds.bottom, axisPaint);
    }
    float incrementX = 1f / (horizontalUnits + 1);
    for (float value = 0; value < 1; value += incrementX) {
      float x;
      if (isRtl) {
        x = cBounds.right - cBounds.width() * value;
      } else {
        x = cBounds.left + cBounds.width() * value;
      }
      if (verticalGuidelines && value > 0) {
        canvas.drawLine(x, cBounds.top, x, cBounds.bottom, axisPaint);
      }
      if (horizontalTickMarks) {
        canvas.drawCircle(x, cBounds.bottom, strokeWidth, axisPaint);
      }
      if (legend) {
        String text = String.format(Locale.getDefault(), "%.2f", value);
        if (adapter != null) {
          text = adapter.getXLabel(value);
        }
        if (isRtl) {
          labelPaint.getTextBounds(text, 0, text.length(), bounds);
          canvas.drawText(text, x - bounds.width(), cBounds.bottom + textHeight, labelPaint);
        } else {
          canvas.drawText(text, x, cBounds.bottom + textHeight, labelPaint);
        }
      }
    }
    float incrementY = 1f / (verticalUnits + 1);
    for (float value = 0; value < 1; value += incrementY) {
      float y = cBounds.bottom - cBounds.height() * value;
      if (horizontalGuidelines && value > 0) {
        canvas.drawLine(cBounds.left, y, cBounds.right, y, axisPaint);
      }
      if (verticalTickMarks) {
        canvas.drawCircle(isRtl ? cBounds.right : cBounds.left, y, strokeWidth, axisPaint);
      }
      if (legend) {
        String text = String.format(Locale.getDefault(), "%.2f", value);
        if (adapter != null) {
          text = adapter.getYLabel(value);
        }
        if (isRtl) {
          labelPaint.getTextBounds(text, 0, text.length(), bounds);
          canvas.drawText(text, cBounds.right - bounds.width(), y - textHeight * 0.5f, labelPaint);
        } else {
          canvas.drawText(text, cBounds.left, y - textHeight * 0.5f, labelPaint);
        }
      }
    }
  }

  private void drawGraph(Iterable<Float[]> points, Path path, Canvas canvas, int color) {
    graphPaint.setColor(color | 0xff000000);
    if (regenerate) {
      path.reset();
      float xStep = cBounds.width() / (adapter.getXValuesCount() * (end - start));
      float yStep = cBounds.height() / adapter.getYValuesCount();
      for (Float[] point : points) {
        float x = cBounds.left + (point[0] - adapter.getXValuesCount() * start) * xStep;
        float y = cBounds.bottom - point[1] * yStep;
        if (point[2] == 1) {
          path.moveTo(isRtl ? cBounds.right - x : x, y);
        } else {
          path.lineTo(isRtl ? cBounds.right - x : x, y);
        }
      }
    }
    canvas.drawPath(path, graphPaint);
  }

  public void setRange(float start, float end) {
    this.start = start;
    this.end = end;
    regenerate = true;
    invalidate();
  }

  private void drawHistogram(Iterable<Float[]> points, Path path, Canvas canvas, int color) {
//    cachedPaths.reset();
//    cachedPaths.arcTo(outerCircle, start, sweep, false);
//    cachedPaths.arcTo(innerCircle, start + sweep, -sweep, false);
//    cachedPaths.close();
//    canvas.drawPath(cachedPaths, paint);
//    if (text != null && valuesVisible) {
//      paint.setColor(textColor);
//      paint.setTextSize(calculatedRadius * 0.2f);
//      float angle = start + sweep / 2;
//      Rect r = new Rect();
//      paint.getTextBounds(text, 0, text.length(), r);
//      float xOffset = r.width() / 2f;
//      float yOffset = r.height() / 2f;
//      float x = calculatedRadius * 0.8f * (float) Math.cos(angle * Math.PI / 180);
//      float y = calculatedRadius * 0.8f * (float) Math.sin(angle * Math.PI / 180);
//      canvas.drawText(text, x - xOffset + canvas.getWidth() / 2f,
//          y + yOffset + canvas.getHeight() / 2f, paint);
//    }
  }

  private void scalePath(Path path, float scaleX, float scaleY) {
    Matrix scaleMatrix = new Matrix();
    RectF rectF = new RectF();
    path.computeBounds(rectF, true);
    scaleMatrix.setScale(scaleX, scaleY, rectF.centerX(), rectF.centerY());
    path.transform(scaleMatrix);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    int heightSize = MeasureSpec.getSize(heightMeasureSpec);

    int width;
    int height;

    //70dp exact
    if (widthMode == MeasureSpec.EXACTLY) {
      width = widthSize;
    } else if (widthMode == MeasureSpec.AT_MOST) {
      //wrap content
      width = Math.min(minWidth, widthSize);
    } else {
      width = widthSize;
    }

    //Measure Height
    if (heightMode == MeasureSpec.EXACTLY) {
      height = heightSize;
    } else if (heightMode == MeasureSpec.AT_MOST) {
      height = Math.min(minHeight, heightSize);
    } else {
      height = heightSize;
    }
    //MUST CALL THIS
    setMeasuredDimension(width, height);
  }

  private void adjustPaths() {
    for (Path path : cachedPaths) {
      path.reset();
    }
    int diff = adapter.getCount() - cachedPaths.size();
    while (diff > 0) {
      cachedPaths.add(new Path());
      diff--;
    }
    while (diff < 0) {
      cachedPaths.remove(0);
      diff++;
    }
  }

  class AdapterDataSetObserver extends DataSetObserver {

    private Parcelable mInstanceState = null;

    @Override
    public void onChanged() {
      // Detect the case where a cursor that was previously invalidated has
      // been repopulated with new data.
      if (getAdapter().hasStableIds() && mInstanceState != null) {
        onRestoreInstanceState(mInstanceState);
        mInstanceState = null;
      }
      regenerate = true;
      adjustPaths();
      invalidate();
      requestLayout();
    }

    @Override
    public void onInvalidated() {
      if (getAdapter().hasStableIds()) {
        // Remember the current state for the case where our hosting activity is being
        // stopped and later restarted
        mInstanceState = onSaveInstanceState();
      }
      setAdapter(null);
      invalidate();
      requestLayout();
    }
  }
}
