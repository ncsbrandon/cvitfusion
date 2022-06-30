package com.apextalos.cvitfusion.common.design;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.engine.ProcessorDigitalOutput;
import com.apextalos.cvitfusion.common.engine.ProcessorFB;
import com.apextalos.cvitfusion.common.engine.ProcessorLidar;
import com.apextalos.cvitfusion.common.engine.ProcessorWWVD;
import com.apextalos.cvitfusion.common.license.DesignTypeFeature;
import com.apextalos.cvitfusion.common.license.License;
import com.apextalos.cvitfusion.common.opflow.Color;
import com.apextalos.cvitfusion.common.opflow.Parameter;
import com.apextalos.cvitfusion.common.opflow.Parameters;
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
	
	public boolean setProcessesInConfig(List<Process> processes, ConfigFile cf) {
		// encode as JSON
		String resultPayload;
		try {
			resultPayload = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(processes);
		} catch (JsonProcessingException e) {
			logger.error("Processes encode failure: {}", e.getMessage());
			return false;
		}
		
		// set the into the configuration
		cf.setString(ConfigItems.ENGINE_DESIGN_CONFIG, resultPayload, false);
		
		// restart the engine
		
		return true;
	}
	
	public List<Process> getProcessesFromConfig(ConfigFile cf) {
		List<Process> processes = new ArrayList<>();
		
		String processJson = cf.getString(ConfigItems.ENGINE_DESIGN_CONFIG, ConfigItems.ENGINE_DESIGN_DEFAULT);
		
		if(processJson.isBlank())
			return processes;
		
		try {
			processes = mapper.readValue(processJson, new TypeReference<List<Process>>(){});
		} catch (JsonProcessingException e) {
			logger.error("Processes decode failure: {}", e.getMessage());
		}
		
		return processes;
	}
	
	public List<Type> getTypes(License license) {
		List<Type> types = new ArrayList<>();
		
		List<Parameter> emailParameters = new ArrayList<>() {{
			add(Parameters.notes);
			add(Parameters.toAddress);
			add(Parameters.fromName);
			add(Parameters.fromAddress);
			add(Parameters.smtpServer);
			add(Parameters.smtpPort);
			add(Parameters.useAuth);
			add(Parameters.username);
			add(Parameters.password);
		}};
		
		List<Parameter> parameters = new ArrayList<>();
		
		// OUTPUTS -------------
		Type typeEmail = new Type(100, 1, "Email", emailParameters, null, false);
		Type typeGPIO = new Type(103, 1, "GPIO", parameters, null, false);
		Type typeRSU = new Type(104, 1, "RSU", parameters, null, false);
		Type typeVMS = new Type(105, 1, "VMS", parameters, null, false);
		Type typeSDLC = new Type(106, 1, "SDLC", parameters, null, false);
		Type typeMQTT = new Type(107, 1, "MQTT", parameters, null, false);
		
		// LOGICS-------------------------
		Type typeCS = new Type(12, 1, "Curve Speed", parameters, new ArrayList<>() {{
			add(Integer.valueOf(typeGPIO.getTypeID()));
			add(Integer.valueOf(typeRSU.getTypeID()));
		}}, false);
		Type typeQD = new Type(13, 1, "Queue Detection", parameters, new ArrayList<>() {{
			add(Integer.valueOf(typeGPIO.getTypeID()));
			add(Integer.valueOf(typeRSU.getTypeID()));
			add(Integer.valueOf(typeVMS.getTypeID()));
		}}, false);
		Type typeVRU = new Type(14, 1, "VRU", parameters, new ArrayList<>() {{
			add(Integer.valueOf(typeRSU.getTypeID()));
			add(Integer.valueOf(typeVMS.getTypeID()));
			add(Integer.valueOf(typeSDLC.getTypeID()));
		}}, false);
		Type typeStopbar = new Type(15, 1, "Stopbar", parameters, new ArrayList<>() {{
			add(Integer.valueOf(typeVMS.getTypeID()));
			add(Integer.valueOf(typeSDLC.getTypeID()));
			add(Integer.valueOf(typeEmail.getTypeID()));
		}}, false);
		Type typeOHVD = new Type(16, 1, "OHV Detection", parameters, new ArrayList<>() {{
			add(Integer.valueOf(typeSDLC.getTypeID()));
			add(Integer.valueOf(typeEmail.getTypeID()));
			add(Integer.valueOf(typeMQTT.getTypeID()));
		}}, false);
		
		// INPUTS----------------------	
		Type typeCamera = new Type(2, 1, "Camera", parameters, new ArrayList<>() {{
			add(Integer.valueOf(typeCS.getTypeID()));
			add(Integer.valueOf(typeQD.getTypeID()));
			add(Integer.valueOf(typeVRU.getTypeID()));
			add(Integer.valueOf(typeStopbar.getTypeID()));
			add(Integer.valueOf(typeOHVD.getTypeID()));
		}}, true);
		Type typeRadar = new Type(3, 1, "Radar", parameters, new ArrayList<>() {{
			add(Integer.valueOf(typeCS.getTypeID()));
			add(Integer.valueOf(typeQD.getTypeID()));
			add(Integer.valueOf(typeVRU.getTypeID()));
			add(Integer.valueOf(typeStopbar.getTypeID()));
			add(Integer.valueOf(typeOHVD.getTypeID()));
		}}, true);
		Type typeBSM = new Type(4, 1, "BSM", parameters, new ArrayList<>() {{
			add(Integer.valueOf(typeCS.getTypeID()));
			add(Integer.valueOf(typeQD.getTypeID()));
			add(Integer.valueOf(typeVRU.getTypeID()));
			add(Integer.valueOf(typeStopbar.getTypeID()));
			add(Integer.valueOf(typeOHVD.getTypeID()));
		}}, true);
		Type typeOHVLaser = new Type(5, 1, "OHV Laser", parameters, new ArrayList<>() {{
			add(Integer.valueOf(typeCS.getTypeID()));
			add(Integer.valueOf(typeQD.getTypeID()));
			add(Integer.valueOf(typeVRU.getTypeID()));
			add(Integer.valueOf(typeStopbar.getTypeID()));
			add(Integer.valueOf(typeOHVD.getTypeID()));
		}}, true);
		
		checkLicense(typeEmail, types, license);
		checkLicense(ProcessorFB.getType(), types, license);
		checkLicense(ProcessorDigitalOutput.getType(), types, license);
		checkLicense(typeGPIO, types, license);
		checkLicense(typeRSU, types, license);
		checkLicense(typeVMS, types, license);
		checkLicense(typeSDLC, types, license);
		checkLicense(typeMQTT, types, license);
		
		checkLicense(ProcessorWWVD.getType(), types, license);
		checkLicense(typeCS, types, license);
		checkLicense(typeQD, types, license);
		checkLicense(typeVRU, types, license);
		checkLicense(typeStopbar, types, license);
		checkLicense(typeOHVD, types, license);
		
		checkLicense(ProcessorLidar.getType(), types, license);
		checkLicense(typeCamera, types, license);
		checkLicense(typeRadar, types, license);
		checkLicense(typeBSM, types, license);
		checkLicense(typeOHVLaser, types, license);
		
		return types;
	}
	
	private void checkLicense(Type type, List<Type> types, License license) {
		// tests, client, and the licensing util
		if(license == null) {
			types.add(type);
			return;
		}

		// check the license key
		if(license.getBooleanFeature(new DesignTypeFeature(type)))
			types.add(type);
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
