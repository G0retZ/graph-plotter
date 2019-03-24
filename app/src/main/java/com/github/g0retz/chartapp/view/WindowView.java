package com.github.g0retz.chartapp.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.github.g0retz.chartapp.R;

public class WindowView extends View {

  private final double minDragDistance;
  private final int minWidth;
  private final int minHeight;
  private final int minWindowWidth;
  private final int horizontalStrokeWidth;
  private final int verticalStrokeWidth;
  boolean drag;
  private int mistColor;
  private int frameColor;
  private Rect cBounds = new Rect();
  private Rect wBounds = new Rect();
  private float restoredLeft = Float.MIN_VALUE;
  private float restoredRight = Float.MIN_VALUE;
  private float cWidthDivider;
  private Path path = new Path();
  private Paint paint;
  private boolean isRtl = false;
  private float lastX = Float.MIN_VALUE;
  private int borderSelected;
  private OnRangeChangeListener onRangeChangeListener;

  public WindowView(Context context) {
    super(context);
    minWidth = Math.round(96 * context.getResources().getDisplayMetrics().density);
    minHeight = Math.round(48 * context.getResources().getDisplayMetrics().density);
    minWindowWidth = Math.round(96 * context.getResources().getDisplayMetrics().density);
    horizontalStrokeWidth = Math.round(2 * context.getResources().getDisplayMetrics().density);
    verticalStrokeWidth = Math.round(6 * context.getResources().getDisplayMetrics().density);
    minDragDistance = Math.round(8 * context.getResources().getDisplayMetrics().density);
  }

