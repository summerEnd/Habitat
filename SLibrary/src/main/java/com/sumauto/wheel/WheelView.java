package com.sumauto.wheel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.sp.lib.R;

import java.lang.reflect.Method;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 3d滚轮控件
 */
public class WheelView extends View {


    private class InertiaTimerTask extends TimerTask {

        float a;
        final float velocityY;

        InertiaTimerTask(float velocityY) {
            this.velocityY = velocityY;
            a = Integer.MAX_VALUE;
        }

        @Override
        public final void run() {
            if (a == Integer.MAX_VALUE) {
                if (Math.abs(velocityY) > 2000F) {
                    if (velocityY > 0.0F) {
                        a = 2000F;
                    } else {
                        a = -2000F;
                    }
                } else {
                    a = velocityY;
                }
            }
            if (Math.abs(a) >= 0.0F && Math.abs(a) <= 20F) {
                cancelFuture();
                post(new Runnable() {
                    @Override
                    public void run() {
                        smoothScroll(WheelView.ACTION.FLING);
                    }
                });
                return;
            }
            int i = (int) ((a * 10F) / 1000F);
            totalScrollY = totalScrollY - i;
            if (!isLoop) {
                float itemHeight = WheelView.this.itemHeight;
                float top = (-initPosition) * itemHeight;
                float bottom = (getItemsCount() - 1 - initPosition) * itemHeight;
                if (totalScrollY - itemHeight * 0.3 < top) {
                    top = totalScrollY + i;
                } else if (totalScrollY + itemHeight * 0.3 > bottom) {
                    bottom = totalScrollY + i;
                }

                if (totalScrollY <= top) {
                    a = 40F;
                    totalScrollY = (int) top;
                } else if (totalScrollY >= bottom) {
                    totalScrollY = (int) bottom;
                    a = -40F;
                }
            }
            if (a < 0.0F) {
                a = a + 20F;
            } else {
                a = a - 20F;
            }
            postInvalidate();
        }

    }

    private class SmoothScrollTimerTask extends TimerTask {

        int realTotalOffset;
        int realOffset;
        int offset;

        SmoothScrollTimerTask(int offset) {
            this.offset = offset;
            realTotalOffset = Integer.MAX_VALUE;
            realOffset = 0;
        }

        @Override
        public final void run() {
            if (realTotalOffset == Integer.MAX_VALUE) {
                realTotalOffset = offset;
            }
            //把要滚动的范围细分成十小份，按是小份单位来重绘
            realOffset = (int) ((float) realTotalOffset * 0.1F);

            if (realOffset == 0) {
                if (realTotalOffset < 0) {
                    realOffset = -1;
                } else {
                    realOffset = 1;
                }
            }

            if (Math.abs(realTotalOffset) <= 1) {
                cancelFuture();
                post(new Runnable() {
                    @Override
                    public void run() {
                        onItemSelected();
                    }
                });
            } else {
                totalScrollY = totalScrollY + realOffset;

                //这里如果不是循环模式，则点击空白位置需要回滚，不然就会出现选到－1 item的 情况
                if (!isLoop) {
                    float itemHeight = getItemHeight();
                    float top = (float) (-initPosition) * itemHeight;
                    float bottom = (float) (getItemsCount() - 1 - initPosition) * itemHeight;
                    if (totalScrollY <= top || totalScrollY >= bottom) {
                        totalScrollY = totalScrollY - realOffset;
                        cancelFuture();
                        post(new Runnable() {
                            @Override
                            public void run() {
                                onItemSelected();
                            }
                        });
                        return;
                    }
                }
                postInvalidate();
                realTotalOffset = realTotalOffset - realOffset;
            }
        }
    }

    public float getItemHeight() {
        return itemHeight;
    }


    public interface WheelAdapter<T> {
        /**
         * Gets items count
         *
         * @return the count of wheel items
         */
        int getItemsCount();

        /**
         * Gets a wheel item by index.
         *
         * @param index the item index
         * @return the wheel item text or null
         */
        T getItem(int index);

        /**
         * Gets maximum item length. It is used to determine the wheel width.
         * If -1 is returned there will be used the default wheel width.
         *
         * @return the maximum item length or -1
         */
        int indexOf(T o);
    }

