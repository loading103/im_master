package com.android.im.imbean;
//SecurityToken	Android/iOS移动应用初始化的Token。	是	[string]	查看
//2	AccessKeyId	Android/iOS移动应用初始化OSSClient获取的 AccessKeyId。	是	[string]	查看
//3	AccessKeySecret	Android/iOS移动应用初始化OSSClient获取AccessKeySecret。	是	[string]	查看
//4	Expiration	该Token失效的时间。Android SDK会自动判断Token是否失效，如果失效，则自动获取Token	是	[string]	查看
//5	ErrorCode	错误编码	是	[string]
//6	ErrorMessage	错误详情	是	[string]
//7	StatusCode
public class IMAliyunTokenBean {
    private String SecurityToken;       //Android/iOS移动应用初始化的Token。
    private String AccessKeyId;         //广告图片地址
    private String AccessKeySecret;
    private String Expiration;   //该Token失效的时间。Android SDK会自动判断Token是否失效，如果失效，则自动获取Token
    private String ErrorCode;
    private String ErrorMessage;
    private String StatusCode;

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }

    public String getSecurityToken() {
        return SecurityToken;
    }

    public void setSecurityToken(String securityToken) {
        SecurityToken = securityToken;
    }

    public String getAccessKeyId() {
        return AccessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        AccessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return AccessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        AccessKeySecret = accessKeySecret;
    }

    public String getExpiration() {
        return Expiration;
    }

    public void setExpiration(String expiration) {
        Expiration = expiration;
    }

}