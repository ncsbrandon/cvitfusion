package com.apextalos.cvitfusionengine.app;

import com.apextalos.cvitfusion.common.IVersion;

public class Version implements IVersion {

	// Singleton
	private static Version instance = null;

	public static Version getInstance() {
		if (instance == null)
			instance = new Version();

		return instance;
	}
		
	@Override
	public String getCopyrightYear() {
		return "2022";
	}

	@Override
	public String getManufacturer() {
		return "Apex Talos";
	}

	@Override
	public String getURL() {
		return "http://www.apextalos.com";
	}

	@Override
	public String getVersion() {
		return "1.1";
	}

	@Override
	public String getBuild() {
		return "20220508";
	}

	@Override
	public String getProduct() {
		return "CVITFusion Engine";
	}

	@Override
	public String getComponentName(int iComponentID) {
		switch (iComponentID) {
		case 0:
			return getProduct();
		case 1:
			return "TBD";
		default:
			return "Undefined";
		}
	}

	@Override
	public String getComponentVersion(int iComponentID) {
		switch (iComponentID) {
		case 0:
			return getVersion();
		case 1:
			return "v1";
		default:
			return "Undefined";
		}
	}

	@Override
	public String getReleaseNotesFile() {
		return "releasenotes.txt";
	}

}
