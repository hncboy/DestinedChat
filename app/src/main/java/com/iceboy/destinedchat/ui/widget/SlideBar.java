package com.iceboy.destinedchat.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.hyphenate.util.DensityUtil;
import com.iceboy.destinedchat.R;

/**
 * Created by hncboy on 2018/6/11.
 * 自定义控件，联系人列表右边的滑动字母
 */
public class SlideBar extends View {

    private Paint mPaint;
    private float mTextSize = 0; //字体的大小
    private int mCurrentIndex = -1;
    private float mTextBaseline = 0; //基线
    private OnSlideBarChangeListener mOnSlideBarChangeListener;
    private static final String[] SECTIONS = {
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    };

    public SlideBar(Context context) {
        super(context);
    }

    public SlideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.section_text_color));
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setAntiAlias(true); //抗锯齿
        mPaint.setTextSize(DensityUtil.sp2px(getContext(), 13)); //sp转px
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mTextSize = h * 1.0f / SECTIONS.length; //计算分配给字符的高度
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics(); //字体测量
        float textHeight = fontMetrics.descent - fontMetrics.ascent; //获取绘制字符的实际高度
        mTextBaseline = mTextSize / 2 + textHeight / 2 - fontMetrics.descent; //计算字符居中时的baseline
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float x = getWidth() * 1.0f / 2;
        float baseline = mTextBaseline;
        //绘制所有首字母
        for (String SECTION : SECTIONS) {
            canvas.drawText(SECTION, x, baseline, mPaint);
            //将Baseline向下移
            baseline += mTextSize;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: //按下
                setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bg_slide_bar_touch));
                notifySectionChange(event);
                break;
            case MotionEvent.ACTION_MOVE: //移动
                notifySectionChange(event);
                break;
            case MotionEvent.ACTION_UP: //松开
                //设置透明
                setBackgroundColor(Color.TRANSPARENT);
                if (mOnSlideBarChangeListener != null) {
                    mOnSlideBarChangeListener.onSlidingFinish();
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 字母的改变
     *
     * @param event
     */
    private void notifySectionChange(MotionEvent event) {
        int index = getTouchIndex(event);
        if (mOnSlideBarChangeListener != null && mCurrentIndex != index) {
            mCurrentIndex = index;
            mOnSlideBarChangeListener.onSectionChange(index, SECTIONS[index]);
        }
    }

    /**
     * 获取触摸字母的index
     *
     * @param event
     * @return
     */
    private int getTouchIndex(MotionEvent event) {
        int index = (int) (event.getY() / mTextSize);
        if (index < 0) {
            index = 0;
        } else if (index > SECTIONS.length - 1) {
            index = SECTIONS.length - 1;
        }
        return index;
    }

    /**
     * 滑动监听
     *
     * @param listener
     */
    public void setOnSlidingBarChangeListener(OnSlideBarChangeListener listener) {
        mOnSlideBarChangeListener = listener;
    }

    public interface OnSlideBarChangeListener {

        /**
         * 滑动改变
         *
         * @param index
         * @param section
         */
        void onSectionChange(int index, String section);

        /**
         * 滑动结束
         */
        void onSlidingFinish();
    }
}
