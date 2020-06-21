package com.android.im.imview.impromptlibrary;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.android.im.R;

/**
 * Created by limxing on 16/1/7.
 * https://www.github.com/limxing
 */
@SuppressLint("AppCompatCustomView")
class IMPromptView extends ImageView {
    public static final int PROMPT_SUCCESS = 101;
    public static final int PROMPT_LOADING = 102;
    public static final int PROMPT_ERROR = 103;
    public static final int PROMPT_NONE = 104;
    public static final int PROMPT_INFO = 105;
    public static final int PROMPT_WARN = 106;
    public static final int PROMPT_ALERT_WARN = 107;
    private static final String TAG = "LOADVIEW";
    public static final int PROMPT_CUSTOM = 108;
    public static final int PROMPT_AD = 109;
    public static final int CUSTOMER_LOADING = 110;
    private IMPromptDialog IMPromptDialog;
    private IMBuilder IMBuilder;
    private int width;
    private int height;
    private ValueAnimator animator;
    private Paint paint;
    private float density;
    private Rect textRect;
    private int canvasWidth;
    private int canvasHeight;
    private RectF roundRect;
    private int currentType;//当前窗口类型
    private IMPromptButton[] buttons = new IMPromptButton[]{};
    private RectF roundTouchRect;
    float buttonW;
    float buttonH;
    private boolean isSheet=true;
    private float bottomHeight;
    private float sheetHeight;
    private Drawable drawableClose;
    private int transX;
    private int transY;
    private Bitmap adBitmap;

//    private static final int sheetCellHeight = 54;
//    private static final int sheetCellPad = 13;
//    private final int pressAlph = 15;


    public IMPromptView(Context context) {
        super(context);
    }

    public IMPromptView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IMPromptView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public IMPromptView(Activity context, IMBuilder IMBuilder, IMPromptDialog IMPromptDialog) {
        super(context);
        density = getResources().getDisplayMetrics().density;

        this.IMBuilder = IMBuilder;
        this.IMPromptDialog = IMPromptDialog;

    }

