package com.beepcall.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadDynParam {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(LoadDynParam.class);

	private String requestTime;
	private String count;

	public LoadDynParam() {
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream(Constants.CONFIG_DYN_PARAM));
			PropertyConfigurator.configure(prop);
			requestTime = prop.getProperty(Constants.REQUEST_TIME);
			count = prop.getProperty(Constants.COUNT);
		} catch (FileNotFoundException e) {
			LOGGER.error("Exception", e);
		} catch (IOException e) {
			LOGGER.error("IOException", e);
		}

	}

	public String getRequestTime() {
		return requestTime;
	}

	public String getCount() {
		return count;
	}

}
