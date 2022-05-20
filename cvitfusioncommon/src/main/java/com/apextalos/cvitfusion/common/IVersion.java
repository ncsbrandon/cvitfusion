package com.apextalos.cvitfusion.common;

public interface IVersion {
	String getCopyrightYear();

	String getManufacturer();

	String getURL();

	String getVersion();

	String getBuild();

	String getProduct();

	String getComponentName(int iComponentID);

	String getComponentVersion(int iComponentID);

	String getReleaseNotesFile();
}
