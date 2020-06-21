package com.rhby.cailexun.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SizeUtils;

/**
 * Created by Administrator on 2020/1/6.
 * Describe:
 */
public class WaveRelativeLayoutView extends RelativeLayout {

    //前景波浪的周期时间
    private static final float FOREGROUND_PERIODIC_TIME = 7000;
    //背景波浪的周期时间
    private static final float BACKGROUND_PERIODIC_TIME = 6000;
    //白色波浪的周期时间
    private static final float WHITE_PERIODIC_TIME = 3500;

    public WaveRelativeLayoutView(Context context) {
        super(context);
        init();
    }

    public WaveRelativeLayoutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private static long startTime = AnimationUtils.currentAnimationTimeMillis();
    private Path mForegroundWavePath = new Path();
    private Path mBackgroundWavePath = new Path();
    private Path mWhiteWavePath = new Path();
    private Paint mForegroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mmWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private void init() {
        mForegroundPaint.setColor(Color.parseColor("#83D7FE"));
        mForegroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mBackgroundPaint.setColor(Color.parseColor("#4EC6FE"));
        mBackgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mmWhitePaint.setColor(Color.parseColor("#FFFFFF"));
        mmWhitePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        //ViewGroup 在没有背景时不会调用onDraw，所以需要手动设置一个background
        if (getBackground() == null) {
            setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float pasedTime = AnimationUtils.currentAnimationTimeMillis() - startTime;

        //------前景层的波浪-------
        mForegroundWavePath.reset();
        int startY = 0;
        //此处的waveLen实际为贝塞尔曲线中每一个控制点的间距，修改这个值可以改变波长，但不直接等于波长
        float waveLen = getWidth() / 4f;
        //此处的WaveHeight实际为贝塞尔曲线中每一个控制点的高度，修改这个值可以改变浪高，但不直接等于浪高
        float foregroundWaveHeight = SizeUtils.dp2px(15);
        float backgroundWaveHeight = SizeUtils.dp2px(20);
        float whiteWaveHeight = SizeUtils.dp2px(13);
        mForegroundWavePath.moveTo(-getWidth(), startY);
        //初始时，View左侧不可见的波浪
        mForegroundWavePath.quadTo(waveLen * -3f, foregroundWaveHeight,waveLen * -2, startY);
        mForegroundWavePath.quadTo(waveLen * -1, -foregroundWaveHeight, 0,startY);
        //初始时，View内可见波浪
        mForegroundWavePath.quadTo(waveLen, foregroundWaveHeight, waveLen *2, startY);
        mForegroundWavePath.quadTo(waveLen * 3, -foregroundWaveHeight,waveLen * 4, startY);
        mForegroundWavePath.lineTo(getWidth(), getHeight());
        mForegroundWavePath.lineTo(-getWidth(), getHeight());
        mForegroundWavePath.close();
        //根据流逝的时间、周期长短 来计算偏移量，得到‘当前位置’
        float currTimeForeground = pasedTime % FOREGROUND_PERIODIC_TIME;
        //注意：前景波浪是向左移动的
//        mForegroundWavePath.offset(getWidth() * (FOREGROUND_PERIODIC_TIME -currTimeForeground) /
//                FOREGROUND_PERIODIC_TIME, getHeight() / 2 + SizeUtils.dp2px(10));
        mForegroundWavePath.offset(getWidth() * currTimeForeground /FOREGROUND_PERIODIC_TIME,
                getHeight() / 2 +SizeUtils.dp2px(10));

        //------背景层波浪-------
        mBackgroundWavePath.reset();
        mBackgroundWavePath.moveTo(-getWidth(), startY);
        //初始时，View左侧不可见的波浪
        mBackgroundWavePath.quadTo(waveLen * -3f, backgroundWaveHeight,waveLen * -2, startY);
        mBackgroundWavePath.quadTo(waveLen * -1, -backgroundWaveHeight, 0,startY);
        //初始时，View内可见波浪
        mBackgroundWavePath.quadTo(waveLen, backgroundWaveHeight, waveLen* 2, startY);
        mBackgroundWavePath.quadTo(waveLen * 3, -backgroundWaveHeight,waveLen * 4, startY);
        mBackgroundWavePath.lineTo(getWidth(), getHeight());
        mBackgroundWavePath.lineTo(-getWidth(), getHeight());
        mBackgroundWavePath.close();
        float currTimeBackground = pasedTime % BACKGROUND_PERIODIC_TIME;
        //根据流逝的时间、周期长短 来计算偏移量，得到‘当前位置’
        mBackgroundWavePath.offset(getWidth() * currTimeBackground /BACKGROUND_PERIODIC_TIME,
                getHeight() / 2 - SizeUtils.dp2px(10));

        //------白色层波浪-------
        mWhiteWavePath.reset();
        mWhiteWavePath.moveTo(-getWidth(), startY);
        //初始时，View左侧不可见的波浪
        mWhiteWavePath.quadTo(waveLen * -3f, whiteWaveHeight,waveLen * -2, startY);
        mWhiteWavePath.quadTo(waveLen * -1, -whiteWaveHeight, 0,startY);
        //初始时，View内可见波浪
        mWhiteWavePath.quadTo(waveLen, whiteWaveHeight, waveLen* 2, startY);
        mWhiteWavePath.quadTo(waveLen * 3, -whiteWaveHeight,waveLen * 4, startY);
        mWhiteWavePath.lineTo(getWidth(), getHeight());
        mWhiteWavePath.lineTo(-getWidth(), getHeight());
        mWhiteWavePath.close();
        float currTimeWhite = pasedTime % WHITE_PERIODIC_TIME;
        //根据流逝的时间、周期长短 来计算偏移量，得到‘当前位置’
        mWhiteWavePath.offset(getWidth() * currTimeWhite /WHITE_PERIODIC_TIME,
                getHeight() / 2 + SizeUtils.dp2px(40));

        canvas.drawPath(mBackgroundWavePath, mBackgroundPaint);
        canvas.drawPath(mForegroundWavePath, mForegroundPaint);
        canvas.drawPath(mWhiteWavePath, mmWhitePaint);
        invalidate();
    }
}


