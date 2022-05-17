package com.apextalos.cvitfusion.common.license;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.apextalos.cvitfusion.common.utils.ByteUtils;

public class LicenseManager {

	private static Logger logger = LogManager.getLogger(LicenseManager.class.getSimpleName());

	private static final String ENCRYPTION_KEY = "a15b5ba75c1641dfa2b5f053fb05887e";
	private static final String ENCRYPTION_ALGORITHM = "AES";

	private SecretKey key;
	private Cipher ecipher;
	private Cipher dcipher;

	private static LicenseManager singleInstance = null;

	public static LicenseManager getInstance()
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		if (singleInstance == null)
			singleInstance = new LicenseManager();

		return singleInstance;
	}

	private LicenseManager() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		// generate keys
		key = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), ENCRYPTION_ALGORITHM);
		ecipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
		ecipher.init(Cipher.ENCRYPT_MODE, key);
		dcipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
		dcipher.init(Cipher.DECRYPT_MODE, key);
	}

	public int enumerateInterfaces() throws SocketException {
		// enumerate all interfaces
		Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
		int count = 0;
		do {
			NetworkInterface network = networks.nextElement();
			if (network.getHardwareAddress() != null)
				logger.debug(network.getName() + " " + ByteUtils.bytesToHex(network.getHardwareAddress()));
			else
				logger.debug(network.getName());
			count++;
		} while (networks.hasMoreElements());

		return count;
	}

	public String getFirstInterface() throws SocketException {
		// enumerate all interfaces
		Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
		// return the first one
		return networks.nextElement().getName();
	}

	public String generateLicenseID(String ifname) throws IllegalBlockSizeException, IOException {
		// get the network interface
		NetworkInterface network = NetworkInterface.getByName(ifname);
		if (network == null) {
			logger.error("Unable to create NetworkInterface for " + ifname);
			return null;
		}

		// if the mac is null, use a simulated value
		byte[] hardwareAddress = network.getHardwareAddress();
		if (hardwareAddress == null) {
			hardwareAddress = new byte[] { 0x00, 0x11, 0x22, 0x33, 0x44, 0x55, 0x66 };
			logger.debug("using generated hardware address");
		} else {
			logger.debug(ifname + " " + ByteUtils.bytesToHex(hardwareAddress));
		}

		// object to encrypt
		License license = new License();
		license.setStringFeature(FeatureManager.FEATURE_IDDATETIME,
				DateTime.now().withZone(DateTimeZone.UTC).toString());
		license.setStringFeature(FeatureManager.FEATURE_ADDRESS, ByteUtils.bytesToHex(hardwareAddress));
		license.setStringFeature(FeatureManager.FEATURE_INTERFACE, ifname);

		return generate(license);
	}

	public String generateLicenseKey(License license) throws IllegalBlockSizeException, IOException {
		return generate(license);
	}

	private String generate(License license) throws IllegalBlockSizeException, IOException {
		// seal
		SealedObject sealedObject = new SealedObject(license.getProperties(), ecipher);

		// encrypt to a byte array
		byte[] idbytes;
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			ObjectOutput out = new ObjectOutputStream(bos);
			out.writeObject(sealedObject);
			out.flush();
			idbytes = bos.toByteArray();
		}

		// check the id
		if (idbytes == null || idbytes.length == 0) {
			logger.error("Null or blank idbytes");
			return null;
		}

		// convert to a string
		String idstring = ByteUtils.bytesToHex(idbytes);
		logger.debug("encrypted string: " + idstring);
		return idstring;
	}

	public License loadLicenseKey(String keystring)
			throws IOException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException {
		// sanity
		if (keystring == null)
			throw new IOException("NULL keystring");

		if (keystring.length() == 0)
			throw new IOException("No keystring provided");

		// string to bytes
		byte[] keybytes = ByteUtils.hexToBytes(keystring);

		// decrypt the byte array
		ByteArrayInputStream bis = new ByteArrayInputStream(keybytes);
		Object keyobject;
		try (ObjectInput in = new ObjectInputStream(bis)) {
			keyobject = in.readObject();
		}

		// parse
		SealedObject keysealed = (SealedObject) keyobject;
		return new License((Properties) keysealed.getObject(dcipher));
	}

	public boolean verifyAddress(String ifname, License license) throws SocketException {
		// get the network interface and MAC
		NetworkInterface network = NetworkInterface.getByName(ifname);
		if (network == null) {
			logger.error("Unable to create NetworkInterface for " + ifname);
			return false;
		}

		// if the mac is null, use a simulated value
		byte[] hardwareAddress = network.getHardwareAddress();
		if (hardwareAddress == null) {
			hardwareAddress = new byte[] { 0x00, 0x11, 0x22, 0x33, 0x44, 0x55, 0x66 };
			logger.debug("using generated hardware address");
		} else {
			logger.debug(ifname + " " + ByteUtils.bytesToHex(hardwareAddress));
		}

		// get the mac of the device
		String macAddressCurrent = ByteUtils.bytesToHex(hardwareAddress);

		// does the ADDRESS feature exist
		String macAddressLicensed = license.getStringFeature(FeatureManager.FEATURE_ADDRESS);

		// do they match
		return (0 == macAddressCurrent.compareToIgnoreCase(macAddressLicensed));
	}
}