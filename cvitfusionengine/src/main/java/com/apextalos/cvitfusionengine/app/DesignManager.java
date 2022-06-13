package com.apextalos.cvitfusionengine.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.opflow.Color;
import com.apextalos.cvitfusion.common.opflow.Process;
import com.apextalos.cvitfusion.common.opflow.Style;
import com.apextalos.cvitfusion.common.opflow.Type;
import com.apextalos.cvitfusion.common.settings.ConfigFile;
import com.apextalos.cvitfusion.common.settings.ConfigItems;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DesignManager {

	private static Logger logger = LogManager.getLogger(DesignManager.class.getSimpleName());

	public static final ObjectMapper mapper = new ObjectMapper();
	
	private static DesignManager singleInstance = null;

	public static DesignManager getInstance() {
		if (singleInstance == null)
			singleInstance = new DesignManager();

		return singleInstance;
	}

	private DesignManager() {
		// must use singleton
	}
	
	public boolean setProcesses(List<Process> processes, ConfigFile cf) {
		// encode as JSON
		String resultPayload;
		try {
			resultPayload = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(processes);
		} catch (JsonProcessingException e) {
			logger.error("Processes encode failure: " + e.getMessage());
			return false;
		}
		
		// set the into the configuration
		cf.setString(ConfigItems.ENGINE_DESIGN_CONFIG, resultPayload, false);
		
		// restart the engine
		
		return true;
	}
	
	public List<Process> getProcesses(ConfigFile cf) {
		List<Process> processes = new ArrayList<>();
		
		String processJson = cf.getString(ConfigItems.ENGINE_DESIGN_CONFIG, ConfigItems.ENGINE_DESIGN_DEFAULT);
		
		if(processJson.isBlank())
			return processes;
		
		try {
			processes = mapper.readValue(processJson, new TypeReference<List<Process>>(){});
		} catch (JsonProcessingException e) {
			logger.error("Processes decode failure: " + e.getMessage());
		}
		
		return processes;
	}
	
	public List<Type> getTypes() {
		List<Type> types = new ArrayList<>();
		
		// INPUTS----------------------
		types.add(new Type(1, 1, "Lidar", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(11));
				add(Integer.valueOf(12));
				add(Integer.valueOf(13));
				add(Integer.valueOf(14));
				add(Integer.valueOf(15));
			}
		}, true));
		types.add(new Type(2, 1, "Camera", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(11));
				add(Integer.valueOf(12));
				add(Integer.valueOf(13));
			}
		}, true));
		types.add(new Type(3, 1, "Radar", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(14));
				add(Integer.valueOf(15));
			}
		}, true));
		types.add(new Type(4, 1, "BSM", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(11));
				add(Integer.valueOf(12));
				add(Integer.valueOf(13));
				add(Integer.valueOf(14));
				add(Integer.valueOf(15));
			}
		}, true));
		types.add(new Type(5, 1, "OHV Laser", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(11));
				add(Integer.valueOf(12));
				add(Integer.valueOf(13));
				add(Integer.valueOf(14));
				add(Integer.valueOf(15));
			}
		}, true));
		
		// LOGICS-------------------------
		types.add(new Type(11, 1, "WWVD", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(100));
				add(Integer.valueOf(101));
				add(Integer.valueOf(102));
			}
		}, false));
		types.add(new Type(12, 1, "Curve Speed", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(101));
				add(Integer.valueOf(102));
				add(Integer.valueOf(103));
			}
		}, false));
		types.add(new Type(13, 1, "Queue Detection", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(102));
				add(Integer.valueOf(103));
				add(Integer.valueOf(104));
			}
		}, false));
		types.add(new Type(14, 1, "VRU", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(103));
				add(Integer.valueOf(104));
				add(Integer.valueOf(105));
			}
		}, false));
		types.add(new Type(15, 1, "Stopbar", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(105));
				add(Integer.valueOf(106));
				add(Integer.valueOf(107));
			}
		}, false));
		types.add(new Type(16, 1, "OHV Detection", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(101));
				add(Integer.valueOf(106));
				add(Integer.valueOf(107));
			}
		}, false));

		// OUTPUTS -------------
		types.add(new Type(100, 1, "Email", new Properties(), null, false));
		types.add(new Type(101, 1, "Flashing Beacon", new Properties(), null, false));
		types.add(new Type(102, 1, "Digital Output", new Properties(), null, false));
		types.add(new Type(103, 1, "GPIO", new Properties(), null, false));
		types.add(new Type(104, 1, "RSU", new Properties(), null, false));
		types.add(new Type(105, 1, "VMS", new Properties(), null, false));
		types.add(new Type(106, 1, "SDLC", new Properties(), null, false));
		types.add(new Type(107, 1, "MQTT", new Properties(), null, false));
		
		return types;
	}
	
	public List<Style> getStyles() {
		List<Style> styles = new ArrayList<>();
		
		// https://coolors.co/233d4d-915e3d-fe7f2d-fda53a-fcca46-cfc664-a1c181-619b8a
		styles.add(new Style(1, 1, new Color(0x23, 0x3D, 0x4D, 1), new Color(255, 255, 255, 1))); // Charcoal w/ White
		styles.add(new Style(2, 1, new Color(0x91, 0x5E, 0x3D, 1), new Color(255, 255, 255, 1))); // Coyote Brown w/ White
		styles.add(new Style(3, 1, new Color(0xFE, 0x7F, 0x2D, 1), new Color(255, 255, 255, 1))); // Pumpkin w/ White
		styles.add(new Style(4, 1, new Color(0xFD, 0xA5, 0x3A, 1), new Color(0, 0, 0, 1))); // Yellow Orange w/ Black
		styles.add(new Style(5, 1, new Color(0xFC, 0xCA, 0x46, 1), new Color(0, 0, 0, 1))); // Sunglow w/ Black
		styles.add(new Style(6, 1, new Color(0xCF, 0xC6, 0x64, 1), new Color(0, 0, 0, 1))); // Straw w/ Black
		styles.add(new Style(7, 1, new Color(0xA1, 0xC1, 0x81, 1), new Color(0, 0, 0, 1))); // Olivine w/ Black
		styles.add(new Style(8, 1, new Color(0x61, 0x9B, 0x8A, 1), new Color(0, 0, 0, 1))); // Polished Pine w/ Black
		
		return styles;
	}
	
	public Map<Integer, Integer> getTypeStyleMap() {
		Map<Integer, Integer> typeStyles = new HashMap<>();
		
		// INPUTS----------------------
		typeStyles.put(1, 1);
		typeStyles.put(2, 2);
		typeStyles.put(3, 1);
		typeStyles.put(4, 2);
		typeStyles.put(5, 1);
		
		// LOGICS-------------------------
		typeStyles.put(11, 3);
		typeStyles.put(12, 4);
		typeStyles.put(13, 5);
		typeStyles.put(14, 3);
		typeStyles.put(15, 4);
		typeStyles.put(16, 4);

		// OUTPUTS -------------
		typeStyles.put(100, 6);
		typeStyles.put(101, 7);
		typeStyles.put(102, 8);
		typeStyles.put(103, 6);
		typeStyles.put(104, 7);
		typeStyles.put(105, 8);
		typeStyles.put(106, 6);
		typeStyles.put(107, 7);
		
		return typeStyles;
	}
}
