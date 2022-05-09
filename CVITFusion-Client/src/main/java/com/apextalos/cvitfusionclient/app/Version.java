package com.apextalos.cvitfusionclient.app;

public class Version {

    // Singleton
    private static Version instance = null;

    public static Version getInstance() {
        if (instance == null)
            instance = new Version();

        return instance;
    }

    public String getCopyrightYear() {
        return "2022";
    }

    public String getManufacturer() {
        return "Apex Talos";
    }

    public String getURL() {
        return "http://www.apextalos.com";
    }

    public String getVersion() {
        return "1.1";
    }

    public String getBuild() {
        return "20220508";
    }

    public String getProduct() {
        return "CVITFusion";
    }

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

    public String getReleaseNotesFile() {
        return "releasenotes.txt";
    }
}