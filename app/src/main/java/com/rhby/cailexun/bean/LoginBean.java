package com.rhby.cailexun.bean;

public class LoginBean {
    private String token;
    private String aliyunOssEndpoint;
    private String aliyunOssRegion;
    private String aliyunOssCallbackUrl;
    private String aliyunOssDomain;
    private String aliyunOssBucketName;
    private String aliyunStsServer;

    public String getAliyunStsServer() {
        return aliyunStsServer;
    }

    public void setAliyunStsServer(String aliyunStsServer) {
        this.aliyunStsServer = aliyunStsServer;
    }

    public String getAliyunOssEndpoint() {
        return aliyunOssEndpoint;
    }

    public void setAliyunOssEndpoint(String aliyunOssEndpoint) {
        this.aliyunOssEndpoint = aliyunOssEndpoint;
    }

    public String getAliyunOssRegion() {
        return aliyunOssRegion;
    }

    public void setAliyunOssRegion(String aliyunOssRegion) {
        this.aliyunOssRegion = aliyunOssRegion;
    }

    public String getAliyunOssCallbackUrl() {
        return aliyunOssCallbackUrl;
    }

    public void setAliyunOssCallbackUrl(String aliyunOssCallbackUrl) {
        this.aliyunOssCallbackUrl = aliyunOssCallbackUrl;
    }

    public String getAliyunOssDomain() {
        return aliyunOssDomain;
    }

    public void setAliyunOssDomain(String aliyunOssDomain) {
        this.aliyunOssDomain = aliyunOssDomain;
    }

    public String getAliyunOssBucketName() {
        return aliyunOssBucketName;
    }

    public void setAliyunOssBucketName(String aliyunOssBucketName) {
        this.aliyunOssBucketName = aliyunOssBucketName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
