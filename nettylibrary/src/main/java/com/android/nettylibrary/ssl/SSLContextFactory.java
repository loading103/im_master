/*
 * 文件名：SSLContextFactory.java
 * 版权：Copyright by www.poly.com
 * 描述：
 * 修改人：gogym
 * 修改时间：2019年7月13日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.android.nettylibrary.ssl;

import android.content.Context;

import com.android.nettylibrary.IMSNettyManager;
import com.android.nettylibrary.R;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;

import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.SslProvider;

/**
 * 
 * 〈一句话功能简述〉 〈功能详细描述〉
 * 
 * @author gogym
 * @version 2019年7月13日
 * @see SSLContextFactory
 * @since
 */
public class SSLContextFactory {
 
	private static final String PROTOCOL = "TLS";
 
	private static SSLContext SERVER_CONTEXT;// 服务器安全套接字协议
 
	private static SslContext openSslContext;
 
	private static SSLContext CLIENT_CONTEXT;// 客户端安全套接字协议
 
	private static SslContext openSslClientContext;
 


	public static SSLContext getClientContext(String pkPath, String passwd) {
		if (CLIENT_CONTEXT != null)
			return CLIENT_CONTEXT;

		InputStream tIN = null;
		try {
			// 信任库
			TrustManagerFactory tf = null;
			if (pkPath != null) {
				// 密钥库KeyStore
				KeyStore tks = KeyStore.getInstance("BKS");
				// 加载客户端证书
				tIN = IMSNettyManager.getInstance().getContext().getAssets().open(pkPath);
				tks.load(tIN, passwd.toCharArray());
				tf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
				// 初始化信任库
				tf.init(tks);
			}

			CLIENT_CONTEXT = SSLContext.getInstance(PROTOCOL);
			// 设置信任证书
			CLIENT_CONTEXT.init(null,
					tf == null ? null : tf.getTrustManagers(), null);

		} catch (Exception e) {
			throw new Error("Failed to initialize the client-side SSLContext");
		} finally {
			if (tIN != null) {
				try {
					tIN.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return CLIENT_CONTEXT;
	}
	public static SslContext getOpenSslClientContext(String pkPath,
			String passwd) {

		if (openSslClientContext != null) {
			return openSslClientContext;
		}

		InputStream tIN = null;
		try {

			// 信任库
			TrustManagerFactory tf = null;
			if (pkPath != null) {
				// 密钥库KeyStore
				KeyStore tks = KeyStore.getInstance("BKS");
				// 加载客户端证书
				tIN = IMSNettyManager.getInstance().getContext().getAssets().open(pkPath);
				tks.load(tIN, passwd.toCharArray());
				tf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
				// 初始化信任库
				tf.init(tks);
			}
			openSslClientContext = SslContextBuilder.forClient()
					.sslProvider(SslProvider.OPENSSL).trustManager(tf).build();

			return openSslClientContext;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (tIN != null) {
				try {
					tIN.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				tIN = null;
			}

		}

		return null;

	}


	public static SSLContext getClientContext(String pkPath, String caPath,
			String passwd) {
		if (CLIENT_CONTEXT != null)
			return CLIENT_CONTEXT;

		InputStream in = null;
		InputStream tIN = null;
		try {
			KeyManagerFactory kmf = null;
			if (pkPath != null) {
				KeyStore ks = KeyStore.getInstance("BKS");
				in = IMSNettyManager.getInstance().getContext().getAssets().open(pkPath);
				ks.load(in, passwd.toCharArray());
				kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
				kmf.init(ks, passwd.toCharArray());
			}

			TrustManagerFactory tf = null;
			if (caPath != null) {
				KeyStore tks = KeyStore.getInstance("BKS");
				tIN = IMSNettyManager.getInstance().getContext().getAssets().open(caPath);
				tks.load(tIN, passwd.toCharArray());
				tf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
				tf.init(tks);
			}

			CLIENT_CONTEXT = SSLContext.getInstance(PROTOCOL);
			// 初始化此上下文
			// 参数一：认证的密钥 参数二：对等信任认证 参数三：伪随机数生成器 。 由于单向认证，服务端不用验证客户端，所以第二个参数为null
			CLIENT_CONTEXT.init(kmf.getKeyManagers(), tf.getTrustManagers(), null);

		} catch (Exception e) {
			throw new Error("Failed to initialize the client-side SSLContext");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				in = null;
			}

			if (tIN != null) {
				try {
					tIN.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				tIN = null;
			}
		}

		return CLIENT_CONTEXT;
	}

	public static SslContext getOpenSslClientContext(String pkPath, String caPath, String passwd) {
		if (openSslClientContext != null) {
			return openSslClientContext;
		}
		InputStream in = null;
		InputStream tIN = null;
		try {
			// 密钥管理器
			KeyManagerFactory kmf = null;
			if (pkPath != null) {
				// 密钥库KeyStore
				KeyStore ks = KeyStore.getInstance("BKS");
				// 加载服务端证书
				in = IMSNettyManager.getInstance().getContext().getAssets().open(pkPath);
				// 加载服务端的KeyStore ；sNetty是生成仓库时设置的密码，用于检查密钥库完整性的密码
				ks.load(in, passwd.toCharArray());
				kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
				// 初始化密钥管理器
				kmf.init(ks, passwd.toCharArray());
			}
			// 信任库
			TrustManagerFactory  tf = null;
			if (caPath != null) {
				KeyStore tks = KeyStore.getInstance("BKS");
				tIN = IMSNettyManager.getInstance().getContext().getAssets().open(caPath);
				tks.load(tIN, passwd.toCharArray());

				tf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
				tf.init(tks);
			}
			return SslContextBuilder
					.forClient()
					.keyManager(kmf)
					.trustManager(tf)
					.sslProvider(SslProvider.OPENSSL) //同样也要加入相应jar包
				.build();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				in = null;
			}
			if (tIN != null) {
				try {
					tIN.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				tIN = null;
			}
		}
		return null;
	}
	public static SSLEngine getSslClientEngine(SSLConfig sslConfig) {
 
		SSLEngine sslEngine = null;
		if (sslConfig.isNeedClientAuth()) {
//			sslEngine = getClientContext(sslConfig.getPkPath(), sslConfig.getCaPath(), sslConfig.getPasswd()).createSSLEngine();
			sslEngine =createSSLContext().createSSLEngine();
		} else {
			sslEngine = getClientContext(sslConfig.getPkPath(), sslConfig.getPasswd()).createSSLEngine();
 
		}
		sslEngine.setEnabledProtocols(new String[] { "TLSv1", "TLSv1.1",
				"TLSv1.2" });
		sslEngine.setUseClientMode(true);
		return sslEngine;
	}
	//获取客户端openssl证书引擎
	public static SSLEngine getOpenSslClientEngine(SSLConfig sslConfig, ByteBufAllocator alloc) {
		SSLEngine sslEngine = null;
		if (sslConfig.isNeedClientAuth()) {
			sslEngine = getOpenSslClientContext(sslConfig.getPkPath(), sslConfig.getCaPath(), sslConfig.getPasswd()).newEngine(alloc);
		} else {
			sslEngine = getOpenSslClientContext(sslConfig.getPkPath(), sslConfig.getPasswd()).newEngine(alloc);
		}
		sslEngine.setEnabledProtocols(new String[] { "TLSv1", "TLSv1.1", "TLSv1.2" });
		sslEngine.setUseClientMode(true);
		return sslEngine;
	}
 
	public static SslHandler getClientOpenSslHandler(SSLConfig sslConfig,
											   ByteBufAllocator alloc) {

		if (sslConfig != null && sslConfig.isSSL()) {
			return new SslHandler(getOpenSslClientEngine(sslConfig, alloc));
		} else {
			return null;
		}
	}

	public  static  SSLContext createSSLContext() {
		SSLContext sslContext = null;
//		try {
//			String keyPassword = "123456";
//
//			// key store manager
//			KeyStore keyStore = KeyStore.getInstance("PKCS12");
//			InputStream keyInput = IMSNettyManager.getInstance().getContext().getResources().openRawResource(R.raw.client);
//			keyStore.load(keyInput, keyPassword.toCharArray());
//			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//			keyManagerFactory.init(keyStore, keyPassword.toCharArray());
//			// trust store manager
//			CertificateFactory cf = CertificateFactory.getInstance("X509");
//			InputStream caInput = IMSNettyManager.getInstance().getContext().getResources().openRawResource(R.raw.server);
//			Certificate ca;
//			try {
//				ca = cf.generateCertificate(caInput);
//			} finally {
//				caInput.close();
//			}
//			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//			trustStore.load(null, null);
//			trustStore.setCertificateEntry("CA", ca);
//			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//			trustManagerFactory.init(trustStore);
//
//			// assemble
//			sslContext = SSLContext.getInstance("TLS");
//			sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return sslContext;
	}

}




