package com.android.im.imbean;

public class IMSingleRedBackBean {
    private IMSinglePacket redPacket;
    private boolean isAudit;

    public IMSinglePacket getRedPacket() {
        return redPacket;
    }

    public void setRedPacket(IMSinglePacket redPacket) {
        this.redPacket = redPacket;
    }

    public boolean isAudit() {
        return isAudit;
    }

    public void setAudit(boolean audit) {
        isAudit = audit;
    }

    public class IMSinglePacket {
        private String desc;
        private String metas;
        private String redPacketId;
        private String style;
        private String title;
        private String type;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getMetas() {
            return metas;
        }

        public void setMetas(String metas) {
            this.metas = metas;
        }

        public String getRedPacketId() {
            return redPacketId;
        }

        public void setRedPacketId(String redPacketId) {
            this.redPacketId = redPacketId;
        }

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
