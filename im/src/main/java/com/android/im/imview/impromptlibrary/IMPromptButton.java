package com.android.im.imview.impromptlibrary;

import android.graphics.Color;
import android.graphics.RectF;

/**
 * Created by FengTing on 2017/5/8.
 * https://www.github.com/limxing
 */

public class IMPromptButton {
    private  boolean isDelyClick;
    private String text = "confirm";
    private boolean focus;
    private int textColor = Color.BLACK;
    private float textSize = 15;
    private RectF rect;
    private IMPromptButtonListener listener;
    private int focusBacColor= Color.parseColor("#DCDCDC");

    private boolean dismissAfterClick=true;

    public IMPromptButton(String text, IMPromptButtonListener listener) {
        this.text = text;
        this.listener = listener;
    }
    public IMPromptButton(String text, IMPromptButtonListener listener, boolean delayClick) {
        this.text = text;
        this.listener = listener;
        this.isDelyClick=delayClick;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTextColor() {
        return textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTouchRect(RectF rectF) {
        this.rect = rectF;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public RectF getRect() {
        return rect;
    }

    public void setRect(RectF rect) {
        this.rect = rect;
    }

    public IMPromptButtonListener getListener() {
        return listener;
    }

    public void setListener(IMPromptButtonListener listener) {
        this.listener = listener;
    }

    public int getFocusBacColor() {
        return focusBacColor;
    }

    public void setFocusBacColor(int focusBacColor) {
        this.focusBacColor = focusBacColor;
    }

    public boolean isDismissAfterClick() {
        return dismissAfterClick;
    }

    public void setDismissAfterClick(boolean dismissAfterClick) {
        this.dismissAfterClick = dismissAfterClick;
    }

    public boolean isDelyClick() {
        return isDelyClick;
    }

    public void setDelyClick(boolean delyClick) {
        isDelyClick = delyClick;
    }

}
