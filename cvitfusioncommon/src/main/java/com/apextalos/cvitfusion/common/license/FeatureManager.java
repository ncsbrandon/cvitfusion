package com.apextalos.cvitfusion.common.license;

import java.util.ArrayList;
import java.util.List;

public class FeatureManager {

	public static final Feature FEATURE_ADDRESS = new Feature("ADDRESS", "MAC address", String.class, "", Feature.MODE_HIDDEN);
	public static final Feature FEATURE_IDDATETIME = new Feature("IDDATETIME", "ID date", String.class, "", Feature.MODE_HIDDEN);
	public static final Feature FEATURE_INTERFACE = new Feature("INTERFACE", "Interface", String.class, "", Feature.MODE_HIDDEN);
	public static final Feature FEATURE_SUBTYPE = new Feature("SUBTYPE", "Subtype", String.class, "", Feature.MODE_HIDDEN);
	public static final Feature FEATURE_LOCATION = new Feature("LOCATION", "Location", String.class, "", Feature.MODE_HIDDEN);
	public static final Feature FEATURE_SERIALNUM = new Feature("SERIAL", "Serial number", String.class, "", Feature.MODE_HIDDEN);
	public static final Feature FEATURE_LICDATETIME = new Feature("LICDATETIME", "License date", String.class, "", Feature.MODE_FEATURE);
	public static final Feature FEATURE_ORG = new Feature("ORG", "Organization", String.class, "", Feature.MODE_FEATURE);
	public static final Feature FEATURE_MHCSO = new Feature("MHCSO", "MHC Sales Order", String.class, "", Feature.MODE_HIDDEN);
	public static final Feature FEATURE_SCRIPTS = new Feature("SCRIPTS", "Number of scripts", Integer.class, "0", Feature.MODE_FEATURE);
	public static final Feature FEATURE_SENSORS = new Feature("SENSORS", "Number of ITS sensors", Integer.class, "0", Feature.MODE_FEATURE);
	public static final Feature FEATURE_V2IRSU = new Feature("V2IRSU", "Enable V2I Roadside Unit features", Boolean.class, "false", Feature.MODE_FEATURE);
	public static final Feature FEATURE_INTLZN = new Feature("INTLZN", "Enable Intellizone", Boolean.class, "false", Feature.MODE_FEATURE);
	public static final Feature FEATURE_RESTAPI = new Feature("RESTAPI", "Enable REST API", Boolean.class, "false", Feature.MODE_FEATURE);
	public static final Feature FEATURE_US33FEAT = new Feature("US33FEAT", "Enable US33 Features", Boolean.class, "false", Feature.MODE_FEATURE);
	public static final Feature FEATURE_XWALKAPP = new Feature("XWALKAPP", "Enable CrossWalk Features", Boolean.class, "false", Feature.MODE_FEATURE);
	public static final Feature FEATURE_METIRI = new Feature("METIRI", "Enable Metiri Features", Boolean.class, "false", Feature.MODE_FEATURE);
	public static final Feature FEATURE_STOPBAR = new Feature("STOPBAR", "Enable Stopbar Detection", Boolean.class, "false", Feature.MODE_FEATURE);
	public static final Feature FEATURE_REVERSABLELANE = new Feature("REVERSABLELANE", "Enable Reversable Lane", Boolean.class, "false", Feature.MODE_FEATURE);
	
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
		features.add(FEATURE_SUBTYPE);
		features.add(FEATURE_LOCATION);
		features.add(FEATURE_SERIALNUM);
		features.add(FEATURE_LICDATETIME);
		features.add(FEATURE_ORG);
		features.add(FEATURE_MHCSO);
		features.add(FEATURE_SCRIPTS);
		features.add(FEATURE_SENSORS);
		features.add(FEATURE_V2IRSU);
		features.add(FEATURE_INTLZN);
		features.add(FEATURE_RESTAPI);
		features.add(FEATURE_US33FEAT);
		features.add(FEATURE_XWALKAPP);
		features.add(FEATURE_METIRI);
		features.add(FEATURE_STOPBAR);
		features.add(FEATURE_REVERSABLELANE);
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