    public interface OnDismissListener {
        void onDismiss(Object o);
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int index);
    }

    public enum ACTION {
        // 点击，滑翔(滑到尽头)，拖拽事件
        CLICK, FLING, DAGGLE
    }



    private GestureDetector gestureDetector;
    private OnItemSelectedListener onItemSelectedListener;

    // Timer mTimer;
    ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> mFuture;

    Paint paintOuterText = new Paint();
    Paint paintCenterText = new Paint();
    Paint paintIndicator = new Paint();

    private WheelAdapter adapter;

    private String label;//附加单位
    private int maxTextWidth;
    private int maxTextHeight;
    private float itemHeight;//每行高度

    private int textColorOut;
    private int textColorCenter;
    private int dividerColor;

    // 条目间距倍数
    static final float lineSpacingMultiplier = 1.7F;
    boolean isLoop;

    //中间Y坐标
    float centerY;

    //滚动总高度y值
    int totalScrollY;
    //初始化默认选中第几个
    int initPosition;
    //选中的Item是第几个
    private int selectedItem;
    int preCurrentIndex;
    //滚动偏移值,用于记录滚动了多少个item
    int change;

    // 显示几个条目
    final int VISIBLE_COUNT = 11;
    Object mDisplayValues[] = new Object[VISIBLE_COUNT];

    int measuredHeight;
    int measuredWidth;

    // 半圆周长
    int halfCircle;
    // 半径
    int radius;

    private int mOffset = 0;
    private float previousY = 0;
    long startTime = 0;

    // 修改这个值可以改变滑行速度
    private static final int VELOCITYFLING = 5;
    int widthMeasureSpec;

    private int mGravity = Gravity.CENTER;
    private int drawCenterContentStart = 0;//中间选中文字开始绘制位置
    private int drawOutContentStart = 0;//非中间文字开始绘制位置
    private static final float SCALECONTENT = 0.8F;//非中间文字则用此控制高度，压扁形成3d错觉
    private static final float CENTERCONTENTOFFSET = 6;//中间文字文字居中需要此偏移值
    private static final String GETPICKERVIEWTEXT = "getPickerViewText";//反射的方法名

    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WheelView, 0, 0);

        mGravity = a.getInt(R.styleable.WheelView_gravity, Gravity.CENTER);
        textColorOut = a.getColor(R.styleable.WheelView_textColorOut, 0xffa8a8a8);
        textColorCenter = a.getColor(R.styleable.WheelView_textColorCenter, 0xff2a2a2a);
        dividerColor = a.getColor(R.styleable.WheelView_dividerColor, 0xffd5d5d5);
        int textSize = a.getDimensionPixelOffset(R.styleable.WheelView_textSize, 0);
        if (textSize == 0) {
            setTextSize(15);
        } else {
            paintOuterText.setTextSize(textSize);
            paintCenterText.setTextSize(textSize);
        }

        a.recycle();


        paintOuterText.setColor(textColorOut);
        paintOuterText.setAntiAlias(true);
        paintOuterText.setTypeface(Typeface.MONOSPACE);

        paintCenterText.setColor(textColorCenter);
        paintCenterText.setAntiAlias(true);
        paintCenterText.setTextScaleX(1.1F);
        paintCenterText.setTypeface(Typeface.MONOSPACE);

        paintIndicator.setColor(dividerColor);
        paintIndicator.setAntiAlias(true);

        setLayerType(LAYER_TYPE_SOFTWARE, null);

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                scrollBy(velocityY);
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
        gestureDetector.setIsLongpressEnabled(false);

        isLoop = true;

        totalScrollY = 0;
        initPosition = -1;
    }


    private void remeasure() {
        measureTextWidthHeight();

        //最大Text的高度乘间距倍数得到 可见文字实际的总高度，半圆的周长
        halfCircle = (int) (itemHeight * (VISIBLE_COUNT - 1));
        //整个圆的周长除以PI得到直径，这个直径用作控件的总高度
        measuredHeight = (int) (halfCircle*2 / Math.PI);
        //求出半径
        radius = (int) (halfCircle / Math.PI);
        //控件宽度，这里支持weight
        measuredWidth = MeasureSpec.getSize(widthMeasureSpec);

        centerY = (measuredHeight + maxTextHeight) / 2.0F - CENTERCONTENTOFFSET;
        //初始化显示的item的position，根据是否loop
        if (initPosition == -1) {
            if (isLoop && adapter != null) {
                initPosition = (adapter.getItemsCount() + 1) / 2;
            } else {
                initPosition = 0;
            }
        }

        preCurrentIndex = initPosition;
    }

    /**
     * 计算最大len的Text的宽高度
     */
    private void measureTextWidthHeight() {
        Rect rect = new Rect();
        if (adapter != null) {
            for (int i = 0; i < adapter.getItemsCount(); i++) {
                String s1 = getContentText(adapter.getItem(i));
                paintCenterText.getTextBounds(s1, 0, s1.length(), rect);
                int textWidth = rect.width();
                if (textWidth > maxTextWidth) {
                    maxTextWidth = textWidth;
                }
            }
        } else {
            paintCenterText.getTextBounds("item11", 0, 6, rect);
        }


        paintCenterText.getTextBounds("\u661F\u671F", 0, 2, rect); // 星期
        int textHeight = rect.height();
        if (textHeight > maxTextHeight) {
            maxTextHeight = textHeight;
        }
        itemHeight = lineSpacingMultiplier * maxTextHeight;
    }

    void smoothScroll(ACTION action) {
        cancelFuture();
        if (action == ACTION.FLING || action == ACTION.DAGGLE) {
            mOffset = (int) ((totalScrollY % itemHeight + itemHeight) % itemHeight);
            if ((float) mOffset > itemHeight / 2.0F) {
                mOffset = (int) (itemHeight - (float) mOffset);
            } else {
                mOffset = -mOffset;
            }
        }
        //停止的时候，位置有偏移，不是全部都能正确停止到中间位置的，这里把文字位置挪回中间去
        mFuture = mExecutor.scheduleWithFixedDelay(new SmoothScrollTimerTask(mOffset), 0, 10, TimeUnit.MILLISECONDS);
    }

    protected final void scrollBy(float velocityY) {
        cancelFuture();

        mFuture = mExecutor.scheduleWithFixedDelay(new InertiaTimerTask(velocityY), 0, VELOCITYFLING, TimeUnit.MILLISECONDS);
    }

    public void cancelFuture() {
        if (mFuture != null && !mFuture.isCancelled()) {
            mFuture.cancel(true);
            mFuture = null;
        }
    }

    /**
     * 设置是否循环滚动
     */
    public final void setCyclic(boolean cyclic) {
        isLoop = cyclic;
    }

    public final void setTextSize(float size) {
        int textSize = (int) (getResources().getDisplayMetrics().scaledDensity * size);
        paintOuterText.setTextSize(textSize);
        paintCenterText.setTextSize(textSize);
    }

    public final void setCurrentItem(int currentItem) {
        this.initPosition = currentItem;
        totalScrollY = 0;//回归顶部，不然重设setCurrentItem的话位置会偏移的，就会显示出不对位置的数据
        invalidate();
    }

    public final void setOnItemSelectedListener(OnItemSelectedListener OnItemSelectedListener) {
        this.onItemSelectedListener = OnItemSelectedListener;
    }

    public final void setAdapter(WheelAdapter adapter) {
        this.adapter = adapter;
        remeasure();
        invalidate();
    }

    public final WheelAdapter getAdapter() {
        return adapter;
    }

    public final int getCurrentItem() {
        return selectedItem;
    }

    protected final void onItemSelected() {
        if (onItemSelectedListener != null) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    onItemSelectedListener.onItemSelected(getCurrentItem());
                }
            }, 200L);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 设置数组中每个元素的值
        int counter = 0;
        //跟滚动流畅度有关，总滑动距离与每个item高度取余，即并不是一格格的滚动，每个item不一定滚到对应Rect里的，这个item对应格子的偏移值
        int itemHeightOffset = (int) (totalScrollY % itemHeight);
        if (isInEditMode()) {
            while (counter < VISIBLE_COUNT) {
                //判断是否循环，如果是循环数据源也使用相对循环的position获取对应的item值，如果不是循环则超出数据源范围使用""空白字符串填充，在界面上形成空白无数据的item项
                mDisplayValues[counter] = "item" + counter;
                counter++;
            }
        } else {
            if (adapter == null) {
                return;
            }
            //可见的item数组
            //滚动的Y值高度除去每行Item的高度，得到滚动了多少个item，即change数
            change = (int) (totalScrollY / itemHeight);
            int itemsCount = adapter.getItemsCount();
            try {
                //滚动中实际的预选中的item(即经过了中间位置的item) ＝ 滑动前的位置 ＋ 滑动相对位置
                preCurrentIndex = initPosition + change % itemsCount;
            } catch (ArithmeticException e) {
                System.out.println("出错了！adapter.getItemsCount() == 0，联动数据不匹配");
            }
            if (!isLoop) {//不循环的情况
                if (preCurrentIndex < 0) {
                    preCurrentIndex = 0;
                }
                if (preCurrentIndex > itemsCount - 1) {
                    preCurrentIndex = itemsCount - 1;
                }
            } else {//循环
                if (preCurrentIndex < 0) {//举个例子：如果总数是5，preCurrentIndex ＝ －1，那么preCurrentIndex按循环来说，其实是0的上面，也就是4的位置
                    preCurrentIndex = itemsCount + preCurrentIndex;
                }
                if (preCurrentIndex > itemsCount - 1) {//同理上面,自己脑补一下
                    preCurrentIndex = preCurrentIndex - itemsCount;
                }
            }

            while (counter < VISIBLE_COUNT) {
                int index = preCurrentIndex - (VISIBLE_COUNT / 2 - counter);//索引值，即当前在控件中间的item看作数据源的中间，计算出相对源数据源的index值

                //判断是否循环，如果是循环数据源也使用相对循环的position获取对应的item值，如果不是循环则超出数据源范围使用""空白字符串填充，在界面上形成空白无数据的item项
                if (isLoop) {
                    index = getLoopMappingIndex(index);
                    mDisplayValues[counter] = adapter.getItem(index);
                } else if (index < 0) {
                    mDisplayValues[counter] = "";
                } else if (index > itemsCount - 1) {
                    mDisplayValues[counter] = "";
                } else {
                    mDisplayValues[counter] = adapter.getItem(index);
                }
                counter++;
            }
        }

        float firstLineY = (getHeight() - itemHeight) / 2.0F;//计算两条横线和控件中间点的Y位置
        float secondLineY = (getHeight() + itemHeight) / 2.0F;

        //中间两条横线
        canvas.drawLine(0.0F, firstLineY, measuredWidth, firstLineY, paintIndicator);
        canvas.drawLine(0.0F, secondLineY, measuredWidth, secondLineY, paintIndicator);
        //单位的Label
        if (label != null) {
            int drawRightContentStart = measuredWidth - getTextWidth(paintCenterText, label);
            //靠右并留出空隙
            canvas.drawText(label, drawRightContentStart - CENTERCONTENTOFFSET, centerY, paintCenterText);
        }
        counter = 0;
        while (counter < VISIBLE_COUNT) {
            canvas.save();
            // L(弧长)=α（弧度）* r(半径) （弧度制）
            // 求弧度--> (L * π ) / (π * r)   (弧长X派/半圆周长)
            float itemHeight = maxTextHeight * lineSpacingMultiplier;
            double radian = ((itemHeight * counter - itemHeightOffset) * Math.PI) / halfCircle;
            // 弧度转换成角度(把半圆以Y轴为轴心向右转90度，使其处于第一象限及第四象限
            float angle = (float) (90D - (radian / Math.PI) * 180D);
            // 九十度以上的不绘制
            if (angle >= 90F || angle <= -90F) {
                canvas.restore();
            } else {
                String contentText = getContentText(mDisplayValues[counter]);

                //计算开始绘制的位置
                measuredCenterContentStart(contentText);
                measuredOutContentStart(contentText);
                float translateY = (float) (radius - Math.cos(radian) * radius - (Math.sin(radian) * maxTextHeight) / 2D);
                //根据Math.sin(radian)来更改canvas坐标系原点，然后缩放画布，使得文字高度进行缩放，形成弧形3d视觉差
                canvas.translate(0.0F, translateY);
                canvas.scale(1.0F, (float) Math.sin(radian));
                if (translateY <= firstLineY && maxTextHeight + translateY >= firstLineY) {
                    // 条目经过第一条线
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, firstLineY - translateY);
                    canvas.scale(1.0F, (float) Math.sin(radian) * SCALECONTENT);
                    canvas.drawText(contentText, drawOutContentStart, maxTextHeight, paintOuterText);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, firstLineY - translateY, measuredWidth, (int) (itemHeight));
                    canvas.scale(1.0F, (float) Math.sin(radian) * 1F);
                    canvas.drawText(contentText, drawCenterContentStart, maxTextHeight - CENTERCONTENTOFFSET, paintCenterText);
                    canvas.restore();
                } else if (translateY <= secondLineY && maxTextHeight + translateY >= secondLineY) {
                    // 条目经过第二条线
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, secondLineY - translateY);
                    canvas.scale(1.0F, (float) Math.sin(radian) * 1.0F);
                    canvas.drawText(contentText, drawCenterContentStart, maxTextHeight - CENTERCONTENTOFFSET, paintCenterText);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, secondLineY - translateY, measuredWidth, (int) (itemHeight));
                    canvas.scale(1.0F, (float) Math.sin(radian) * SCALECONTENT);
                    canvas.drawText(contentText, drawOutContentStart, maxTextHeight, paintOuterText);
                    canvas.restore();
                } else if (translateY >= firstLineY && maxTextHeight + translateY <= secondLineY) {
                    // 中间条目
                    canvas.clipRect(0, 0, measuredWidth, (int) (itemHeight));
                    canvas.drawText(contentText, drawCenterContentStart, maxTextHeight - CENTERCONTENTOFFSET, paintCenterText);
                    if (!isInEditMode()) {
                        selectedItem = adapter.indexOf(mDisplayValues[counter]);
                    }

                } else {
                    // 其他条目
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, (int) (itemHeight));
                    canvas.scale(1.0F, (float) Math.sin(radian) * SCALECONTENT);
                    canvas.drawText(contentText, drawOutContentStart, maxTextHeight, paintOuterText);
                    canvas.restore();
                }
                canvas.restore();
            }
            counter++;
        }
    }

    //递归计算出对应的index
    private int getLoopMappingIndex(int index) {
        if (index < 0) {
            index = index + adapter.getItemsCount();
            index = getLoopMappingIndex(index);
        } else if (index > adapter.getItemsCount() - 1) {
            index = index - adapter.getItemsCount();
            index = getLoopMappingIndex(index);
        }
        return index;
    }

    /**
     * 根据传进来的对象反射出getPickerViewText()方法，来获取需要显示的值
     */
    private String getContentText(Object item) {
        String contentText = item.toString();
        try {
            Class<?> clz = item.getClass();
            Method m = clz.getMethod(GETPICKERVIEWTEXT);
            contentText = m.invoke(item, new Object[0]).toString();
        } catch (Exception e) {
        }
        return contentText;
    }

    private void measuredCenterContentStart(String content) {
        Rect rect = new Rect();
        paintCenterText.getTextBounds(content, 0, content.length(), rect);
        switch (mGravity) {
            case Gravity.CENTER:
                drawCenterContentStart = (int) ((measuredWidth - rect.width()) * 0.5);
                break;
            case Gravity.LEFT:
                drawCenterContentStart = 0;
                break;
            case Gravity.RIGHT:
                drawCenterContentStart = measuredWidth - rect.width();
                break;
        }
    }

    private void measuredOutContentStart(String content) {
        Rect rect = new Rect();
        paintOuterText.getTextBounds(content, 0, content.length(), rect);
        switch (mGravity) {
            case Gravity.CENTER:
                drawOutContentStart = (int) ((measuredWidth - rect.width()) * 0.5);
                break;
            case Gravity.LEFT:
                drawOutContentStart = 0;
                break;
            case Gravity.RIGHT:
                drawOutContentStart = measuredWidth - rect.width();
                break;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.widthMeasureSpec = widthMeasureSpec;
        remeasure();
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean eventConsumed = gestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                cancelFuture();
                previousY = event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                float dy = previousY - event.getRawY();
                previousY = event.getRawY();
                totalScrollY = (int) (totalScrollY + dy);

                // 边界处理。
                if (!isLoop) {
                    float top = -initPosition * itemHeight;
                    float bottom = (adapter.getItemsCount() - 1 - initPosition) * itemHeight;
                    if (totalScrollY - itemHeight * 0.3 < top) {
                        top = totalScrollY - dy;
                    } else if (totalScrollY + itemHeight * 0.3 > bottom) {
                        bottom = totalScrollY - dy;
                    }

                    if (totalScrollY < top) {
                        totalScrollY = (int) top;
                    } else if (totalScrollY > bottom) {
                        totalScrollY = (int) bottom;
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
            default:
                if (!eventConsumed) {
                    float y = event.getY();
                    double l = Math.acos((radius - y) / radius) * radius;
                    int circlePosition = (int) ((l + itemHeight / 2) / itemHeight);

                    float extraOffset = totalScrollY % itemHeight;
                    mOffset = (int) ((circlePosition - VISIBLE_COUNT / 2) * itemHeight - extraOffset);

                    if ((System.currentTimeMillis() - startTime) > 120) {
                        // 处理拖拽事件
                        smoothScroll(ACTION.DAGGLE);
                    } else {
                        // 处理条目点击事件
                        smoothScroll(ACTION.CLICK);
                    }
                }
                break;
        }
        invalidate();

        return true;
    }

    /**
     * 获取Item个数
     */
    public int getItemsCount() {
        return adapter != null ? adapter.getItemsCount() : 0;
    }

    /**
     * 附加在右边的单位字符串
     */
    public void setLabel(String label) {
        this.label = label;
    }

    public void setGravity(int gravity) {
        this.mGravity = gravity;
    }

    public int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }
}