    /**
     * 根据原图添加圆角
     *
     * @param source
     * @return
     */
    private Bitmap createRoundConerImage(Bitmap source) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        RectF rect = new RectF(0, 0, source.getWidth(), source.getHeight());
        float mRadius = 50;
        canvas.drawRoundRect(rect, mRadius, mRadius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
//        source.draw(canvas);
        return target;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (paint == null) return;
        if (canvasWidth == 0) {
            canvasWidth = getWidth();
            canvasHeight = getHeight();
        }

        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(IMBuilder.backColor);
        paint.setAlpha(IMBuilder.backAlpha);
        canvas.drawRect(0, 0, canvasWidth, canvasHeight, paint);
        if (currentType == PROMPT_AD) {
            Drawable drawable = getDrawable();
            if (drawable == null) return;
            Rect bound = drawable.getBounds();
            transX = canvasWidth / 2 - bound.width() / 2;
            transY = canvasHeight / 2 - bound.height() / 2 - bound.height() / 10;
            canvas.translate(transX, transY);

            if (adBitmap == null) {
                Bitmap.Config config =
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565;
                Bitmap bitmap = Bitmap.createBitmap(drawable.getMinimumWidth(), drawable.getMinimumHeight(), config);
                Canvas ca = new Canvas(bitmap);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                drawable.draw(ca);
                adBitmap = createRoundConerImage(bitmap);
            }
            canvas.drawBitmap(adBitmap, 0, 0, null);
//            drawable.draw(canvas);
            if (drawableClose == null)
                drawableClose = getResources().getDrawable(R.mipmap.im_ic_prompt_close);
            width = drawableClose.getMinimumWidth() / 2;
            height = drawableClose.getMinimumHeight() / 2;
            int left = bound.width() / 2 - width;
//            int top = canvasHeight - canvasHeight / 2 + bound.height() / 2 + bound.height() / 10 + height * 4;
            int top = bound.height() + height;
            int right = left + width * 2;
            int bottom = top + height * 2;
            drawableClose.setBounds(left, top, right, bottom);
            drawableClose.draw(canvas);
            canvas.save();
            return;
        }
        if (isSheet) {
            String text = IMBuilder.text;
            boolean textNotNull = text != null && text.length() > 0;

            if (roundTouchRect == null)
                roundTouchRect = new RectF();
            roundTouchRect.set(0, canvasHeight - bottomHeight, canvasWidth, canvasHeight);

            canvas.translate(0, canvasHeight - bottomHeight);
//            paint.reset();
//            paint.setAntiAlias(true);
//            paint.setColor(Color.WHITE);
//            canvas.drawRect(0, 0, canvasWidth, canvasHeight - sheetHeight, paint);


            paint.reset();
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            paint.setAlpha(IMBuilder.roundAlpha);
            int sheetCellPad = IMBuilder.sheetCellPad;
            float padBottom = sheetCellPad * density;
            float left = padBottom;
            float top = sheetHeight - padBottom - IMBuilder.sheetCellHeight * density;
            float right = canvasWidth - padBottom;
            float bottom = sheetHeight - padBottom;
            float round = IMBuilder.round * density;
            if (roundRect == null) roundRect = new RectF();
            roundRect.set(left, top, right, bottom);
            canvas.drawRoundRect(roundRect, round, round, paint);

            bottom = top - padBottom / 2;
            top = 0;
            if (textNotNull) {
                paint.reset();
                paint.setColor(IMBuilder.textColor);
                paint.setStrokeWidth(1 * density);
                paint.setTextSize(density * IMBuilder.textSize);
                paint.setAntiAlias(true);
                paint.getTextBounds(text, 0, text.length(), textRect);
                top = -textRect.height() - 1.5f * IMBuilder.sheetCellPad * density;
            }
            paint.reset();
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            paint.setAlpha(IMBuilder.roundAlpha);
            roundRect.set(left, top, right, bottom);
            canvas.drawRoundRect(roundRect, round, round, paint);
//画分割线
            paint.setColor(Color.GRAY);
            paint.setAlpha(100);
            paint.setStrokeWidth(1);
            paint.setAntiAlias(true);
            top = bottom - IMBuilder.sheetCellHeight * density;
            canvas.drawLine(left, top, right, top, paint);
            if (IMBuilder.sheetCellPad == 0) {
                canvas.drawLine(left, bottom, right, bottom, paint);
            }
            if (textNotNull) {
                canvas.drawLine(left, 0, right, 0, paint);
            }

            IMPromptButton button = buttons[0];

            String buttonText = button.getText();
            paint.reset();
            paint.setColor(button.getTextColor());
            paint.setStrokeWidth(1 * density);
            paint.setTextSize(density * button.getTextSize());
            paint.setAntiAlias(true);
            paint.getTextBounds(buttonText, 0, buttonText.length(), textRect);

            bottom = sheetHeight - sheetCellPad * density - IMBuilder.sheetCellHeight * density / 2 + textRect.height() / 2;
            left = canvasWidth / 2 - textRect.width() / 2;
            if (button.getRect() == null)
                button.setTouchRect(new RectF(sheetCellPad * density, canvasHeight -
                        sheetCellPad * density - IMBuilder.sheetCellHeight * density,
                        canvasWidth - sheetCellPad * density, canvasHeight - sheetCellPad * density));
            canvas.drawText(buttonText, left, bottom, paint);
            if (button.isFocus()) {
                paint.reset();
                paint.setAntiAlias(true);
                paint.setColor(Color.BLACK);
                paint.setAlpha(IMBuilder.sheetPressAlph);
                RectF rect = new RectF(sheetCellPad * density, sheetHeight -
                        sheetCellPad * density - IMBuilder.sheetCellHeight * density,
                        canvasWidth - sheetCellPad * density, sheetHeight -
                        sheetCellPad * density);
                canvas.drawRoundRect(rect, round, round, paint);
            }
            //第一个选择
            button = buttons[1];
            buttonText = button.getText();
            paint.reset();
            paint.setColor(button.getTextColor());
            paint.setStrokeWidth(1 * density);
            paint.setTextSize(density * button.getTextSize());
            paint.setAntiAlias(true);
            paint.getTextBounds(buttonText, 0, buttonText.length(), textRect);

            bottom = sheetHeight - 1.5f * sheetCellPad * density -
                    IMBuilder.sheetCellHeight * density * 1.5f + textRect.height() / 2;
            left = canvasWidth / 2 - textRect.width() / 2;
            if (button.getRect() == null)
                button.setTouchRect(new RectF(sheetCellPad * density, canvasHeight - 1.5f * sheetCellPad * density - 2f * IMBuilder.sheetCellHeight * density,
                        canvasWidth - sheetCellPad * density, canvasHeight - 1.5f * sheetCellPad * density - IMBuilder.sheetCellHeight * density));
            canvas.drawText(buttonText, left, bottom, paint);
            if (button.isFocus()) {
                float[] outerR = new float[]{0, 0, 0, 0, round, round, round, round};
                ShapeDrawable mDrawables = new ShapeDrawable(new RoundRectShape(outerR, null, null));
                mDrawables.getPaint().setColor(Color.BLACK);
                mDrawables.getPaint().setAlpha(IMBuilder.sheetPressAlph);
                RectF rect = button.getRect();
                Rect rectPre = new Rect((int) rect.left, (int) (rect.top - canvasHeight + sheetHeight),
                        (int) rect.right, (int) (rect.bottom - canvasHeight + sheetHeight));
                mDrawables.setBounds(rectPre);
                mDrawables.draw(canvas);

            }

            for (int i = 2; i < buttons.length; i++) {
                button = buttons[i];
                buttonText = button.getText();
                paint.reset();
                paint.setColor(button.getTextColor());
                paint.setStrokeWidth(1 * density);
                paint.setTextSize(density * button.getTextSize());
                paint.setAntiAlias(true);
                paint.getTextBounds(buttonText, 0, buttonText.length(), textRect);
                bottom = sheetHeight - 1.5f * sheetCellPad * density - (i + 0.5f) * IMBuilder.sheetCellHeight * density + textRect.height() / 2;
                left = canvasWidth / 2 - textRect.width() / 2;
                if (button.getRect() == null)
                    button.setTouchRect(new RectF(sheetCellPad * density, canvasHeight - 1.5f * sheetCellPad * density - (i + 1f) * IMBuilder.sheetCellHeight * density,
                            canvasWidth - sheetCellPad * density, canvasHeight - 1.5f * sheetCellPad * density - i * IMBuilder.sheetCellHeight * density));
                canvas.drawText(buttonText, left, bottom, paint);

                if (i != buttons.length - 1) {
                    paint.setColor(Color.GRAY);
                    paint.setAlpha(100);
                    paint.setStrokeWidth(1);
                    paint.setAntiAlias(true);

                    top = sheetHeight - 1.5f * padBottom - (i + 1) * IMBuilder.sheetCellHeight * density;

                    canvas.drawLine(padBottom, top, canvasWidth - padBottom, top, paint);

                }

                if (button.isFocus()) {
                    RectF rect = button.getRect();
                    Rect rectPre = new Rect((int) rect.left, (int) (rect.top - canvasHeight + sheetHeight),
                            (int) rect.right, (int) (rect.bottom - canvasHeight + sheetHeight));
                    if (i == buttons.length - 1 && !textNotNull) {

                        float[] outerR = new float[]{round, round, round, round, 0, 0, 0, 0};
                        ShapeDrawable mDrawables = new ShapeDrawable(new RoundRectShape(outerR, null, null));
                        mDrawables.getPaint().setColor(Color.BLACK);
                        mDrawables.getPaint().setAlpha(IMBuilder.sheetPressAlph);
                        mDrawables.setBounds(rectPre);
                        mDrawables.draw(canvas);

                    } else {
                        paint.reset();
                        paint.setAntiAlias(true);
                        paint.setColor(Color.BLACK);
                        paint.setAlpha(IMBuilder.sheetPressAlph);
                        canvas.drawRect(rectPre, paint);
                    }
                }
            }

            if (textNotNull) {
                paint.reset();
                paint.setColor(IMBuilder.textColor);
                paint.setStrokeWidth(1 * density);
                paint.setTextSize(density * IMBuilder.textSize);
                paint.setAntiAlias(true);
                paint.getTextBounds(text, 0, text.length(), textRect);
//文字背景
//                float[] outerR = new float[]{round, round, round, round, 0, 0, 0, 0};
//                ShapeDrawable mDrawables = new ShapeDrawable(new RoundRectShape(outerR, null, null));
//                mDrawables.getPaint().setColor(Color.WHITE);
                top = -textRect.height() - 1.5f * IMBuilder.sheetCellPad * density;
//                Rect rectPre = new Rect((int) (IMBuilder.sheetCellPad * density), (int) top,
//                        (int) (canvasWidth - IMBuilder.sheetCellPad * density), 0);
//                mDrawables.setBounds(rectPre);
//                mDrawables.draw(canvas);

                canvas.drawText(text, canvasWidth / 2 - textRect.width() / 2, top / 2 + textRect.height() / 2, paint);

            }

            return;
        }


        String text = IMBuilder.text;
        float pad = IMBuilder.padding * density;
        float round = IMBuilder.round * density;
        paint.reset();
        paint.setColor(IMBuilder.textColor);
        paint.setStrokeWidth(1 * density);
        paint.setTextSize(density * IMBuilder.textSize);
        paint.setAntiAlias(true);
        paint.getTextBounds(text, 0, text.length(), textRect);
//        paint.getTextBounds(text, 0, text.length(), textRect);
        float popWidth = 0;
        float popHeight = 0;

        switch (currentType) {
            case PROMPT_ALERT_WARN:

                popWidth = Math.max(textRect.width() + pad * 2, 2 * buttonW);
                if (buttonW * 2 < textRect.width() + pad * 2) {
                    buttonW = (textRect.width() + pad * 2) / 2;
                }

                popHeight = textRect.height() + 3 * pad + height * 2 + buttonH;
                break;
            default:
                popWidth = Math.max(100 * density, textRect.width() + pad * 2);
                popHeight = textRect.height() + 3 * pad + height * 2;
                break;
        }


        float transTop = canvasHeight / 2 - popHeight / 2;
        float transLeft = canvasWidth / 2 - popWidth / 2;

        canvas.translate(transLeft, transTop);


        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(IMBuilder.roundColor);
        paint.setAlpha(IMBuilder.roundAlpha);
        if (roundTouchRect == null)
            roundTouchRect = new RectF();
        roundTouchRect.set(transLeft, transTop, transLeft + popWidth, transTop + popHeight);
        if (roundRect == null)
            roundRect = new RectF(0, 0, popWidth, popHeight);
        roundRect.set(0, 0, popWidth, popHeight);
        canvas.drawRoundRect(roundRect, round, round, paint);


        paint.reset();
        paint.setColor(IMBuilder.textColor);
        paint.setStrokeWidth(1 * density);
        paint.setTextSize(density * IMBuilder.textSize);
        paint.setAntiAlias(true);

        float top = pad * 2 + height * 2 + textRect.height();
        float left = popWidth / 2 - textRect.width() / 2;

//        canvas.save();
//        canvas.translate(left, top);
//        layout.draw(canvas);
//        canvas.restore();//别忘了restore
        canvas.drawText(text, left, top, paint);


        if (currentType == PROMPT_ALERT_WARN) {
            top = top + pad;
            paint.setColor(Color.GRAY);
            paint.setStrokeWidth(1);
            paint.setAntiAlias(true);
            canvas.drawLine(0, top, popWidth, top, paint);
            if (buttons.length == 1) {
                IMPromptButton button = buttons[0];
                if (button.isFocus()) {
                    paint.reset();
                    paint.setAntiAlias(true);
                    paint.setColor(button.getFocusBacColor());
                    paint.setStyle(Paint.Style.FILL);

                    canvas.drawRect(0, top, popWidth, top + buttonH - round, paint);
                    canvas.drawCircle(round, top + buttonH - round, round, paint);
                    canvas.drawCircle(popWidth - round, top + buttonH - round, round, paint);
                    canvas.drawRect(round, top + buttonH - round, popWidth - round, top + buttonH, paint);

                }

                String buttonText = button.getText();
                paint.reset();
                paint.setColor(button.getTextColor());
                paint.setStrokeWidth(1 * density);
                paint.setTextSize(density * button.getTextSize());
                paint.setAntiAlias(true);
                paint.getTextBounds(buttonText, 0, buttonText.length(), textRect);

                button.setTouchRect(new RectF(transLeft, transTop + top,
                        transLeft + popWidth, transTop + top + buttonH));
                canvas.drawText(buttonText, popWidth / 2 - textRect.width() / 2,
                        top + textRect.height() / 2 + buttonH / 2, paint);
            }
            if (buttons.length > 1) {

                canvas.drawLine(popWidth / 2, top, popWidth / 2, popHeight, paint);

                for (int i = 0; i < buttons.length; i++) {
                    IMPromptButton button = buttons[i];
                    if (button.isFocus()) {
                        paint.reset();
                        paint.setAntiAlias(true);
                        paint.setColor(button.getFocusBacColor());
                        paint.setStyle(Paint.Style.FILL);
//                        paint.setAlpha(120);
                        canvas.drawRect(buttonW * i, top + 1, buttonW * (i + 1), top + 1 + buttonH - round, paint);
                        if (i == 0) {
                            canvas.drawCircle(round, top + buttonH - round, round, paint);
                            canvas.drawRect(round, top + buttonH - round, buttonW * (i + 1), top + buttonH, paint);
                        } else if (i == 1) {
                            canvas.drawCircle(buttonW * 2 - round, top + buttonH - round, round, paint);
                            canvas.drawRect(buttonW, top + buttonH - round, buttonW * 2 - round, top + buttonH, paint);
                        }
                    }
                    String buttonText = button.getText();
                    paint.reset();
                    paint.setColor(button.getTextColor());
                    paint.setStrokeWidth(1 * density);
                    paint.setTextSize(density * button.getTextSize());
                    paint.setAntiAlias(true);
                    paint.getTextBounds(buttonText, 0, buttonText.length(), textRect);

                    button.setTouchRect(new RectF(transLeft + i * buttonW, transTop + top,
                            transLeft + i * buttonW + buttonW, transTop + top + buttonH));
                    canvas.drawText(buttonText, buttonW / 2 - textRect.width() / 2 + i * buttonW,
                            top + textRect.height() / 2 + buttonH / 2, paint);

                }

            }

        }
        canvas.translate(popWidth / 2 - width, pad);
        super.onDraw(canvas);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setScaleType(ScaleType.MATRIX);

        if (paint == null)
            paint = new Paint();
        initData();
    }

