package com.android.nettylibrary.bean;

import com.android.nettylibrary.utils.IMStringUtil;

public class IMSingleMessage extends IMMessageBean implements Cloneable {


    @Override
    public int hashCode() {
        try {
            return this.getFingerprint().hashCode();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof IMSingleMessage)) {
            return false;
        }

        return IMStringUtil.equals(this.getFingerprint(), ((IMSingleMessage) obj).getFingerprint());
}
}
