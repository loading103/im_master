/*
 * 文件名：SSLConfig.java
 * 版权：Copyright by www.poly.com
 * 描述：
 * 修改人：gogym
 * 修改时间：2019年7月13日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.android.nettylibrary.ssl;
 
public class SSLConfig {
 
	// 是否开启ssl
	private boolean isSSL = true;
	// 是否开启双向验证
	private boolean needClientAuth = true;
	// 密匙库地址
	private String pkPath = "client.bks";
	// 签名证书地址  信任库
	private String caPath = "root.bks";
	// 证书密码
	private String passwd = "123456";
 
	public SSLConfig isSSL(boolean isSSL) {
		this.isSSL = isSSL;
		return this;
	}
 
	public SSLConfig needClientAuth(boolean needClientAuth) {
		this.needClientAuth = needClientAuth;
		return this;
	}
 
	public SSLConfig pkPath(String pkPath) {
		this.pkPath = pkPath;
		return this;
	}
 
	public SSLConfig caPath(String caPath) {
		this.caPath = caPath;
		return this;
	}
 
	public SSLConfig passwd(String passwd) {
		this.passwd = passwd;
		return this;
	}
 
	public boolean isSSL() {
 
		return isSSL;
	}
 
	public boolean isNeedClientAuth() {
 
		return needClientAuth;
	}
 
	public String getPkPath() {
 
		return pkPath;
	}
 
	public String getCaPath() {
 
		return caPath;
	}
 
	public String getPasswd() {
 
		return passwd;
	}
 
}