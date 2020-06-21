package com.android.im.imeventbus;


public class IMDeleteShowEvent {
    private boolean isShow;
    private boolean isCanDelete;

    public IMDeleteShowEvent() {

    }

    public IMDeleteShowEvent(boolean isShow) {
        this.isShow = isShow;
    }

    public boolean isCanDelete() {
        return isCanDelete;
    }

    public void setCanDelete(boolean canDelete) {
        isCanDelete = canDelete;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
