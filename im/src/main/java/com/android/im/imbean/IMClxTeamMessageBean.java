package com.android.im.imbean;

public class IMClxTeamMessageBean {

    private IMClxTeamMap meta;
    private String sysCode;
    private String text;
    private String type;

    public IMClxTeamMap getMeta() {
        return meta;
    }

    public void setMeta(IMClxTeamMap meta) {
        this.meta = meta;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public static class IMClxTeamMap {
        private IMClxTeamMessageDetailkBean map;

        public IMClxTeamMessageDetailkBean getMap() {
            return map;
        }

        public void setMap(IMClxTeamMessageDetailkBean map) {
            this.map = map;
        }

        @Override
        public String toString() {
            return "IMClxTeamMap{" +
                    "map=" + map.toString() +
                    '}';
        }
    }

    public static class IMClxTeamMessageDetailkBean {
        private String channelCode;
        private String customerId;
        private String msgType;
        private String msgContent;
        private String msgTime;
        private String msgTitle;
        private IMClxParamBean msgParam;
        private String isIllegal;
        private int penalizeMethod;
        private String username;

        public String getIsIllegal() {
            return isIllegal;
        }

        public void setIsIllegal(String isIllegal) {
            this.isIllegal = isIllegal;
        }

        public Integer getPenalizeMethod() {
            return penalizeMethod;
        }

        public void setPenalizeMethod(Integer penalizeMethod) {
            this.penalizeMethod = penalizeMethod;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        @Override
        public String toString() {
            return "IMClxTeamMessageDetailkBean{" +
                    "channelCode='" + channelCode + '\'' +
                    ", customerId='" + customerId + '\'' +
                    ", msgType='" + msgType + '\'' +
                    ", msgContent='" + msgContent + '\'' +
                    ", msgTime='" + msgTime + '\'' +
                    ", msgTitle='" + msgTitle + '\'' +
                    ", msgParam=" + msgParam.toString() +
                    '}';
        }

        public IMClxParamBean getMsgParam() {
            return msgParam;
        }

        public void setMsgParam(IMClxParamBean msgParam) {
            this.msgParam = msgParam;
        }

        public String getChannelCode() {
            return channelCode;
        }

        public void setChannelCode(String channelCode) {
            this.channelCode = channelCode;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getMsgType() {
            return msgType;
        }

        public void setMsgType(String msgType) {
            this.msgType = msgType;
        }

        public String getMsgContent() {
            return msgContent;
        }

        public void setMsgContent(String msgContent) {
            this.msgContent = msgContent;
        }

        public String getMsgTime() {
            return msgTime;
        }

        public void setMsgTime(String msgTime) {
            this.msgTime = msgTime;
        }

        public String getMsgTitle() {
            return msgTitle;
        }

        public void setMsgTitle(String msgTitle) {
            this.msgTitle = msgTitle;
        }

        public static class IMClxParamBean {
            private String isIllegal;
            private String penalizeMethod;
            private String complaintContent;
            private String auditDescription;
            private String complaintTime;
            private String gmtCreate;
            private String complaintCustomerId;
            private String complaintStatus;
            private String complaintId;
            private String auditTime;
            private String auditManagerId;
            private String auditManagerName;

            private String complaintType;
            private String gmtUpdate;
            private String complaintImages;
            private String respondentsObjId;
            private String logicalDeletion;

            @Override
            public String toString() {
                return "IMClxParamBean{" +
                        "isIllegal='" + isIllegal + '\'' +
                        ", penalizeMethod='" + penalizeMethod + '\'' +
                        ", complaintContent='" + complaintContent + '\'' +
                        ", auditDescription='" + auditDescription + '\'' +
                        ", complaintTime='" + complaintTime + '\'' +
                        ", gmtCreate='" + gmtCreate + '\'' +
                        ", complaintCustomerId='" + complaintCustomerId + '\'' +
                        ", complaintStatus='" + complaintStatus + '\'' +
                        ", complaintId='" + complaintId + '\'' +
                        ", auditTime='" + auditTime + '\'' +
                        ", auditManagerId='" + auditManagerId + '\'' +
                        ", auditManagerName='" + auditManagerName + '\'' +
                        ", complaintType='" + complaintType + '\'' +
                        ", gmtUpdate='" + gmtUpdate + '\'' +
                        ", complaintImages='" + complaintImages + '\'' +
                        ", respondentsObjId='" + respondentsObjId + '\'' +
                        ", logicalDeletion='" + logicalDeletion + '\'' +
                        '}';
            }

            public String getIsIllegal() {
                return isIllegal;
            }

            public void setIsIllegal(String isIllegal) {
                this.isIllegal = isIllegal;
            }

            public String getPenalizeMethod() {
                return penalizeMethod;
            }

            public void setPenalizeMethod(String penalizeMethod) {
                this.penalizeMethod = penalizeMethod;
            }

            public String getComplaintContent() {
                return complaintContent;
            }

            public void setComplaintContent(String complaintContent) {
                this.complaintContent = complaintContent;
            }

            public String getAuditDescription() {
                return auditDescription;
            }

            public void setAuditDescription(String auditDescription) {
                this.auditDescription = auditDescription;
            }

            public String getComplaintTime() {
                return complaintTime;
            }

            public void setComplaintTime(String complaintTime) {
                this.complaintTime = complaintTime;
            }

            public String getGmtCreate() {
                return gmtCreate;
            }

            public void setGmtCreate(String gmtCreate) {
                this.gmtCreate = gmtCreate;
            }

            public String getComplaintCustomerId() {
                return complaintCustomerId;
            }

            public void setComplaintCustomerId(String complaintCustomerId) {
                this.complaintCustomerId = complaintCustomerId;
            }

            public String getComplaintStatus() {
                return complaintStatus;
            }

            public void setComplaintStatus(String complaintStatus) {
                this.complaintStatus = complaintStatus;
            }

            public String getComplaintId() {
                return complaintId;
            }

            public void setComplaintId(String complaintId) {
                this.complaintId = complaintId;
            }

            public String getAuditTime() {
                return auditTime;
            }

            public void setAuditTime(String auditTime) {
                this.auditTime = auditTime;
            }

            public String getAuditManagerId() {
                return auditManagerId;
            }

            public void setAuditManagerId(String auditManagerId) {
                this.auditManagerId = auditManagerId;
            }

            public String getAuditManagerName() {
                return auditManagerName;
            }

            public void setAuditManagerName(String auditManagerName) {
                this.auditManagerName = auditManagerName;
            }

            public String getComplaintType() {
                return complaintType;
            }

            public void setComplaintType(String complaintType) {
                this.complaintType = complaintType;
            }

            public String getGmtUpdate() {
                return gmtUpdate;
            }

            public void setGmtUpdate(String gmtUpdate) {
                this.gmtUpdate = gmtUpdate;
            }

            public String getComplaintImages() {
                return complaintImages;
            }

            public void setComplaintImages(String complaintImages) {
                this.complaintImages = complaintImages;
            }

            public String getRespondentsObjId() {
                return respondentsObjId;
            }

            public void setRespondentsObjId(String respondentsObjId) {
                this.respondentsObjId = respondentsObjId;
            }

            public String getLogicalDeletion() {
                return logicalDeletion;
            }

            public void setLogicalDeletion(String logicalDeletion) {
                this.logicalDeletion = logicalDeletion;
            }
        }

    }

    @Override
    public String toString() {
        return "IMClxTeamMessageBean{" +
                "meta=" + meta.toString() +
                ", sysCode='" + sysCode + '\'' +
                ", text='" + text + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}