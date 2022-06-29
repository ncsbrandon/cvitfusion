package com.apextalos.cvitfusion.common.license;

import java.util.ArrayList;
import java.util.List;

public class FeatureManager {

	public static final Feature FEATURE_ADDRESS = new Feature("ADDRESS", "MAC address", String.class, "", Feature.MODE_CLIENTGEN);
	public static final Feature FEATURE_IDDATETIME = new Feature("IDDATETIME", "ID date", String.class, "", Feature.MODE_CLIENTGEN);
	public static final Feature FEATURE_INTERFACE = new Feature("INTERFACE", "Interface", String.class, "", Feature.MODE_CLIENTGEN);
	public static final Feature FEATURE_ENGINEVERSION = new Feature("ENGINEVERSION", "Engine version", String.class, "", Feature.MODE_CLIENTGEN);
	
	public static final Feature FEATURE_LICDATETIME = new Feature("KEY_DATETIME", "Key date", String.class, "", Feature.MODE_KEYDETAIL);
	public static final Feature FEATURE_ORG = new Feature("ORG", "Organization", String.class, "", Feature.MODE_KEYDETAIL);
	public static final Feature FEATURE_SALESORDER = new Feature("SALESORDER", "Sales Order", String.class, "", Feature.MODE_KEYDETAIL);
	
	private List<Feature> features = new ArrayList<>();
	
	private static FeatureManager singleInstance = null;

	public static FeatureManager getInstance() {
		if (singleInstance == null)
			singleInstance = new FeatureManager();

		return singleInstance;
	}
	
	private FeatureManager() {
		features.add(FEATURE_ADDRESS);
		features.add(FEATURE_IDDATETIME);
		features.add(FEATURE_INTERFACE);
		features.add(FEATURE_ENGINEVERSION);
		
		features.add(FEATURE_LICDATETIME);
		features.add(FEATURE_ORG);
		features.add(FEATURE_SALESORDER);
	}
	
	public List<Feature> getFeatures() {
		return features;
	}
	
	public Feature getFeature(String id) {
		for(Feature feature : features) {
			if(0 == id.compareToIgnoreCase(feature.getId()))
				return feature;
		}
		
		// unknown
		return null;
	}
	
	public String getFeatureDescription(String id) {
		for(Feature feature : features) {
			if(0 == id.compareToIgnoreCase(feature.getId()))
				return feature.getDescription();
		}
		
		// unknown
		return id;
	}
}