    private void initData() {
        if (textRect == null)
            textRect = new Rect();
        if (roundRect == null)
            roundTouchRect = new RectF();

        buttonW = density * 120;
        buttonH = density * 44;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (adBitmap != null) {
            adBitmap.recycle();
        }
        adBitmap = null;

        if (animator != null)
            animator.cancel();
        animator = null;
        buttons = null;
//        textRect = null;
//        roundTouchRect = null;
        IMPromptDialog.onDetach();
//        IMPromptDialog = null;
        currentType = PROMPT_NONE;

    }


    private Matrix max;

    /**
     * loading start
     */
    private void start() {
        if (max == null || animator == null) {
            max = new Matrix();
            animator = ValueAnimator.ofInt(0, 12);
            animator.setDuration(12 * 80);
            animator.setInterpolator(new LinearInterpolator());
            animator.setRepeatCount(Animation.INFINITE);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float degrees = 30 * (Integer) valueAnimator.getAnimatedValue();
                    max.setRotate(degrees, width, height);
                    setImageMatrix(max);
                }
            });
        }
        if (!animator.isRunning())
            animator.start();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (currentType == PROMPT_ALERT_WARN) {

            if (IMBuilder.cancleAble && event.getAction() == MotionEvent.ACTION_UP && !roundTouchRect.contains(x, y)) {
                IMPromptDialog.dismiss();
            }
            for (final IMPromptButton button : buttons) {
                if (button.getRect() != null && button.getRect().contains(x, y)) {


                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        button.setFocus(true);
                        invalidate();
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        button.setFocus(false);
                        invalidate();
                        if (button.isDismissAfterClick())
                            IMPromptDialog.dismiss();
                        if (button.getListener() != null) {
                            if (button.isDelyClick()) {
                                postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        button.getListener().onClick(button);
                                    }
                                }, IMPromptDialog.viewAnimDuration + 100);
                            } else {
                                button.getListener().onClick(button);
                            }

                        }

                    }
                    return true;
                }
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                for (IMPromptButton button : buttons) {
                    button.setFocus(false);
                    invalidate();
                }
            }
        } else if (currentType == PROMPT_AD) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if ((drawableClose != null && drawableClose.getBounds()
                        .contains((int) event.getX() - transX, (int) event.getY() - transY)) || IMBuilder.cancleAble) {
                    IMPromptDialog.dismiss();
                } else if (getDrawable() !=
                        null && getDrawable().getBounds().contains((int) event.getX() - transX, (int) event.getY() - transY)) {
                    IMPromptDialog.onAdClick();
                    IMPromptDialog.dismiss();
                }

            }
        }
        return !IMBuilder.touchAble;
    }


    /**
     * 停止旋转
     */

    private void endAnimator() {
        if (animator != null && animator.isRunning()) {
            animator.end();
        }
    }

    /**
     *
     */
    public void showLoading() {
        if (currentType == PROMPT_ALERT_WARN) {
            isSheet = true;
        } else {
            isSheet = true;
        }
        setImageDrawable(getResources().getDrawable(IMBuilder.icon));
        width = getDrawable().getMinimumWidth() / 2;
        height = getDrawable().getMinimumHeight() / 2;
        start();
        currentType = PROMPT_LOADING;

    }

    IMBuilder getIMBuilder() {
        return IMBuilder;
    }

    public void showSomthing(int currentType) {
        this.currentType = currentType;
        if (currentType == PROMPT_ALERT_WARN) {
            isSheet =true;
        } else {
            isSheet = true;
        }
        endAnimator();
        setImageDrawable(getResources().getDrawable(IMBuilder.icon));
        width = getDrawable().getMinimumWidth() / 2;
        height = getDrawable().getMinimumHeight() / 2;

        if (max != null) {
            max.setRotate(0, width, height);
            setImageMatrix(max);
        }

        if (isSheet) {
            //计算高度
            sheetHeight = (1.5f * IMBuilder.sheetCellPad + IMBuilder.sheetCellHeight * buttons.length) * density;
            Log.i(TAG, "showSomthing: " + sheetHeight);
            startBottomToTopAnim();
        }
        invalidate();
    }

    /**
     * 底部Sheet
     */
    private void startBottomToTopAnim() {
        ValueAnimator bottomToTopAnim = ObjectAnimator.ofFloat(0, 1);
//        bottomToTopAnim.setStartDelay(100);
        bottomToTopAnim.setDuration(300);
        bottomToTopAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Float value = (Float) valueAnimator.getAnimatedValue();
                bottomHeight = sheetHeight * value;
                Log.i(TAG, "onAnimationUpdate: " + bottomHeight);
                invalidate();
            }
        });
        bottomToTopAnim.start();
    }


    void showSomthingAlert(IMPromptButton... button) {
        this.buttons = button;
        showSomthing(PROMPT_ALERT_WARN);

    }

    public void setIMBuilder(IMBuilder IMBuilder) {
        if (this.IMBuilder != IMBuilder)
            this.IMBuilder = IMBuilder;
    }

    public int getCurrentType() {
        return currentType;
    }

    public void setText(String msg) {
        IMBuilder.text(msg);
        invalidate();
    }

    /**
     * 底部Sheet 退出动画
     */
    public void dismiss() {
//        currentType = PROMPT_NONE;
        if (isSheet) {

            ValueAnimator bottomToTopAnim = ObjectAnimator.ofFloat(1, 0);
            bottomToTopAnim.setDuration(300);
            bottomToTopAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    Float value = (Float) valueAnimator.getAnimatedValue();
                    bottomHeight = sheetHeight * value;
                    invalidate();
                }
            });
            bottomToTopAnim.start();
        }

    }

    public void showAd() {
        this.currentType = PROMPT_AD;
        endAnimator();
    }

    /**
     * 展示自定义的loading
     */
    public void showCustomLoading() {
        if (currentType == PROMPT_ALERT_WARN) {
            isSheet =true;
        } else {
            isSheet = true;
        }
        setImageDrawable(getResources().getDrawable(IMBuilder.icon));
        width = getDrawable().getMinimumWidth() / 2;
        height = getDrawable().getMinimumHeight() / 2;
        AnimationDrawable animationDrawable = (AnimationDrawable) getDrawable();
        animationDrawable.start();
        currentType = CUSTOMER_LOADING;
    }

    /**
     * 停止自定义的loading
     */
    public void stopCustomerLoading() {
        AnimationDrawable animationDrawable = (AnimationDrawable) getDrawable();
        animationDrawable.stop();
    }
}