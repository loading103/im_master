package com.android.im.imview.impromptlibrary;

import android.graphics.Color;

/**
 * Created by FengTing on 2017/5/8.
 * https://www.github.com/limxing
 */

public class IMBuilder {
    private static IMBuilder defaultIMBuilder;
    private static IMBuilder alertDefaultIMBuilder;
    int backColor = Color.BLACK;
    int backAlpha = 90;
    int textColor = Color.WHITE;
    float textSize = 14;
    float padding = 15;
    float round = 8;
    int roundColor = Color.BLACK;
    int roundAlpha = 120;
    boolean touchAble = false;
    boolean withAnim = true;//
    long stayDuration = 1000;
    boolean cancleAble;
    int icon;
    String text;
    long loadingDuration;

    int sheetPressAlph=15;
    int sheetCellHeight=54;
    int sheetCellPad=13;

    public IMBuilder sheetCellPad(int pad){
        this.sheetCellPad=pad;
        return this;
    }

    public IMBuilder sheetCellHeight(int height){
        this.sheetCellHeight=height;
        return this;
    }


    public IMBuilder sheetPressAlph(int alpha){
        this.sheetPressAlph=alpha;
        return this;
    }

    public IMBuilder backColor(int backColor) {
        this.backColor = backColor;
        return this;
    }

    public IMBuilder backAlpha(int backAlpha) {
        this.backAlpha = backAlpha;
        return this;
    }

    public IMBuilder textColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    public IMBuilder textSize(float textSize) {
        this.textSize = textSize;
        return this;
    }

    public IMBuilder padding(float padding) {
        this.padding = padding;
        return this;
    }

    public IMBuilder round(float round) {
        this.round = round;
        return this;
    }

    public IMBuilder roundColor(int roundColor) {
        this.roundColor = roundColor;
        return this;
    }

    public IMBuilder roundAlpha(int roundAlpha) {
        this.roundAlpha = roundAlpha;
        return this;
    }

    public IMBuilder touchAble(boolean touchAble) {
        this.touchAble = touchAble;
        return this;
    }

    public IMBuilder withAnim(boolean withAnim) {
        this.withAnim = withAnim;
        return this;
    }

    public IMBuilder stayDuration(long time) {
        this.stayDuration = time;
        return this;
    }

    public IMBuilder cancleAble(boolean time) {
        this.cancleAble = time;
        return this;
    }

    public IMBuilder() {
    }

    /**
     * @return
     */
    static IMBuilder getDefaultIMBuilder() {
        if (defaultIMBuilder == null)
            defaultIMBuilder = new IMBuilder();
        return defaultIMBuilder;
    }

    static IMBuilder getAlertDefaultIMBuilder() {
        if (alertDefaultIMBuilder == null)
            alertDefaultIMBuilder = new IMBuilder().roundColor(Color.WHITE).roundAlpha(255).
                    textColor(Color.GRAY).textSize(15).cancleAble(true);
        return alertDefaultIMBuilder;
    }

    public IMBuilder icon(int icon) {
        this.icon = icon;
        return this;
    }

    public IMBuilder text(String msg) {
        this.text = msg;
        return this;
    }

    public IMBuilder loadingDuration(long duration) {
        this.loadingDuration = duration;
        return this;
    }
}