  public WindowView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public WindowView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    TypedArray typedArray = context.getTheme().obtainStyledAttributes(
        attrs,
        R.styleable.WindowView,
        defStyleAttr,
        0
    );
    minWidth = Math.round(96 * context.getResources().getDisplayMetrics().density);
    minHeight = Math.round(48 * context.getResources().getDisplayMetrics().density);
    minWindowWidth = Math.round(96 * context.getResources().getDisplayMetrics().density);
    horizontalStrokeWidth = Math.round(2 * context.getResources().getDisplayMetrics().density);
    verticalStrokeWidth = Math.round(6 * context.getResources().getDisplayMetrics().density);
    minDragDistance = Math.round(8 * context.getResources().getDisplayMetrics().density);
    init(typedArray);
  }

  @TargetApi(VERSION_CODES.LOLLIPOP)
  public WindowView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    TypedArray typedArray = context.getTheme().obtainStyledAttributes(
        attrs,
        R.styleable.WindowView,
        defStyleAttr,
        defStyleRes
    );
    minWidth = Math.round(96 * context.getResources().getDisplayMetrics().density);
    minHeight = Math.round(48 * context.getResources().getDisplayMetrics().density);
    minWindowWidth = Math.round(96 * context.getResources().getDisplayMetrics().density);
    horizontalStrokeWidth = Math.round(2 * context.getResources().getDisplayMetrics().density);
    verticalStrokeWidth = Math.round(6 * context.getResources().getDisplayMetrics().density);
    minDragDistance = Math.round(8 * context.getResources().getDisplayMetrics().density);
    init(typedArray);
  }

  private void init(TypedArray typedArray) {
    try {
      mistColor = typedArray.getColor(R.styleable.WindowView_mistColor, 0);
      frameColor = typedArray.getColor(R.styleable.WindowView_frameColor, 0);
    } finally {
      typedArray.recycle();
    }
    paint = new Paint();
    paint.setDither(false);
    paint.setStyle(Style.FILL);
    paint.setAntiAlias(false);
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    cBounds.set(getPaddingLeft(), getPaddingTop(), w - getPaddingRight(), h - getPaddingBottom());
    if (cBounds.width() != 0) {
      cWidthDivider = 1f / cBounds.width();
    }
    if (wBounds.width() == 0) {
      if (restoredLeft > Float.MIN_VALUE && restoredRight > Float.MIN_VALUE) {
        wBounds.left = cBounds.left + Math.round(cBounds.width() * restoredLeft);
        wBounds.right = cBounds.left + Math.round(cBounds.width() * restoredRight);
        wBounds.top = cBounds.top;
        wBounds.bottom = cBounds.bottom;
      } else {
        if (isRtl) {
          wBounds.set(cBounds.right - minWindowWidth, cBounds.top, cBounds.right, cBounds.bottom);
        } else {
          wBounds.set(cBounds.left, cBounds.top, cBounds.left + minWindowWidth, cBounds.bottom);
        }
      }
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
      isRtl = getLayoutDirection() == LAYOUT_DIRECTION_RTL;
    }
    path.reset();
    path.moveTo(cBounds.left, cBounds.top);
    path.lineTo(cBounds.left, cBounds.bottom);
    path.lineTo(wBounds.left, cBounds.bottom);
    path.lineTo(wBounds.left, cBounds.top);
    path.close();
    path.moveTo(cBounds.right, cBounds.top);
    path.lineTo(cBounds.right, cBounds.bottom);
    path.lineTo(wBounds.right, cBounds.bottom);
    path.lineTo(wBounds.right, cBounds.top);
    path.close();
    paint.setColor(mistColor);
    canvas.drawPath(path, paint);
    path.reset();
    path.setFillType(FillType.EVEN_ODD);
    path.moveTo(wBounds.left, wBounds.top);
    path.lineTo(wBounds.left, wBounds.bottom);
    path.lineTo(wBounds.right, wBounds.bottom);
    path.lineTo(wBounds.right, wBounds.top);
    path.moveTo(wBounds.left + verticalStrokeWidth, wBounds.top + horizontalStrokeWidth);
    path.lineTo(wBounds.left + verticalStrokeWidth, wBounds.bottom - horizontalStrokeWidth);
    path.lineTo(wBounds.right - verticalStrokeWidth, wBounds.bottom - horizontalStrokeWidth);
    path.lineTo(wBounds.right - verticalStrokeWidth, wBounds.top + horizontalStrokeWidth);
    path.close();
    paint.setColor(frameColor);
    canvas.drawPath(path, paint);
  }

  public OnRangeChangeListener getOnRangeChangeListener() {
    return onRangeChangeListener;
  }

  public void setOnRangeChangeListener(OnRangeChangeListener onRangeChangeListener) {
    this.onRangeChangeListener = onRangeChangeListener;
  }

  @Override
  @SuppressLint("ClickableViewAccessibility")
  public boolean onTouchEvent(MotionEvent motionEvent) {
    switch (motionEvent.getAction()) {
      case MotionEvent.ACTION_DOWN:
        float offset = minWindowWidth / 4f;
        lastX = motionEvent.getX();
        if (lastX > wBounds.left - offset && lastX < wBounds.right + offset) {
          if (lastX < wBounds.left + offset) {
            borderSelected = -1;
          } else if (lastX > wBounds.right - offset) {
            borderSelected = 1;
          } else {
            borderSelected = 0;
          }
        } else {
          lastX = -1;
        }
        break;
      case MotionEvent.ACTION_MOVE:
        if (drag) {
          float dX = motionEvent.getX() - lastX;
          switch (borderSelected) {
            case -1:
              wBounds.left = wBounds.left + (int) dX;
              if (wBounds.left < cBounds.left) {
                wBounds.left = cBounds.left;
              } else if (wBounds.left > wBounds.right - minWindowWidth) {
                wBounds.left = wBounds.right - minWindowWidth;
              }
              invalidate();
              break;
            case 1:
              wBounds.right = wBounds.right + (int) dX;
              if (wBounds.right > cBounds.right) {
                wBounds.right = cBounds.right;
              } else if (wBounds.right < wBounds.left + minWindowWidth) {
                wBounds.right = wBounds.left + minWindowWidth;
              }
              invalidate();
              break;
            case 0:
              if (wBounds.left + dX > cBounds.left && wBounds.right + dX < cBounds.right) {
                wBounds.left = wBounds.left + (int) dX;
                wBounds.right = wBounds.right + (int) dX;
                invalidate();
              }
              break;
            default:
              return true;
          }
          lastX = lastX + dX;
          restoredLeft = (wBounds.left - cBounds.left) * cWidthDivider;
          restoredRight = (wBounds.right - cBounds.left) * cWidthDivider;
          if (onRangeChangeListener != null) {
            if (isRtl) {
              onRangeChangeListener.onRangeChanged(restoredRight, restoredLeft);
            } else {
              onRangeChangeListener.onRangeChanged(restoredLeft, restoredRight);
            }
          }
        } else if (lastX >= Float.MIN_VALUE
            && Math.abs(lastX - motionEvent.getX()) >= minDragDistance) {
          drag = true;
        }
        break;
      case MotionEvent.ACTION_UP:
        lastX = Float.MIN_VALUE;
        drag = false;
        break;
    }
    return true;
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

  @Override
  protected Parcelable onSaveInstanceState() {
    Parcelable superState = super.onSaveInstanceState();

    SavedState ss = new SavedState(superState);

    ss.left = restoredLeft;
    ss.right = restoredRight;
    return ss;
  }

  @Override
  protected void onRestoreInstanceState(Parcelable state) {
    if (!(state instanceof SavedState)) {
      super.onRestoreInstanceState(state);
      return;
    }

    SavedState ss = (SavedState) state;
    super.onRestoreInstanceState(ss.getSuperState());

    restoredLeft = ss.left;
    restoredRight = ss.right;
  }

  static class SavedState extends BaseSavedState {

    //required field that makes Parcelables from a Parcel
    public static final Parcelable.Creator<SavedState> CREATOR =
        new Parcelable.Creator<SavedState>() {
          public SavedState createFromParcel(Parcel in) {
            return new SavedState(in);
          }

          public SavedState[] newArray(int size) {
            return new SavedState[size];
          }
        };
    float left;
    float right;

    SavedState(Parcelable superState) {
      super(superState);
    }

    private SavedState(Parcel in) {
      super(in);
      in.setDataPosition(0);
      this.left = in.readFloat();
      in.setDataPosition(1);
      this.right = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
      super.writeToParcel(out, flags);
      out.setDataPosition(0);
      out.writeFloat(this.left);
      out.setDataPosition(1);
      out.writeFloat(this.right);
    }
  }

  public interface OnRangeChangeListener {

    void onRangeChanged(float startWeight, float endWeight);
  }
}
