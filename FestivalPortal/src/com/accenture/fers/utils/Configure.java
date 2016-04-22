package com.accenture.fers.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Configure {

	private String applicationUrl;
	private String hubUrl;
    private boolean gridMode;
    private boolean zapEnabled;
    private String zapIp;
    private int zapPort;
    private static final Logger LOGGER = Logger.getLogger(Configure.class);

	public Configure() {
		Properties prop = new Properties();
		String propFileName = "config.properties";

		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(propFileName);
		try {
			if (inputStream != null) {
				prop.load(inputStream);
			}
		}catch (FileNotFoundException fnf){
			LOGGER.info(propFileName+ " not found in the classpath",fnf);
		}catch (IOException e) {
			LOGGER.info("info",e);
		}
		setApplicationUrl(prop.getProperty("application.url"));
		setZapEnabled(Boolean.parseBoolean(prop.getProperty("zap.enabled")));
		setZapIp(prop.getProperty("zap.url"));
		setZapPort(Integer.parseInt(prop.getProperty("zap.port")));
		setHubUrl(prop.getProperty("grid.hub.url"));
		setGridMode(Boolean.parseBoolean(prop.getProperty("grid.enabled")));

	}




	public String getApplicationUrl() {
		return applicationUrl;
	}

	public void setApplicationUrl(String applicationUrl) {
		this.applicationUrl = applicationUrl;
	}



	public String getHubUrl() {
		return hubUrl;
	}



	public void setHubUrl(String hubUrl) {
		this.hubUrl = hubUrl;
	}



	public boolean isGridMode() {
		return gridMode;
	}



	public void setGridMode(boolean gridMode) {
		this.gridMode = gridMode;
	}



	public boolean isZapEnabled() {
		return zapEnabled;
	}



	public void setZapEnabled(boolean zapEnabled) {
		this.zapEnabled = zapEnabled;
	}



	public String getZapIp() {
		return zapIp;
	}



	public void setZapIp(String zapIp) {
		this.zapIp = zapIp;
	}



	public int getZapPort() {
		return zapPort;
	}



	public void setZapPort(int zapPort) {
		this.zapPort = zapPort;
	}


}
