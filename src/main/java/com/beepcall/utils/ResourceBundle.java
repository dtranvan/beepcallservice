package com.beepcall.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Admin
 */
public class ResourceBundle {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ResourceBundle.class);

	private static volatile ResourceBundle instance = null;
	private Properties prop;

	private ResourceBundle() throws FileNotFoundException, IOException {
		prop = new Properties();
		prop.load(new FileInputStream(Constants.CONFIG_PROP));
		PropertyConfigurator.configure(prop);

	}

	public static ResourceBundle getInstance() throws FileNotFoundException,
			IOException {
		if (null == instance) {
			synchronized (ResourceBundle.class) {
				if (instance == null) {
					instance = new ResourceBundle();
				}
			}
		}
		return instance;

	}

	public static String getMessage(String key) {
		try {
			return ResourceBundle.getInstance().prop.getProperty(key);
		} catch (IOException e) {
			LOGGER.error("IOException", e.toString());
		}
		return null;
	}
}
