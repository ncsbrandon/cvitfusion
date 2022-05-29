package com.apextalos.cvitfusion.common.settings;

import com.apextalos.cvitfusion.common.Strings;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigFile {

	private static final Logger logger = LogManager.getLogger(ConfigFile.class.getSimpleName());

	private final ObjectMapper mapper = new ObjectMapper();

	// The derived class makes sure the keys are returned
	// in alphabetical order. This way the configuration file
	// will have sorted entries instead of random mess.
	private final Properties p = new Properties() {
		private static final long serialVersionUID = 1L;

		@Override
		public synchronized Enumeration<Object> keys() {
			return Collections.enumeration(new TreeSet<>(super.keySet()));
		}
	};

	private String configFilename;

	public String getConfigFilename() {
		return configFilename;
	}

	public ConfigFile(String configFilename) {
		this.configFilename = configFilename;
	}

	public boolean load() {
		// check the filename
		if (configFilename == null || configFilename.isEmpty()) {
			logger.error("Config filename not set");
			return false;
		}

		// open the file
		try (FileInputStream fis = new FileInputStream(configFilename)) {
			logger.info("Loading config file: " + configFilename);
			p.load(fis);
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException while loading the configuration: " + e.getMessage());
			return false;
		} catch (IOException e) {
			logger.error("IOException while loading the configuration: " + e.getMessage());
			return false;
		}

		logger.info("Loading config file success");
		return true;
	}

	public boolean save() {
		// check the filename
		if (configFilename == null || configFilename.length() == 0) {
			logger.error("Config filename not set");
			return false;
		}

		// store to file
		try (FileOutputStream fos = new FileOutputStream(configFilename)) {
			logger.info("Saving config file %s", configFilename);
			p.store(fos, ConfigFile.class.getName());
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException while saving the configuration: %s", e.getMessage());
			return false;
		} catch (IOException e) {
			logger.error("IOException while saving the configuration: %s", e.getMessage());
			return false;
		}

		logger.info("Saving config file success");
		return true;
	}

	public boolean hasKey(String key) {
		return p.containsKey(key);
	}

	public void removeKey(String key) {
		p.remove(key);
	}

	public void setString(String key, String value, boolean allowBlank) {
		if (key == null || key.length() == 0) {
			logger.error("null or blank key name ignored");
			return;
		}

		if (value != null && value.length() > 0) {
			p.setProperty(key, value);

			// hide passwords in the log
			if (!key.contains("PWD"))
				logger.debug(key + " set to " + value);

			return;
		}

		if (allowBlank) {
			p.setProperty(key, "");
			logger.debug(key + " set to blank");
			return;
		}

		logger.debug(key + " NOT set because value is null or blank");
	}

	public String getString(String key, String defaultValue) {
		return p.getProperty(key, defaultValue);
	}

	public void setInt(String key, int value) {
		setString(key, String.valueOf(value), false);
	}

	public int getInt(String key, int defaultValue) {
		String value = getString(key, Integer.toString(defaultValue));

		if (value != null && value.length() > 0) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException e) {
				logger.error(String.format(Strings.PARSE_FAILED, value, key));
			}
		}

		return defaultValue;
	}

	public void setBoolean(String key, boolean value) {
		setString(key, String.valueOf(value), false);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		String value = getString(key, String.valueOf(defaultValue));

		if (value != null && value.length() > 0) {
			try {
				return Boolean.parseBoolean(value);
			} catch (NumberFormatException e) {
				logger.error(String.format(Strings.PARSE_FAILED, value, key));
			}
		}

		return defaultValue;
	}

	public void setDouble(String key, double value) {
		setString(key, String.valueOf(value), false);
	}

	public double getDouble(String key, double defaultValue) {
		String value = getString(key, String.valueOf(defaultValue));

		if (value != null && value.length() > 0) {
			try {
				return Double.parseDouble(value);
			} catch (NumberFormatException e) {
				logger.error(String.format(Strings.PARSE_FAILED, value, key));
			}
		}

		return defaultValue;
	}

	public void setFloat(String key, float value) {
		setString(key, String.valueOf(value), false);
	}

	public float getFloat(String key, float defaultValue) {
		String value = getString(key, String.valueOf(defaultValue));

		if (value != null && value.length() > 0) {
			try {
				return Float.parseFloat(value);
			} catch (NumberFormatException e) {
				logger.error(String.format(Strings.PARSE_FAILED, value, key));
			}
		}

		return defaultValue;
	}

	public void setJSON(ConfigFile cf, String key, Object obj) throws JsonProcessingException {
		cf.setString(key, mapper.writeValueAsString(obj), false);
	}

	public <T> T getJSON(ConfigFile cf, String key, Class<?> typeClass) throws IOException {
		if (cf == null)
			return null;

		String value = cf.getString(key, "");
		if (value == null || value.length() == 0)
			return null;

		JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, typeClass);
		return mapper.readValue(value, type);
	}
}