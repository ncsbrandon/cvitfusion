package com.apextalos.cvitfusion.common.license;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.design.DesignManager;
import com.apextalos.cvitfusion.common.opflow.Type;

public class License {

	private static Logger logger = LogManager.getLogger(License.class.getSimpleName());

	private Properties props;

	public License() {
		this.props = new Properties();
	}

	public License(Properties props) {
		this.props = props;
	}

	public Properties getProperties() {
		return props;
	}
	
	public boolean hasStringFeature(Feature feature) {
		return (props != null && props.containsKey(feature.getId()));
	}
	
	public Map<Feature, String> getAllFeatures() {
		FeatureManager fm = FeatureManager.getInstance();
		
		// plus "type" features
    	DesignManager dm = DesignManager.getInstance();
    	List<Type> types = dm.getTypes(null);
    	types.forEach(type -> fm.getFeatures().add(new DesignTypeFeature(type)));
		
		Map<Feature, String> features = new HashMap<>();
		for(Object propKey : props.keySet()) {
			Feature feature = fm.getFeature(String.valueOf(propKey));
			if(feature != null)
				features.put(feature, props.getProperty((String) propKey));
		}
		return features;
	}
	
	public Map<Feature, String> getVisibleFeatures() {
		FeatureManager fm = FeatureManager.getInstance();
		
		// plus "type" features
    	DesignManager dm = DesignManager.getInstance();
    	List<Type> types = dm.getTypes(null);
    	types.forEach(type -> fm.getFeatures().add(new DesignTypeFeature(type)));
		
		Map<Feature, String> features = new HashMap<>();
		for(Object propKey : props.keySet()) {
			Feature feature = fm.getFeature(String.valueOf(propKey));
			if(feature != null && feature.getMode() == Feature.MODE_FEATURE)
				features.put(feature, props.getProperty((String) propKey));
		}
		return features;
	}

	public String getStringFeature(Feature feature) {
		if (props == null || !props.containsKey(feature.getId())) {
			logger.info("{} [{}] not found", feature.getDescription(), feature.getId());
			return feature.getDefaultValue();
		}

		return props.getProperty(feature.getId(), feature.getDefaultValue());
	}

	public int getIntFeature(Feature feature) {
		return Integer.parseInt(getStringFeature(feature));
	}

	public boolean getBooleanFeature(Feature feature) {
		return Boolean.parseBoolean(getStringFeature(feature));
	}

	public void setStringFeature(Feature feature, String optionValue) {
		props.setProperty(feature.getId(), optionValue);
	}

	public void setIntFeature(Feature feature, int optionValue) {
		setStringFeature(feature, String.valueOf(optionValue));
	}

	public void setBooleanFeature(Feature feature, boolean optionValue) {
		setStringFeature(feature, String.valueOf(optionValue));
	}
}
