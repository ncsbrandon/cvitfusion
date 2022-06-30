package com.apextalos.cvitfusion.common.mqtt;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.apache.logging.log4j.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;

public class SSLUtils {

	private static Logger logger = LogManager.getLogger(SSLUtils.class.getSimpleName());
	
	private SSLUtils() {
		// prevent instances
	}
	
	public static SSLSocketFactory getSocketFactory(final String caCrtFile, final String crtFile, final String keyFile, final String password) throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
		Security.addProvider(new BouncyCastleProvider());

		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		
		// load CA certificate
		X509Certificate caCert = null;
		FileInputStream fis = new FileInputStream(caCrtFile);
		try(BufferedInputStream bis = new BufferedInputStream(fis)) {
			while (bis.available() > 0) {
				caCert = (X509Certificate) cf.generateCertificate(bis);
				//logger.info("cacert {}", caCert);
			}
		}

		// load client certificate
		X509Certificate cert = null;
		try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(crtFile))) {
			while (bis.available() > 0) {
				cert = (X509Certificate) cf.generateCertificate(bis);
				//logger.info("cert {}", cert);
			}
		}

		// load client private key
		KeyPair key;
		try(PEMParser pemParser = new PEMParser(new FileReader(keyFile))) {
			Object object = pemParser.readObject();
			PEMDecryptorProvider decProv = new JcePEMDecryptorProviderBuilder().build(password.toCharArray());
			JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
			if (object instanceof PEMEncryptedKeyPair) {
				logger.info("Encrypted key - we will use provided password");
				key = converter.getKeyPair(((PEMEncryptedKeyPair) object).decryptKeyPair(decProv));
			} else {
				logger.info("Unencrypted key - no password needed");
				key = converter.getKeyPair((PEMKeyPair) object);
			}
		}

		// CA certificate is used to authenticate server
		KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
		caKs.load(null, null);
		caKs.setCertificateEntry("ca-certificate", caCert);
		TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
		tmf.init(caKs);

		// client key and certificates are sent to server so it can authenticate us
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(null, null);
		ks.setCertificateEntry("certificate", cert);
		ks.setKeyEntry("private-key", key.getPrivate(), password.toCharArray(),	new java.security.cert.Certificate[] { cert });
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(ks, password.toCharArray());

		// finally, create SSL socket factory
		SSLContext context = SSLContext.getInstance("TLSv1.2");
		context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

		return context.getSocketFactory();
	}
}
